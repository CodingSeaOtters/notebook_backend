package com.example.trello_new.Controller;

import com.example.trello_new.Entities.CheckList;
import com.example.trello_new.Entities.Step;
import com.example.trello_new.Repositories.CheckListRepository;
import com.example.trello_new.Repositories.StepRepository;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/step")
public class StepController {
    StepRepository stepRepository;
    CheckListRepository checkListRepository;


    @PostMapping("/{listId}")
    public void createNote(@RequestBody String note, @PathVariable Long listId) {
        JSONObject jo = new JSONObject(note);
        Step generatedStep = new Step();
        generatedStep.setTitle(jo.getString("title"));
        generatedStep.setContent(jo.getString("content"));
        try {
            generatedStep.setStartDate(new SimpleDateFormat("dd/MM/yyyy").parse(jo.getString("startDate")));
            generatedStep.setEndDate(new SimpleDateFormat("dd/MM/yyyy").parse(jo.getString("endDate")));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Step createdStep = stepRepository.save(generatedStep);
        Optional<CheckList> list = checkListRepository.findById(listId);

        if (list.isPresent()) {
            CheckList gottenCheckList = list.get();
            gottenCheckList.getSteps().add(createdStep);
            createdStep.setCheckList(gottenCheckList);
            stepRepository.save(createdStep);
            checkListRepository.save(gottenCheckList);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board with this id not found");
        }
    }

    @GetMapping("/{stepId}")
    public Step getNote(@PathVariable Long stepId){
        Optional<Step> note = stepRepository.findById(stepId);
        if(note.isPresent()){
            return note.get();
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note with this id not found");
        }
    }

    @GetMapping("/all/{checkListId}")
    public List<Step> getAllNotesFromBoard(@PathVariable Long checkListId){
        return stepRepository.findAllByCheckList_Id(checkListId);
    }

    @PutMapping("/{stepId}")
    public void changeNote(@PathVariable Long stepId, @RequestBody String noteString) {
        Optional<Step> step = stepRepository.findById(stepId);
        if (step.isPresent()) {
            Step gottenStep = step.get();
            JSONObject jo = new JSONObject(noteString);
            gottenStep.setTitle(jo.getString("title"));
            gottenStep.setContent(jo.getString("content"));
            try {
                gottenStep.setStartDate(new SimpleDateFormat("dd/MM/yyyy").parse(jo.getString("startDate")));
                gottenStep.setEndDate(new SimpleDateFormat("dd/MM/yyyy").parse(jo.getString("endDate")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            stepRepository.save(gottenStep);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note with this id not found");
        }
    }

    @DeleteMapping("/{Id}")
    public void deleteNote(@PathVariable Long Id) {
        Optional<Step> step = stepRepository.findById(Id);
        if (step.isPresent()) {
            Step gottenStep = step.get();
            CheckList list = gottenStep.getCheckList();
            list.getSteps().remove(gottenStep);
            checkListRepository.save(list);
            stepRepository.deleteById(Id);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note with this id not found");
        }
    }

}