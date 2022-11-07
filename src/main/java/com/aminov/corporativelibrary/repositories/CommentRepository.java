package com.aminov.corporativelibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aminov.corporativelibrary.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
}