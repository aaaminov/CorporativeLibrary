package com.aminov.corporativelibrary.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    private Set<User> users = new HashSet<>(); // 0 или несколько


    public Role() { }

    public Role(String name) {
        this.name = name;
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

    public Set<User> getUsers() {
        return this.users;
    }


    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        if (!users.contains(user)){
            users.add(user);
            user.setRole(this);
        }
    }

    public void removeUser(User user){ // вызывается из класса Book
        if (users.contains(user)){
            users.remove(user);
        }
    }

}
