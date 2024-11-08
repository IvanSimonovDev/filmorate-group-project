package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Director {

    @NotNull(groups = OnUpdate.class)
    private Long id;

    @NotEmpty
    private String name;

}
