package com.aminov.corporativelibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aminov.corporativelibrary.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    
}