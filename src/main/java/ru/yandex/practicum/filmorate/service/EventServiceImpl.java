package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.repository.inDatabase.JdbcEventRepository;
import ru.yandex.practicum.filmorate.repository.inDatabase.JdbcUserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final JdbcUserRepository userRepository;
    private final JdbcEventRepository eventRepository;

    @Override
    public void createEvent(long userId, long entityId, EventType eventType, Operation operation) {
        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .userId(userId)
                .eventType(eventType)
                .operation(operation)
                .entityId(entityId)
                .build();

        eventRepository.addEvent(event);
    }

    @Override
    public List<Event> getEvents(long userId) {
        userRepository.get(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + userId + " не найден"));
        return eventRepository.getEvents(userId);
    }
}
