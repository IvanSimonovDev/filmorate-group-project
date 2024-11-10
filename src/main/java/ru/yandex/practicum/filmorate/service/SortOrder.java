package ru.yandex.practicum.filmorate.service;

public enum SortOrder {

    YEAR,
    LIKES;

    public static String from(String order) {

        return switch (order.toLowerCase()) {
            case "year" -> "RELEASE_DATE ASC";
            case "likes" -> "likes DESC";
            default -> "null";
        };
    }

}