package com.example.trello_new.Controller;


import com.example.trello_new.Entities.Board;
import com.example.trello_new.Entities.User;
import com.example.trello_new.Repositories.BoardsRepository;
import com.example.trello_new.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardsRepository boardsRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/{userId}")
    public void createBoard(@RequestBody Board board, @PathVariable Long userId) {
        Board createdBoard = boardsRepository.save(board);
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User getUser = user.get();
            getUser.getUses().add(createdBoard);
            userRepository.save(getUser);
            createdBoard.getUsed_By().add(getUser);
            boardsRepository.save(createdBoard);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this id not found");
        }
    }

    @GetMapping("/{boardId}")
    public Board getBoard(@PathVariable Long boardId) {
        Optional<Board> board = boardsRepository.findById(boardId);
        if (board.isPresent()) {
            return board.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board with this id not found");
        }
    }

    @PutMapping("/{boardId}")
    public void changeName(@RequestBody Board boardName, @PathVariable Long boardId) {
        Optional<Board> findBoard = boardsRepository.findById(boardId);
        if (findBoard.isPresent()) {
            Board finalBoard = findBoard.get();
            finalBoard.setBoardName(boardName.getBoardName());
            boardsRepository.save(finalBoard);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board with this id not found");
        }
    }

    @DeleteMapping("/{boardId}")
    public void deleteBoard(@PathVariable Long boardId) {
        Optional<Board> board = boardsRepository.findById(boardId);
        if (board.isPresent()) {
            Board gottenBoard = board.get();
            List<User> userList = gottenBoard.getUsed_By().stream().toList();
            for (User user: userList){
                user.getUses().remove(gottenBoard);
                userRepository.save(user);
            }
           boardsRepository.deleteById(boardId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this id not found");
        }
    }
}
