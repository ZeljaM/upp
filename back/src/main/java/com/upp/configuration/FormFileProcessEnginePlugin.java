package com.upp.configuration;

import java.util.ArrayList;
import java.util.List;


import com.upp.dtos.FileUpload;


import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.form.type.AbstractFormFieldType;
import org.springframework.stereotype.Component;

@Component
public class FormFileProcessEnginePlugin extends AbstractProcessEnginePlugin
{

    @Override
    public void preInit( ProcessEngineConfigurationImpl processEngineConfiguration )
    {

        if ( processEngineConfiguration.getCustomFormTypes() == null )
        {
            processEngineConfiguration.setCustomFormTypes( new ArrayList< AbstractFormFieldType >() );
        }

        List< AbstractFormFieldType > formTypes = processEngineConfiguration.getCustomFormTypes();
        formTypes.add( new FileUpload() );

    }

}
