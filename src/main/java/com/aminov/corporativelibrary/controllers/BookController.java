package com.aminov.corporativelibrary.controllers;

import java.util.HashSet;
import java.util.List;
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

import com.aminov.corporativelibrary.models.Book;
import com.aminov.corporativelibrary.models.Comment;
import com.aminov.corporativelibrary.models.User;
import com.aminov.corporativelibrary.repositories.BookRepository;
import com.aminov.corporativelibrary.repositories.CommentRepository;
import com.aminov.corporativelibrary.repositories.UserRepository;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookRepository repository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public BookController(BookRepository repository, UserRepository userRepository, CommentRepository commentRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping()
    public String home() {
        return "redirect:/books/search";
    }
    

    // получение всех книг
    @GetMapping("/search")
    public String search(Model model, 
        @RequestParam(value = "title", required = false) String title, 
        @RequestParam(value = "book", required = false) String book,
        @RequestParam(value = "genre", required = false) String genre) {
        List<Book> books = repository.findByTitleAndAuthorAndGenre(title, book, genre);
        model.addAttribute("title", title);
        model.addAttribute("book", book);
        model.addAttribute("genre", genre);
        model.addAttribute("books", books);
        return "books/search";
    }
    
    // получение формы для создания книги
    @GetMapping("/new")
    public String newBook(Model model){
        model.addAttribute("book", new Book());
        return "books/new";
    }

    // создание книги
    @PostMapping()
    public String createBook(@ModelAttribute("book") Book book) { 
        if (book == null){
            return null;
        }
        repository.save(book);
        return "redirect:/books";
    }



    // получение одной книги
    @GetMapping("/{id}")
    public String oneBook(Model model, @PathVariable("id") Long id){
        model.addAttribute("book", repository.findById(id).get());
        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByLogin(currentPrincipalName);
        Book book = repository.findById(id).get();
        model.addAttribute("comment", new Comment(user, book, null));
        return "books/one";
    }

    // получение формы для редактирования книги
    @GetMapping("/{id}/edit")
    public String editBook(Model model, @PathVariable("id") Long id){
        model.addAttribute("book", repository.findById(id).get());
        return "books/edit";
    }

    // обновление книги
    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book") Book book, @PathVariable("id") Long id){
        repository.save(book);
        return "redirect:/books/{id}";
    }

    // удаление книги
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") Long id){
        repository.deleteById(id);
        return "redirect:/books";
    }
    
    
    // создание комментария
    @PostMapping("/{id}/new_comment")
    public String createComment(@ModelAttribute("content") String content, @PathVariable("id") Long id) { 
        if (content == null){
            return null;
        }
        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByLogin(currentPrincipalName);
        if (user == null){
            return "redirect:/books/{id}";
        }
        Book book = repository.findById(id).get();
        Comment comment = commentRepository.save(new Comment(user, book, content));
        // commentRepository.save(comment);
        user.addComment(comment);
        book.addComment(comment);
        userRepository.save(user);
        repository.save(book);
        return "redirect:/books/{id}"; //  + currentPrincipalName
    }

    // // после создания комментария
    // @GetMapping("/{id}/new_comment")
    // public String afterComment(@PathVariable("id") Long id){
    //     return "redirect:/books/{id}/";
    // }
    
}
