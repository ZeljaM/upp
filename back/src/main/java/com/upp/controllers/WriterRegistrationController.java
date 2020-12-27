package com.upp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.upp.dtos.FormFields;
import com.upp.dtos.PostFormRequest;
import com.upp.repositories.IGenreRepository;
import com.upp.repositories.IUserRepository;


import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( value = "/register/writer" )
public class WriterRegistrationController
{

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IGenreRepository iGenreRepository;

    @Autowired
    private IUserRepository iUserRepository;

    // register_writer

    @GetMapping( "/process" )
    public ResponseEntity< FormFields > startProcess()
    {
        ProcessInstance instance = runtimeService.startProcessInstanceByKey( "register_writer" );
        Task firstTask = this.taskService.createTaskQuery().processInstanceId( instance.getId() ).list().get( 0 );

        TaskFormData taskFormData = this.formService.getTaskFormData( firstTask.getId() );
        String formKey = taskFormData.getFormKey();

        List< FormField > formFields = taskFormData.getFormFields();

        FormFields returnFormFields = new FormFields( firstTask.getId(), instance.getId(), formFields, new HashMap< String, String >(), formKey );

        return new ResponseEntity< FormFields >( returnFormFields, HttpStatus.OK );

    }


    @PostMapping( "/task" )
    public ResponseEntity< ? > postTask( @RequestBody PostFormRequest form )
    {
        Task currentTask = taskService.createTaskQuery().processInstanceId( form.getProcess() ).singleResult();

        final Map< String, Object > map = new HashMap< String, Object >();

        map.putAll( form.getFields() );

        this.formService.submitTaskForm( form.getTask(), map );

        List< Task > tasks = this.taskService.createTaskQuery().processInstanceId( form.getProcess() ).list();

        Task nextTask = tasks.get( 0 );

        TaskFormData taskFormData = this.formService.getTaskFormData( nextTask.getId() );
        String formKey = taskFormData.getFormKey();
        List< FormField > formFields = taskFormData.getFormFields();

        HashMap< String, String > errors = ( HashMap< String, String > ) runtimeService.getVariable( form.getProcess(), "errors" );
        FormFields returnFormFields = new FormFields( nextTask.getId(), form.getProcess(), formFields, errors, formKey );

        return new ResponseEntity< FormFields >( returnFormFields, HttpStatus.OK );

    }

}
