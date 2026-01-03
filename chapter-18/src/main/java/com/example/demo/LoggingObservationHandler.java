package com.example.demo;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import org.springframework.stereotype.Component;

@Component
public class LoggingObservationHandler implements ObservationHandler<Observation.Context> {

    @Override
    public boolean supportsContext(Observation.Context context) {
        return true;
    }

    @Override
    public void onStart(Observation.Context context) {
        System.out.println("Starting observation: " + context.getName());
        System.out.println(context.getHighCardinalityKeyValues());
        System.out.println("Starting observation: " + context.getAllKeyValues());
    }

    @Override
    public void onStop(Observation.Context context) {
        System.out.println("Completed observation: " + context.getName());
        System.out.println(context.getHighCardinalityKeyValues());
        System.out.println("Completed observation: " + context.getAllKeyValues());
    }
}

