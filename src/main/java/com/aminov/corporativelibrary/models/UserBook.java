package com.aminov.corporativelibrary.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UserBook {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // только 1

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_vendor_code", nullable = false)
    private Book book; // только 1

    private Date issue_date;
    private Date return_date;


    public UserBook() { }

    public UserBook(User user, Book book, Date issue_date, Date return_date) {
        this.user = user;
        this.book = book;
        this.issue_date = issue_date;
        this.return_date = return_date;
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
    //             this.user.removeUserBook(this); // убрать привязку у старого объекта
    //         this.user = user;
    //         this.user.addUserBook(this);
    //     }
    // }


    public Book getBook() {
        return this.book;
    }

    // public void setBook(Book book) {
    //     if (this.book != book) {
    //         if (this.book != null)
    //             this.book.removeUserBook(this); // убрать привязку у старого объекта
    //         this.book = book;
    //         this.book.addUserBook(this);
    //     }
    // }



    public Date getIssue_date() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(issue_date);
        // calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }

    public void setIssue_date(Date issue_date) {
        this.issue_date = issue_date;
    }

    public Date getReturn_date() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(return_date);
        // calendar.add(Calendar.MONTH, -1);
        // System.out.println("'LOLOL getReturn_date' - " + return_date.toString() + calendar.toString());
        return calendar.getTime();
    }

    public void setReturn_date(Date return_date) {
        this.return_date = return_date;
    }

    
    public String getIssue_dateStr() {
        return new SimpleDateFormat("dd.MM.yyyy").format(getIssue_date());
    }

    public String getReturn_dateStr() {
        return new SimpleDateFormat("dd.MM.yyyy").format(getReturn_date());
    }


}
