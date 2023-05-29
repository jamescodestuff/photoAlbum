package photopack;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 * This class is a controller for the photo album GUI, which displays a list of
 * photos and allows users to upload new photos, delete photos,switch to other
 * GUI screens.
 * The class receives a user and album object upon initialization and implements
 * the Initializable interface to initialize the GUI components
 * @author Joey Zheng jz813 JunFeng Wang jw1397
 */
public class fourController implements Initializable {

    // Recieve an user class and album class temporary
    /**
     * the user object associated with this controller
     */
    private User user;
    /**
     * the album object associated with this controller
     */
    private Album currAlbum;
    /**
     * the root JavaFX node for this controller's view
     */
    private Parent root;
    /**
     * the JavaFX ListView object for displaying photos in the album
     */
    @FXML
    private ListView<Photo> listView = new ListView<Photo>();
    /**
     * the JavaFX button for uploading a new photo to the album
     */
    @FXML
    private Button upload;
    /**
     * the JavaFX ChoiceBox object for selecting a photo to add to the album.
     */
    @FXML
    private ChoiceBox<Photo> choiceBox;

    /**
     * Constructs a new instance of the fourController class with the specified user
     * and album parameters.
     * The user parameter represents the user for whom the album belongs to, and the
     * album parameter represents the album being viewed.
     * 
     * @param user  the User object associated with the current session
     * @param album the Album object associated with the current session
     */
    public fourController(User user, Album album) {
        this.currAlbum = album;
        this.user = user;
    }

    /**
     * This method initializes the JavaFX components and populates the choiceBox
     * with all the photos in all of the user's albums.
     * It also sets up the listView with the photos in the current album, and sets
     * up a double-click listener to switch to the photo view of the selected photo
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize choiceBox, make array/array list of all Photo captions
        ArrayList<Album> arr = user.getAlbums();
        HashSet<Photo> allPhotos = new HashSet<Photo>();
        for (int i = 0; i < arr.size(); i++) {
            Album album = arr.get(i);
            ArrayList<Photo> photoArr = album.getAlbum();
            for (int j = 0; j < photoArr.size(); j++) {
                Photo photo = photoArr.get(j);
                allPhotos.add(photo);
            }
        }
        choiceBox.getItems().addAll(allPhotos);

        ObservableList<Photo> items = FXCollections.observableList(currAlbum.getAlbum());
        listView.setItems(items);
        resize();

        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    // Use ListView's getSelected Item
                    Photo currentItemSelected = listView.getSelectionModel().getSelectedItem();
                    if (currentItemSelected == null)
                        return;
                    try {
                        switchToFive(click, currentItemSelected);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Adds the selected photo from the choice box to the current album's list view,
     * displays an alert message if the selected photo is already in the album.
     */
    public void add() {
        if (choiceBox.getValue() == null)
            return;
        if (currAlbum.duplicatePhoto(choiceBox.getValue())) {
            alert("Duplicate Photo");
            return;
        }
        try {
            listView.getItems().add(choiceBox.getValue());
        } catch (Exception e) {
        }

    }

    /**
     * Resize the images and set the list view cell factory
     */
    public void resize() {
        listView.setCellFactory(param -> new ListCell<Photo>() {
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(Photo pic, boolean empty) {
                super.updateItem(pic, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        FileInputStream fileIn = new FileInputStream(pic.getPath());
                        ObjectInputStream in = new ObjectInputStream(fileIn);
                        SerializableImage serializableImage = (SerializableImage) in.readObject();
                        in.close();
                        fileIn.close();
                        Image deserializedImage = serializableImage.getImage();

                        imageView.setImage(deserializedImage);
                    } catch (Exception e) {
                        try {
                            Image image = new Image(pic.getPath());
                            imageView.setImage(image);
                        } catch (Exception p) {

                        }
                    }
                    imageView.setFitHeight(150);
                    imageView.setFitWidth(200);
                    setText(pic.getCaption());
                    setFont(Font.font(20));
                    setGraphic(imageView);
                }
            }
        });
    }

    /**
     * Deletes the currently selected photo from the list view and selects the next
     * item.
     * If no item is selected, nothing happens.
     */
    public void delete() {
        listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
        listView.getSelectionModel().selectNext();
    }

    /**
     * Allows the user to upload a new photo to the current album.
     * Opens a file chooser dialog to select an image file, then creates a new Photo
     * object with the selected file's path,
     * creation date, and name. If the photo already exists in the album, does
     * nothing. Otherwise, adds the photo to the
     * album's list and updates the displayed list view.
     * 
     * @throws IOException if there is an error opening or reading the selected file
     */
    public void uploadImage() throws IOException {
        Stage currentStage = (Stage) upload.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(currentStage);
        if (selectedFile != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MILLISECOND, 0);
            Photo photo = new Photo(selectedFile.toURI().toString(), cal, selectedFile.getName());
            if (currAlbum.duplicate(photo.getCaption()))
                return;
            listView.getItems().add(photo);
            resize();
        }

    }

    /**
     * Switches to the slideshow screen, displaying all the photos in the current
     * album.
     * Creates an array of images from the current album's photos.
     * Initializes a new instance of the slideshow controller, passing the array of
     * images.
     * Sets the scene and stage of the slideshow, and shows the slideshow window.
     * Enables the user to use the left and right arrow keys to navigate the
     * slideshow.
     * 
     * @param event The action event that triggered the method.
     * @throws IOException If an I/O error occurs when loading the slideshow.fxml
     *                     file.
     */
    public void switchToslideshow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/photopack/fxml files/slideshow.fxml"));
        ArrayList<Photo> arr = currAlbum.getAlbum();
        ArrayList<Image> images = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            try {
                FileInputStream fileIn = new FileInputStream(arr.get(i).getPath());
                ObjectInputStream in = new ObjectInputStream(fileIn);
                SerializableImage serializableImage = (SerializableImage) in.readObject();
                in.close();
                fileIn.close();
                Image deserializedImage = serializableImage.getImage();

                images.add(deserializedImage);
            } catch (Exception e) {
                try {
                    Image image = new Image(arr.get(i).getPath());
                    images.add(image);
                } catch (Exception p) {
                }
            }
        }
        slideshowController slideshowcontroller = new slideshowController(images);
        loader.setController(slideshowcontroller);
        root = loader.load();
        slideshowController controller = loader.getController();
        Stage popUpWindow = new Stage();

        Scene scene = new Scene(root);
        scene.setOnKeyPressed((EventHandler<KeyEvent>) new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case LEFT:
                        controller.left();
                        break;
                    case RIGHT:
                        controller.right();
                        break;
                    default:
                        break;
                }
            }
        });
        popUpWindow.setTitle("Use left and right arrow keys to change pictures");
        popUpWindow.setScene(scene);
        popUpWindow.show();

    }

    /**
     * Displays an alert with the given warning message.
     * 
     * @param str the warning message to display
     */
    public void alert(String str) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(str);
        alert.showAndWait();
    }

    /**
     * Switches the scene to the five.fxml file.
     * 
     * @param click The mouse event that triggers the function.
     * @param photo The photo to be displayed in the new scene.
     * @throws IOException If the file cannot be loaded.
     */
    public void switchToFive(MouseEvent click, Photo photo) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/photopack/fxml files/five.fxml"));
        fiveController fivecontroller = new fiveController(photo, currAlbum, user);
        loader.setController(fivecontroller);
        root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) upload.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switch to Three.fxml view
     * 
     * @param click the MouseEvent that triggered the action
     * @throws IOException if Three.fxml file is not found
     */
    public void switchToThree(MouseEvent click) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/photopack/fxml files/three.fxml"));
        threeController threecontroller = new threeController(user);
        loader.setController(threecontroller);
        root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) upload.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
