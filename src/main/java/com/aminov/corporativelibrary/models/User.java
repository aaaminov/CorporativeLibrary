package com.aminov.corporativelibrary.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="`USER`") // чтобы название не конфликтило с ключевыми словами SQL
public class User implements UserDetails{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String login;
    private String password;
    private String surname;
    private String name;
    private String patronymic;
    private Long foreign_id;

    @ManyToOne()
    @JoinColumn(name = "role_id", nullable = false)
    private Role role; // только 1

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // при удалении "книги у пользователя" из пользователя, он удалится и из БД
    private Set<UserBook> user_books = new HashSet<>(); // 0 или несколько

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // при удалении комментария у пользователя, он удалится и из БД
    private Set<Comment> comments = new HashSet<>(); // (скорее всего) 0 или несколько


    public User() { }

    public User(String login, String password, String surname, String name, String patronymic, Long foreign_id, Role role) {
        this.login = login;
        this.password = password;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.foreign_id = foreign_id;
        this.role = role;
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


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
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



    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        if (this.role != role) {
            if (this.role != null)
                this.role.removeUser(this); // убрать привязку у старого объекта
            this.role = role;
            this.role.addUser(this);
        }
    }

    public boolean isManager() {
        if (role.getId() == 2){
            return true;
        }
        return false;
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


    // security

    @Override
    public String getUsername() {
        return getLogin();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(getRole().getName());
		return Arrays.asList(authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
