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
public class Author {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String surname;
    private String name;
    private String patronymic;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "author_book",
        joinColumns = @JoinColumn(name = "author_id"), // referencedColumnName = "id"
        inverseJoinColumns = @JoinColumn(name = "book_id")) // referencedColumnName = "id"
    private Set<Book> books = new HashSet<>(); // 0 или несколько

    
    public Author() {}
    
    public Author(String surname, String name, String patronymic) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
    }


    public String getFIO(){
        String fio = "";
        if (surname != null)
            fio += surname + " ";
        if (name != null)
            fio += name + " ";
        if (patronymic != null)
            fio += patronymic;
        return fio;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", surname='" + getSurname() + "'" +
            ", name='" + getName() + "'" +
            ", patronymic='" + getPatronymic() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return this.patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }


    
    public Set<Book> getBooks() {
        return (this.books);
    }

    public void addBook(Book book){
        if (!books.contains(book)) {
            books.add(book);
            book.addAuthor(this);
        }
    }

    public void removeBook(Book book){
        if (books.contains(book) && book.getAuthors().size() >= 2) { // чтобы осталось минимум 1
            books.remove(book);
            book.removeAuthor(this);
        }
    }

}
