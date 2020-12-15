package com.upp.repositories;

import java.util.Optional;


import com.upp.models.Genre;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IGenreRepository extends JpaRepository< Genre, Long >
{

    Optional< Genre > findByName( String name );

}
