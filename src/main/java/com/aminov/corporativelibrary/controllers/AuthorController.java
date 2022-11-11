package com.aminov.corporativelibrary.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.aminov.corporativelibrary.models.Author;
import com.aminov.corporativelibrary.repositories.AuthorRepository;

@Controller
@RequestMapping("/author")
public class AuthorController {

    private final AuthorRepository repository;

    public AuthorController(AuthorRepository repository) {
        this.repository = repository;
    }
    
    // получение всех авторов
    @GetMapping()
    public String allAuthors(Model model){
        model.addAttribute("authors", repository.findAll());
        return "author/all";
    }
    
    // получение формы для создания автора
    @GetMapping("/new")
    @PreAuthorize("hasAuthority('manager')")
    public String newAuthor(Model model){
        model.addAttribute("author", new Author());
        return "author/new";
    }

    // создание автора
    @PostMapping()
    @PreAuthorize("hasAuthority('manager')")
    public String createAuthor(@ModelAttribute("author") Author author) { 
        if (author == null){
            return null;
        }
        repository.save(author);
        return "redirect:/author";
    }



    // получение одного автора
    @GetMapping("/{id}")
    public String oneAuthor(Model model, @PathVariable("id") Long id){
        model.addAttribute("author", repository.findById(id).get());
        return "author/one";
    }

    // получение формы для редактирования автора
    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('manager')")
    public String editAuthor(Model model, @PathVariable("id") Long id){
        model.addAttribute("author", repository.findById(id).get());
        return "author/edit";
    }

    // обновление автора
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('manager')")
    public String updateAuthor(@ModelAttribute("author") Author author, @PathVariable("id") Long id){
        repository.save(author);
        return "redirect:/author/{id}";
    }

    // удаление автора
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('manager')")
    public String deleteAuthor(@PathVariable("id") Long id){
        repository.deleteById(id);
        return "redirect:/author";
    }

}
