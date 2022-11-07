package com.aminov.corporativelibrary.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class BookType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book_type")
    private Set<Book> books = new HashSet<>(); // 0 или несколько


    public BookType() { }

    public BookType(String name) {
        this.name = name;
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Set<Book> getBooks() {
        return (this.books);
    }

    public void addBook(Book book) {
        if (!books.contains(book)){
            books.add(book);
            book.setBookType(this);
        }
    }

    public void removeBook(Book book){ // вызывается из класса Book
        if (books.contains(book)){
            books.remove(book);
        }
    }

}
