package phase2.trade.trade;

import phase2.trade.user.User;

/**
 * Used for when the confirmation of a trade causes different effects
 * @author Grace Leung
 */
interface Tradable {
    Trade confirmTrade(User editingUser);
}