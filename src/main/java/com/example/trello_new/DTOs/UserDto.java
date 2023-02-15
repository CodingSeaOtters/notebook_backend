package com.example.trello_new.DTOs;

import java.util.List;

public class UserDto {

    //region Fields

    private final long id;

    private String userName;
    private String password;
    private List<Long> boards;

    //endregion

    //region Constructor

    public UserDto(long id) {
        this.id = id;
    }

    public UserDto(long id, String userName, String password, List<Long> boards) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.boards = boards;
    }

    //endregion

    //region Getter+Setter

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Long> getBoards() {
        return boards;
    }

    public void setBoards(List<Long> boards) {
        this.boards = boards;
    }

    //endregion
}
