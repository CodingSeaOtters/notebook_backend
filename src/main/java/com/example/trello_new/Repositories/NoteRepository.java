package com.example.trello_new.Repositories;

import com.example.trello_new.Entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    ArrayList<Note> findAllByBoardId(Long boardId);

}