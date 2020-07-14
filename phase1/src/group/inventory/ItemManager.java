package group.inventory;

import group.item.Item;
import group.menu.data.Response;
//import group.repository.Repository;
import java.util.ArrayList;

public class ItemManager {
    private ArrayList<Item> items;

    public ItemManager(ArrayList<Item> items) {
        this.items = items;
    }

    public Response add (Item item) {
        items.add(item);
        return new Response.Builder(true).translatable("itemAdd").build();
    }

    public Response remove (Item item) {
        items.remove(item);
        return new Response.Builder(true).translatable("itemRemove").build();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Item i : this.items) {
            stringBuilder.append(i.toString()).append("\n");
        }
        return stringBuilder.toString();
    }

}
