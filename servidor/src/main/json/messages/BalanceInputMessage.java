package main.json.messages;

import java.util.List;

import main.json.messages.entities.BalanceChange;

public class BalanceInputMessage {
    private String userId;
    private List<BalanceChange> spents;
    private List<BalanceChange> incomes;

    public BalanceInputMessage() {
    }

    public String getUserId() {
        return userId;
    }

    public List<BalanceChange> getSpents() {
        return spents;
    }

    public List<BalanceChange> getIncomes() {
        return incomes;
    }

    @Override
    public String toString() {
        return "BalanceInputMessage(" +
                "userId='" + getUserId() + '\'' +
                ", spents=" + getSpents() +
                ", incomes=" + getIncomes() +
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

        final var monthBalanceInputMessage = (BalanceInputMessage) o;
        return userId.equals(monthBalanceInputMessage.userId) &&
                spents.equals(monthBalanceInputMessage.spents) &&
                incomes.equals(monthBalanceInputMessage.incomes);
    }

    @Override
    public int hashCode() {
        var ret = 1;

        ret = ret * 2 + userId.hashCode();
        ret = ret * 3 + spents.hashCode();
        ret = ret * 5 + incomes.hashCode();

        if (ret < 0)
            ret = -ret;
        return ret;
    }
}
