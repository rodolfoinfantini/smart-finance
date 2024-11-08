package test;

import java.util.Objects;

public class PersonData {
    private String name;
    private int age;
    private PersonOccupation personOccupation;

    public PersonData() {
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PersonData that = (PersonData) object;
        return age == that.age && Objects.equals(name, that.name) && Objects.equals(personOccupation, that.personOccupation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, personOccupation);
    }
}
