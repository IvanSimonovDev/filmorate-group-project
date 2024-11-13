package ru.yandex.practicum.filmorate.repository.inDatabase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.repository.EventRepository;
import ru.yandex.practicum.filmorate.repository.inDatabase.mapper.EventRowMapper;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class JdbcEventRepository extends JdbcBaseRepository<Event> implements EventRepository {

    public JdbcEventRepository(NamedParameterJdbcOperations jdbc, EventRowMapper eventRowMapper) {
        super(jdbc, eventRowMapper);
    }

    public void addEvent(Event newEvent) {
        String sql = "INSERT INTO events (user_id, timestamp, event_type, operation, entity_id) " +
                "VALUES (:user_id, :timestamp, :event_type, :operation, :entity_id)";
        Map<String, Object> params = Map.of(
                "user_id", newEvent.getUserId(),
                "timestamp", newEvent.getTimestamp(),
                "event_type", newEvent.getEventType().name(),
                "operation", newEvent.getOperation().name(),
                "entity_id", newEvent.getEntityId());
        long id = insert(sql, params);
        newEvent.setId(id);
        log.info("Insert event: {}", newEvent);
    }

    public List<Event> getEvents(long userId) {
        String sql = "SELECT * FROM events WHERE user_id = :user_id";
        Map<String, Long> params = Map.of("user_id", userId);
        return findMany(sql, params);
    }
}