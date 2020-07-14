package group.system;

import group.menu.data.Request;
import group.menu.data.Response;
import group.repository.Repository;
import group.trade.Trade;
import group.trade.TradeManager;
import group.user.PersonalUser;

import java.time.LocalDateTime;

public class TradeController {
    private final TradeManager tradeManager;
    final Repository<Trade> tradeRepository;
    final Repository<PersonalUser> personalUserRepository;

    /**
     * Takes requests from a user and turns it into information that can be used by TradeManager
     * @param dispatcher The controller dispatcher
     */
    public TradeController(ControllerDispatcher dispatcher){
        tradeRepository = dispatcher.tradeRepository;
        personalUserRepository = dispatcher.personalUserRepository;
        tradeManager = new TradeManager(tradeRepository, personalUserRepository, dispatcher.tradeProperties);
        dispatcher.menuConstructor.supportTrade(this);
    }

    /**
     * Creates a new trade
     * @param request All the user's input
     * @return A description of the success of the creation of the trade
     */
    public Response addTrade(Request request) {
        Long item1;
        Long item2;
        Long user1 =  request.getLong("initiator");
        Long user2 =  request.getLong("respondent");
        if (request.get("lendingItem").equals("null")){
            item1 = null;
        } else {
            item1 = request.getLong("lendingItem");
        }
        if (request.get("borrowingItem").equals("null")){
            item2 = null;
        } else {
            item2 = request.getLong("lendingItem");
        }        Boolean isPermanent = request.getBoolean("isPermanent");
        String[] data = request.get("dateAndTime").split("-");
        LocalDateTime dateAndTime = LocalDateTime.of(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]));
        String location =  request.get("location");
        return tradeManager.createTrade(user1, user2, item1, item2, isPermanent, dateAndTime, location);
    }

    /**
     * Edits the date and time of a trade
     * @param request All the user's input
     * @return A description of the success of editing date and time
     */
    public Response editMeetingDateAndTime(Request request){
        Integer tradeID = request.getInt("tradeID");
        Integer editingUser = request.getInt("editingUser");
        String[] data = request.get("dateAndTime").split("-");
        LocalDateTime dateAndTime = LocalDateTime.of(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]));
        return tradeManager.editDateAndTime(tradeID, editingUser, dateAndTime);
    }

    /**
     * Edits the location of a trade
     * @param request All the user's input
     * @return A description of the success of editing location
     */
    public Response editMeetingLocation(Request request){
        Integer tradeID = request.getInt("tradeID");
        Integer editingUser = request.getInt("editingUser");
        String location = request.get("location");
        return tradeManager.editLocation(tradeID, editingUser, location);
    }

    /**
     * Confirms that a trade will occur
     * @param request All the user's input
     * @return A description of the state of confirmation
     */
    public Response confirmingTradeOpen(Request request){
        Integer tradeID = request.getInt("tradeID");
        Integer editingUser = request.getInt("editingUser");
        return tradeManager.confirmTrade(tradeID, editingUser);
    }

    /**
     * Confirms the a trade has happened
     * @param request All the user's input
     * @return A description of the state of confirmation
     */
    public Response confirmingTradeComplete(Request request){
        Integer tradeID = request.getInt("tradeID");
        Integer editingUser = request.getInt("editingUser");
        return tradeManager.confirmTradeComplete(tradeID, editingUser);
    }

    /**
     * @param input The user's input
     * @return True iff the input can be parsed into a Boolean
     */
    public boolean isBool(String input){
       return (input.equals("true") || input.equals("false"));
    }

}
