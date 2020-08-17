package phase2.trade.item;

public enum Tag {

    USED, NEW, STOLEN, WEED;


    public String resourcePath;

    Tag(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    Tag() {

    }
}
