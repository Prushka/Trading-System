package group.system;

import group.config.property.TradeProperties;
import group.menu.data.Request;
import group.menu.data.Response;
import group.notification.SupportTicket;
import group.notification.SupportTicketManager;
import group.repository.RepositorySavable;
import group.system.ControllerDispatcher;
import group.trade.Trade;
import group.trade.TradeManager;
import group.user.PersonalUser;
import group.user.User;

import java.util.Date;

public class TestTradeController {
    private final TradeManager tradeManager;
    private final RepositorySavable<Trade> tradeRepository;
    private final RepositorySavable<PersonalUser> personalUserRepository;


    public TestTradeController(ControllerDispatcher dispatcher){
        tradeRepository = dispatcher.tradeRepository;
        personalUserRepository = dispatcher.personalUserRepository;
        tradeManager = new TradeManager(tradeRepository, personalUserRepository, new TradeProperties());
        dispatcher.menuConstructor.supportTrade(this);
    }

    // return response
    public Response addTrade(Request request) {
        Long user1 =  request.getLong("tradeInitiator");
        Long user2 =  request.getLong("tradeRespondent");
        Long item1 =  request.getLong("item1");
        Long item2 =  request.getLong("item2");
        Boolean isPermanent = request.getBoolean("isPermanent");
        Date dateAndTime = request.getDate("dateAndTime");
        String location =  request.get("location");
        return tradeManager.createTrade(user1, user2, item1, item2, isPermanent, dateAndTime, location);
    }

    public Response testEditDateAndTime(Request request){
        Integer tradeID = request.getInt("tradeID");
        Integer editingUser = request.getInt("editingUser");
        Date dateAndTime = request.getDate("dateAndTime");
        return tradeManager.editDateAndTime(tradeID, editingUser, dateAndTime);
    }

    public Response testEditLocation(Request request){
        Integer tradeID = request.getInt("tradeID");
        Integer editingUser = request.getInt("editingUser");
        String location = request.get("location");
        return tradeManager.editLocation(tradeID, editingUser, location);
    }

    public void testConfirmTrade(Request request){
        Integer tradeID = request.getInt("tradeID");
        Integer editingUser = request.getInt("editingUser");
        tradeManager.confirmTrade(tradeID, editingUser);
    }

    public void testConfirmComplete(Request request){
        Integer tradeID = request.getInt("tradeID");
        Integer editingUser = request.getInt("editingUser");
        tradeManager.confirmTrade(tradeID, editingUser);
    }

    public boolean ifTradeNotExist(String input) {
        return !tradeRepository.ifExists(entity -> input.equalsIgnoreCase(entity.toString()));
    }
}
