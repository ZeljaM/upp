package com.upp.dtos;

import java.util.HashMap;
import java.util.Map;


import org.camunda.bpm.engine.impl.form.type.SimpleFormFieldType;
import org.camunda.bpm.engine.variable.value.TypedValue;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiEnum extends SimpleFormFieldType
{

    private String name;

    private Map< String, String > values = new HashMap<>();

    @Override
    protected TypedValue convertValue( TypedValue propertyValue )
    {
        return null;

    }


    @Override
    public String getName()
    {
        return this.name;

    }


    @Override
    public Object convertFormValueToModelValue( Object propertyValue )
    {
        return null;

    }


    @Override
    public String convertModelValueToFormValue( Object modelValue )
    {
        return null;

    }

}
