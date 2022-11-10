package com.aminov.corporativelibrary.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Comment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // только 1

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_vendor_code", nullable = false)
    private Book book; // только 1

    private String content;


    public Comment() { }

    public Comment(User user, Book book, String content) {
        this.user = user;
        this.book = book;
        this.content = content;
    }
    

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public User getUser() {
        return this.user;
    }

    // public void setUser(User user) {
    //     if (this.user != user){
    //         if (this.user != null)
    //             this.user.removeComment(this); // убрать привязку у старого объекта
    //         this.user = user;
    //         this.user.addComment(this);
    //     }
    // }


    public Book getBook() {
        return this.book;
    }

    // public void setBook(Book book) {
    //     if (this.book != book) {
    //         if (this.book != null)
    //             this.book.removeComment(this); // убрать привязку у старого объекта
    //         this.book = book;
    //         this.book.addComment(this);
    //     }
    // }



    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
