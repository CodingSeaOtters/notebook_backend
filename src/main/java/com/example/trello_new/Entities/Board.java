package com.example.trello_new.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "board_name", nullable = false)
    private String boardName;


    @JsonManagedReference
    @ManyToMany(mappedBy = "uses")
    private Set<User> used_By = new HashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "board")
    private Set<Note> note = new HashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "boardProcedure")
    private Set<CheckList> checkLists = new HashSet<>();


    //region Constructors


    public Board(String boardName) {
        this.boardName = boardName;

    }

    public Board(Long id, String boardName, Set<User> used_By, Set<Note> note, Set<CheckList> checkLists) {
        this.id = id;
        this.boardName = boardName;
        this.used_By = used_By;
        this.note = note;
        this.checkLists = new HashSet<>();
    }

    public Board() {
    }

    //endregion



    //region Getter+Setter

    public Set<CheckList> getCheckLists() {
        return checkLists;
    }

    public void setCheckLists(Set<CheckList> checkLists) {
        this.checkLists = checkLists;
    }
    public Long getId() {
        return id;
    }

    public Set<Note> getNote() {
        return note;
    }

    public void setNote(Set<Note> note) {
        this.note = note;
    }

    public Set<User> getUsed_By() {
        return used_By;
    }

    public void setUsed_By(Set<User> useed_By) {
        this.used_By = useed_By;
    }


    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }


    public Long getBoardId() {
        return id;
    }

    public void setBoardId(Long boardId) {
        this.id = boardId;
    }

    //endregion
}
