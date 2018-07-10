package util.stream;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CollectOneTest {

	@Test
	public void singletonCollector() {

		List<Object> objects = new ArrayList<>();

		for (int i = 0; i < 99; i++)
			objects.add(i);

		Integer toCollect = (Integer) objects.stream()
				.filter(o -> (Integer) o == 5)
				.collect(CollectOne.singletonCollector());
		Assert.assertEquals(5, toCollect.longValue());
	}

	@Test(expected = IllegalStateException.class)
	public void singletonCollectorFailing() {

		List<Object> objects = new ArrayList<>();

		for (int i = 0; i < 99; i++)
			objects.add(i);

		Integer toCollect = (Integer) objects.stream()
				.collect(CollectOne.singletonCollector());
	}

	@Test
	public void filteringCollector() {

		List<Object> objects = new ArrayList<>();

		for (int i = 0; i < 99; i++)
			objects.add(i);


		Integer toCollect = (Integer) objects.stream()
				.collect(Collectors.filtering(o -> (Integer) o == 5,
						CollectOne.singletonCollector()));

		Assert.assertEquals(5, toCollect.longValue());
	}

	@Test
	public void findOne() {

		List<Object> objects = new ArrayList<>();

		for (int i = 0; i < 99; i++)
			objects.add(i);

		Integer toCollect = (Integer) objects.stream()
				.collect(CollectOne.find(o -> (Integer) o == 5));

		Assert.assertEquals(5, toCollect.longValue());
	}

}