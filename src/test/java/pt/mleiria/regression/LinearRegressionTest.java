package pt.mleiria.regression;

import org.junit.jupiter.api.Test;
import org.tribuo.*;
import org.tribuo.data.csv.CSVLoader;
import org.tribuo.math.optimisers.AdaGrad;
import org.tribuo.math.optimisers.SGD;
import org.tribuo.regression.RegressionFactory;
import org.tribuo.regression.Regressor;
import org.tribuo.regression.evaluation.RegressionEvaluator;
import org.tribuo.regression.sgd.linear.LinearSGDModel;
import org.tribuo.regression.sgd.linear.LinearSGDTrainer;
import org.tribuo.regression.sgd.objectives.SquaredLoss;
import org.tribuo.transform.TransformationMap;
import org.tribuo.transform.TransformerMap;
import org.tribuo.transform.transformations.LinearScalingTransformation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LinearRegressionTest {

    private static final String workDir = "/home/manuel/Downloads/Databases/ThreeBodyProblem/X_test/processed_data/";

    @Test
    public void testLinearRegSGDTribuo() {
        List<String> resume = new ArrayList<>();
        try {
            // --- 0) Paths ---
            Path trainPath = Paths.get(workDir + "train_data.csv");
            Path validPath = Paths.get(workDir + "validation_data.csv");
            Path testPath  = Paths.get(workDir + "test_data.csv");
            Path mergedPath = Paths.get(workDir + "train_validation_merged.csv");

            // Merge train+validation to retrain the final model on more data
            // If your class only has main(String[]), add a small wrapper, or call main(...) directly.
            //MergeCsvSameHeader.merge(trainPath, validPath, mergedPath);

            // --- 1) Loader & targets (multi-output) ---
            var loader  = new CSVLoader<Regressor>(new RegressionFactory());
            Set<String> targets = new LinkedHashSet<>(Arrays.asList("x_1","y_1","x_2","y_2","x_3","y_3"));

            DataSource<Regressor> trainSrc  = loader.loadDataSource(trainPath, targets);
            DataSource<Regressor> validSrc  = loader.loadDataSource(validPath, targets);
            DataSource<Regressor> testSrc   = loader.loadDataSource(testPath, targets);
            DataSource<Regressor> mergeSrc  = loader.loadDataSource(mergedPath, targets);

            MutableDataset<Regressor> train = new MutableDataset<>(trainSrc);
            MutableDataset<Regressor> valid = new MutableDataset<>(validSrc);
            MutableDataset<Regressor> test  = new MutableDataset<>(testSrc);
            MutableDataset<Regressor> trainPlusValid = new MutableDataset<>(mergeSrc);

            // --- 2) Scale features (fit ONLY on TRAIN, apply to all) ---
            TransformationMap tMapDef = new TransformationMap(List.of(new LinearScalingTransformation(0.0, 1.0)));
            TransformerMap fitted = train.createTransformers(tMapDef);
            train.transform(fitted);
            valid.transform(fitted);
            test.transform(fitted);
            trainPlusValid.transform(fitted);
            // If needed: train.densify(); valid.densify(); test.densify(); trainPlusValid.densify();

            // --- 3) Hyperparameter tuning on VALIDATION ---
            double[] lrs = {1e-3, 5e-3, 1e-2, 5e-2};   // learning rates for SGD with linear decay
            int[] epochs = {10, 20, 30};
            long seed = 1L;

            double bestRmse = Double.POSITIVE_INFINITY;
            double bestLR = lrs[0];
            int bestEpochs = epochs[0];

            for (double lr : lrs) {
                for (int ep : epochs) {
                    var trainer = new LinearSGDTrainer(
                            new SquaredLoss(),
                            new AdaGrad(lr), // you can also try SGD.getLinearDecaySGD(lr)
                            ep,
                            1,      // logging interval
                            seed
                    );
                    LinearSGDModel model = trainer.train(train);
                    double valRmse = multiOutputRMSE(model, valid);
                    resume.add("lr=" + lr + " epochs=" + ep + " | val RMSE=" + valRmse);
                    System.out.printf(Locale.US, "lr=%.4g epochs=%d | val RMSE=%.6f%n", lr, ep, valRmse);
                    if (valRmse < bestRmse) {
                        bestRmse = valRmse;
                        bestLR = lr;
                        bestEpochs = ep;
                    }
                }
            }
            resume.add("Best (by validation RMSE): lr=" + bestLR + " epochs=" + bestEpochs + " | RMSE=" + bestRmse);
            System.out.printf(Locale.US, "Best (by validation RMSE): lr=%.4g epochs=%d (RMSE=%.6f)%n",
                    bestLR, bestEpochs, bestRmse);

            // --- 4) Final training on TRAIN+VALID with best hyperparams ---
            var finalTrainer = new LinearSGDTrainer(
                    new SquaredLoss(),
                    SGD.getLinearDecaySGD(bestLR),
                    bestEpochs,
                    1,
                    seed
            );
            LinearSGDModel finalModel = finalTrainer.train(trainPlusValid);

            // --- 5) Evaluate on TEST ---
            var evalTest = new RegressionEvaluator().evaluate(finalModel, test);
            System.out.println("--- Test evaluation (train+valid final model) ---");
            System.out.println(evalTest);
            System.out.println(resume);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** ===== Helpers (Tribuo 4.3.2-friendly) ===== */

    private static class RegressorAlignedPair {
        final double[] truth, pred;
        RegressorAlignedPair(double[] truth, double[] pred) { this.truth = truth; this.pred = pred; }
    }

    private static RegressorAlignedPair alignByName(Regressor truth, Regressor pred) {
        String[] tNames = truth.getNames();
        double[] tVals  = truth.getValues();
        String[] pNames = pred.getNames();
        double[] pVals  = pred.getValues();

        Map<String, Double> pMap = new HashMap<>(pNames.length);
        for (int i = 0; i < pNames.length; i++) pMap.put(pNames[i], pVals[i]);

        double[] alignedTruth = new double[tNames.length];
        double[] alignedPred  = new double[tNames.length];
        for (int i = 0; i < tNames.length; i++) {
            alignedTruth[i] = tVals[i];
            alignedPred[i]  = pMap.getOrDefault(tNames[i], Double.NaN);
        }
        return new RegressorAlignedPair(alignedTruth, alignedPred);
    }

    private static double multiOutputRMSE(Model<Regressor> model, Iterable<org.tribuo.Example<Regressor>> ds) {
        double se = 0.0; long count = 0L;
        for (var ex : ds) {
            var pred = model.predict(ex);
            var pair = alignByName(ex.getOutput(), pred.getOutput());
            for (int i = 0; i < pair.truth.length; i++) {
                double d = pair.pred[i] - pair.truth[i];
                se += d * d; count++;
            }
        }
        return (count == 0L) ? Double.NaN : Math.sqrt(se / count);
    }



    private void analyzeDataSet(DataSource<Regressor> trainSource) {
        int count = 0; // number of successfully parsed examples
        try {
            for (Example<Regressor> ex : trainSource) { // your DataSource<Regressor>
                count++;
                for (Feature f : ex) {
                    double v = f.getValue();
                    if (Double.isNaN(v) || Double.isInfinite(v)) {
                        int csvLine = count + 1; // +1 for header
                        System.err.printf("NaN/Inf at CSV line %d, feature '%s'%n", csvLine, f.getName());
                        throw new IllegalArgumentException("Found NaN/Inf");
                    }
                }
            }
            System.out.println("No NaN/Inf found in features.");
        } catch (IllegalArgumentException e) {
            int csvLine = count + 1;
            System.err.println("Likely offending CSV line: " + csvLine);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Path in  = Paths.get(workDir + "test_data.csv");
        Path out = Paths.get(workDir + "test_poly_data.csv");

        Set<String> targets = new LinkedHashSet<>(Arrays.asList(
                "x_1","y_1","x_2","y_2","x_3","y_3"
        ));

        // example: expand everything except targets; keep time "t" as feature and expand it too
        expandDegree2(in, out, targets, Collections.emptySet(), true);
    }

    public static void expandDegree2(
            Path inputCsv,
            Path outputCsv,
            Set<String> targetColumns,      // columns that are outputs (Tribuo targets)
            Set<String> featureWhitelist,   // columns to expand (null/empty -> expand all non-target numeric columns)
            boolean includeTimeColumn        // if true and "t" exists, it stays in features and is expanded
    ) throws IOException {

        try (BufferedReader br = Files.newBufferedReader(inputCsv, StandardCharsets.UTF_8);
             BufferedWriter bw = Files.newBufferedWriter(outputCsv, StandardCharsets.UTF_8)) {

            // --- read header ---
            String header = br.readLine();
            if (header == null) throw new IllegalArgumentException("Empty CSV: " + inputCsv);
            String[] cols = header.split(",", -1);
            List<String> allCols = Arrays.asList(cols);

            // figure out base feature columns (exclude targets; optionally exclude t)
            List<String> baseFeatures = new ArrayList<>();
            for (String c : allCols) {
                if (targetColumns.contains(c)) continue;
                if (!includeTimeColumn && c.equals("t")) continue;
                // whitelist logic (optional)
                if (featureWhitelist != null && !featureWhitelist.isEmpty() && !featureWhitelist.contains(c)) continue;
                baseFeatures.add(c);
            }

            // --- build output header ---
            List<String> outHeader = new ArrayList<>(allCols); // original columns first

            // squares
            for (String f : baseFeatures) outHeader.add(f + "^2");

            // pairwise interactions (i < j to avoid duplicates and f*f again)
            for (int i = 0; i < baseFeatures.size(); i++) {
                for (int j = i + 1; j < baseFeatures.size(); j++) {
                    outHeader.add(baseFeatures.get(i) + "*" + baseFeatures.get(j));
                }
            }
            bw.write(String.join(",", outHeader));
            bw.newLine();

            // --- process rows ---
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] vals = line.split(",", -1);
                if (vals.length != allCols.size()) {
                    // skip malformed row
                    continue;
                }

                // start with original row values
                List<String> outRow = new ArrayList<>(Arrays.asList(vals));

                // cache parsed doubles for the base features
                Map<String, Double> vmap = new HashMap<>();
                boolean badRow = false;
                for (String f : baseFeatures) {
                    int idx = allCols.indexOf(f);
                    try {
                        double v = Double.parseDouble(vals[idx]);
                        if (Double.isNaN(v) || Double.isInfinite(v)) {
                            badRow = true;
                            break;
                        }
                        vmap.put(f, v);
                    } catch (NumberFormatException nfe) {
                        badRow = true;
                        break;
                    }
                }
                if (badRow) {
                    // You can choose to skip or impute. Here we skip to avoid NaNs in Tribuo.
                    // Comment the next line if you prefer to write the row with zeros instead.
                    continue;
                }

                // squares
                for (String f : baseFeatures) {
                    double v = vmap.get(f);
                    outRow.add(Double.toString(v * v));
                }

                // pairwise interactions
                for (int i = 0; i < baseFeatures.size(); i++) {
                    for (int j = i + 1; j < baseFeatures.size(); j++) {
                        double vi = vmap.get(baseFeatures.get(i));
                        double vj = vmap.get(baseFeatures.get(j));
                        outRow.add(Double.toString(vi * vj));
                    }
                }

                bw.write(String.join(",", outRow));
                bw.newLine();
            }
        }
    }





}
