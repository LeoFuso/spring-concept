package util;

@FunctionalInterface
public interface TrySupplier<T> {
	T tryGet() throws Throwable;
}