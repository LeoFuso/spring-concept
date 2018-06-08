package exception.util;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * <p>Handling exceptions using an Java Optional-like inspired Interface</p>
 *
 * @param <T> reference Type of the expected value
 * @see <a href="https://dzone.com/articles/exceptional-optional"> source at </a>
 */
public class Exceptional<T> {

	private static final Exceptional<?> EMPTY = new Exceptional<>();

	private final T value;
	private final Exception exception;

	private Exceptional() {
		this.value = null;
		this.exception = null;
	}

	public void rethrowRuntime() {
		if (exception != null)
			throw new RuntimeException(exception);
	}

	public void rethrow() throws Exception {
		if (exception != null)
			throw exception;
	}

	public Exception getException() {
		return exception;
	}

	@SuppressWarnings("unchecked")
	private static <T> Exceptional<T> empty() {

		Exceptional<T> t = (Exceptional<T>) EMPTY;
		return t;
	}

	private Exceptional(T value) {
		this.value = Objects.requireNonNull(value);
		this.exception = null;
	}

	private Exceptional(Exception exception) {
		this.exception = Objects.requireNonNull(exception);
		value = null;
	}

	/**
	 * <p>
	 * Returns a new instance of the Exceptional class built upon the value passed as parameter.
	 * </p>
	 *
	 * @param value The value passed as a parameter.
	 * @param <T>   reference Type of the expected value
	 * @return Returns a new instance of the Exceptional class built upon the method call passed as parameter.
	 */
	public static <T> Exceptional<T> of(T value) {
		return new Exceptional<>(value);
	}

	/**
	 * <p>
	 * Returns a new instance of the Exceptional class built upon the exception passed as parameter.
	 * </p>
	 *
	 * @param exception The exception passed as a parameter.
	 * @param <T>       reference Type of the expected value
	 * @return Returns a new instance of the Exceptional class built upon the method call passed as parameter.
	 */
	public static <T> Exceptional<T> of(Exception exception) {
		return new Exceptional<>(exception);
	}

	public static <T> Exceptional<T> ofException(Exception exception) {
		return exception == null ? empty() : of(exception);
	}

	public T get() {

		if (exception != null)
			throw new RuntimeException(exception);

		return value;
	}

	/**
	 * <p>Returns <code>false</code> if an exception has been thrown, returns <code>true</code> otherwise</p>
	 *
	 * @return whether or not the value is present
	 */
	public boolean isPresent() {
		return exception == null;
	}

	/**
	 * <p>
	 * If the value is present, it will perform a consumer-based action.
	 * If an exception has been thrown, nothing will happen.
	 * </p>
	 *
	 * @param consumer An operation that accepts a single input argument and returns no result
	 */
	public void ifPresent(Consumer<? super T> consumer) {

		if (exception == null)
			consumer.accept(value);
	}

	public void ifExceptionPresent(Consumer<? super Exception> consumer) {

		if (exception != null)
			consumer.accept(exception);
	}

	public void ifExceptionPresent(Class<? extends Exception> targetType,
	                               Consumer<? super Exception> consumer) {

		if (exception != null && targetType.isAssignableFrom(exception.getClass()))
			consumer.accept(exception);
	}

	public T orElse(T other) {
		return exception == null ? value : other;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (!(obj instanceof Exceptional))
			return false;

		Exceptional<?> other = (Exceptional<?>) obj;
		return Objects.equals(value, other.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}

	@Override
	public String toString() {
		return exception == null ? String.format("Exceptional[%s]", value)
				: String.format("Exceptional[%s]", exception);
	}

}