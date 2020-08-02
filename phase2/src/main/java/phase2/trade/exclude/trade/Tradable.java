package main.java.phase2.trade.exclude.trade;

/**
 * Used for when the confirmation of a trade causes different effects
 * @author Grace Leung
 */
interface Tradable {
    void confirmTrade(int tradeID, int editingUser);
}