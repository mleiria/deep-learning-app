package pt.mleiria;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NDArrayTest {

    private static final Logger logger = Logger.getLogger(NDArrayTest.class.getName());

    @Test
    public void shouldHaveSize12() {
        try (final NDManager manager = NDManager.newBaseManager()) {
            var x = manager.arange(12);
            logger.info(x.toDebugString());
            logger.info("Shape: " + x.getShape().toString());
            assertEquals(12, x.size());
        }
    }

    @Test
    public void shouldHaveShape3_4() {
        try (final NDManager manager = NDManager.newBaseManager()) {
            var x = manager.arange(12);
            var y = x.reshape(3, 4);
            logger.info(y.toDebugString());
            logger.info("Shape: " + y.getShape().toString());
            // rows
            assertEquals(3, y.getShape().get(0));
            // cols
            assertEquals(4, y.getShape().get(1));
            // Infer the number of columns
            var z = x.reshape(3, -1);
            logger.info(z.toDebugString());
            logger.info("Shape: " + z.getShape().toString());
            // rows
            assertEquals(3, z.getShape().get(0));
            // cols
            assertEquals(4, z.getShape().get(1));

        }
    }

    @Test
    public void shouldHaveShape3_5() {
        try (final NDManager manager = NDManager.newBaseManager()) {
            var x = manager.create(new Shape(3, 5));
            assertEquals(3, x.getShape().get(0));
            assertEquals(5, x.getShape().get(1));
            logger.info(x.toDebugString());
        }
    }

    @Test
    public void testRandomNormal() {
        try (final NDManager manager = NDManager.newBaseManager()) {
            // mean 0 and std 1
            var x = manager.randomNormal(0f, 1f, new Shape(3, 5), DataType.FLOAT32);
            assertEquals(3, x.getShape().get(0));
            assertEquals(5, x.getShape().get(1));
            logger.info(x.toDebugString());
        }
    }
    @Test
    public void createFullTensor(){
        // Creating a tensor with a specific value
        try(NDManager manager = NDManager.newBaseManager()){
            NDArray tensor5 = manager.full(new Shape(2, 3), 7.5f);
            System.out.println("Tensor with specific value: " + tensor5);
        }
    }
}
