package com.example.trello_new.Controller;

import com.example.trello_new.Entities.Board;
import com.example.trello_new.Entities.User;
import com.example.trello_new.JWTValidate;
import com.example.trello_new.Repositories.BoardsRepository;
import com.example.trello_new.Repositories.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardsRepository boardsRepository;

    @Autowired
    UserRepository userRepository;

    JWTValidate verifier;

    @PostMapping("/{userId}")
    public void createBoard(@RequestBody String json, @PathVariable Long userId, @RequestHeader("Authorization") String authorizationHeader) {
        if (verifier.validateToken(authorizationHeader)) {
            JSONObject jo = new JSONObject(json);
            Optional<User> user = userRepository.findById(userId);

            if (user.isPresent()) {
                Board createdBoard = new Board(jo.getString("boardName"));
                User getUser = user.get();
                getUser.getUses().add(createdBoard);
                boardsRepository.save(createdBoard);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this id not found");
            }
        }
    }

    @GetMapping("/{boardId}")
    @ResponseBody
    public Board getBoard(@PathVariable Long boardId, @RequestHeader("Authorization") String authorizationHeader) {
        Optional<Board> board = boardsRepository.findById(boardId);
        if (board.isPresent()) {
            return board.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board with this id not found");
        }
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<Long>> getAllBoardsFromUserId(@PathVariable Long userId, @RequestHeader("Authorization") String authorizationHeader) {
        if (verifier.validateToken(authorizationHeader)) {
            List<Long> sendBoards = new ArrayList<>();
            Optional<User> user = userRepository.findById(userId);

            if (user.isPresent()) {
                User gottenUser = user.get();
                Set<Board> boards = gottenUser.getUses();
                for (Board b : boards) {
                    sendBoards.add(b.getBoardId());
                }
                return new ResponseEntity<>(sendBoards, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
            for (User user : userList) {
                user.getUses().remove(gottenBoard);
                userRepository.save(user);
            }
            boardsRepository.deleteById(boardId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this id not found");
        }
    }

}
