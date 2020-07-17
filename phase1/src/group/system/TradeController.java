package group.system;

import group.menu.data.Request;
import group.menu.data.Response;
import group.menu.validator.RepositoryIdValidator;
import group.repository.Repository;
import group.trade.Trade;
import group.trade.TradeManager;
import group.user.PersonalUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

// Glitch List: Trade ID 0 works even when this trade, pressing exit immediately exists program, normal to keep removing personalUser.csv?
public class TradeController {
    private final TradeManager tradeManager;
    final Repository<Trade> tradeRepository;
    final Repository<PersonalUser> personalUserRepository;
    final UserController userController;

    /**
     * Takes requests from a user and turns it into information that can be used by TradeManager
     * @param dispatcher The controller dispatcher
     */
    public TradeController(ControllerDispatcher dispatcher){
        tradeRepository = dispatcher.tradeRepository;
        personalUserRepository = dispatcher.personalUserRepository;
        this.userController = dispatcher.userController;
        // currUser = userController.getCurrUser();
        tradeManager = new TradeManager(tradeRepository, personalUserRepository, dispatcher.tradeProperties, userController.getItemManager());
    }

    /**
     * Creates a new trade
     * @param request All the user's input
     * @return A description of the success of the creation of the trade
     */
    public Response addTrade(Request request) {
        PersonalUser currUser = userController.currUser;

        Integer item1;
        Integer item2;
        int user2 = request.getInt("respondent");

        PersonalUser trader2 = personalUserRepository.get(user2);

        // Check if items are in their inventories or are null
        if (request.get("lendingItem").equals("null")) {
            item1 = null;
        } else if (currUser.getInventory().contains(request.getInt("lendingItem"))) {
            item1 = request.getInt("lendingItem");
        } else {
            return new Response.Builder(false).translatable("failed.create.trade").build();
        }
        if (request.get("borrowingItem").equals("null")) {
            item2 = null;
        } else if (trader2.getInventory().contains(request.getInt("borrowingItem"))) {
            item2 = request.getInt("borrowingItem");
        } else if (currUser.getUid() == user2){
            return new Response.Builder(false).translatable("failed.same.trade").build();
        } else {
            return new Response.Builder(false).translatable("failed.create.trade").build();
        }

        Boolean isPermanent = request.getBoolean("isPermanent");
        String[] data = request.get("dateAndTime").split("-");
        LocalDateTime dateAndTime = LocalDateTime.of(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]));
        String location =  request.get("location");
        return tradeManager.createTrade(currUser.getUid(), user2, item1, item2, isPermanent, dateAndTime, location);
    }

    /**
     * Edits the date and time of a trade
     * @param request All the user's input
     * @return A description of the success of editing date and time
     */
    public Response editMeetingDateAndTime(Request request){
        PersonalUser currUser = userController.currUser;

        int tradeID = request.getInt("tradeID");
        String[] data = request.get("dateAndTime").split("-");
        LocalDateTime dateAndTime = LocalDateTime.of(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]));
        return tradeManager.editDateAndTime(tradeID, currUser.getUid(), dateAndTime);
    }

    /**
     * Edits the location of a trade
     * @param request All the user's input
     * @return A description of the success of editing location
     */
    public Response editMeetingLocation(Request request){
        PersonalUser currUser = userController.currUser;

        currUser = userController.getCurrUser();

        int tradeID = request.getInt("tradeID");
        String location = request.get("location");
        return tradeManager.editLocation(tradeID, currUser.getUid(), location);
    }

    /**
     * Confirms that a trade will occur
     * @param request All the user's input
     * @return A description of the state of confirmation
     */
    public Response confirmingTradeOpen(Request request){
        PersonalUser currUser = userController.currUser;

        int tradeID = request.getInt("tradeID");
        return tradeManager.confirmTrade(tradeID, currUser.getUid());
    }

    /**
     * Confirms the a trade has happened
     * @param request All the user's input
     * @return A description of the state of confirmation
     */
    public Response confirmingTradeComplete(Request request){
        PersonalUser currUser = userController.currUser;

        int tradeID = request.getInt("tradeID");
        return tradeManager.confirmTradeComplete(tradeID, currUser.getUid());
    }

    /**
     * @param input The user's input
     * @return True iff the input can be parsed into a Boolean
     */
    public boolean isBool(String input){
       return (input.equals("true") || input.equals("false"));
    }

    /**
     * @param input The user's input
     * @return True iff the input is correct item input
     */
    public boolean isAnItem(String input){
        try {
            if (input.equals("null") || new RepositoryIdValidator(userController.itemRepo).validate(input)){
                return true;
            }
            return true;
        } catch (IllegalArgumentException e){
            return false;
        }
    }

    /**
     * @param request The user's input
     * @return A description of the top three traders
     */
    public Response getRecentTrades(Request request){
        PersonalUser currUser = userController.currUser;

        List<Integer> recentCompleteTrades = currUser.getRecentCompleteTrades();
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer i : recentCompleteTrades) {
            Trade trade = tradeRepository.get(i);
            stringBuilder.append(trade.toString()).append("\n");
        }
        return new Response.Builder(true).translatable("recentTrades", stringBuilder.toString()).build();
    }

    /**
     * @param request The user's input
     * @return A description of all the user's created trades (including second meetings)
     */
    public Response getAllTrades(Request request){
        PersonalUser currUser = userController.currUser;

        List<Integer> allTrades = currUser.getTrades();
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer i : allTrades) {
            Trade trade = tradeRepository.get(i);
            stringBuilder.append(trade.toString()).append("\n");
        }
        return new Response.Builder(true).translatable("allTrades", stringBuilder.toString()).build();
    }

    /**
     * @param user The userID
     * @return The user IDs and frequency of trading with this user
     */
    public Map<Integer, Integer> getTradeFrequency(int user){
        return tradeManager.getTradeFrequency(user);
    }

    public Response skip(Request request){
        return new Response.Builder(true).translatable("return").build();
    }
}
