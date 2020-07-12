package group.item;

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

    public String toString(){
        return this.name + ": " + this.description + "\n Owned By: " + this.ownerusernsme;
    }
}
