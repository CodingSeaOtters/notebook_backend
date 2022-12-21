package com.example.trello_new.Repositories;

import com.example.trello_new.Entities.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StepRepository extends JpaRepository<Step, Long> {
    List<Step> findAllByCheckList_Id(Long id);
}