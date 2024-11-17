package main.json.messages.handlers;

import main.json.messages.BalanceInputMessage;
import main.json.messages.BalanceOutputMessage;
import main.network.Client;

public class BalanceMessageHandler extends MessageHandler<BalanceInputMessage> {
    public BalanceMessageHandler(Client client) {
        super(client);
    }

    @Override
    public String getEventName() {
        return "balance";
    }

    @Override
    public Class<BalanceInputMessage> getEventClass() {
        return BalanceInputMessage.class;
    }

    @Override
    public void handle(BalanceInputMessage message) {
        final var balance = new BalanceOutputMessage(message.getUserId());

        if (message.getSpents() != null)
            for (final var spent : message.getSpents())
                balance.addSpent(spent.getCreatedAt().getYear(), spent.getCreatedAt().getMonthValue(),
                        spent.getValue());
        if (message.getIncomes() != null)
            for (final var income : message.getIncomes())
                balance.addIncome(income.getCreatedAt().getYear(), income.getCreatedAt().getMonthValue(),
                        income.getValue());

        balance.finish();

        sendMessage(balance);
    }
}
