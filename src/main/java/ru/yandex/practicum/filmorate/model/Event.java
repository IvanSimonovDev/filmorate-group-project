package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;

@Data
@Builder
public class Event {
    private Long eventId;
    @NotNull(message = "id Пользователя должен быть указан")
    private Long userId;
    @NotNull(message = "Временная метка не может быть пустой")
    private Long timestamp;
    @NotNull(message = "Тип события должен быть указан. Укажите LIKE, REVIEW или FRIEND")
    private EventType eventType;
    @NotNull(message = "Тип операции должен быть указан. Укажите ADD,REMOVE или UPDATE")
    private Operation operation;
    @NotNull(message = "id сущности, с которой произошло событие, не может быть пустым")
    private Long entityId;
}
