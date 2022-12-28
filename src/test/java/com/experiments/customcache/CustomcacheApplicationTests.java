package com.experiments.customcache;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.experiments.customcache.cache.CacheableEntry;
import com.experiments.customcache.cache.MonoCacheAdapter;
import java.time.Duration;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;


class CustomcacheApplicationTests {

	@Test
	void cacheRoutine() {
		final var ss = new SomeService();

		final var func = new Function<String, Mono<SomeData>>() {
			@Override
			public Mono<SomeData> apply(String s) {
				return ss.getValue();
			}
		};

		final var cacheAdapter = new MonoCacheAdapter<>(3,1000, func);

		var result = cacheAdapter.get("1");

		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("1");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("1");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("1");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("1");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("1");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("1");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("1");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("1");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("1");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("1");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("2");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("2");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("3");

		result = cacheAdapter.get("1");
		assertThat(result.block().value()).isEqualTo(10);

		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("3");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("4");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("4");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("5");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("5");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("5");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("5");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("5");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("5");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("5");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("5");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("5");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("5");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("5");
		assertThat(result.block().value()).isEqualTo(10);
		result = cacheAdapter.get("5");
		assertThat(result.block().value()).isEqualTo(10);

	}
	private static class SomeService {
		Mono<SomeData> getValue() {
			return Mono.just(10).delayElement(Duration.ofSeconds(2)).map(SomeData::new);
		}
	}

	private record SomeData(Integer value) implements CacheableEntry {
	}
}
