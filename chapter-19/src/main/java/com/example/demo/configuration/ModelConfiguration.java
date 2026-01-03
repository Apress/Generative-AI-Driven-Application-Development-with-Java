package com.example.demo.configuration;

import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import com.example.demo.ml.PhysiologyTranslator;
import com.example.demo.model.PhysiologicalInput;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Configuration
public class ModelConfiguration {

    @Bean
    public Criteria<PhysiologicalInput, float[]> criteria() {
        String modelLocation = Thread.currentThread()
                .getContextClassLoader()
                .getResource("model.onnx")
                .getPath();

        return Criteria.builder()
                .setTypes(PhysiologicalInput.class, float[].class)
                .optModelUrls(modelLocation)
                .optTranslator(new PhysiologyTranslator())
                .optEngine("OnnxRuntime")
                .build();
    }

    @Bean
    public ZooModel<PhysiologicalInput, float[]> model(
            @Qualifier("criteria") Criteria<PhysiologicalInput, float[]> criteria)
            throws Exception {
        return criteria.loadModel();
    }

    @Bean
    public Supplier<Predictor<PhysiologicalInput, float[]>>
    predictorProvider(ZooModel<PhysiologicalInput, float[]> model) {
        return model::newPredictor;
    }
}
