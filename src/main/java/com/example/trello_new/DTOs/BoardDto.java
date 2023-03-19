package com.example.trello_new.DTOs;

public class BoardDto {
    private long boardId;
    private String boardName;

    public BoardDto(long boardId, String boardName) {
        this.boardId = boardId;
        this.boardName = boardName;
    }

    public BoardDto() {
    }

    public long getBoardId() {
        return boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }
}
