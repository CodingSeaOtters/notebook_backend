package com.example.trello_new.Repositories;

import com.example.trello_new.Entities.Board;
import com.example.trello_new.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardsRepository extends JpaRepository<Board, Long> {

}