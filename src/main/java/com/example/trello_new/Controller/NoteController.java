package com.example.trello_new.Controller;

import com.example.trello_new.Entities.Board;
import com.example.trello_new.Entities.Note;
import com.example.trello_new.JWTValidate;
import com.example.trello_new.Repositories.BoardsRepository;
import com.example.trello_new.Repositories.NoteRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/note")
public class NoteController {
    @Autowired
    NoteRepository noteRepository;
    @Autowired
    BoardsRepository boardsRepository;


    JWTValidate verifier = new JWTValidate();


    /* {
  "title" : "",
  "content" : "",
  "title_image_path":"",
  "startDate": "00/00/0000",
  "endDate" : "15/12/2022"
  }
   */
    @PostMapping("/{boardId}")
    public void createNote(@RequestBody String note, @PathVariable Long boardId, @RequestHeader("Authorization") String authorizationHeader) {
        if (verifier.validateToken(authorizationHeader)) {
            JSONObject jo = new JSONObject(note);
            Note generatedNote = new Note();
            fillNote(jo, generatedNote);
            Note createdNote = noteRepository.save(generatedNote);
            Optional<Board> board = boardsRepository.findById(boardId);
            if (board.isPresent()) {
                Board gottenBoard = board.get();
                gottenBoard.getNote().add(createdNote);
                createdNote.setBoard(gottenBoard);
                noteRepository.save(createdNote);
                boardsRepository.save(gottenBoard);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board with this id not found");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized");
        }
    }

    private void fillNote(JSONObject jo, Note generatedNote) {
        generatedNote.setTitle(jo.getString("title"));
        generatedNote.setContent(jo.getString("content"));
        generatedNote.setImagePath(jo.getString("title_image_path"));
        try {
            generatedNote.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse(jo.getString("startDate")));
            generatedNote.setEndDate(new SimpleDateFormat("yyyy-MM-dd").parse(jo.getString("endDate")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/{noteId}")
    @ResponseBody
    public Note getNote(@PathVariable Long noteId, @RequestHeader("Authorization") String authorizationHeader) {
        if (verifier.validateToken(authorizationHeader)) {
            Optional<Note> note = noteRepository.findById(noteId);
            if (note.isPresent()) {
                return note.get();
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note with this id not found");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized");
        }
    }


    @GetMapping("/all/{boardId}")
    public ResponseEntity<ArrayList<Note>> getAllNotesFromBoard(@PathVariable Long boardId, @RequestHeader("Authorization") String authorizationHeader) {
        if (verifier.validateToken(authorizationHeader)) {
            return new ResponseEntity<>(noteRepository.findAllByBoardId(boardId), HttpStatus.OK);
        } else {
            return  new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/{noteId}")
    public void changeNote(@PathVariable Long noteId, @RequestBody String noteString) {
        Optional<Note> note = noteRepository.findById(noteId);
        if (note.isPresent()) {
            Note gottenNote = note.get();
            JSONObject jo = new JSONObject(noteString);
            fillNote(jo, gottenNote);
            noteRepository.save(gottenNote);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note with this id not found");
        }
    }

    @DeleteMapping("/{noteId}")
    public void deleteNote(@PathVariable Long noteId, @RequestHeader("Authorization") String authorizationHeader) {
        if (verifier.validateToken(authorizationHeader)) {
            Optional<Note> note = noteRepository.findById(noteId);
            if (note.isPresent()) {
                Note gottenNote = note.get();
                Board board = gottenNote.getBoard();
                board.getNote().remove(gottenNote);
                boardsRepository.save(board);
                noteRepository.deleteById(noteId);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note with this id not found");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT False");
        }
    }
}
