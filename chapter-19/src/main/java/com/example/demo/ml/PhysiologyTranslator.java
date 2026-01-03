package com.example.demo.ml;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.NoBatchifyTranslator;
import ai.djl.translate.TranslatorContext;
import com.example.demo.model.PhysiologicalInput;

public class PhysiologyTranslator
        implements NoBatchifyTranslator<PhysiologicalInput, float[]> {

    public PhysiologyTranslator() {}

    @Override
    public NDList processInput(TranslatorContext ctx, PhysiologicalInput input) {
        float[] data = {input.waist, input.weight, input.pulse};
        NDArray array = ctx.getNDManager().create(data, new Shape(1, 3));
        return new NDList(array);
    }

    @Override
    public float[] processOutput(TranslatorContext ctx, NDList list) {
        float[] data = list.get(0).toFloatArray();
        return data;
    }
}
