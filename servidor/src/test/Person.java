package test;

import java.util.List;
import java.util.Objects;

public class Person {
    private int id;
    private List<Integer> numbers;
    private PersonData data;
    private List<Salary> salaries;

    public Person() {
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Person person = (Person) object;
        return id == person.id && Objects.equals(numbers, person.numbers) && Objects.equals(data, person.data) && Objects.equals(salaries, person.salaries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numbers, data, salaries);
    }
}
