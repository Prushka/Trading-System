package phase2.trade.avatar;

import javax.persistence.*;

/**
 * The Avatar that holds a name for the avatar and a byte array for the image data.
 * The byte array will be saved as blob in database.
 * This may not be the best practice but it works for now.
 *
 * @author Dan Lyu
 */
@Entity
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The Image data.
     */
    @Lob
    byte[] imageData;

    /**
     * The Avatar name.
     */
    String avatarName;

    /**
     * Gets avatar name.
     *
     * @return the avatar name
     */
    public String getAvatarName() {
        return avatarName;
    }

    /**
     * Sets avatar name.
     *
     * @param avatarName the avatar name
     */
    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    /**
     * Get image data in byte array.
     *
     * @return the byte [ ]
     */
    public byte[] getImageData() {
        return imageData;
    }

    /**
     * Sets image data.
     *
     * @param imageData the image data
     */
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }
}
