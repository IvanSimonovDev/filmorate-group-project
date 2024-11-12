package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventRepository {

    void addEvent(Event newEvent);

    List<Event> getEvents(long userId);
}
