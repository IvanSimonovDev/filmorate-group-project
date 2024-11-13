package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventService {

    void createEvent(long userId, long entityId, EventType eventType, Operation operation);

    List<Event> getEvents(long userId);
}
