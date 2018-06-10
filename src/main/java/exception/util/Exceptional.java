package exception.util;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * <p>
 * A container object inspired in {@link java.util.Optional} to handle exceptions, in which may or may not contain a
 * non-{@code null} value or a non-{@code null} {@link Exception} thrown.
 * </p>
 *
 * <p>
 * If a value is present, {@link #isPresent()} returns {@code true}. If no
 * value is present, the object is considered <i>empty</i> and
 * {@link #isPresent()} returns {@code false}. The same goes for
 * {@link #isExceptionPresent()}, and in this case, {@link #isPresent()} always
 * returns {@code false}.
 * </p>
 *
 * @param <T> reference type of value
 * @see <a href="https://dzone.com/articles/exceptional-optional"> inspired by </a>
 */
public class Exceptional<T> {

	/**
	 * Common instance of {@code empty()}.
	 */
	private static final Exceptional<?> EMPTY = new Exceptional<>();

	/**
	 * If non-{@code null}, the value; if {@code null}, indicates no value is present.
	 * In the case of a non-{@link #ofNullable(Object)} usage could also indicates that an
	 * {@link Exception} has been thrown.
	 */
	private final T value;

	/**
	 * If non-{@code null}, the exception; if {@code null}, indicates no exception is present.
	 */
	private final Exception exception;

	/**
	 * Constructs an empty instance.
	 *
	 * @implNote Generally only one empty instance, {@link Optional#EMPTY},
	 * should exist per VM.
	 * I don't know if this is valid for the {@link Exceptional#EMPTY} too.
	 */
	private Exceptional() {
		this.value = null;
		this.exception = null;
	}

	/**
	 * Returns an empty {@link Exceptional} instance.  No value is present for this
	 * {@link Exceptional}.
	 *
	 * @param <T> The type of the non-existent value
	 * @return an empty {@link Exceptional}
	 * @apiNote Though it may be tempting to do so, avoid testing if an object is empty
	 * by comparing with {@code ==} against instances returned by
	 * {@link #empty()}.  There is no guarantee that it is a singleton.
	 * Instead, use {@link #isPresent()}.
	 */
	private static <T> Exceptional<T> empty() {
		@SuppressWarnings("unchecked")
		Exceptional<T> t = (Exceptional<T>) EMPTY;
		return t;
	}

	/**
	 * Constructs an instance with the described value.
	 *
	 * @param value the non-{@code null} value to describe
	 * @throws NullPointerException if value is {@code null}
	 */
	private Exceptional(T value) {
		this.value = Objects.requireNonNull(value);
		this.exception = null;
	}

	/**
	 * Constructs an instance with the described value.
	 *
	 * @param exception the non-{@code null} exception to describe
	 * @throws NullPointerException if exception is {@code null}
	 */
	private Exceptional(Exception exception) {
		this.exception = Objects.requireNonNull(exception);
		value = null;
	}

	/**
	 * Returns an {@link Exceptional} describing the given non-{@code null}
	 * value.
	 *
	 * @param value the value to describe, which must be non-{@code null}
	 * @param <T>   the type of the value
	 * @return an {@link Exceptional} with the value present
	 * @throws NullPointerException if value is {@code null}
	 */
	public static <T> Exceptional<T> of(T value) {
		return new Exceptional<>(value);
	}

	/**
	 * Returns an {@code Exceptional} with value provided by given {@code ExceptionSupplier} function.
	 *
	 * @param <T>      the type of value
	 * @param supplier a supplier function
	 * @return an {@code Exceptional}
	 */
	public static <T> Exceptional<T> of(ExceptionSupplier<T, Exception> supplier) {
		try {
			return of(supplier.get());
		} catch (Exception exception) {
			return of(exception);
		}
	}

	/**
	 * Returns an {@link Exceptional} describing the given non-{@code null}
	 * exception.
	 *
	 * @param exception the exception to describe, which must be non-{@code null}
	 * @param <T>       the type of the value
	 * @return an {@link Exceptional} with the exception present
	 * @throws NullPointerException if value is {@code null}
	 */
	public static <T> Exceptional<T> of(Exception exception) {
		return new Exceptional<>(exception);
	}

	/**
	 * Returns an {@link Exceptional} describing the given value, if
	 * non-{@code null}, otherwise returns an empty {@link Exceptional}.
	 *
	 * @param value the possibly-{@code null} value to describe
	 * @param <T>   the type of the value
	 * @return an {@link Exceptional} with a present value if the specified value
	 * is non-{@code null} and no {@link Exception} is thrown, otherwise an
	 * {@link Exceptional} with a present exception or an empty {@link Exceptional}
	 */
	public static <T> Exceptional<T> ofNullable(T value) {
		return value == null ? empty() : of(value);
	}

	/**
	 * Returns an {@link Exceptional} describing the given non-{@code null}
	 * exception.
	 *
	 * @param exception the exception to describe, which must be non-{@code null}
	 * @param <T>       the type of the value
	 * @return an {@link Exceptional} with the exception present
	 * @throws NullPointerException if value is {@code null}
	 */
	public static <T> Exceptional<T> ofNullable(Exception exception) {
		return of(exception);
	}

	/**
	 * If an exception is present, throws {@link RuntimeException}, otherwise
	 * checks if the value is present.
	 * If a value is present, returns the value, otherwise throws
	 * {@link NoSuchElementException}.
	 *
	 * @return the non-{@code null} value described by this {@link Exceptional}
	 * @throws NoSuchElementException if no value is present
	 * @throws RuntimeException       if an exception is present
	 * @apiNote The preferred alternative to this method is {@link #orElse(Object)}.
	 */
	public T get() {
		if (exception != null)
			throw new RuntimeException(exception);

		if (value == null)
			throw new NoSuchElementException("No value present");
		return value;
	}

	/**
	 * If no exception is present, returns the value, otherwise returns
	 * {@code other}.
	 *
	 * @param other the value to be returned, if no value is present.
	 *              May be {@code null}.
	 * @return the value, if no exception is present, otherwise {@code other}
	 */
	public T orElse(T other) {
		return exception == null ? value : other;
	}

	/**
	 * If an exception is present, returns the exception, otherwise throws
	 * {@link NoSuchElementException}.
	 *
	 * @return the non-{@code null} exception described by this {@link Exceptional}
	 * @throws NoSuchElementException if no exception is present
	 */
	public Exception getException() {
		if (exception == null)
			throw new NoSuchElementException("No value present");
		return exception;
	}

	/**
	 * If a value is present, returns {@code true}, otherwise {@code false}.
	 *
	 * @return {@code true} if a value is present, otherwise {@code false}
	 */
	public boolean isPresent() {
		return value != null;
	}

	/**
	 * If an exception is present, returns {@code true}, otherwise {@code false}.
	 *
	 * @return {@code true} if an exception is present, otherwise {@code false}
	 */
	public boolean isExceptionPresent() {
		return exception == null;
	}


	/**
	 * If no exception is thrown and a value is present, performs the given action with the value,
	 * otherwise does nothing.
	 *
	 * @param action the action to be performed, if a value is present and no exception was thrown
	 * @return TODO: This
	 * @throws NullPointerException if value is present and the given action is
	 *                              {@code null}
	 */
	public Exceptional<T> ifPresent(Consumer<? super T> action) {
		if (exception == null && value != null)
			action.accept(value);
		return this;
	}

	/**
	 * If no exception is thrown and a value is present, performs the given action with the value,
	 * otherwise performs the given empty-based action.
	 *
	 * @param action      the action to be performed, if a value is present and no exception was thrown
	 * @param emptyAction the empty-based action to be performed, if no value is
	 *                    present and an exception was thrown
	 * @throws NullPointerException if a value is present and the given action
	 *                              is {@code null}, or no value is present and the given empty-based
	 *                              action is {@code null}.
	 */
	public void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction) {
		if (exception == null && value != null)
			action.accept(value);
		else
			emptyAction.run();

		/*
		 *
		 * TODO: I'm not sure about the NullPointerException explanation,
		 * I think the emptyAction does not need to be not-null all the time.
		 *
		 */
	}

	public void rethrowRuntime() {
		if (exception != null)
			throw new RuntimeException(exception);
	}

	public void rethrow() throws Exception {
		if (exception != null)
			throw exception;
	}

	public void ifException(Consumer<? super Exception> action) {
		if (exception != null)
			action.accept(exception);
	}

	public void ifException(Class<? extends Exception> targetType,
	                        Consumer<? super Exception> consumer) {

		if (exception != null && targetType.isAssignableFrom(exception.getClass()))
			consumer.accept(exception);
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