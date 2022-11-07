package com.aminov.corporativelibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aminov.corporativelibrary.models.Library;

public interface LibraryRepository extends JpaRepository<Library, Long> {
    
}