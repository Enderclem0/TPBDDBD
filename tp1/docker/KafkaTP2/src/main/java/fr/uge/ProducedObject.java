package fr.uge;

import java.util.Objects;

public record ProducedObject(String firstName, String lastName, int age, String engineeringDegree) {
    public ProducedObject {
        Objects.requireNonNull(firstName);
        Objects.requireNonNull(lastName);
        Objects.requireNonNull(engineeringDegree);
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
    }
}
