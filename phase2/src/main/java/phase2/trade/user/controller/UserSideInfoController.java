package phase2.trade.user.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import phase2.trade.avatar.Avatar;
import phase2.trade.avatar.PersistAvatarCommand;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.user.User;
import phase2.trade.user.command.UpdateUsers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@ControllerProperty(viewFile = "user_info_side.fxml")
public class UserSideInfoController extends AbstractController implements Initializable {

    public Label userId = new Label();
    public Label userName = new Label();
    public Label email = new Label();
    public Label home = new Label();
    public Label bio = new Label();
    public Label currentStatus = new Label();
    public Label permissionGroup = new Label();
    public ImageView imageView;

    private final User user;

    public UserSideInfoController(ControllerResources controllerResources) {
        super(controllerResources);
        this.user = getAccountManager().getLoggedInUser();
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userId.setText("User Id: " + user.getUid());
        userName.setText("User Name: " + user.getName());
        email.setText("Email: " + user.getEmail());
        permissionGroup.setText("PermissionGroup: " + user.getPermissionGroup());
        imageView.setOnMouseClicked(event -> uploadAvatar());
        // address book + if user didnt input address
        // home.setText("Location: " + user.getAddressBook().getSelectedAddress().getCity() + ", " + user.getAddress().getCountry());
        bio.setText("Bio: ");
        currentStatus.setText("Current Status: " + user.getUid());
    }

    private  void refreshAvatar() {
        if (user.getAvatar() != null && user.getAvatar().getImageData() != null) {
            Image img = new Image(new ByteArrayInputStream(user.getAvatar().getImageData()));
            imageView.setImage(img);
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
        }
    }

    // TODO: decouple
    // https://stackoverflow.com/questions/24038524/how-to-get-byte-from-javafx-imageview
    public void uploadAvatar() {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(getSceneManager().getWindow());
        if (file != null) {
            // openFile(file);
            Image image = new Image(file.toURI().toString());
            BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream s = new ByteArrayOutputStream();
            try {
                ImageIO.write(bImage, "png", s);
                byte[] res = s.toByteArray();
                s.close();

                Avatar avatar = new Avatar();
                avatar.setImageData(res);
                getAccountManager().getLoggedInUser().setAvatar(avatar);
                PersistAvatarCommand persistAvatarCommand = getCommandFactory().getCommand(PersistAvatarCommand::new, c -> c.setAvatar(avatar));
                persistAvatarCommand.execute((result, status) -> {
                    status.setSucceeded(() -> {
                        UpdateUsers update = getCommandFactory().getCommand(UpdateUsers::new, c -> c.setUserToUpdate(
                                getAccountManager().getLoggedInUser()));
                        update.execute((result1, status1) -> {
                            status1.setSucceeded(this::refreshAvatar);
                            status1.handle(getPopupFactory());
                        });
                    });
                    status.handle(getPopupFactory());
                });

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
