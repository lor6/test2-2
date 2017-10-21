package com.baeldung.metrics.micrometer;

import com.netflix.spectator.atlas.AtlasConfig;
import io.micrometer.atlas.AtlasMeterRegistry;
import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.core.instrument.stats.hist.Histogram;
import io.micrometer.core.instrument.stats.quantile.WindowSketchQuantiles;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.micrometer.core.instrument.Meter.Type;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author aiet
 */
public class MicrometerAtlasTest {

    private AtlasConfig atlasConfig;

    @Before
    public void init() {
        atlasConfig = new AtlasConfig() {

            @Override
            public Duration step() {
                return Duration.ofSeconds(1);
            }

            @Override
            public String get(String k) {
                return null;
            }
        };
    }

    @Test
    public void givenCompositeRegistries_whenRecordMeter_thenAllRegistriesRecorded() {
        CompositeMeterRegistry compositeRegistry = new CompositeMeterRegistry();

        SimpleMeterRegistry oneSimpleMeter = new SimpleMeterRegistry();
        AtlasMeterRegistry atlasMeterRegistry = new AtlasMeterRegistry(atlasConfig, Clock.SYSTEM);

        compositeRegistry.add(oneSimpleMeter);
        compositeRegistry.add(atlasMeterRegistry);

        compositeRegistry.gauge("baeldung.heat", 90);

        Optional<Gauge> oneGauge = oneSimpleMeter
          .find("baeldung.heat")
          .gauge();
        assertTrue(oneGauge.isPresent());
        Iterator<Measurement> measurements = oneGauge
          .get()
          .measure()
          .iterator();

        assertTrue(measurements.hasNext());
        assertThat(measurements
          .next()
          .getValue(), equalTo(90.00));

        Optional<Gauge> atlasGauge = atlasMeterRegistry
          .find("baeldung.heat")
          .gauge();
        assertTrue(atlasGauge.isPresent());
        Iterator<Measurement> anotherMeasurements = atlasGauge
          .get()
          .measure()
          .iterator();

        assertTrue(anotherMeasurements.hasNext());
        assertThat(anotherMeasurements
          .next()
          .getValue(), equalTo(90.00));
    }

    @Test
    public void givenGlobalRegistry_whenIncrementAnywhere_thenCounted() {
        class CountedObject {
            private CountedObject() {
                Metrics
                  .counter("objects.instance")
                  .increment(1.0);
            }
        }
        Metrics.addRegistry(new SimpleMeterRegistry());

        Metrics
          .counter("objects.instance")
          .increment();
        new CountedObject();

        Optional<Counter> counterOptional = Metrics.globalRegistry
          .find("objects.instance")
          .counter();

        assertTrue(counterOptional.isPresent());
        assertTrue(counterOptional
          .get()
          .count() == 2.0);
    }

    @Test
    public void givenCounter_whenIncrement_thenValueChanged() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        Counter counter = Counter
          .builder("objects.instance")
          .description("indicates instance count of the object")
          .tags("dev", "performance")
          .register(registry);

        counter.increment(2.0);
        assertTrue(counter.count() == 2);

        counter.increment(-1);
        assertTrue(counter.count() == 2);
    }

    @Test
    public void givenTimer_whenWrapTasks_thenTimeRecorded() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        Timer timer = registry.timer("app.event");
        timer.record(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(1500);
            } catch (InterruptedException ignored) {
            }
        });

        timer.record(3000, TimeUnit.MILLISECONDS);

        assertTrue(2 == timer.count());
        assertTrue(4510 > timer.totalTime(TimeUnit.MILLISECONDS) && 4500 <= timer.totalTime(TimeUnit.MILLISECONDS));
    }

    @Test
    public void givenLongTimer_whenRunTasks_thenTimerRecorded() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        LongTaskTimer longTaskTimer = LongTaskTimer
          .builder("3rdPartyService")
          .register(registry);

        long currentTaskId = longTaskTimer.start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ignored) {
        }
        long timeElapsed = longTaskTimer.stop(currentTaskId);

        assertTrue(timeElapsed / (int) 1e9 == 2);
    }

    @Test
    public void givenGauge_whenMeterListSize_thenCurrentSizeMonitored() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        List<String> list = new ArrayList<>(4);
        Gauge gauge = Gauge
          .builder("cache.size", list, List::size)
          .register(registry);

        assertTrue(gauge.value() == 0.0);

        list.add("1");
        assertTrue(gauge.value() == 1.0);
    }

    @Test
    public void givenDistributionSummary_whenRecord_thenSummarized() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        DistributionSummary distributionSummary = DistributionSummary
          .builder("request.size")
          .baseUnit("bytes")
          .register(registry);
        distributionSummary.record(3);
        distributionSummary.record(4);
        distributionSummary.record(5);

        assertTrue(3 == distributionSummary.count());
        assertTrue(12 == distributionSummary.totalAmount());
    }

    @Test
    public void givenTimer_whenEnrichWithQuantile_thenQuantilesComputed() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        Timer timer = Timer
          .builder("test.timer")
          .quantiles(WindowSketchQuantiles
            .quantiles(0.3, 0.5, 0.95)
            .create())
          .register(registry);

        timer.record(2, TimeUnit.SECONDS);
        timer.record(2, TimeUnit.SECONDS);
        timer.record(3, TimeUnit.SECONDS);
        timer.record(4, TimeUnit.SECONDS);
        timer.record(8, TimeUnit.SECONDS);
        timer.record(13, TimeUnit.SECONDS);

        Map<String, Integer> quantileMap = extractTagValueMap(registry, Type.Gauge, 1e9);

        assertThat(quantileMap.keySet(), hasItems("quantile=0.3", "quantile=0.5", "quantile=0.95"));
        assertThat(quantileMap.get("quantile=0.3"), is(2));
        assertThat(quantileMap.get("quantile=0.5"), is(3));
        assertThat(quantileMap.get("quantile=0.95"), is(8));
    }

    private Map<String, Integer> extractTagValueMap(MeterRegistry registry, Type meterType, double valueDivisor) {
        return registry
          .getMeters()
          .stream()
          .filter(meter -> meter.getType() == meterType)
          .collect(Collectors.toMap(meter -> {
              Tag tag = meter
                .getId()
                .getTags()
                .iterator()
                .next();
              return tag.getKey() + "=" + tag.getValue();
          }, meter -> (int) (meter
            .measure()
            .iterator()
            .next()
            .getValue() / valueDivisor)));
    }

    @Test
    public void givenDistributionSummary_whenEnrichWithHistograms_thenDataAggregated() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        DistributionSummary hist = DistributionSummary
          .builder("summary")
          .histogram(Histogram.linear(0, 10, 5))
          .register(registry);

        hist.record(3);
        hist.record(8);
        hist.record(20);
        hist.record(40);
        hist.record(13);
        hist.record(26);

        Map<String, Integer> histograms = extractTagValueMap(registry, Type.Counter, 1.0);

        assertThat(histograms, allOf(hasEntry("bucket=0.0", 0), hasEntry("bucket=10.0", 2), hasEntry("bucket=20.0", 2), hasEntry("bucket=30.0", 1), hasEntry("bucket=40.0", 1), hasEntry("bucket=Infinity", 0)));
    }

    @Test
    public void givenTimer_whenEnrichWithTimescaleHistogram_thenTimeScaleDataCollected() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        Timer timer = Timer
          .builder("timer")
          .histogram(Histogram.linearTime(TimeUnit.MILLISECONDS, 0, 200, 3))
          .register(registry);

        timer.record(1000, TimeUnit.MILLISECONDS);
        timer.record(23, TimeUnit.MILLISECONDS);
        timer.record(450, TimeUnit.MILLISECONDS);
        timer.record(341, TimeUnit.MILLISECONDS);
        timer.record(500, TimeUnit.MILLISECONDS);

        Map<String, Integer> histograms = extractTagValueMap(registry, Type.Counter, 1.0);

        assertThat(histograms, allOf(hasEntry("bucket=0.0", 0), hasEntry("bucket=2.0E8", 1), hasEntry("bucket=4.0E8", 1), hasEntry("bucket=Infinity", 3)));

    }

}
