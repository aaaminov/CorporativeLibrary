package com.aminov.corporativelibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aminov.corporativelibrary.models.BookType;

public interface BookTypeRepository extends JpaRepository<BookType, Long> {
    
}