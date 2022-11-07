package com.aminov.corporativelibrary.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "library")
    private Set<Book> books = new HashSet<>(); // 0 или несколько


    public Library() { }

    public Library(String name, String address) {
        this.name = name;
        this.address = address;
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

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public Set<Book> getBooks() {
        return (this.books);
    }

    public void addBook(Book book) {
        if (!books.contains(book)){
            books.add(book);
            book.setLibrary(this);
        }
    }

    public void removeBook(Book book){ // вызывается из класса Book
        if (books.contains(book)){
            books.remove(book);
        }
    }
    
}
