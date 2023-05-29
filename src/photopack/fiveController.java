package photopack;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This class represents the controller for the "five.fxml" user interface.
 * It allows the user to view and modify a Photo object's properties, including
 * its image, caption, and tags,
 * move or copy the photo to a different album.
 * @author Joey Zheng jz813 JunFeng Wang jw1397
 */

public class fiveController implements Initializable {
    /**
     * This instance variable represents the Photo object associated with the
     * displayed photo in the user interface.
     */
    private Photo photo;
    /**
     * This instance variable represents the Album object associated with the
     * displayed album in the user interface.
     */
    private Album album;
    /**
     * This instance variable represents the User object associated with the
     * displayed user in the user interface.
     */
    private User user;
    /**
     * This instance variable represents the root node of the user interface scene,
     * contains all the graphical user interface elements of the "five.fxml"
     * interface.
     */
    private Parent root;
    /**
     * This instance variable represents the ImageView element of the user
     * interface, which displays the photo's image.
     */
    @FXML
    private ImageView image;
    /**
     * This instance variable represents the Text element of the user interface,
     * which displays the photo's date.
     */
    @FXML
    private Text date;
    /**
     * This instance variable represents the TextField element of the user
     * interface, which allows the user to edit the photo's caption.
     */
    @FXML
    private TextField cap;
    /**
     * This instance variable represents the TextField element of the user
     * interface, which allows the user to input a new tag to be added to the
     * photo's tags.
     */
    @FXML
    private TextField inputTag;
    /**
     * This instance variable represents the ListView element of the user interface,
     * which displays the photo's tags in a list format.
     */
    @FXML
    private ListView<String> listView = new ListView<String>();
    /**
     * This instance variable represents the ChoiceBox element of the user
     * interface, which allows the user to select an album to copy or move the photo
     * to.
     */
    @FXML
    private ChoiceBox<Album> choiceBox;
    /**
     * This instance variable represents the ChoiceBox element of the user
     * interface, which allows the user to select a tag type to add a tag value to
     * or delete a tag value from.
     */
    @FXML
    private ChoiceBox<String> tagBox;
    /**
     * This instance variable represents the TextField element of the user
     * interface, which allows the user to input a new tag value to be added to a
     * tag type or deleted from a tag
     */
    @FXML
    private TextField tagVal;

    /**
     * This constructor creates a new instance of the Five Controller class and
     * initializes its instance variables.
     * 
     * @param photo the Photo object to be displayed and edited in the user
     *              interface
     * @param album the Album object that contains the photo
     * @param user  the User object that owns the album
     */

    public fiveController(Photo photo, Album album, User user) {
        this.photo = photo;
        this.album = album;
        this.user = user;
    }

    /**
     * Initializes the controller class. It sets up the UI elements with the
     * information from the given Photo object, including the image, caption, and
     * tags.
     * If the Photo object is null, the method does nothing.
     * 
     * @param location  URL location of the fxml file
     * @param resources ResourceBundle containing resources used by the fxml file
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (photo == null)
            return;

        tagBox.getItems().addAll(photo.getKeys());
        choiceBox.getItems().addAll(user.getAlbums());
        try {
            FileInputStream fileIn = new FileInputStream(photo.getPath());
            ObjectInputStream in = new ObjectInputStream(fileIn);
            SerializableImage serializableImage = (SerializableImage) in.readObject();
            in.close();
            fileIn.close();
            Image deserializedImage = serializableImage.getImage();

            image.setImage(deserializedImage);
        } catch (Exception e) {
            try {
                Image image1 = new Image(photo.getPath());
                image.setImage(image1);
            } catch (Exception p) {

            }

        }

        date.setText(photo.getDate() + "");
        cap.setText(photo.getCaption());
        cap.setEditable(true);

        cap.textProperty().addListener((observable, oldValue, newValue) -> {
            // This code will be executed whenever the text of the TextField changes
            photo.setCaption(newValue);
        });

        ObservableList<String> itemsListTag = FXCollections.observableArrayList();
        ;
        for (Map.Entry<String, List<String>> entry : photo.getTags().entrySet()) {
            String tagType = entry.getKey();
            List<String> tagValues = entry.getValue();
            StringBuilder sb = new StringBuilder();
            sb.append(tagType).append(": ");
            for (int i = 0; i < tagValues.size(); i++) {
                sb.append(tagValues.get(i));
                if (i < tagValues.size() - 1) {
                    sb.append(", ");
                }
            }
            itemsListTag.add(sb.toString());
        }
        listView.setItems(itemsListTag);
    }

    /**
     * Deletes the selected tag type from the photo's tags, and removes it from the
     * tag list view and tag choice box.
     * If no tag type is selected, does nothing.
     */
    public void delete() {
        if (listView.getSelectionModel().getSelectedItem() == null)
            return;
        String tagType = listView.getSelectionModel().getSelectedItem();
        listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
        tagType = tagType.substring(0, tagType.indexOf(":"));
        photo.deleteTagType(tagType);
        listView.getSelectionModel().selectNext();

        ObservableList<String> items = tagBox.getItems();
        items.remove(tagType);
        tagBox.setItems(items);
        tagBox.getSelectionModel().selectFirst();

    }

    /**
     * Adds a new tag type to the photo's list of tags.
     * If the input is empty or the tag type already exists, nothing happens.
     * Otherwise, adds the new tag type to the photo's list of tags and to the
     * listView.
     * Clears the inputTag text field after adding the new tag type to the list.
     */
    public void add() {
        if (inputTag.getText().equals(""))
            return;
        if (!photo.addTagType(inputTag.getText())) {
            alert("TagType already exist");
            inputTag.setText("");
            return;
        }
        String str = inputTag.getText();
        listView.getItems().add(str + ": ");
        inputTag.setText("");
        tagBox.getItems().add(str);

    }

    /**
     * Deletes the current photo from the album and switches back to the previous
     * scene.
     * 
     * @param click The MouseEvent triggered by the delete button.
     * @throws IOException If there is an error switching to the previous scene.
     */
    public void deletePhoto(MouseEvent click) throws IOException {
        album.deletePhoto(photo);
        switchToFour(click);

    }

    /**
     * Copies the current photo to the selected album, if the selected album is not
     * the current album.
     * Displays an alert message if the user tries to copy the photo to its own
     * album.
     * 
     * @throws NullPointerException if the choiceBox value is null
     */

    public void copyPhoto() {
        if (choiceBox.getValue() == null)
            return;
        if (choiceBox.getValue() == album) {
            alert("cannot copy to own album");
            return;
        }
        Album album = choiceBox.getValue();
        album.addPhoto(photo);

    }

    /**
     * Moves the current photo to the selected album in the choice box and deletes
     * the current photo from the current album.
     * If no album is selected or the selected album is the current album, an alert
     * will be shown.
     * 
     * @param click the MouseEvent associated with the action
     * @throws IOException if an error occurs while loading the next scene
     */
    public void movePhoto(MouseEvent click) throws IOException {
        if (choiceBox.getValue() == null)
            return;
        if (choiceBox.getValue() == album) {
            alert("cannot move to own album");
            return;
        }
        Album album = choiceBox.getValue();
        album.addPhoto(photo);
        deletePhoto(click);
    }

    /**
     * Displays a warning alert with the given message.
     * 
     * @param str the message to display in the alert
     */
    public void alert(String str) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(str);
        alert.showAndWait();
    }

    /**
     * Adds a new tag value to the photo.
     * If the text field for the tag value is empty or the tag type is not selected,
     * nothing happens
     * If the tag value already exists for the selected tag type, an alert is shown
     * and the text field is cleared.
     * Otherwise, the new tag value is added to the photo and the corresponding item
     * in the tag list view is updated.
     */
    public void addTagValue() {
        if (tagVal.getText().equals(""))
            return;
        if (tagBox.getValue() == null) {
            alert("Please select a tagType");
            return;
        }
        String tagType = tagBox.getValue();

        if (!photo.addTag(tagType, tagVal.getText())) {
            alert("TagValue already exist");
            tagVal.setText("");
            return;
        }
        ObservableList<String> itemsListTag = listView.getItems();

        for (int i = 0; i < listView.getItems().size(); i++) {
            String item = listView.getItems().get(i);
            if (item.substring(0, item.indexOf(":")).equals(tagType)) {
                if (item.length() - 2 == item.indexOf(":"))
                    itemsListTag.set(i, item + tagVal.getText());
                else
                    itemsListTag.set(i, item + ", " + tagVal.getText());
                break;
            }
        }
        tagVal.setText("");
    }

    /**
     * Deletes the selected tag value from the photo's tags and updates the ListView
     * and Photo object.
     * If the tag type or tag value is not selected or not found in the photo's
     * tags, an alert is displayed.
     */
    public void deleteTagValue() {
        if (tagVal.getText().equals(""))
            return;
        if (tagBox.getValue() == null) {
            alert("Please select a tagType");
            return;
        }

        int index = -1;
        String tagType = tagBox.getValue();
        String tagValue = tagVal.getText();
        ObservableList<String> itemsListTag = listView.getItems();

        for (int i = 0; i < listView.getItems().size(); i++) {
            String item = listView.getItems().get(i);
            if (item.substring(0, item.indexOf(":")).equals(tagType)) {
                index = i;
                break;
            }

        }

        try {
            String item = listView.getItems().get(index);
            int start = item.indexOf(tagValue);
            int end = start + tagValue.length();
            if (item.charAt(start - 2) == ',')
                start = start - 2;
            if (item.charAt(start - 2) == ':' && end != item.length())
                end = end + 2;
            item = item.substring(0, start) + item.substring(end);
            itemsListTag.set(index, item);
        } catch (Exception e) {
            alert("TagValue not in " + tagType);
        }

        photo.deleteTag(tagType, tagVal.getText());
        tagVal.setText("");

    }

    /**
     * Switches the view to the fourth screen (album view) when the user clicks the
     * back button.
     * Loads the FXML file for the fourth screen and creates a new instance of the
     * fourController.
     * Sets the controller for the loaded FXML file to the newly created
     * fourController instance.
     * Sets the root of the loaded FXML file to the root of the new instance of
     * fourController.
     * Creates a new Scene with the root as its parent.
     * Sets the new Scene to be displayed on the current Stage, Shows the Stage.
     * 
     * @param click The MouseEvent object representing the click event.
     * @throws IOException If the FXML file for the fourth screen cannot be loaded.
     */
    public void switchToFour(MouseEvent click) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/photopack/fxml files/four.fxml"));
        fourController sixcontroller = new fourController(user, album);
        loader.setController(sixcontroller);
        root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) date.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
