package com.example.trello_new.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;


@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;


    @JsonBackReference
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="user_board",
    joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "board_id"))
    private Set<Board> uses;



    //region Constructors
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
        this.username = "UNKOWN";
        this.password = "UNKNOWN";
    }

    //endregion


    //region Setters+Getters


    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Board> getUses() {
        return uses;
    }

    public void setUses(Set<Board> uses) {
        this.uses = uses;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
