package com.aminov.corporativelibrary.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aminov.corporativelibrary.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Query(value = 
    "select distinct g.* from book b, genre g, genre_book gb " + 
    "where b.vendor_code = gb.book_vendor_code and gb.genre_id = g.id " + 
    "and b.vendor_code = :vendor_code ",
    nativeQuery = true)
    List<Genre> findByBookVendorCode(Long vendor_code);
    
}