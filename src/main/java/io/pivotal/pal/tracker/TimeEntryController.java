package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;


    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntry = timeEntryRepository.create(timeEntryToCreate);
        ResponseEntity response = new ResponseEntity(timeEntry, HttpStatus.CREATED);
        return response;
    }

    @GetMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {
        TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
        ResponseEntity<TimeEntry> response = null;
        if(null != timeEntry) {
            response = new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.OK);
        }else{
            response = new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> entryList = timeEntryRepository.list();
        ResponseEntity<List<TimeEntry>> response = new ResponseEntity(entryList, HttpStatus.OK);
        return response;
    }

    @PutMapping("/time-entries/{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry entry) {
        TimeEntry timeEntry = timeEntryRepository.update(timeEntryId, entry);
        ResponseEntity<TimeEntry> response = null;
        if(null != timeEntry) {
            response = new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.OK);
        }else{
            response = new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @DeleteMapping("/time-entries/{timeEntryId}")
    public ResponseEntity delete(@PathVariable long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        ResponseEntity response = new ResponseEntity(HttpStatus.NO_CONTENT);
        return response;
    }
}
