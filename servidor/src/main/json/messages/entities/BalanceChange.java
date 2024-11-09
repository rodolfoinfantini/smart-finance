package main.json.messages.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BalanceChange {
    private double value;
    private String createdAt;

    public BalanceChange() {
    }

    public BalanceChange(final double value, final String createdAt) {
        this.value = value;
        this.createdAt = createdAt;
    }

    public double getValue() {
        return value;
    }

    public LocalDateTime getCreatedAt() {
        return LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME);
    }

    @Override
    public String toString() {
        return "Spent(" +
                "value=" + getValue() +
                ", createdAt='" + getCreatedAt() + '\'' +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != this.getClass()) return false;

        final var spent = (BalanceChange) o;
        return Double.compare(spent.value, value) == 0 &&
                createdAt.equals(spent.createdAt);
    }

    @Override
    public int hashCode() {
        var ret = 1;

        ret = ret * 2 + Double.hashCode(value);
        ret = ret * 5 + createdAt.hashCode();

        if (ret < 0) ret = -ret;
        return ret;
    }
}
