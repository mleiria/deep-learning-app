package pt.mleiria;

import ai.djl.ndarray.NDArray;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class DJLContextTest {

    private static final Logger logger = Logger.getLogger(DJLContextTest.class.getName());

    @Test
    void use() {
        DJLContext.use(manager -> {
            var x = manager.arange(12);
            assertEquals(12, x.size());
            logger.info("x: " + x);
        });
    }

    @Test
    void useAndGet() {
        float[] resultArray = DJLContext.useAndGet(manager -> {
            var x = manager.arange(12f);
            assertEquals(12, x.size());
            logger.info("x: " + x);
            return x.toFloatArray();
        });
        assertEquals(12, resultArray.length);
    }

    @Test
    void useAndGetError() {
        try {
            NDArray result = DJLContext.useAndGet(manager -> {
                var x = manager.create(new float[]{1f, 2f, 3f});
                return x;
            });
            logger.info(Arrays.toString(result.toFloatArray()));
        }catch (IllegalStateException e){
            // expected
            assertTrue(e.getMessage().contains("Native resource has been released already"));
        }
    }
}