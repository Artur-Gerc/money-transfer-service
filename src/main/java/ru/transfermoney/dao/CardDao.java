package ru.transfermoney.dao;

import org.springframework.stereotype.Component;
import ru.transfermoney.models.Card;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CardDao {
    private final Map<String, Card> cards = new ConcurrentHashMap<>();

    public void addCard(Card card) {
        cards.put(card.getNumber(), card);
    }

    public Card getCard(String cardNumber) {
        return cards.get(cardNumber);
    }

    public boolean cardExists(String cardNumber) {
        return cards.containsKey(cardNumber);
    }
}
