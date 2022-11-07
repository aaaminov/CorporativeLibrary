package com.aminov.corporativelibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aminov.corporativelibrary.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    
}