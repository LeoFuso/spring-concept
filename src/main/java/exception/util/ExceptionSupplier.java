package exception.util;

/**
 * Represents a supplier of results that can throw an exception
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #get()}.
 *
 * @param <T> the type of the result supplied by this supplier
 * @param <E> the type of the exception that this supplier can throw
 * @see java.util.function.Supplier
 * @since 1.8
 */
@FunctionalInterface
public interface ExceptionSupplier<T, E extends Exception> {

	/**
	 * Gets a result.
	 *
	 * @return {@link T} a result
	 * @throws E an {@link Exception}
	 */
	T get() throws E;

}
