package com.upp.dtos;

import java.io.File;


import org.camunda.bpm.engine.impl.form.type.SimpleFormFieldType;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.camunda.bpm.engine.variable.value.TypedValue;

public class FileUpload extends SimpleFormFieldType
{

    @Override
    protected TypedValue convertValue( TypedValue propertyValue )
    {
        if ( propertyValue instanceof FileValue )
        {
            return propertyValue;
        }
        else
        {
            Object value = propertyValue.getValue();
            if ( value == null )
            {
                return Variables.fileValue( "null" ).create();
            }
            else
            {
                return Variables.fileValue( ( File ) value );
            }
        }

    }


    @Override
    public String getName()
    {
        return "customfile";

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
