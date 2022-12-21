package com.example.trello_new.Controller;

import com.example.trello_new.Entities.Board;
import com.example.trello_new.Entities.CheckList;
import com.example.trello_new.Repositories.BoardsRepository;
import com.example.trello_new.Repositories.CheckListRepository;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/check_list")
public class CheckListController {

    BoardsRepository boardsRepository;

    CheckListRepository checkListRepository;


    @PostMapping("/{boardId}")
    public void createNote(@RequestBody String note, @PathVariable Long boardId) {
        JSONObject jo = new JSONObject(note);
        CheckList generatedCheckList = new CheckList();
        generatedCheckList.setName(jo.getString("name"));
        generatedCheckList.setTitleImagePath(jo.getString("title_image_path"));

        CheckList createdcheckList = checkListRepository.save(generatedCheckList);
        Optional<Board> board = boardsRepository.findById(boardId);
        if (board.isPresent()) {
            Board gottenBoard = board.get();
            gottenBoard.getCheckLists().add(createdcheckList);
            createdcheckList.setBoardProcedure(gottenBoard);
            checkListRepository.save(createdcheckList);
            boardsRepository.save(gottenBoard);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board with this id not found");
        }
    }

    @GetMapping("/{Id}")
    public CheckList getNote(@PathVariable Long Id){
        Optional<CheckList> list = checkListRepository.findById(Id);
        if(list.isPresent()){
            return list.get();
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Checklist with this id not found");
        }
    }

    @GetMapping("/all/{boardId}")
    public List<CheckList> getAllNotesFromBoard(@PathVariable Long boardId){
        return checkListRepository.findAllByBoardProcedure_Id(boardId);
    }

    @PutMapping("/{noteId}")
    public void changeNote(@PathVariable Long noteId, @RequestBody String noteString) {
        Optional<CheckList> list = checkListRepository.findById(noteId);
        if (list.isPresent()) {
            CheckList gottenList = list.get();
            JSONObject jo = new JSONObject(noteString);
            gottenList.setName(jo.getString("title"));
            gottenList.setTitleImagePath(jo.getString("title_image_path"));
            checkListRepository.save(gottenList);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note with this id not found");
        }
    }

    @DeleteMapping("/{Id}")
    public void deleteNote(@PathVariable Long Id) {
        Optional<CheckList> list = checkListRepository.findById(Id);
        if (list.isPresent()) {
            CheckList gottenList = list.get();
            Board board = gottenList.getBoardProcedure();
            board.getNote().remove(gottenList);
            boardsRepository.save(board);
            checkListRepository.deleteById(Id);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note with this id not found");
        }
    }

}
