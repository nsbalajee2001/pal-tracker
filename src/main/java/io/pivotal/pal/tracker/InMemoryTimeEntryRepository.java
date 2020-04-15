package io.pivotal.pal.tracker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long,TimeEntry> timeEntries = new HashMap<>();
    private AtomicLong primaryKey = new AtomicLong();


    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(primaryKey.addAndGet(1));
        timeEntries.put(timeEntry.getId(), timeEntry);
        return timeEntry;
    }

    public TimeEntry find(long timeEntryId) {
        return timeEntries.get(timeEntryId);
    }

    public List<TimeEntry> list() {
        return timeEntries.values().stream().collect(Collectors.toList());
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry entry = find(id);
        if(entry != null ) {
            entry.setDate(timeEntry.getDate());
            entry.setHours(timeEntry.getHours());
            entry.setProjectId(timeEntry.getProjectId());
            entry.setUserId(timeEntry.getUserId());
        }
        return timeEntries.get(id);
    }

    public void delete(long id) {
        timeEntries.remove(id);
    }
}
