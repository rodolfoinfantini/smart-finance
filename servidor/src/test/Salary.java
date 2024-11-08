package test;

import java.util.Objects;

public class Salary {
    private double value;

    public Salary() {
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Salary salary = (Salary) object;
        return Double.compare(value, salary.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
