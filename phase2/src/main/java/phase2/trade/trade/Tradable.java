package phase2.trade.trade;

/**
 * Used for when the confirmation of a trade causes different effects
 * @author Grace Leung
 */
interface Tradable {
    void confirmTrade(int editingUser);
}