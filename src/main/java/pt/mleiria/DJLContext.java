package pt.mleiria;

import ai.djl.ndarray.NDManager;

import java.util.function.Consumer;
import java.util.function.Function;

public final class DJLContext {
    // Private constructor to prevent instantiation
    private DJLContext() {
    }

    /**
     * Creates a new NDManager, "loans" it to the provided block of code,
     * and guarantees that it is closed afterwards.
     * This version is for operations that do not return a value.
     *
     * @param managerConsumer A block of code that accepts an NDManager.
     */
    public static void use(final Consumer<NDManager> managerConsumer) {
        // try-with-resources is the perfect, concise way to implement this pattern
        // for any class that implements AutoCloseable.
        try (NDManager manager = NDManager.newBaseManager()) {
            managerConsumer.accept(manager);
        }
    }

    /**
     * Creates a new NDManager, "loans" it to the provided block of code,
     * and guarantees that it is closed afterwards.
     * This version is for operations that return a value.
     *
     * @param managerFunction A block of code that accepts an NDManager and returns a result.
     * @param <R>             The type of the result.
     * @return The result produced by the provided function.
     */
    public static <R> R useAndGet(final Function<NDManager, R> managerFunction) {
        try (NDManager manager = NDManager.newBaseManager()) {
            return managerFunction.apply(manager);
        }
    }
}
