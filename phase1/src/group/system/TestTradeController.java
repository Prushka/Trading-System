package group.system;

import group.config.property.TradeProperties;
import group.menu.data.Request;
import group.menu.data.Response;
import group.repository.Repository;
import group.trade.Trade;
import group.trade.TradeManager;
import group.user.PersonalUser;

import java.time.LocalDateTime;

public class TestTradeController {
    private final TradeManager tradeManager;
    final Repository<Trade> tradeRepository;
    final Repository<PersonalUser> personalUserRepository;

    /**
     * Takes Requests from a User and turns it into information that can be used by TradeManager
     * @param dispatcher The controller dispatcher
     */
    public TestTradeController(ControllerDispatcher dispatcher){
        tradeRepository = dispatcher.tradeRepository;
        personalUserRepository = dispatcher.personalUserRepository;
        final TradeProperties tradeProperties = dispatcher.tradeProperties;
        tradeManager = new TradeManager(tradeRepository, personalUserRepository, tradeProperties);
        dispatcher.menuConstructor.supportTrade(this);
    }

    public Response addTrade(Request request) {
        Long user1 =  request.getLong("initiator");
        Long user2 =  request.getLong("respondent");
        Long item1 =  request.getLong("lendingItem");
        Long item2 =  request.getLong("borrowingItem");
        Boolean isPermanent = request.getBoolean("isPermanent");
        String[] data = request.get("dateAndTime").split("-");
        LocalDateTime dateAndTime = LocalDateTime.of(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]));
        String location =  request.get("location");
        return tradeManager.createTrade(user1, user2, item1, item2, isPermanent, dateAndTime, location);
    }

    public Response editMeetingDateAndTime(Request request){
        Integer tradeID = request.getInt("tradeID");
        Integer editingUser = request.getInt("editingUser");
        String[] data = request.get("dateAndTime").split("-");
        LocalDateTime dateAndTime = LocalDateTime.of(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]));
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
        return tradeManager.confirmTradeComplete(tradeID, editingUser);
    }

}
