package group.system;

import group.config.property.TradeProperties;
import group.menu.data.Request;
import group.menu.data.Response;
import group.notification.SupportTicket;
import group.notification.SupportTicketManager;
import group.repository.Repository;
import group.repository.RepositorySavable;
import group.system.ControllerDispatcher;
import group.trade.Trade;
import group.trade.TradeManager;
import group.user.PersonalUser;
import group.user.User;

import java.util.Date;

// Takes Requests from a User and turns it into information that can be used by TradeManager
public class TestTradeController {
    private final TradeManager tradeManager;
    private final Repository<Trade> tradeRepository;
    private final Repository<PersonalUser> personalUserRepository;
    private final TradeProperties tradeProperties;


    public TestTradeController(ControllerDispatcher dispatcher){
        tradeRepository = dispatcher.tradeRepository;
        personalUserRepository = dispatcher.personalUserRepository;
        tradeProperties = dispatcher.tradeProperties;
        tradeManager = new TradeManager(tradeRepository, personalUserRepository, tradeProperties);
        dispatcher.menuConstructor.supportTrade(this);
    }

    public Response addTrade(Request request) {
        Long user1 =  request.getLong("initiator");
        Long user2 =  request.getLong("respondent");
        Long item1 =  request.getLong("lendingItem");
        Long item2 =  request.getLong("borrowingItem");
        Boolean isPermanent = request.getBoolean("isPermanent");
        Date dateAndTime = request.getDate("dateAndTime");
        String location =  request.get("location");
        return tradeManager.createTrade(user1, user2, item1, item2, isPermanent, dateAndTime, location);
    }

    public Response editMeetingDateAndTime(Request request){
        Integer tradeID = request.getInt("tradeID");
        Integer editingUser = request.getInt("editingUser");
        Date dateAndTime = request.getDate("dateAndTime");
        return tradeManager.editDateAndTime(tradeID, editingUser, dateAndTime);
    }

    public Response editMeetingLocation(Request request){
        Integer tradeID = request.getInt("tradeID");
        Integer editingUser = request.getInt("editingUser");
        String location = request.get("location");
        return tradeManager.editLocation(tradeID, editingUser, location);
    }

    public Response confirmingTradeOpen(Request request){
        Integer tradeID = request.getInt("tradeID");
        Integer editingUser = request.getInt("editingUser");
        return tradeManager.confirmTrade(tradeID, editingUser);
    }

    public Response confirmingTradeComplete(Request request){
        Integer tradeID = request.getInt("tradeID");
        Integer editingUser = request.getInt("editingUser");
        return tradeManager.confirmTrade(tradeID, editingUser);
    }

    public boolean ifTradeNotExist(String input) {
        return !tradeRepository.ifExists(entity -> input.equalsIgnoreCase(entity.toString()));
    }
}
