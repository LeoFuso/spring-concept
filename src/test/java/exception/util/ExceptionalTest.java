package exception.util;

import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.*;

public class ExceptionalTest {

	/**
	 * This shows that if the value is present then you should have no problem getting it.
	 */
	@Test
	public void testSimpleValuePresent() {

		Exceptional<String> value = Exceptional.of("Value");
		assertTrue(value.isPresent());
		assertEquals("Value", value.get());

	}

	/**
	 * If the value does not exists and you pass a Consumer to it, the consumer will not execute.
	 */
	@Test
	public void testIfPresentWithValue() {

		Exceptional<String> value = Exceptional.of("Value");

		StringBuilder result = new StringBuilder();
		value.ifPresent(val -> result.append(val));

		assertEquals("Value", result.toString());
	}

	public void testIfPresentWithoutValue() {

		Exceptional<String> value = Exceptional.of(new IllegalArgumentException("12345"));

		StringBuilder result = new StringBuilder();
		value.ifPresent(result::append);

		assertEquals("", result.toString());
	}

	/**
	 * You can also test to see if an Exception is present and take actions accordingly.
	 */
	@Test(expected = RuntimeException.class)
	public void testExceptionIsPresent() {
		Exceptional<String> value = Exceptional.of(new IllegalArgumentException("12345"));
		assertFalse(value.isPresent());
		value.get();
	}


	@Test
	public void testIfAnyExceptionIsPresent() {
		Exceptional<String> value = Exceptional.of(new IllegalArgumentException("12345"));
		StringBuilder result = new StringBuilder();
		value.ifExceptionPresent(ex -> result.append(ex.getMessage()));
		assertEquals("12345", result.toString());
	}


	@Test
	public void testIfOneTypeOfExceptionIsNotPresent() {

		Exceptional<String> value = Exceptional.of(new IllegalArgumentException("12345"));

		StringBuilder result = new StringBuilder();
		value.ifExceptionPresent(IOException.class, ex -> result.append(
				ex.getMessage()));

		assertEquals("", result.toString());
	}


	@Test
	public void testIfOneTypeOfExceptionIsPresent() {

		Exceptional<String> value = Exceptional.of(new IOException("12345"));

		StringBuilder result = new StringBuilder();

		value.ifExceptionPresent(IOException.class, ex -> result.append(
				ex.getMessage()));

		assertEquals("12345", result.toString());
	}


	@Test
	public void testIfExceptionIsNotPresent() {

		Exceptional<String> value = Exceptional.of("Value");

		StringBuilder result = new StringBuilder();

		value.ifExceptionPresent(ex -> result.append(ex.getMessage()));

		assertEquals("", result.toString());
	}

	/**
	 * Now, if you don't care what happened you can just use orElse to return a default value.
	 */
	@Test
	public void testOrElseValue() {

		Exceptional<String> value = Exceptional.of("Value");

		assertEquals("Value", value.orElse("Alternative Value"));
	}

	@Test
	public void testOrElseAlternativeValue() {

		Exceptional<String> value = Exceptional.of(new IllegalArgumentException("12345"));

		assertEquals("Alternative Value", value.orElse("Alternative Value"));
	}

	/**
	 * Finally it has the ability to rethrow the exception if necessary.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testRethrow() throws Exception {

		Exceptional<String> value = Exceptional.of(new IllegalArgumentException("12345"));

		value.rethrow();
	}


	@Test
	public void testRethrowWithoutException() throws Exception {

		Exceptional<String> value = Exceptional.of("Value");

		value.rethrow();
	}


	@Test(expected = RuntimeException.class)
	public void testRethrowRuntime() throws Exception {

		Exceptional<String> value = Exceptional.of(new IllegalArgumentException("12345"));

		value.rethrowRuntime();
	}


	@Test
	public void testRethrowRuntimeWithoutException() throws Exception {

		Exceptional<String> value = Exceptional.of("Value");
		value.rethrowRuntime();

	}

	@Test
	public void aBunch(){

		/* Null String */
		String nullable = null;

		boolean isPresent = util.Exceptional.of(exceptionThrower())
				.isPresent();

		assertFalse(isPresent);

	}


	private String exceptionThrower(){
		throw new NullPointerException();
	}
}