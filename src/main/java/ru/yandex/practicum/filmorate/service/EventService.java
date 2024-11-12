package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.enums.*;

import java.util.List;

public interface EventService {

    void createEvent(long userId, long entityId, EventType eventType, Operation operation);

    List<Event> getEvents(long userId);
}
