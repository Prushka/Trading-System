package phase2.trade.avatar;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerResources;
import phase2.trade.refresh.ReType;
import phase2.trade.user.User;
import phase2.trade.user.command.UpdateUsers;
import phase2.trade.user.controller.UserSideInfoController;
import phase2.trade.widget.UserWidget;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A Controller used to upload Avatar using a FileChooser.
 * This Controller uses {@link phase2.trade.user.AccountManager} to setAvatar and {@link UpdateUsers} to update User using a Gateway interface.
 *
 * @author Dan Lyu
 */
public class UploadAvatarController extends AbstractController {

    /**
     * Constructs a new Upload avatar controller.
     *
     * @param controllerResources the controller resources
     */
    public UploadAvatarController(ControllerResources controllerResources) {
        super(controllerResources);
    }


    /**
     * Upload avatar.
     */
// https://stackoverflow.com/questions/24038524/how-to-get-byte-from-javafx-imageview
    public void uploadAvatar() {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(getSceneManager().getWindow());
        if (file != null) {
            // openFile(file);
            Image image = new Image(file.toURI().toString());
            BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);

            BufferedImage newImage = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);

            Graphics g = newImage.createGraphics();
            g.drawImage(bImage, 0, 0, 150, 150, null);
            g.dispose();

            ByteArrayOutputStream s = new ByteArrayOutputStream();
            try {
                ImageIO.write(newImage, "png", s);
                byte[] res = s.toByteArray();
                s.close();

                Avatar avatar = new Avatar();
                avatar.setImageData(res);
                getAccountManager().setAvatar(avatar);
                UpdateUsers update = getCommandFactory().getCommand(UpdateUsers::new, c -> c.setToUpdate(
                        new ArrayList<User>() {{
                            add(getAccountManager().getLoggedInUser());
                        }}));
                update.execute((result1, status1) -> {
                    status1.setSucceeded(() -> publish(ReType.REFRESH, UserSideInfoController.class, UserWidget.class));
                    status1.handle(getNotificationFactory());
                });

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
