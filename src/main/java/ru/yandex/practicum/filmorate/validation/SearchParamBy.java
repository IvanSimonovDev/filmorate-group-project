package ru.yandex.practicum.filmorate.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SearchParamBy {
    DIRECTOR_TITLE("director,title"),
    TITLE_DIRECTOR("title,director"),
    DIRECTOR("director"),
    TITLE("title");

    private final String value;

    public static boolean isValidOption(String value) {
        for (SearchParamBy option : SearchParamBy.values()) {
            if (option.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static SearchParamBy from(String value) {
        return switch (value) {
            case "director,title" -> DIRECTOR_TITLE;
            case "title,director" -> TITLE_DIRECTOR;
            case "director" -> DIRECTOR;
            case "title" -> TITLE;
            default -> null;
        };
    }
}
