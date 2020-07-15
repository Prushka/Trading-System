package group.item;

import group.notification.SupportTicket;
import group.repository.UniqueId;
import group.repository.reflection.CSVMappable;
import group.repository.reflection.MappableBase;
import group.user.PersonalUser;

import java.util.List;
import java.util.Objects;

public class Item extends MappableBase implements CSVMappable, UniqueId {

    private Long uid;
    private Long ownerUID;
    // private PersonalUser owner; CHANGED
    private String name;
    private String description;

    public Item(List<String> record) {
        super(record);
    }

    public Item(Long ownerUID, String name, String description){
        this.ownerUID = ownerUID;
        this.name = name;
        this.description = description;
    }

    public Long getOwner(){
        return this.ownerUID;
    }

    public String getItemName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public void setOwnerUsername(Long owner){
        this.ownerUID = owner;
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
        return name + "- " + description + " (Owned by: " + ownerUID + ")";
    }

    @Override
    public boolean equals(Object other){
        if (other instanceof Item) {
            Item otherItem = (Item) other;
            return this.name.equals(otherItem.name) && this.description.equals(otherItem.description) &&
                    this.ownerUID.equals(otherItem.ownerUID);
        }
        return false;
    }

    @Override
    public int hashCode(){ return name.hashCode() + description.hashCode() + ownerUID.hashCode(); }
}
