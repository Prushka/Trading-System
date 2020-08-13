package phase2.trade.item;

public enum Category {
    BOOK("/svg/book.svg"),
    MOVIE("/image/video-camera.png"),
    ELECTRONIC("/image/plug.png"),
    VIDEO_GAME("/svg/book.svg"),
    FURNITURE("/image/pouf.png"),
    EQUIPMENT("/svg/book.svg"),
    MISCELLANEOUS("/svg/book.svg");

    public String resourcePath;

    Category(String resourcePath){
        this.resourcePath = resourcePath;
    }
}
