package group.item;

import group.notification.SupportTicket;

import java.util.Objects;

public class Item {

    private String ownerusernsme;
    private String name;
    private String description;

    public Item(String owner, String name, String description){
        this.ownerusernsme = owner;
        this.name = name;
        this.description = description;
    }

    public String getOwnerName(){
        return this.ownerusernsme;
    }

    public String getItemName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public void setOwnerusernsme(String owner){
        this.ownerusernsme = owner;
    }

    public void setItemName(String newname){
        name = newname;
    }

    public void setDescription(String newdescription){
        description = newdescription;
    }

    @Override
    public boolean equals(Object other){
        if (other instanceof Item) {
            Item otherItem = (Item) other;
            return this.name.equals(otherItem.name) && this.description.equals(otherItem.description) &&
                    this.ownerusernsme.equals(otherItem.ownerusernsme);
        }
        return false;
    }

    @Override
    public int hashCode(){ return name.hashCode() + description.hashCode() + ownerusernsme.hashCode(); }
}
