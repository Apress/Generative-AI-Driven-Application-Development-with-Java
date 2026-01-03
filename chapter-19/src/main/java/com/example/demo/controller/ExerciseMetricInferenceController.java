package com.example.demo.controller;

import ai.djl.inference.Predictor;
import ai.djl.translate.TranslateException;
import com.example.demo.model.PredictionResponse;
import com.example.demo.model.PhysiologicalInput;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

@RestController
public class ExerciseMetricInferenceController {
    @Resource
    private Supplier<Predictor<PhysiologicalInput, float[]>> predictorSupplier;

    @PostMapping("/inference")
    PredictionResponse detectExerciseMetric(@RequestBody PhysiologicalInput physiologicalInput)
            throws TranslateException {
        try (var p = predictorSupplier.get()) {
            float[] response = p.predict(physiologicalInput);
            return new PredictionResponse(((int) response[0]), (int) response[1], (int) response[2]);
        }
    }
}
