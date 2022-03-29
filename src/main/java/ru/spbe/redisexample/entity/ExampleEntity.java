package ru.spbe.redisexample.entity;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ExampleEntity {
    private int id;
    private String name;

    public String toString() {
        return "{id=" + id + "; name=" + name + "}";
    }
}
