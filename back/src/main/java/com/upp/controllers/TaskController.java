package com.upp.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


import com.upp.configuration.UrlStorage;
import com.upp.dtos.ApiResponse;
import com.upp.dtos.FormFields;
import com.upp.dtos.PostFormRequest;
import com.upp.dtos.TaskInfo;
import com.upp.models.Book;
import com.upp.models.Role;
import com.upp.models.RoleName;
import com.upp.models.User;
import com.upp.repositories.IBookRepository;
import com.upp.repositories.IRoleRepository;
import com.upp.repositories.IUserRepository;
import com.upp.security.JWTUtil;


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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping( "/api/task" )
@SuppressWarnings( value = "unchecked" )

public class TaskController
{

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private IRoleRepository iRoleRepository;

    @Autowired
    private IBookRepository iBookRepository;

    @GetMapping( "/" )
    public ResponseEntity< ? > next( @RequestHeader( required = true, value = "Authorization" ) final String token )
    {
        List< TaskInfo > tasks = new ArrayList< TaskInfo >();
        Long id = this.jwtUtil.extractId( token );

        List< Task > list = this.taskService.createTaskQuery().taskAssignee( id.toString() ).list();

        list.forEach( t ->
        {
            tasks.add( new TaskInfo( t.getProcessInstanceId(), t.getId(), t.getName(), t.getCreateTime(), null ) );
        } );

        return new ResponseEntity<>( tasks, HttpStatus.OK );

    }


    @PostMapping( "/form" )
    public ResponseEntity< ? > form( @RequestHeader( required = true, value = "Authorization" ) final String token, @RequestBody PostFormRequest form )
    {
        try
        {

            String userId = ( String ) this.runtimeService.getVariable( form.getProcess(), "userId" );

            long parseLong = Long.parseLong( userId );
            User user = this.iUserRepository.findById( parseLong ).get();

            TaskFormData taskFormData = this.formService.getTaskFormData( form.getTask() );
            System.err.println( taskFormData.getFormKey() );
            String formKey = taskFormData.getFormKey();
            List< FormField > formFields = taskFormData.getFormFields();
            String url = UrlStorage.HOST;

            ProcessInstance currentInstance = this.runtimeService.createProcessInstanceQuery().processInstanceId( form.getProcess() ).singleResult();

            if ( taskFormData.getFormKey().equals( "files" ) )
            {
                url += UrlStorage.FILES;
            }
            else if ( taskFormData.getFormKey().equals( "payMembership" ) )
            {
                url += UrlStorage.POST_WRITER;
            }
            else if ( taskFormData.getFormKey().equals( "uploadFileBookForm" ) )
            {
                url += UrlStorage.BOOK_FILE;
            }
            else
            {
                url += UrlStorage.TASK;
            }

            FormFields returnFormFields = new FormFields( form.getTask(), form.getProcess(), formFields, new HashMap<>(), formKey, url );
            if ( taskFormData.getFormKey().equals( "voting" ) )
            {

                List< byte[] > collect = user.getBooks().stream().map( b -> b.getBook() ).collect( Collectors.toList() );

                returnFormFields.setFiles( collect );
            }
            else if ( taskFormData.getFormKey().equals( "makeCommentForm" ) )
            {
                List< byte[] > collect = user.getBooks().stream().map( b -> b.getBook() ).collect( Collectors.toList() );
                returnFormFields.setFiles( new ArrayList<>() );
                returnFormFields.getFiles().addAll( collect );

            }
            else if ( taskFormData.getFormKey().equals( "votePlagiarism" ) )
            {
                ArrayList< String > comments = ( ArrayList< String > ) this.runtimeService.getVariable( form.getProcess(), "comments" );
                returnFormFields.getComments().addAll( comments );
            }
            else if ( taskFormData.getFormKey().equals( "readOrRejectForm" ) )
            {
                String bookId = ( String ) this.runtimeService.getVariable( form.getProcess(), "bookId" );

                Optional< Book > findById = this.iBookRepository.findById( Long.parseLong( bookId ) );
                if ( findById.isPresent() )
                {
                    Book book = findById.get();

                    String bookString = "Title: " + book.getTitle() + " Synopsis: " + book.getSynopsis() + " Genre: " + book.getGenre().getName();
                    returnFormFields.setComments( new ArrayList<>() );
                    returnFormFields.getComments().add( bookString );
                }
            }

            return new ResponseEntity<>( returnFormFields, HttpStatus.OK );
        }
        catch ( Exception e )
        {
            return new ResponseEntity< ApiResponse >( new ApiResponse( "Cant find task with id [ " + form.getTask() + " ]", false ), HttpStatus.BAD_REQUEST );
        }

    }


    @GetMapping( "/start" )
    public ResponseEntity< ? > startTask( @RequestHeader( required = true, value = "Authorization" ) final String token )
    {

        List< TaskInfo > tasks = new ArrayList< TaskInfo >();
        Long extractId = this.jwtUtil.extractId( token );

        User user = this.iUserRepository.findById( extractId ).get();

        Role roleComplete = this.iRoleRepository.findByName( RoleName.ROLE_WRITER ).get();

        if ( user.getRoles().contains( roleComplete ) )
        {
            TaskInfo publishBook = new TaskInfo();
            TaskInfo appealPlagiarism = new TaskInfo();

            publishBook.setTitle( "Publish new book" );
            publishBook.setUrl( UrlStorage.HOST + UrlStorage.BOOK );

            tasks.add( publishBook );

            appealPlagiarism.setTitle( "Appeal against plagiarism" );
            appealPlagiarism.setUrl( UrlStorage.HOST + UrlStorage.PLAGIARISM );

            tasks.add( appealPlagiarism );
        }

        return new ResponseEntity< List< TaskInfo > >( tasks, HttpStatus.OK );

    }


    @PostMapping( "/" )
    public ResponseEntity< ? > postTask( @RequestBody PostFormRequest form )
    {

        final Map< String, Object > map = new HashMap< String, Object >();

        map.putAll( form.getFields() );

        if ( form.getFields().containsKey( "vote" ) )
        {
            List< String > votes = ( List< String > ) this.runtimeService.getVariable( form.getProcess(), "votes" );
            List< String > opinions = ( List< String > ) this.runtimeService.getVariable( form.getProcess(), "voteOpinions" );

            String string = form.getFields().get( "vote" );
            String opinion = form.getFields().get( "voteOpinion" );
            votes.add( string );
            opinions.add( opinion );

            this.runtimeService.removeVariable( form.getProcess(), "votes" );
            this.runtimeService.setVariable( form.getProcess(), "votes", votes );

            this.runtimeService.removeVariable( form.getProcess(), "voteOpinions" );
            this.runtimeService.setVariable( form.getProcess(), "voteOpinions", opinions );

        }
        else if ( form.getFields().containsKey( "commentPlagiarism" ) )
        {

            ArrayList< String > comments = ( ArrayList< String > ) this.runtimeService.getVariable( form.getProcess(), "comments" );

            comments.add( form.getFields().get( "commentPlagiarism" ) );

            this.runtimeService.removeVariable( form.getProcess(), "comments" );
            this.runtimeService.setVariable( form.getProcess(), "comments", comments );
        }
        else if ( form.getFields().containsKey( "plagiarismVote" ) )
        {
            ArrayList< String > votes = ( ArrayList< String > ) this.runtimeService.getVariable( form.getProcess(), "votes" );
            votes.add( form.getFields().get( "plagiarismVote" ) );

            this.runtimeService.removeVariable( form.getProcess(), "votes" );
            this.runtimeService.setVariable( form.getProcess(), "votes", votes );
        }

        this.formService.submitTaskForm( form.getTask(), map );

        return new ResponseEntity< ApiResponse >( new ApiResponse( "Finished task", true ), HttpStatus.OK );

    }


    @PostMapping( "/files" )
    public ResponseEntity< ApiResponse > upload( @RequestParam( "files" ) final MultipartFile file,
            @RequestHeader( required = true, value = "Authorization" ) final String token, @RequestParam final String task, @RequestParam final String process )
    {

        try
        {

            Long extractId = this.jwtUtil.extractId( token );

            User user = this.iUserRepository.findById( extractId ).get();

            String bookId = ( String ) this.runtimeService.getVariable( process, "bookId" );

            Book book = this.iBookRepository.findById( Long.parseLong( bookId ) ).get();

            book.setBook( file.getBytes() );

            return null;
        }
        catch ( Exception e )
        {
            System.err.println( e );
            return new ResponseEntity< ApiResponse >( new ApiResponse( "Error", false ), HttpStatus.BAD_REQUEST );
        }

    }

}
