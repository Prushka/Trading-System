package phase2.trade.item;

public enum Category {
    BOOK("/svg/book.svg"),
    MOVIE("/svg/movie.svg"),
    ELECTRONIC("/svg/semiconductor.svg"),
    VIDEO_GAME("/svg/controller.svg"),
    FURNITURE("/svg/bath.svg"),
    EQUIPMENT("/svg/ruler.svg"),
    MISCELLANEOUS("/svg/stain.svg"),
    FOOD("/svg/food.svg");

    public String resourcePath;

    Category(String resourcePath) {
        this.resourcePath = resourcePath;
    }
}
