package com.aminov.corporativelibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aminov.corporativelibrary.models.UserBook;

public interface UserBookRepository extends JpaRepository<UserBook, Long> {
    
}