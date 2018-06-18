package util.stream;

import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public interface CollectOne {

	/**
	 * Returns the single element of a {@link java.util.Collection}, and will throw an {@link IllegalStateException}
	 * if the {@link java.util.Collection} has more than one element
	 *
	 * @param <T> the result type of the {@code collector}
	 * @return Returns the single element of a {@link java.util.Collection}, and will throw an
	 * {@link IllegalStateException} if the {@link java.util.Collection} has more than one element
	 * @throws IllegalStateException if the {@link java.util.Collection} has more than one element
	 */
	static <T> Collector<T, ?, T> singletonCollector() {
		return Collectors.collectingAndThen(
				Collectors.toList(),
				list -> {
					if (list.size() != 1) {
						throw new IllegalStateException("The size( " + list.size() + " ) of list is illegal.");
					}
					return list.get(0);
				}
		);
	}

	/**
	 * Returns the first element of a {@link java.util.Collection}, and will throw an {@link IllegalStateException}
	 * if the {@link java.util.Collection} is empty
	 *
	 * @param <T> the result type of the {@code collector}
	 * @return Returns the first element of a {@link java.util.Collection}, and will throw an {@link IllegalStateException}
	 * if the {@link java.util.Collection} is empty
	 * @throws IllegalStateException if the {@link java.util.Collection} is empty
	 */
	static <T> Collector<T, ?, T> collectFirstOne() {

		return Collectors.collectingAndThen(
				Collectors.toList(),
				list -> {
					if (list.size() == 0)
						throw new IllegalStateException();
					return list.get(0);
				}
		);
	}

	/**
	 * @param predicate a {@code predicate} to be applied to the input elements
	 * @param <T>       the result type of the {@code collector}
	 * @return Returns the single element resulting of the {@code predicate} applied to the {@link java.util.Collection}
	 * @throws IllegalStateException if the resulting {@link java.util.Collection} has more than one element
	 * @apiNote It encapsulates the {@link Collectors#filtering(Predicate, Collector)} with the {@link #singletonCollector()}
	 * behavior
	 */
	static <T> Collector<T, ?, T> find(Predicate<? super T> predicate) {
		return Collectors.filtering(predicate, CollectOne.singletonCollector());
	}
}
