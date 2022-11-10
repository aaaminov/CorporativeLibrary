package com.aminov.corporativelibrary.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aminov.corporativelibrary.models.Author;
import com.aminov.corporativelibrary.models.Book;
import com.aminov.corporativelibrary.models.Comment;
import com.aminov.corporativelibrary.models.Genre;
import com.aminov.corporativelibrary.models.User;
import com.aminov.corporativelibrary.repositories.AuthorRepository;
import com.aminov.corporativelibrary.repositories.BookRepository;
import com.aminov.corporativelibrary.repositories.CommentRepository;
import com.aminov.corporativelibrary.repositories.GenreRepository;
import com.aminov.corporativelibrary.repositories.UserRepository;

@Controller
@RequestMapping("/book")
public class BookController {

    private final BookRepository repository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public BookController(BookRepository repository, AuthorRepository authorRepository, GenreRepository genreRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.repository = repository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("")
    public String home() {
        return "redirect:/book/search";
    }
    

    // получение всех книг
    @GetMapping("/search")
    public String search(Model model, 
        @RequestParam(value = "title", required = false, defaultValue = "") String title, 
        @RequestParam(value = "author_id", required = false) List<Long> author_id,
        @RequestParam(value = "genre_id", required = false, defaultValue = "0") Long genre_id) {
        List<Book> books = null;
        // System.out.format("LOLOL "+ String.valueOf(genre_id));
        if (title != "" && author_id != null && genre_id != 0){
            books = repository.findByTitleAndAuthorsAndGenre(title, author_id, genre_id);
        } 
        else if (title != "" && author_id == null && genre_id == 0){
            books = repository.findByTitle(title);
        } 
        else if (title == "" && author_id != null && genre_id == 0){
            books = repository.findByAuthor(author_id);
        } 
        else if (title == "" && author_id == null && genre_id != 0){
            books = repository.findByGenre(genre_id);
        } 
        else if (title != "" && author_id != null && genre_id == 0) {
            books = repository.findByTitleAndAuthors(title, author_id);
        }
        else if (title != "" && author_id == null && genre_id != 0){
            books = repository.findByTitleAndGenre(title, genre_id);
        } 
        else if (title != "" && author_id != null && genre_id == 0) {
            books = repository.findByAuthorsAndGenre(author_id, genre_id);
        }
        else {
            books = repository.findAll();
        }
        model.addAttribute("title", title);
        model.addAttribute("author_ids", author_id);
        model.addAttribute("genre_id", genre_id);
        model.addAttribute("count_books", books.size());

        List<Author> authors = authorRepository.findAll();
        List<Genre> genres = genreRepository.findAll();
        // Set<Author> authors = new HashSet();
        // Set<Genre> genres = new HashSet();
        // for (Book book : books) {
        //     authors.addAll(book.getAuthors());
        //     genres.addAll(book.getGenres());
        // }
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        model.addAttribute("books", books);
        return "book/search";
    }
    
    // получение формы для создания книги
    @GetMapping("/new")
    public String newBook(Model model){
        model.addAttribute("book", new Book());
        return "book/new";
    }

    // создание книги
    @PostMapping()
    public String createBook(@ModelAttribute("book") Book book) { 
        if (book == null){
            return null;
        }
        repository.save(book);
        return "redirect:/book";
    }



    // получение одной книги
    @GetMapping("/{vendor_code}") // {title}-
    public String oneBook(Model model, @PathVariable("vendor_code") Long vendor_code){ // @PathVariable("title") String title, 
        model.addAttribute("book", repository.findByVendorCode(vendor_code));
        // String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
        // User user = userRepository.findByLogin(currentPrincipalName);
        // Book book = repository.findByVendorCode(vendor_code);
        // model.addAttribute("comment", new Comment(user, book, null));
        return "book/one";
    }

    // // получение формы для редактирования книги
    // @GetMapping("/{vendor_code}/edit")
    // public String editBook(Model model, @PathVariable("vendor_code") String vendor_code){
    //     model.addAttribute("book", repository.findById(id).get());
    //     return "books/edit";
    // }

    // // обновление книги
    // @PatchMapping("/{vendor_code}")
    // public String updateBook(@ModelAttribute("book") Book book, @PathVariable("vendor_code") String vendor_code){
    //     repository.save(book);
    //     return "redirect:/books/{vendor_code}";
    // }

    // // удаление книги
    // @DeleteMapping("/{vendor_code}")
    // public String deleteBook(@PathVariable("id") Long id){
    //     repository.deleteById(id);
    //     return "redirect:/books";
    // }
    
    
    // создание комментария
    @PostMapping("{vendor_code}/comments/new")
    public String createComment(@RequestParam(value = "content") String content, @PathVariable("vendor_code") Long vendor_code) { 
        if (content == null){
            return null;
        }
        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByLogin(currentPrincipalName);
        // if (user == null){
        //     return "redirect:/books/{vendor_code}";
        // }
        Book book = repository.findByVendorCode(vendor_code);
        Comment comment = commentRepository.save(new Comment(user, book, content));
        // // commentRepository.save(comment);
        user.addComment(comment);
        book.addComment(comment);
        userRepository.save(user);
        repository.save(book);
        return "redirect:/book/{vendor_code}";
    }

    // // после создания комментария
    // @GetMapping("/{id}/new_comment")
    // public String afterComment(@PathVariable("id") Long id){
    //     return "redirect:/books/{id}/";
    // }
    
}
