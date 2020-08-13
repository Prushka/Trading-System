package phase2.trade.item;

public enum Category {
    BOOK("/svg/book.svg"),
    MOVIE("/svg/movie.svg"),
    ELECTRONIC("/svg/electronic.svg"),
    VIDEO_GAME("/svg/book.svg"),
    FURNITURE("/svg/furniture.svg"),
    EQUIPMENT("/svg/book.svg"),
    MISCELLANEOUS("/svg/book.svg");

    public String resourcePath;

    Category(String resourcePath){
        this.resourcePath = resourcePath;
    }
}
