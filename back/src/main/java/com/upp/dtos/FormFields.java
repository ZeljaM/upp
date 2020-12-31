package com.upp.dtos;

import java.util.HashMap;
import java.util.List;


import org.camunda.bpm.engine.form.FormField;
import org.springframework.web.multipart.MultipartFile;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormFields
{

    private String formDataKey;

    private String task;

    private String process;

    private List< FormField > fields;

    private HashMap< String, String > errors;

    private String url;

    private MultipartFile files;

    public FormFields( String task, String process, List< FormField > fields, HashMap< String, String > errors, String key, String url )
    {
        this.task = task;
        this.process = process;
        this.fields = fields;
        this.errors = errors;
        this.formDataKey = key;
        this.url = url;

    }

}
