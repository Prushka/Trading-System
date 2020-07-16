package group.item;

import group.item.Item;
import group.menu.data.Response;
import group.repository.Repository;
import group.user.PersonalUser;

import java.util.ArrayList;

public class ItemManager {
    private Repository<Item> items;
    private ArrayList<Long> itemsArrayList; //temp solution

    public ItemManager(Repository<Item> items){
        this.items = items;
        itemsArrayList = new ArrayList<>();
    }

    public Response add (Item item) {
        items.add(item);
        itemsArrayList.add(item.getUid());
        return new Response.Builder(true).translatable("itemAdd").build();
    }

    public Response remove(Item item) {
        items.remove(item);
        itemsArrayList.remove(item.getUid());
        return new Response.Builder(true).translatable("itemAdd").build();
    }

    public void addAvailable (Long itemID) {
        itemsArrayList.add(itemID);
    }

    public void removeAvailable (Long itemID) {
        itemsArrayList.remove(itemID);
    }

    public Item findItemByUid(Long uid) {
        return items.get(uid);
    }


    public  Response browseItemsDisplay() { StringBuilder stringBuilder = new StringBuilder();
        for (Long i : itemsArrayList) {
            Item item = items.get(i);
            stringBuilder.append(item.toString()).append("\n");
        }
        return new Response.Builder(true).translatable("allItems", stringBuilder.toString()).build();
    }
}