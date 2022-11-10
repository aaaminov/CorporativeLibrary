package com.aminov.corporativelibrary.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aminov.corporativelibrary.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = 
        "select b.* from book b, author a, genre g, author_book ab, genre_book gb " + 
        "where b.id = ab.book_id and ab.author_id = a.id  and  b.id = gb.book_id and gb.genre_id = g.id " + 
        "and (b.title ilike %:title% " + 
        "and (a.surname ilike %:author% or a.name ilike %:author% or a.patronymic ilike %:author%) " + 
        "and g.name ilike %:genre%)",
        nativeQuery = true)
    List<Book> findByTitleAndAuthorAndGenre(@Param("title") String title, @Param("author") String author, @Param("genre") String genre);


    
}