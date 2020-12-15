package com.upp.dtos;

import java.util.HashMap;
import java.util.List;


import org.camunda.bpm.engine.form.FormField;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormFields
{

    private String task;

    private String process;

    private List< FormField > fields;

    private HashMap< String, String > errors;

    public FormFields( String task, String process, List< FormField > fields )
    {
        this.task = task;
        this.process = process;
        this.fields = fields;
        this.errors = new HashMap<>();

    }

}
