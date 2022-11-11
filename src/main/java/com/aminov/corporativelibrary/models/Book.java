package com.aminov.corporativelibrary.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
// @IdClass(BookKey.class)
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vendor_code;
    private String title;
    
    @Column(length = 500)
    private String description;
    private String cover_link;
    private String file_link;

    @ManyToOne()
    @JoinColumn(name = "book_type_id", nullable = false)
    private BookType book_type; // только 1

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "books")
    private Set<Author> authors = new HashSet<>(); // 1 или несколько

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "books")
    private Set<Genre> genres = new HashSet<>(); // 1 или несколько

    @ManyToOne()
    @JoinColumn(name = "library_id", nullable = false)
    private Library library; // только 1

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true) // при удалении "книги у пользователя" из книги, он удалится и из БД
    private Set<UserBook> user_books = new HashSet<>(); // 0 или несколько

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true) // при удалении комментария у книги, он удалится и из БД
    private Set<Comment> comments = new HashSet<>(); // 0 или несколько


    public Book() {}

    public Book(String title, String description, String cover_link, String file_link, BookType book_type, Library library) {
        // this.vendor_code = vendor_code;
        this.title = title;
        this.description = description;
        this.cover_link = cover_link;
        this.file_link = file_link;
        setBookType(book_type);
        setLibrary(library);;
    }

    @Override
    public String toString() {
        return "Book = {" +
            // " id='" + getId() + "'" +
            ", vendor_code='" + getVendorCode() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", cover_link='" + getCover_link() + "'" +
            ", file_link='" + getFile_link() + "'" +
            ", book_type='" + getBookType() + "'" +
            ", authors='" + getAuthors() + "'" +
            ", genres='" + getGenres() + "'" +
            ", library='" + getLibrary() + "'" +
            ", user_books='" + getUserBooks() + "'" +
            ", comments='" + getComments() + "'" +
            "}";
    }


    // public Long getId() {
    //     return this.id;
    // }

    // public void setId(Long id) {
    //     this.id = id;
    // }

    public Long getId() {
        return this.vendor_code;
    }

    public void setId(Long vendor_code) {
        this.vendor_code = vendor_code;
    }


    public Long getVendorCode() {
        return this.vendor_code;
    }

    public void setVendorCode(Long vendor_code) {
        this.vendor_code = vendor_code;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover_link() {
        return this.cover_link;
    }

    public void setCover_link(String cover_link) {
        this.cover_link = cover_link;
    }

    public String getFile_link() {
        return this.file_link;
    }

    public void setFile_link(String file_link) {
        this.file_link = file_link;
    }



    public BookType getBookType() {
        return this.book_type;
    }

    public void setBookType(BookType book_type) {
        if (this.book_type != book_type) {
            if (this.book_type != null)
                this.book_type.removeBook(this); // убрать привязку у старого объекта
            this.book_type = book_type;
            this.book_type.addBook(this);
        }
    }


    public Set<Author> getAuthors() {
        return (this.authors);
    }

    public void addAuthor(Author author){  // вызывается из класса Author
        if (!authors.contains(author)){
            authors.add(author);
            //author.addBook(this);
        }
    }

    public void removeAuthor(Author author){ // вызывается из класса Author
        if (authors.contains(author)) {
            authors.remove(author);
            //author.removeBook(this);
        }
    }


    public Set<Genre> getGenres() {
        return (this.genres);
    }

    public void addGenre(Genre genre) { // вызывается из класса Genre
        if (!genres.contains(genre)){
            genres.add(genre);
            // genre.addBook(this);
            // // System.out.println();
            // // System.out.println(this.toString());
            // // System.out.println();
        }
    }
    public void removeGenre(Genre genre){ // вызывается из класса Genre
        if (genres.contains(genre)){
            genres.remove(genre);
            genre.removeBook(this);
        }
    }


    public Library getLibrary() {
        return this.library;
    }

    public void setLibrary(Library library) {
        if (this.library != library) {
            if (this.library != null)
                this.library.removeBook(this); // убрать привязку у старого объекта
            this.library = library;
            this.library.addBook(this);
        }
    }



    public Set<UserBook> getUserBooks() {
        return (this.user_books);
    }

    public void addUserBook(UserBook user_book){
        if (!user_books.contains(user_book)){
            user_books.add(user_book);
            //user_book.setBook(this);
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
            //comment.setBook(this);
        }
    }

    public void removeComment(Comment comment){
        if (comments.contains(comment)){
            comments.remove(comment);
        }
    }


    public String getTitleAndVendorCode(){
        return title + " - " + String.valueOf(vendor_code);
    }
    
}
