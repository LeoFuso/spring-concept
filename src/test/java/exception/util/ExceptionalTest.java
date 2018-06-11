package exception.util;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ExceptionalTest {

	/**
	 * This shows that if the value is present then you should have no problem getting it.
	 */
	@Test
	public void testSimpleValuePresent() {

		Exceptional<String> exceptional = Exceptional.of(this::getValue);

		assertTrue(exceptional.isPresent());
		assertEquals("Value", exceptional.get());
		assertEquals("Value", exceptional.filterValue());

	}

	/**
	 * If the value does not exists and you pass a Consumer to it, the consumer will not execute.
	 */
	@Test
	public void testIfPresentWithValue() {

		Exceptional<String> exceptional = Exceptional.of(this::getValue);

		StringBuilder result = new StringBuilder();
		exceptional.ifPresent(val -> result.append(val));
		assertEquals("Value", result.toString());
	}

	@Test
	public void testIfPresentWithoutValue() {

		Exceptional<String> exceptional = Exceptional.of(this::exceptionThrower);

		StringBuilder result = new StringBuilder();
		exceptional.ifPresent(result::append);
		assertEquals("", result.toString());

	}

	/**
	 * You can also test to see if an Exception is present and take actions accordingly.
	 */
	@Test(expected = RuntimeException.class)
	public void testExceptionIsPresent() {

		Exceptional<String> exceptional = Exceptional.of(this::exceptionThrower);
		assertFalse(exceptional.isPresent());
		exceptional.get();

	}

	@Test
	public void testIfAnyExceptionIsPresent() {

		Exceptional<String> exceptional = Exceptional.of(this::exceptionThrower);

		StringBuilder result = new StringBuilder();
		exceptional.ifException(ex -> result.append(ex.getMessage()));
		assertEquals("message", result.toString());

	}

	@Test
	public void testIfOneTypeOfExceptionIsNotPresent() {

		Exceptional<String> exceptional = Exceptional.of(this::runtimeExceptionThrower);

		StringBuilder result = new StringBuilder();

		exceptional.ifException(IOException.class, ex -> result.append(
				ex.getMessage()));

		assertEquals("", result.toString());

	}

	@Test
	public void testIfOneTypeOfExceptionIsPresent() {

		Exceptional<String> exceptional = Exceptional.of(this::runtimeExceptionThrower);

		StringBuilder result = new StringBuilder();

		exceptional.ifException(RuntimeException.class, ex -> result.append(
				ex.getMessage()
		));

		assertEquals("message", result.toString());
	}


	@Test
	public void testIfExceptionIsNotPresent() {

		Exceptional<String> exceptional = Exceptional.of(this::getValue);

		StringBuilder result = new StringBuilder();

		exceptional.ifException(ex -> result.append(ex.getMessage()));

		assertEquals("", result.toString());
	}

	/**
	 * Now, if you don't care what happened you can just use orElseDo to return a default value.
	 */
	@Test
	public void testOrElseValue() {

		Exceptional<String> exceptional = Exceptional.of(this::getValue);

		assertEquals("Value", exceptional.orElse("Alternative Value"));
	}

	@Test
	public void testOrElseAlternativeValue() {

		Exceptional<String> exceptional = Exceptional.of(this::exceptionThrower);

		assertEquals("Alternative Value", exceptional.orElse("Alternative Value"));
	}

	/**
	 * Finally it has the ability to rethrow the exception if necessary.
	 */
	@Test(expected = InstantiationException.class)
	public void testRethrow() throws Exception {

		Exceptional<String> exceptional = Exceptional.of(this::exceptionThrower);
		exceptional.rethrow();

	}


	@Test
	public void testRethrowWithoutException() throws Exception {

		Exceptional<String> exceptional = Exceptional.of(this::getValue);
		exceptional.rethrow();

	}


	@Test(expected = RuntimeException.class)
	public void testRethrowRuntime() {

		Exceptional<String> exceptional = Exceptional.of(this::runtimeExceptionThrower);
		exceptional.rethrowRunTime();

	}


	@Test
	public void testRethrowRuntimeWithoutException() {

		Exceptional<String> exceptional = Exceptional.of(this::getValue);
		exceptional.rethrowRunTime();

	}

	/*
	 * You can have null return types too
	 */
	@Test(expected = NullPointerException.class)
	public void missingNullableInterface() {

		Exceptional<String> exceptional = Exceptional.of(this::getNullValue);
		exceptional.rethrowRunTime();

	}

	@Test
	public void usingNullableInterface() {
		Exceptional<String> exceptional = Exceptional.ofNullable(this::getNullValue);

		assertFalse(exceptional.isExceptionPresent());
		assertFalse(exceptional.isPresent());
		exceptional.rethrowRunTime();
	}

	/*
	 * If you want to perform an action after
	 */
	@Test
	public void nullableOrElseDo() {

		Exceptional<String> exceptional = Exceptional.ofNullable(this::getNullValue);

		StringBuilder result = new StringBuilder();

		exceptional
				.ifPresent(result::append)
				.orElseDo(result, res -> res.append("Value"));

		assertEquals("Value", result.toString());

	}

	@Test
	public void ifPresentReturn() {

		Exceptional<String> exceptional = Exceptional.ofNullable(this::getValue);
		String actual = exceptional
				.ifPresent()
				.orElse("Other value");

		assertEquals("Value", actual);
	}

	@Test
	public void ifNotPresentReturn() {

		Exceptional<String> exceptional = Exceptional.ofNullable(this::getNullValue);
		String actual = exceptional
				.ifPresent()
				.orElse(this::getValue);

		assertEquals("Value", actual);
	}

	private String exceptionThrower() throws Exception {
		throw new InstantiationException("message");
	}

	private String runtimeExceptionThrower() {
		throw new RuntimeException("message");
	}

	private String getNullValue() {
		return null;
	}

	private String getValue() {
		return "Value";
	}

}