package ru.otus.hw.metrics;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.BaseUnits;
import io.micrometer.core.instrument.search.MeterNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class MetricsHelper {

    private final MeterRegistry registry;

    public MetricsHelper(MeterRegistry registry) {
        this.registry = registry;
    }

    public Counter registrateCounter(String name, String unit, String description) {
        try {
            return registry.get(name).counter();
        } catch (MeterNotFoundException ex) {
            log.info("Register counter {}", name);
        }
        var baseUnit = StringUtils.hasText(unit) ? unit : BaseUnits.EVENTS;
        return Counter.builder(name)
                .baseUnit(baseUnit)
                .description(description)
                .register(registry);
    }
}
