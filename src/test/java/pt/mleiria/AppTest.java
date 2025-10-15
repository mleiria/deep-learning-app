package pt.mleiria;


import ai.djl.Model;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.Block;
import ai.djl.nn.SequentialBlock;
import ai.djl.nn.core.Linear;
import ai.djl.training.DefaultTrainingConfig;
import ai.djl.training.EasyTrain;
import ai.djl.training.Trainer;
import ai.djl.training.dataset.ArrayDataset;
import ai.djl.training.dataset.Batch;
import ai.djl.training.loss.Loss;
import ai.djl.training.optimizer.Optimizer;
import ai.djl.training.tracker.Tracker;
import ai.djl.translate.TranslateException;
import org.junit.jupiter.api.Test;
import tech.tablesaw.api.Table;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testSplitTable(){
        try {
            Table trainData = Table.read().file("/home/manuel/Downloads/Databases/ThreeBodyProblem/X_test/processed_data/train_data.csv");
            Table xTrainTable = trainData.select("t","x0_1","y0_1","x0_2","y0_2","x0_3","y0_3");
            System.out.println(xTrainTable.structure());
            System.out.println(xTrainTable.first(5));
            assertFalse(xTrainTable.isEmpty());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testConvertTableToNDArray(){
        try(NDManager nd = NDManager.newBaseManager()) {
            Table trainData = Table.read().file("/home/manuel/Downloads/Databases/ThreeBodyProblem/X_test/processed_data/train_data.csv");
            Table xTrainTable = trainData.select("t","x0_1","y0_1","x0_2","y0_2","x0_3","y0_3");
            NDArray x = nd.create(xTrainTable.as().doubleMatrix());
            System.out.println(x.getShape());
            assertFalse(x.isEmpty());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
