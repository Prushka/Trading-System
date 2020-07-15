package group.item;

import group.item.Item;
import group.menu.data.Response;
import group.repository.Repository;
import group.user.PersonalUser;

import java.util.ArrayList;

public class ItemManager {
    private Repository<Item> items;
    private ArrayList<Item> itemsArrayList; //temp solution

    public ItemManager(Repository<Item> items){
        this.items = items;
        itemsArrayList = new ArrayList<Item>();
    }

    public Response add (Item item) {
        items.add(item);
        itemsArrayList.add(item);
        return new Response.Builder(true).translatable("itemAdd").build();
    }

    public Response remove (Item item) {
        items.remove(item);
        itemsArrayList.remove(item);
        return new Response.Builder(true).translatable("itemRemove").build();
    }

    public Item findItemByUid(Long uid) {
        return items.get(uid);
    }


    public  Response browseItemsDisplay() { StringBuilder stringBuilder = new StringBuilder();
        for (Item i : itemsArrayList) {
            stringBuilder.append(i.toString()).append("\n");
        }
        return new Response.Builder(true).translatable("allItems", stringBuilder.toString()).build();
    }
}