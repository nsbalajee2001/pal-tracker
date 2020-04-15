package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            jdbcTemplate.update(new PreparedStatementCreator(){
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement statement = connection.prepareStatement(
                            "INSERT INTO time_entries (project_id, user_id, date, hours) " +
                                    "VALUES (?, ?, ?, ?)",
                            RETURN_GENERATED_KEYS
                    );
                    statement.setLong(1, timeEntry.getProjectId());
                    statement.setLong(2, timeEntry.getUserId());
                    statement.setDate(3, Date.valueOf(timeEntry.getDate()));
                    statement.setInt(4, timeEntry.getHours());
                    return statement;
                }
            }, generatedKeyHolder);
        }catch (Exception e){
            e.printStackTrace();
        }

        return find(generatedKeyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        List<TimeEntry> rows =  jdbcTemplate.query("SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?",
                new Object[]{timeEntryId},new RowMapper<TimeEntry>(){
                    @Override
                    public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
                        TimeEntry entry =  new TimeEntry(
                                rs.getLong("id"),
                                rs.getLong("project_id"),
                                rs.getLong("user_id"),
                                rs.getDate("date").toLocalDate(),
                                rs.getInt("hours")
                        );
                        return entry;
                    }
                });
        if(null != rows && rows.size() > 0){
            return rows.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query("SELECT id, project_id, user_id, date, hours FROM time_entries",
                new RowMapper<TimeEntry>(){
                    @Override
                    public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
                       TimeEntry entry =  new TimeEntry(
                                rs.getLong("id"),
                                rs.getLong("project_id"),
                                rs.getLong("user_id"),
                                rs.getDate("date").toLocalDate(),
                                rs.getInt("hours")
                        );
                       return entry;
                    }
                });
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        jdbcTemplate.update("UPDATE time_entries " +
                        "SET project_id = ?, user_id = ?, date = ?,  hours = ? " +
                        "WHERE id = ?",
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),
                timeEntry.getHours(),
                id);

        return find(id);
    }

    @Override
    public void delete(long timeEntryId) {
        jdbcTemplate.update("DELETE FROM time_entries WHERE id = ?", timeEntryId);
    }
}
