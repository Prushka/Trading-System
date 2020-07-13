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

    // return response
    public Response addTrade(Request request) {
        Long user1 =  request.getLong("initiator");
        Long user2 =  request.getLong("respondent");
        Long item1 =  request.getLong("lendingItem");
        Long item2 =  request.getLong("borrowingItem");
        Boolean isPermanent = request.getBoolean("isPermanent");
        Date dateAndTime = request.getDate("dateAndTime");
        String location =  request.get("location");
        Response response = tradeManager.createTrade(user1, user2, item1, item2, isPermanent, dateAndTime, location);
        response.setNextMasterNodeIdentifier("master.support.trade");
        return response;
    }

    public Response testEditDateAndTime(Request request){
        Integer tradeID = request.getInt("tradeID");
        Integer editingUser = request.getInt("editingUser");
        Date dateAndTime = request.getDate("dateAndTime");
        Response response = tradeManager.editDateAndTime(tradeID, editingUser, dateAndTime);
        response.setNextMasterNodeIdentifier("master.support.trade");
        return response;
    }

    public Response testEditLocation(Request request){
        Integer tradeID = request.getInt("tradeID");
        Integer editingUser = request.getInt("editingUser");
        String location = request.get("location");
        Response response = tradeManager.editLocation(tradeID, editingUser, location);
        response.setNextMasterNodeIdentifier("master.support.trade");
        return response;
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

    public Response testing(Request request){
        Response response = new Response.Builder(true).
                translatable("input.add.trade.initiator", "hello")
                .build();
        response.setNextMasterNodeIdentifier("master.support.trade");
        return response;
    }

    public boolean ifTradeNotExist(String input) {
        return !tradeRepository.ifExists(entity -> input.equalsIgnoreCase(entity.toString()));
    }
}
