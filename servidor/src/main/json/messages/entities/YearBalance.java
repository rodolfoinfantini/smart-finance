package main.json.messages.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class YearBalance implements Cloneable, Comparable<YearBalance> {
    private final List<MonthBalance> months = new ArrayList<>();
    private int year;
    private double totalSpent = 0;
    private double totalIncome = 0;
    private double totalBalance = 0;

    private double averageSpent;
    private double averageIncome;
    private double averageBalance;

    private double maxSpent;
    private double maxIncome;
    private double maxBalance;

    private double minSpent;
    private double minIncome;
    private double minBalance;

    public YearBalance() {
    }

    public YearBalance(final int year) {
        this.year = year;
    }

    public void sort() {
        months.sort(Comparator.reverseOrder());
    }

    public void addSpent(final int month, final double spent) {
        final var monthBalance = getMonthBalance(month);
        if (monthBalance == null) return;
        monthBalance.addSpent(spent);
    }

    public void addIncome(final int month, final double income) {
        final var monthBalance = getMonthBalance(month);
        if (monthBalance == null) return;
        monthBalance.addIncome(income);
    }

    private MonthBalance getMonthBalance(final int month) {
        for (final var monthBalance : months) {
            if (monthBalance.getMonth() == month) {
                return monthBalance;
            }
        }

        try {
            final var newMonthBalance = new MonthBalance(month);
            months.add(newMonthBalance);
            return newMonthBalance;
        } catch (final Exception e) {
            return null;
        }
    }

    public int getYear() {
        return year;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public double getTotalBalance() {
        return totalBalance;
    }

    public double getAverageSpent() {
        return averageSpent;
    }

    public double getAverageIncome() {
        return averageIncome;
    }

    public double getAverageBalance() {
        return averageBalance;
    }

    public double getMaxSpent() {
        return maxSpent;
    }

    public double getMaxIncome() {
        return maxIncome;
    }

    public double getMaxBalance() {
        return maxBalance;
    }

    public double getMinSpent() {
        return minSpent;
    }

    public double getMinIncome() {
        return minIncome;
    }

    public double getMinBalance() {
        return minBalance;
    }

    @Override
    public String toString() {
        return "YearBalance(" +
                "year=" + getYear() +
                ", months=" + months +
                ", totalSpent=" + getTotalSpent() +
                ", totalIncome=" + getTotalIncome() +
                ", totalBalance=" + getTotalBalance() +
                ", averageSpent=" + getAverageSpent() +
                ", averageIncome=" + getAverageIncome() +
                ", averageBalance=" + getAverageBalance() +
                ", maxSpent=" + getMaxSpent() +
                ", maxIncome=" + getMaxIncome() +
                ", maxBalance=" + getMaxBalance() +
                ", minSpent=" + getMinSpent() +
                ", minIncome=" + getMinIncome() +
                ", minBalance=" + getMinBalance() +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != this.getClass()) return false;

        final var yearBalance = (YearBalance) o;
        return year == yearBalance.year &&
                months.equals(yearBalance.months) &&
                Double.compare(yearBalance.totalSpent, totalSpent) == 0 &&
                Double.compare(yearBalance.totalIncome, totalIncome) == 0 &&
                Double.compare(yearBalance.totalBalance, totalBalance) == 0 &&
                Double.compare(yearBalance.averageSpent, averageSpent) == 0 &&
                Double.compare(yearBalance.averageIncome, averageIncome) == 0 &&
                Double.compare(yearBalance.averageBalance, averageBalance) == 0 &&
                Double.compare(yearBalance.maxSpent, maxSpent) == 0 &&
                Double.compare(yearBalance.maxIncome, maxIncome) == 0 &&
                Double.compare(yearBalance.maxBalance, maxBalance) == 0 &&
                Double.compare(yearBalance.minSpent, minSpent) == 0 &&
                Double.compare(yearBalance.minIncome, minIncome) == 0 &&
                Double.compare(yearBalance.minBalance, minBalance) == 0;
    }

    @Override
    public int hashCode() {
        var ret = 1;

        ret = ret * 2 + year;
        ret = ret * 3 + months.hashCode();
        ret = ret * 5 + Double.hashCode(totalSpent);
        ret = ret * 7 + Double.hashCode(totalIncome);
        ret = ret * 11 + Double.hashCode(totalBalance);
        ret = ret * 13 + Double.hashCode(averageSpent);
        ret = ret * 17 + Double.hashCode(averageIncome);
        ret = ret * 19 + Double.hashCode(averageBalance);
        ret = ret * 23 + Double.hashCode(maxSpent);
        ret = ret * 29 + Double.hashCode(maxIncome);
        ret = ret * 31 + Double.hashCode(maxBalance);
        ret = ret * 37 + Double.hashCode(minSpent);
        ret = ret * 41 + Double.hashCode(minIncome);
        ret = ret * 43 + Double.hashCode(minBalance);

        if (ret < 0) ret = -ret;
        return ret;
    }

    public void calculate() {
        if (months.isEmpty()) {
            totalSpent = 0;
            totalIncome = 0;
            totalBalance = 0;
            maxSpent = 0;
            maxIncome = 0;
            maxBalance = 0;
            minSpent = 0;
            minIncome = 0;
            minBalance = 0;
            return;
        }

        maxSpent = months.getFirst().getSpent();
        maxIncome = months.getFirst().getIncome();
        maxBalance = months.getFirst().getBalance();
        minSpent = months.getFirst().getSpent();
        minIncome = months.getFirst().getIncome();
        minBalance = months.getFirst().getBalance();

        for (final var month : months) {
            totalSpent += month.getSpent();
            totalIncome += month.getIncome();
            totalBalance += month.getBalance();

            if (month.getSpent() > maxSpent) maxSpent = month.getSpent();
            if (month.getIncome() > maxIncome) maxIncome = month.getIncome();
            if (month.getBalance() > maxBalance) maxBalance = month.getBalance();
            if (month.getSpent() < minSpent) minSpent = month.getSpent();
            if (month.getIncome() < minIncome) minIncome = month.getIncome();
            if (month.getBalance() < minBalance) minBalance = month.getBalance();
        }

        averageSpent = totalSpent / months.size();
        averageIncome = totalIncome / months.size();
        averageBalance = totalBalance / months.size();
    }

    @Override
    public YearBalance clone() {
        final var clone = new YearBalance();
        for (final var month : this.months)
            clone.months.add((MonthBalance) month.clone());
        clone.totalSpent = totalSpent;
        clone.totalIncome = totalIncome;
        clone.totalBalance = totalBalance;
        clone.averageSpent = averageSpent;
        clone.averageIncome = averageIncome;
        clone.averageBalance = averageBalance;
        clone.maxSpent = maxSpent;
        clone.maxIncome = maxIncome;
        clone.maxBalance = maxBalance;
        clone.minSpent = minSpent;
        clone.minIncome = minIncome;
        clone.minBalance = minBalance;
        return clone;
    }

    @Override
    public int compareTo(YearBalance o) {
        return year - o.year;
    }
}
