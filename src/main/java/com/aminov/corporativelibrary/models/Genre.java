package com.aminov.corporativelibrary.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Genre {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "genre_book",
        joinColumns = @JoinColumn(name = "genre_id"), //referencedColumnName = "id"
        inverseJoinColumns = @JoinColumn(name = "book_vendor_code", referencedColumnName = "vendor_code")) // referencedColumnName = "id"
    private Set<Book> books = new HashSet<>(); // 0 или несколько


    public Genre() { }

    public Genre(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            "}";
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

    public void addBook(Book book){
        if (!books.contains(book)) {
            books.add(book);
            book.addGenre(this);
        }
    }

    public void removeBook(Book book){
        if (books.contains(book) && book.getGenres().size() >= 2) { // чтобы осталось минимум 1
            books.remove(book);
            book.removeGenre(this);
        }
    }

}
