package main.json.messages.entities;

import java.util.Objects;

public class MonthBalance implements Comparable<MonthBalance>, Cloneable {
    private int month;
    private String monthName;

    private double income;
    private double spent;
    private double balance;

    public MonthBalance(final int month) throws Exception {
        this.setMonth(month);
        this.income = 0;
        this.spent = 0;
        this.balance = 0;
    }

    public void addIncome(final double income) {
        this.income += income;
        this.balance += income;
    }

    public void addSpent(final double spent) {
        this.spent += spent;
        this.balance -= spent;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) throws Exception {
        if (month < 1 || month > 12)
            throw new Exception("Invalid month");
        this.month = month;
        this.monthName = switch (month) {
            case 1 -> "Janeiro";
            case 2 -> "Fevereiro";
            case 3 -> "Março";
            case 4 -> "Abril";
            case 5 -> "Maio";
            case 6 -> "Junho";
            case 7 -> "Julho";
            case 8 -> "Agosto";
            case 9 -> "Setembro";
            case 10 -> "Outubro";
            case 11 -> "Novembro";
            case 12 -> "Dezembro";
            default -> null;
        };
    }

    public String getMonthName() {
        return monthName;
    }

    public double getIncome() {
        return income;
    }

    public double getSpent() {
        return spent;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "MonthBalance(" +
                "month=" + getMonth() +
                ", monthName=" + getMonthName() +
                ", income=" + getIncome() +
                ", spent=" + getSpent() +
                ", balance=" + getBalance() +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != this.getClass()) return false;

        final var monthBalance = (MonthBalance) o;
        return month == monthBalance.month &&
                Objects.equals(monthName, monthBalance.monthName) &&
                Double.compare(monthBalance.income, income) == 0 &&
                Double.compare(monthBalance.spent, spent) == 0 &&
                Double.compare(monthBalance.balance, balance) == 0;
    }

    @Override
    public int hashCode() {
        var ret = 1;

        ret = ret * 2 + month;
        ret = ret * 3 + monthName.hashCode();
        ret = ret * 7 + Double.hashCode(income);
        ret = ret * 11 + Double.hashCode(spent);
        ret = ret * 13 + Double.hashCode(balance);

        if (ret < 0) ret = -ret;
        return ret;
    }

    @Override
    public int compareTo(MonthBalance o) {
        return month - o.month;
    }

    @Override
    public Object clone() {
        try {
            final var balance = new MonthBalance(month);
            balance.addSpent(spent);
            balance.addIncome(income);
            return balance;
        } catch (Exception e) {
            return null;
        }
    }
}
