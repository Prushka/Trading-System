package phase2.trade.item;

/**
 * The enum Category.
 *
 * @author Dan Lyu
 * @author Grace Leung
 */
public enum Category {
    /**
     * Book category.
     */
    BOOK("/svg/book.svg"),
    /**
     * Movie category.
     */
    MOVIE("/svg/movie.svg"),
    /**
     * Electronic category.
     */
    ELECTRONIC("/svg/semiconductor.svg"),
    /**
     * Video game category.
     */
    VIDEO_GAME("/svg/controller.svg"),
    /**
     * Furniture category.
     */
    FURNITURE("/svg/bath.svg"),
    /**
     * Equipment category.
     */
    EQUIPMENT("/svg/ruler.svg"),
    /**
     * Miscellaneous category.
     */
    MISCELLANEOUS("/svg/stain.svg"),
    /**
     * Food category.
     */
    FOOD("/svg/food.svg");

    /**
     * The Resource path.
     */
    public String resourcePath;

    Category(String resourcePath) {
        this.resourcePath = resourcePath;
    }
}
