package com.upp.repositories;

import com.upp.models.Book;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookRepository extends JpaRepository< Book, Long >
{

}
