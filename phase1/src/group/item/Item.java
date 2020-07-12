package group.item;

import group.notification.SupportTicket;

import java.util.Objects;

public class Item {

    private String ownerUsername;
    private String name;
    private String description;

    public Item(String owner, String name, String description){
        this.ownerUsername = owner;
        this.name = name;
        this.description = description;
    }

    public String getOwnerName(){
        return this.ownerUsername;
    }

    public String getItemName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public void setOwnerUsername(String owner){
        this.ownerUsername = owner;
    }

    public void setItemName(String newName){
        name = newName;
    }

    public void setDescription(String newDescription){
        description = newDescription;
    }

    @Override
    public String toString(){
        return name + ": " + description + "\n Owned by: " + ownerUsername;
    }
    @Override
    public boolean equals(Object other){
        if (other instanceof Item) {
            Item otherItem = (Item) other;
            return this.name.equals(otherItem.name) && this.description.equals(otherItem.description) &&
                    this.ownerUsername.equals(otherItem.ownerUsername);
        }
        return false;
    }

    @Override
    public int hashCode(){ return name.hashCode() + description.hashCode() + ownerUsername.hashCode(); }
}
