package io.acari.pojo;

import java.io.Serializable;
import java.util.List;

public class Programmer implements Serializable {
    private static final long serialVersionUID = 7026171646349890369L;
    private final String name;
    private final int age;
    private final Computer computer;
    private final List<String> languages;

    public Programmer(String name, int age, Computer computer, List<String> languages) {
        this.name = name;
        this.age = age;
        this.computer = computer;
        this.languages = languages;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Computer getComputer() {
        return computer;
    }

    public List<String> getLanguages() {
        return languages;
    }
}
