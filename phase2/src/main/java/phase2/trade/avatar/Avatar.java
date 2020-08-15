package phase2.trade.avatar;

import javax.persistence.*;

// it may be a bad idea to store image as blob into sql database
// but it works for now
// 1. store it in a nosql database
// 2. implement a server side and store the file there (never mind)
@Entity
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    byte[] imageData;

    String avatarName;

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
