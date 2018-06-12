package exception.util;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A container object inspired in {@link java.util.Optional} to handle exceptions, in which may or may not contain a
 * non-{@code null} value or a non-{@code null} {@link Exception} thrown.
 *
 * <p>
 * If a value is present, {@link #isPresent()} returns {@code true}. If no
 * value is present, the object is considered <i>empty</i> and
 * {@link #isPresent()} returns {@code false}. The same goes for
 * {@link #isExceptionPresent()}, and in this case, {@link #isPresent()} always
 * returns {@code false}.
 * </p>
 *
 * <pre>
 *      <code>
 *
 *      Exceptional&lt;Class&gt; exceptional = Exceptional.of(Class::methodReference);
 *
 *      exceptional.ifPresent(AnotherClass::method)
 *                 .orElseDo(object, obj -&gt; action(obj));
 *
 *      Exceptional.of(Class::methodReference)
 *                 .ifPresent(System.out::println);
 *
 *      Exceptional.of(() -&gt; IOUtils.readBytes(inputStream)).getOrElse(new byte[0]);
 *
 *      Exceptional&lt;OtherClass&gt; exceptional = Exceptional.of(() -&gt Class.staticMethod(arg));
 *      exceptional.rethrowRunTime();
 *
 *      </code>
 * </pre>
 *
 * @param <T> reference type of value
 * @apiNote {@link Exceptional} is intended to consume only method references,
 * to capture its {@link Exception} thrown; {@link Exceptional} is not intended
 * for handling {@code null} outputs, this is a {@link java.util.Optional}
 * usage. This is not a substitute of {@link java.util.Optional}, but has the
 * ability to handle {@code null} outputs of any method return. A variable whose
 * type is {@link Exceptional} should never itself be {@code null}; it should
 * always point to an {@link Exceptional} instance.
 * @since 1.8
 */
public class Exceptional<T> {

	/**
	 * Returns an {@link Exceptional} describing the given non-{@code null}
	 * value or an {@link Exceptional}  describing an {@link Exception} thrown.
	 *
	 * @param <T>      the type of value
	 * @param supplier a supplier function
	 * @return an {@link Exceptional} describing the given non-{@code null}
	 * value or an {@link Exceptional}  describing an {@link Exception} thrown.
	 * @apiNote The value must always be provided by an {@link ExceptionSupplier},
	 * in another words, the value must always come from a method reference. If the
	 * value provided is {@code null} an {@link Exceptional}  describing a
	 * {@link NullPointerException} is returned instead of a {@link NullPointerException} throws.
	 */
	public static <T> Exceptional<T> of(ExceptionSupplier<T, Exception> supplier) {
		Objects.requireNonNull(supplier);

		try {
			return of(supplier.get());
		} catch (Exception exception) {
			return of(exception);
		}
	}

	/**
	 * Returns an {@link Exceptional} describing the given value, if
	 * non-{@code null}, otherwise returns an empty {@link Exceptional};
	 * can also returns an {@link Exceptional} describing a {@link Exception}
	 * thrown. Can also returns empty {@link Exceptional} if the supplier
	 * function is {@code null}.
	 *
	 * @param supplier a supplier function
	 * @param <T>      the type of value
	 * @return Returns an {@link Exceptional} describing the given value, if
	 * non-{@code null}, otherwise returns an empty {@link Exceptional};
	 * can also returns an {@link Exceptional} describing a {@link Exception}
	 * thrown. Can also returns empty {@link Exceptional} if the supplier
	 * function is {@code null}.
	 * @apiNote The value must always be provided by an {@link ExceptionSupplier},
	 * in another words, the value must always come from a method reference.
	 */
	public static <T> Exceptional<T> ofNullable(ExceptionSupplier<T, Exception> supplier) {
		if (supplier == null) return empty();
		try {
			return ofNullable(supplier.get());
		} catch (Exception exception) {
			return of(exception);
		}
	}

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
	 * @implNote Generally only one empty instance, {@link Exceptional#EMPTY},
	 * should exist per VM.
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
	 * {@code empty()}.  There is no guarantee that it is a singleton.
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
	 * @apiNote This method isn't defined to be used directly
	 */
	private static <T> Exceptional<T> of(T value) {
		return new Exceptional<>(value);
	}

	/**
	 * Returns an {@link Exceptional} describing the given non-{@code null}
	 * exception.
	 *
	 * @param exception the exception to describe, which must be non-{@code null}
	 * @param <T>       the type of the value
	 * @return an {@link Exceptional} with the exception present
	 * @throws NullPointerException if value is {@code null}
	 * @apiNote This method isn't defined to be used directly
	 */
	private static <T> Exceptional<T> of(Exception exception) {
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
	 * @apiNote This method isn't defined to be used directly
	 */
	private static <T> Exceptional<T> ofNullable(T value) {
		return value == null ? empty() : of(value);
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
		if (exception != null) {
			throw new RuntimeException(exception);
		}
		if (value == null) {
			throw new NoSuchElementException("No value present");
		}
		return value;
	}

	/**
	 * If no exception is present and if the value is non-{@code null}, returns the value, otherwise returns
	 * {@code other}.
	 *
	 * @param other the value to be returned, if no value is present.
	 *              May be {@code null}.
	 * @return If no exception is present and if the value is non-{@code null}, returns the value, otherwise returns
	 * {@code other}.
	 */
	public T orElse(T other) {
		return value != null ? value : other;
	}

	/**
	 * If no exception is present and if the value is non-{@code null},
	 * returns the value, otherwise the result produced by the supplying function
	 *
	 * @param supplier the supplying function that produces a value to be returned, can't be {@code null}.
	 * @return If no exception is present and if the value is non-{@code null},
	 * returns the value, otherwise the result produced by the supplying function
	 * @throws NullPointerException if no value is present and the supplying
	 *                              function is {@code null}
	 */
	public T orElse(Supplier<? extends T> supplier) {
		return value != null ? value : supplier.get();
	}

	/**
	 * If an {@link Exception} is thrown or the {@code value} is {@code null}, performs the given
	 * {@code action} on {@code object} passed as parameter, otherwise does nothing.
	 *
	 * @param object target of a given {@code action} to be performed
	 * @param action the {@code action} to be performed on {@code object}, if no {@code value} is present
	 *               and an {@link Exception} is thrown
	 * @param <O>    type of {@code object} passed as parameter
	 * @throws NullPointerException if given {@code action} or {@code object} is {@code null}
	 */
	//TODO: Javadoc return
	public <O> O orElseDo(O object, Consumer<O> action) {
		if (value == null) {
			action.accept(object);
		}
		return object;
	}

	/**
	 * Will return the {@code value}, whether it is {@code null} or not
	 *
	 * @return {@link #value}
	 */
	public T filterValue() {
		return value;
	}

	/**
	 * Will return the {@code exception}, whether it is {@code null} or not
	 *
	 * @return {@link #exception}
	 */
	public Exception filterException() {
		return exception;
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
		return exception != null;
	}


	/**
	 * Will perform the given action with the value and then will return itself if no exception is thrown
	 * and the value is non-{@code null}, otherwise will only return itself.
	 *
	 * @param action the action to be performed, if the value is non-{@code null} and no exception was thrown
	 * @return Will perform the given action with the value and then will return itself if no exception is thrown
	 * and the value is non-{@code null}, otherwise will only return itself.
	 * @throws NullPointerException if value is present and the given action is
	 *                              {@code null}
	 */
	public Exceptional<T> ifPresent(Consumer<? super T> action) {
		if (exception == null && value != null) {
			action.accept(value);
		}
		return this;
	}

	/**
	 * Invokes action function if there were any exception.
	 *
	 * @param action a action function
	 * @return an {@code Exceptional}
	 */
	//TODO: Javadoc and test methods
	public Exceptional<T> ifException(Consumer<? super Exception> action) {
		if (exception != null) {
			action.accept(exception);
		}
		return this;
	}

	/**
	 * Returns itself if no exception is thrown and the value is non-{@code null}, otherwise
	 * returns an empty {@link Exceptional}
	 *
	 * @return Returns itself if no exception is thrown and the value is non-{@code null}, otherwise
	 * returns an empty {@link Exceptional}
	 */
	public Exceptional<T> ifPresent() {
		if (exception == null && value != null) {
			return this;
		}
		return empty();
	}

	//TODO: Javadoc and test methods

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
	//TODO: Javadoc and utility
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

	public Exceptional<T> ifException() {
		if (exception != null) {
			return this;
		}
		return empty();
	}

	/**
	 * Invokes consumer function if {@link Exception} matches {@code exception} class
	 * reference passed as parameter
	 *
	 * @param <E>        the type of exception
	 * @param targetType the class of an {@code exception}  to be compared
	 * @param consumer   a consumer function
	 * @return an {@code Exceptional}
	 */
	@SuppressWarnings("unchecked")
	public <E extends Exception> Exceptional<T> ifExceptionIs(Class<E> targetType, Consumer<? super E> consumer) {
		if ((exception != null) &&
				(targetType.isAssignableFrom(exception.getClass()))) {
			consumer.accept((E) exception);
		}
		return this;
	}

	/**
	 * If an {@link Exception} is present, will try to rethrow it as a {@link RuntimeException}
	 *
	 * @throws RuntimeException if an {@link Exception} is present
	 */
	public void rethrowRunTime() {
		if (exception != null) {
			try {
				throw (RuntimeException) exception;
			} catch (ClassCastException ignored) { /*empty */}
		}
	}

	/**
	 * If an {@link Exception} is present, will rethrow it as an {@link Exception}
	 *
	 * @throws Exception if an {@link Exception} is present
	 */
	public void rethrow() throws Exception {
		if (exception != null) {
			throw exception;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Exceptional)) {
			return false;
		}

		Exceptional<?> other = (Exceptional<?>) obj;
		return Objects.equals(value, other.value) &&
				Objects.equals(exception, other.exception);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value, exception);
	}

	@Override
	public String toString() {
		return exception == null
				? String.format("Exceptional value %s", value)
				: String.format("Exceptional exception %s", exception);
	}

}