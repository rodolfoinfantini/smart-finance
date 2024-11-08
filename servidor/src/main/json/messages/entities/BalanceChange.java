package main.json.messages.entities;

import java.time.LocalDateTime;

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
        // yyyy-MM-dd
        final var year = Integer.parseInt(createdAt.substring(0, 4));
        final var month = Integer.parseInt(createdAt.substring(5, 7));
        final var day = Integer.parseInt(createdAt.substring(8, 10));
        return LocalDateTime.of(year, month, day, 0, 0);
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
