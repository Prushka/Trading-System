package group.item;

import group.notification.SupportTicket;
import group.repository.UniqueId;
import group.user.PersonalUser;

import java.util.Objects;

public class Item implements UniqueId {

    private Long uid;
    private PersonalUser owner;
    private String name;
    private String description;

    public Item(PersonalUser owner, String name, String description){
        this.owner = owner;
        this.name = name;
        this.description = description;
    }

    public PersonalUser getOwner(){
        return this.owner;
    }

    public String getItemName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public void setOwnerUsername(PersonalUser owner){
        this.owner = owner;
    }

    public void setItemName(String newName){
        name = newName;
    }

    public void setDescription(String newDescription){
        description = newDescription;
    }

    @Override
    public long getUid() { return this.uid;}

    @Override
    public void setUid(long new_uid) { this.uid = new_uid;}


    @Override
    public String toString(){
        return name + ": " + description + "\n Owned by: " + owner.getUserName();
    }
    @Override
    public boolean equals(Object other){
        if (other instanceof Item) {
            Item otherItem = (Item) other;
            return this.name.equals(otherItem.name) && this.description.equals(otherItem.description) &&
                    this.owner.equals(otherItem.owner);
        }
        return false;
    }

    @Override
    public int hashCode(){ return name.hashCode() + description.hashCode() + owner.getUserName().hashCode(); }
}
