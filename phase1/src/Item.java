package src;

public class Item {

    private String name;
    private String description;

    public Item(String name, String description){
        this.name = name;
        this.description = description;
    }

    public String getItemName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public void setItemName(String newname){
        name = newname;
    }

    public void setDescription(String newdescription){
        description = newdescription;
    }
}
