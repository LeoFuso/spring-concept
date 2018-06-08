package util;

@FunctionalInterface
public interface TryRunnable {
	void run() throws Throwable;
}