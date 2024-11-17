package main.json.messages;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import main.json.messages.entities.YearBalance;

public class BalanceOutputMessage {
    private String userId;

    private final List<YearBalance> years = new ArrayList<>();
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

    public BalanceOutputMessage(final String userId) {
        this.userId = userId;
    }

    public void addSpent(final int year, final int month, final double spent) {
        final var yearBalance = getYearBalance(year);
        if (yearBalance == null)
            return;
        yearBalance.addSpent(month, spent);
    }

    public void addIncome(final int year, final int month, final double income) {
        final var yearBalance = getYearBalance(year);
        if (yearBalance == null)
            return;
        yearBalance.addIncome(month, income);
    }

    private YearBalance getYearBalance(final int year) {
        for (final var yearBalance : years)
            if (yearBalance.getYear() == year)
                return yearBalance;

        try {
            final var newYearBalance = new YearBalance(year);
            years.add(newYearBalance);
            return newYearBalance;
        } catch (final Exception e) {
            return null;
        }
    }

    public void finish() {
        calculate();
        sort();
    }

    private void sort() {
        years.sort(Comparator.reverseOrder());
        for (final var year : years)
            year.sort();
    }

    private void calculate() {
        for (final var year : years)
            year.calculate();

        if (years.isEmpty()) {
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

        maxSpent = years.getFirst().getTotalSpent();
        maxIncome = years.getFirst().getTotalIncome();
        maxBalance = years.getFirst().getTotalBalance();
        minSpent = years.getFirst().getTotalSpent();
        minIncome = years.getFirst().getTotalIncome();
        minBalance = years.getFirst().getTotalBalance();

        for (final var year : years) {
            totalSpent += year.getTotalSpent();
            totalIncome += year.getTotalIncome();
            totalBalance += year.getTotalBalance();

            if (year.getTotalSpent() > maxSpent)
                maxSpent = year.getTotalSpent();
            if (year.getTotalIncome() > maxIncome)
                maxIncome = year.getTotalIncome();
            if (year.getTotalBalance() > maxBalance)
                maxBalance = year.getTotalBalance();

            if (year.getTotalSpent() < minSpent)
                minSpent = year.getTotalSpent();
            if (year.getTotalIncome() < minIncome)
                minIncome = year.getTotalIncome();
            if (year.getTotalBalance() < minBalance)
                minBalance = year.getTotalBalance();
        }

        averageSpent = totalSpent / years.size();
        averageIncome = totalIncome / years.size();
        averageBalance = totalBalance / years.size();
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
        return "BalanceOutputMessage(" +
                "totalSpent=" + totalSpent +
                ", totalIncome=" + totalIncome +
                ", totalBalance=" + totalBalance +
                ", averageSpent=" + averageSpent +
                ", averageIncome=" + averageIncome +
                ", averageBalance=" + averageBalance +
                ", maxSpent=" + maxSpent +
                ", maxIncome=" + maxIncome +
                ", maxBalance=" + maxBalance +
                ", minSpent=" + minSpent +
                ", minIncome=" + minIncome +
                ", minBalance=" + minBalance +
                ", years=" + years +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o == this)
            return true;
        if (o.getClass() != this.getClass())
            return false;

        final var balanceMessage = (BalanceOutputMessage) o;
        return Double.compare(balanceMessage.totalSpent, totalSpent) == 0 &&
                Double.compare(balanceMessage.totalIncome, totalIncome) == 0 &&
                Double.compare(balanceMessage.totalBalance, totalBalance) == 0 &&
                Double.compare(balanceMessage.averageSpent, averageSpent) == 0 &&
                Double.compare(balanceMessage.averageIncome, averageIncome) == 0 &&
                Double.compare(balanceMessage.averageBalance, averageBalance) == 0 &&
                Double.compare(balanceMessage.maxSpent, maxSpent) == 0 &&
                Double.compare(balanceMessage.maxIncome, maxIncome) == 0 &&
                Double.compare(balanceMessage.maxBalance, maxBalance) == 0 &&
                Double.compare(balanceMessage.minSpent, minSpent) == 0 &&
                Double.compare(balanceMessage.minIncome, minIncome) == 0 &&
                Double.compare(balanceMessage.minBalance, minBalance) == 0 &&
                years.equals(balanceMessage.years);
    }

    @Override
    public int hashCode() {
        var ret = 1;

        ret = ret * 2 + Double.hashCode(totalSpent);
        ret = ret * 3 + Double.hashCode(totalIncome);
        ret = ret * 5 + Double.hashCode(totalBalance);
        ret = ret * 7 + Double.hashCode(averageSpent);
        ret = ret * 11 + Double.hashCode(averageIncome);
        ret = ret * 13 + Double.hashCode(averageBalance);
        ret = ret * 17 + Double.hashCode(maxSpent);
        ret = ret * 19 + Double.hashCode(maxIncome);
        ret = ret * 23 + Double.hashCode(maxBalance);
        ret = ret * 29 + Double.hashCode(minSpent);
        ret = ret * 31 + Double.hashCode(minIncome);
        ret = ret * 37 + Double.hashCode(minBalance);
        ret = ret * 41 + years.hashCode();

        if (ret < 0)
            ret = -ret;
        return ret;
    }
}
