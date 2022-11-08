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
import javax.persistence.Table;

@Entity
@Table(name="`USER`") // чтобы название не конфликтило с ключевыми словами
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String surname;
    private String name;
    private String patronymic;
    private Long foreign_id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // при удалении "книги у пользователя" из пользователя, он удалится и из БД
    private Set<UserBook> user_books = new HashSet<>(); // 0 или несколько

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true) // при удалении комментария у пользователя, он удалится и из БД
    private Set<Comment> comments = new HashSet<>(); // (скорее всего) 0 или несколько


    public User() { }

    public User(String surname, String name, String patronymic, Long foreign_id) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.foreign_id = foreign_id;
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

    public Long getForeign_id() {
        return this.foreign_id;
    }

    public void setForeign_id(Long foreign_id) {
        this.foreign_id = foreign_id;
    }



    public Set<UserBook> getUserBooks() {
        return (this.user_books);
    }

    public void addUserBook(UserBook user_book){
        if (!user_books.contains(user_book)){
            user_books.add(user_book);
            //user_book.setUser(this);
        }
    }

    public void removeUserBook(UserBook user_book){
        if (user_books.contains(user_book)){
            user_books.remove(user_book);
        }
    }


    public Set<Comment> getComments() {
        return (this.comments);
    }

    public void addComment(Comment comment) {
        if (!comments.contains(comment)){
            comments.add(comment);
            //comment.setUser(this);
        }
    }

    public void removeComment(Comment comment){
        if (comments.contains(comment)){
            comments.remove(comment);
        }
    }
    
}
