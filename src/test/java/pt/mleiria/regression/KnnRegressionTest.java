package pt.mleiria.regression;

import org.junit.jupiter.api.Test;
import org.tribuo.DataSource;
import org.tribuo.Model;
import org.tribuo.MutableDataset;
import org.tribuo.Prediction;
import org.tribuo.common.nearest.KNNModel;
import org.tribuo.common.nearest.KNNTrainer;
import org.tribuo.data.csv.CSVLoader;
import org.tribuo.math.distance.Distance;
import org.tribuo.math.distance.DistanceType;
import org.tribuo.math.neighbour.NeighboursQueryFactoryType;
import org.tribuo.regression.RegressionFactory;
import org.tribuo.regression.Regressor;
import org.tribuo.regression.ensemble.AveragingCombiner;
import org.tribuo.regression.evaluation.RegressionEvaluator;
import org.tribuo.transform.TransformationMap;
import org.tribuo.transform.TransformerMap;
import org.tribuo.transform.transformations.LinearScalingTransformation;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class KnnRegressionTest {
    private static final String workDir = "/home/manuel/Downloads/Databases/ThreeBodyProblem/X_test/processed_data/";

    @Test
    public void testKNNRegressionTribuo() {
        try {
            // ---- Paths ----
            Path trainPath = Paths.get(workDir + "train_data.csv");
            Path validPath = Paths.get(workDir + "validation_data.csv");
            Path testPath  = Paths.get(workDir + "test_data.csv");
            Path mergedPath = Paths.get(workDir + "train_validation_merged.csv");

            // ---- Merge train + validation into a single CSV (using your class) ----
            // If your class only has main(String[]), use: MergeCsvSameHeader.main(new String[]{trainPath.toString(), validPath.toString(), mergedPath.toString()});
            //MergeCsvSameHeader.merge(trainPath, validPath, mergedPath);

            // ---- Loader & targets (multi-output) ----
            var loader  = new CSVLoader<Regressor>(new RegressionFactory());
            Set<String> targets = new LinkedHashSet<>(Arrays.asList("x_1","y_1","x_2","y_2","x_3","y_3"));
            DataSource<Regressor> trainSrc = loader.loadDataSource(trainPath, targets);
            DataSource<Regressor> validSrc = loader.loadDataSource(validPath, targets);
            DataSource<Regressor> testSrc  = loader.loadDataSource(testPath, targets);
            DataSource<Regressor> mergedSrc= loader.loadDataSource(mergedPath, targets);

            MutableDataset<Regressor> train = new MutableDataset<>(trainSrc);
            MutableDataset<Regressor> valid = new MutableDataset<>(validSrc);
            MutableDataset<Regressor> test  = new MutableDataset<>(testSrc);
            MutableDataset<Regressor> trainPlusValid = new MutableDataset<>(mergedSrc);

            // ---- Scale features (fit on TRAIN, apply to all) ----
            TransformationMap tMapDef = new TransformationMap(List.of(new LinearScalingTransformation(0.0, 1.0)));
            TransformerMap fitted = train.createTransformers(tMapDef);
            train.transform(fitted);
            valid.transform(fitted);
            test.transform(fitted);
            trainPlusValid.transform(fitted);

            // ---- Candidate k values (around sqrt(n_train)) ----
            int nTrain = train.size();
            int k0 = Math.max(1, (int)Math.round(Math.sqrt(nTrain)));
            int[] kCandidates = new int[]{
                    //Math.max(1, k0/2),
                    //k0,
                    //Math.min(nTrain, Math.max(1, k0*2)),
                    14
            };

            // ---- KNN config (Tribuo 4.3.2) ----
            Distance dist = DistanceType.L2.getDistance();                // try L1 too
            var combiner = new AveragingCombiner();                       // average neighbor vectors
            var nqType  = NeighboursQueryFactoryType.KD_TREE;             // or BRUTE_FORCE
            var backend = KNNModel.Backend.THREADPOOL;                    // or STREAMS / INNERTHREADPOOL
            int numThreads = Math.max(1, Runtime.getRuntime().availableProcessors() - 1);

            // ---- Tune k on VALIDATION ----
            double bestRmse = Double.POSITIVE_INFINITY;
            int bestK = kCandidates[0];

            for (int k : kCandidates) {
                var trainer = new KNNTrainer<Regressor>(k, dist, numThreads, combiner, backend, nqType);
                Model<Regressor> model = trainer.train(train);
                double valRmse = multiOutputRMSE(model, valid);
                System.out.printf(Locale.US, "k=%d  |  validation RMSE=%.6f%n", k, valRmse);

                if (valRmse < bestRmse) {
                    bestRmse = valRmse;
                    bestK = k;
                }
            }
            System.out.println("Best k (by validation RMSE): " + bestK);

            // ---- Final model on TRAIN+VALID ----
            var finalTrainer = new KNNTrainer<Regressor>(bestK, dist, numThreads, combiner, backend, nqType);
            Model<Regressor> finalModel = finalTrainer.train(trainPlusValid);

            // ---- Evaluate on TEST ----
            var evalTest = new RegressionEvaluator().evaluate(finalModel, test);
            System.out.println("--- Test evaluation (train+valid final model) ---");
            System.out.println(evalTest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== Helpers =====

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
            Prediction<Regressor> pred = model.predict(ex);
            var pair = alignByName(ex.getOutput(), pred.getOutput());
            for (int i = 0; i < pair.truth.length; i++) {
                double d = pair.pred[i] - pair.truth[i];
                se += d * d; count++;
            }
        }
        return (count == 0L) ? Double.NaN : Math.sqrt(se / count);
    }
}
