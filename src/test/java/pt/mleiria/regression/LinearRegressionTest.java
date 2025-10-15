package pt.mleiria.regression;

import org.junit.jupiter.api.Test;
import org.tribuo.DataSource;
import org.tribuo.Example;
import org.tribuo.Feature;
import org.tribuo.MutableDataset;
import org.tribuo.data.csv.CSVLoader;
import org.tribuo.math.optimisers.AdaGrad;
import org.tribuo.regression.RegressionFactory;
import org.tribuo.regression.Regressor;
import org.tribuo.regression.evaluation.RegressionEvaluator;
import org.tribuo.regression.sgd.linear.LinearSGDModel;
import org.tribuo.regression.sgd.linear.LinearSGDTrainer;
import org.tribuo.regression.sgd.objectives.SquaredLoss;
import org.tribuo.transform.TransformationMap;
import org.tribuo.transform.TransformerMap;
import org.tribuo.transform.transformations.LinearScalingTransformation;
import org.tribuo.transform.transformations.MeanStdDevTransformation;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LinearRegressionTest {

    @Test
    public void testLinearRegSGDTribuo() {
        try {
            var trainSource = "/home/manuel/Downloads/Databases/ThreeBodyProblem/X_test/processed_data/train_data.csv";
            var testSource = "/home/manuel/Downloads/Databases/ThreeBodyProblem/X_test/processed_data/test_data.csv";
            // --- Load your data as before (example for multi-target CSV) ---
            var loader = new CSVLoader<Regressor>(new RegressionFactory());
            Set<String> targets = new LinkedHashSet<>(Arrays.asList("x_1", "y_1", "x_2", "y_2", "x_3", "y_3"));
            var trainSrc = loader.loadDataSource(Paths.get(trainSource), targets);
            var testSrc = loader.loadDataSource(Paths.get(testSource), targets);

            //analyzeDataSet(trainSrc);

            MutableDataset<Regressor> train = new MutableDataset<>(trainSrc);
            MutableDataset<Regressor> test = new MutableDataset<>(testSrc);

            // --- 1) Define the standard scaler (mean=0, std=1) for all features ---
            // global transform, applies to all features
            //TransformationMap tMapDef = new TransformationMap(List.of(new MeanStdDevTransformation()));

            TransformationMap tMapDef = new TransformationMap(List.of(new LinearScalingTransformation(0., 1.)));

            // --- 2) Fit the transformers on TRAIN ONLY, then apply to train & test ---
            TransformerMap fitted = train.createTransformers(tMapDef); // fits mean/std on train features
            train.transform(fitted);                                    // in-place transform of train set
            test.transform(fitted);                                     // apply same train stats to test

            // (Optional) If your pipeline relies on zeros as real values, call train.densify()/test.densify() before fitting;

            // --- 3) Train as usual ---
            var trainer = new LinearSGDTrainer(new SquaredLoss(), new AdaGrad(0.1), 100, 1, 1L);
            LinearSGDModel model = trainer.train(train);

            // --- 4) Evaluate / Predict ---
            var eval = new RegressionEvaluator().evaluate(model, test);
            System.out.println(eval);

            // For inference on a single raw example, transform it with the SAME fitted map:
            //Example<Regressor> ex = new org.tribuo.impl.ListExample<>(new Regressor("dummy", 0.0), List.of(new Feature("x1", 5.0), new Feature("x2", 10.0), new Feature("x3", 2.5)));
            // ex.transform(fitted); // scale the example in-place before predict
            //System.out.println("Pred: " + model.predict(ex).getOutput());

        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
