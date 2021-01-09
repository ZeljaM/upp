package com.upp.models;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table( name = "model_book", uniqueConstraints =
{ @UniqueConstraint( columnNames =
{ "isbn" } ) } )
public class Book
{

    public Book( Map< String, String > map )
    {
        this.title = map.get( "title" );
        this.synopsis = map.get( "synopsis" );

    }

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private String title;

    @ManyToOne( fetch = FetchType.EAGER )
    private Genre genre;

    private Integer isbn;

    @ManyToMany( fetch = FetchType.LAZY )
    @JoinTable( name = "model_book_keywords", joinColumns = @JoinColumn( name = "book_id" ), inverseJoinColumns = @JoinColumn( name = "keyword" ) )
    private Set< KeyWord > keyWords = new HashSet<>();

    private String publisher;

    private Short year;

    private String publishPlace;

    private Short pages;

    private String synopsis;

    @Lob
    private byte[] book;

    @ManyToMany( fetch = FetchType.LAZY )
    @JoinTable( name = "model_book_editors", joinColumns = @JoinColumn( name = "book_id" ), inverseJoinColumns = @JoinColumn( name = "user_id" ) )
    private Set< User > editors = new HashSet<>();

}
