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
        "where b.vendor_code = ab.book_vendor_code and ab.author_id = a.id  and  b.vendor_code = gb.book_vendor_code and gb.genre_id = g.id " + 
        "and b.title ilike %:title% " + 
        "and a.id in (:author_ids_q) " + 
        "and g.id = :genre_id_q",
        nativeQuery = true)
    List<Book> findByTitleAndAuthorsAndGenre(@Param("title") String title, @Param("author_ids_q") List<Long> author_ids, @Param("genre_id_q") Long genre_id);
    

    @Query(value = 
        "select b.* from book b, author a, genre g, author_book ab, genre_book gb " + 
        "where b.vendor_code = ab.book_vendor_code and ab.author_id = a.id  and  b.vendor_code = gb.book_vendor_code and gb.genre_id = g.id " + 
        "and b.title ilike %:title% ",
        nativeQuery = true)
    List<Book> findByTitle(@Param("title") String title);
    
    @Query(value = 
        "select b.* from book b, author a, genre g, author_book ab, genre_book gb " + 
        "where b.vendor_code = ab.book_vendor_code and ab.author_id = a.id  and  b.vendor_code = gb.book_vendor_code and gb.genre_id = g.id " + 
        "and a.id in (:author_ids_q) ",
        nativeQuery = true)
    List<Book> findByAuthor(@Param("author_ids_q") List<Long> author_id);

    @Query(value = 
        "select b.* from book b, author a, genre g, author_book ab, genre_book gb " + 
        "where b.vendor_code = ab.book_vendor_code and ab.author_id = a.id  and  b.vendor_code = gb.book_vendor_code and gb.genre_id = g.id " + 
        "and g.id = :genre_id_q",
        nativeQuery = true)
    List<Book> findByGenre(@Param("genre_id_q") Long genre_id);


    @Query(value = 
        "select b.* from book b, author a, genre g, author_book ab, genre_book gb " + 
        "where b.vendor_code = ab.book_vendor_code and ab.author_id = a.id  and  b.vendor_code = gb.book_vendor_code and gb.genre_id = g.id " + 
        "and b.title ilike %:title% " + 
        "and a.id in (:author_ids_q) ",
        nativeQuery = true)
    List<Book> findByTitleAndAuthors(@Param("title") String title, @Param("author_ids_q") List<Long> author_ids);

    @Query(value = 
        "select b.* from book b, author a, genre g, author_book ab, genre_book gb " + 
        "where b.vendor_code = ab.book_vendor_code and ab.author_id = a.id  and  b.vendor_code = gb.book_vendor_code and gb.genre_id = g.id " + 
        "and b.title ilike %:title% " + 
        "and g.id = :genre_id_q",
        nativeQuery = true)
    List<Book> findByTitleAndGenre(@Param("title") String title, @Param("genre_id_q") Long genre_id);

    @Query(value = 
        "select b.* from book b, author a, genre g, author_book ab, genre_book gb " + 
        "where b.vendor_code = ab.book_vendor_code and ab.author_id = a.id  and  b.vendor_code = gb.book_vendor_code and gb.genre_id = g.id " + 
        "and a.id in (:author_ids_q) " + 
        "and g.id = :genre_id_q",
        nativeQuery = true)
    List<Book> findByAuthorsAndGenre(@Param("author_ids_q") List<Long> author_ids, @Param("genre_id_q") Long genre_id);

    @Query(value = 
        "select b.* from book b " + 
        "where b.vendor_code = :vendor_code", // b.title = :title and 
        nativeQuery = true)
    Book findByVendorCode(@Param("vendor_code") Long vendor_code); // @Param("vendor_code") String title, 

}