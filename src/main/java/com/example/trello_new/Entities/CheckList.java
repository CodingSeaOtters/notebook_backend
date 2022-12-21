package com.example.trello_new.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;


import java.util.LinkedHashSet;

import java.util.Set;

@Entity
@Table(name = "check_list")
public class CheckList {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "title_image_path")
    private String titleImagePath;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "board_id")
    private Board boardProcedure;

    @JsonManagedReference
    @OneToMany(mappedBy = "checkList", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Step> steps = new LinkedHashSet<>();

    //region Constructors


    public CheckList(String name, String titleImagePath, Board boardProcedure) {
        this.name = name;
        this.titleImagePath = titleImagePath;
        this.boardProcedure = boardProcedure;
    }

    public CheckList() {
    }

    //endregion


    //region Getter+Setter

    public String getTitleImagePath() {
        return titleImagePath;
    }

    public void setTitleImagePath(String titleImagePath) {
        this.titleImagePath = titleImagePath;
    }

    public Board getBoardProcedure() {
        return boardProcedure;
    }

    public void setBoardProcedure(Board boardProcedure) {
        this.boardProcedure = boardProcedure;
    }

    public Board getBoards() {
        return boardProcedure;
    }

    public void setBoards(Board board) {
        this.boardProcedure = board;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Step> getSteps() {
        return steps;
    }

    public void setSteps(Set<Step> steps) {
        this.steps = steps;
    }

    //endregion
}