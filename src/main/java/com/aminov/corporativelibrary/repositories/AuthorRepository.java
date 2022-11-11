package com.aminov.corporativelibrary.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aminov.corporativelibrary.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query(value = 
    "select distinct a.* from book b, author a, author_book ab " + 
    "where b.vendor_code = ab.book_vendor_code and ab.author_id = a.id " + 
    "and b.vendor_code = :vendor_code ",
    nativeQuery = true)
    List<Author> findByBookVendorCode(Long vendor_code);
    
}