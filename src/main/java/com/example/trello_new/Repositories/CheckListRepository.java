package com.example.trello_new.Repositories;

import com.example.trello_new.Entities.CheckList;
import com.example.trello_new.Entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CheckListRepository extends JpaRepository<CheckList, Long> {


    List<CheckList> findAllByBoardProcedure_Id(Long id);
}