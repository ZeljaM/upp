package com.upp.dtos;

import org.camunda.bpm.engine.impl.form.type.SimpleFormFieldType;
import org.camunda.bpm.engine.variable.value.TypedValue;

public class Simple extends SimpleFormFieldType {

    @Override
    protected TypedValue convertValue(TypedValue typedValue) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Object convertFormValueToModelValue(Object o) {
        return null;
    }

    @Override
    public String convertModelValueToFormValue(Object o) {
        return null;
    }
}
