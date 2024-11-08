package test;

import java.util.Objects;

public class PersonOccupation {
    private String description;
    private Double salary;
    private Boolean current;

    public PersonOccupation() {
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PersonOccupation that = (PersonOccupation) object;
        return Objects.equals(description, that.description) && Objects.equals(salary, that.salary) && Objects.equals(current, that.current);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, salary, current);
    }
}
