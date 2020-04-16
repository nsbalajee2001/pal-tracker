package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;

    private TimeEntryRepository timeEntriesRepo;
    private DistributionSummary timeEntrySummary;
    private  Counter actionCounter;


    public TimeEntryController(TimeEntryRepository timeEntryRepository,
                               MeterRegistry meterRegistry) {
        this.timeEntryRepository = timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntry = timeEntryRepository.create(timeEntryToCreate);
        actionCounter.increment();
        timeEntrySummary.record(this.timeEntryRepository .list().size());
        ResponseEntity response = new ResponseEntity(timeEntry, HttpStatus.CREATED);
        return response;
    }

    @GetMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {
        TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
        ResponseEntity<TimeEntry> response = null;
        if(null != timeEntry) {
            actionCounter.increment();
            response = new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.OK);
        }else{
            response = new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> entryList = timeEntryRepository.list();
        actionCounter.increment();
        ResponseEntity<List<TimeEntry>> response = new ResponseEntity(entryList, HttpStatus.OK);
        return response;
    }

    @PutMapping("/time-entries/{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry entry) {
        TimeEntry timeEntry = timeEntryRepository.update(timeEntryId, entry);
        ResponseEntity<TimeEntry> response = null;
        if(null != timeEntry) {
            actionCounter.increment();
            response = new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.OK);
        }else{
            response = new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @DeleteMapping("/time-entries/{timeEntryId}")
    public ResponseEntity delete(@PathVariable long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        actionCounter.increment();
        timeEntrySummary.record(this.timeEntryRepository.list().size());
        ResponseEntity response = new ResponseEntity(HttpStatus.NO_CONTENT);
        return response;
    }
}
