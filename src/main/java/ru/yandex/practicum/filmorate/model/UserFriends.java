package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import jdk.jfr.BooleanFlag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class UserFriends {

    @NotNull
    private Long userId;
    @NotNull
    private Long friendId;
    @BooleanFlag
    private boolean isConfirmed;

}
