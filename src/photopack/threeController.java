package photopack;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


/**
 * Controller class for the third screen of the application, which displays a list of albums for the user to view and edit.
 * Allows for the addition, deletion, and renaming of albums, as well as the navigation to the fourth and sixth screens.
 */
public class threeController implements Initializable{
    /**
     * The User object representing the currently logged in user.
     */
    private User user; 
    /**
     * The Parent object representing the root node of the JavaFX scene
     */
    private Parent root;
    /**
     * The TextField object for entering the name of a new or renamed album.
     */
    @FXML 
    private TextField name; 
    /**
     * The ListView object for displaying the list of albums.
     */
    @FXML 
    private ListView<Album> listView = new ListView<Album>();

    /**
     * Constructor that sets the user of the controller.
     * @param user the user whose albums are being displayed
     */
    public threeController(User user){
        this.user = user; 
    }
    /**
     * Initializes the controller by setting the list of albums to the ListView and setting up the double-click handler for each item.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 2) {
                    //Use ListView's getSelected Item
                    Album currentItemSelected = listView.getSelectionModel().getSelectedItem();
                    if(currentItemSelected == null) return;
                    try {
                        switchToFour(click, currentItemSelected);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        ObservableList<Album> items = FXCollections.observableList(user.getAlbums());
        listView.setItems(items);
        
    }
    /**
     * Adds a new album to the user's list of albums.
     */
    public void add(){
        if(name.getText().equals("")) return; 
        if(user.duplicate(name.getText())){
            alert("Album already exist");
            return;
        }
        listView.getItems().add(new Album(name.getText()));
        name.setText("");
    }
    /**
     * Deletes the selected album from the user's list of albums.
     */
    public void delete() {
        listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
        listView.getSelectionModel().selectNext();
    }
    /**
     * Renames the selected album to the inputted name.
     */
    public void rename(){
        if(name.getText().equals("")) return; 
        if(listView.getSelectionModel().getSelectedItem() == null) return; 
        if(user.duplicate(name.getText())){
            alert("Album already exist");
            return;
        }
        Album album = listView.getSelectionModel().getSelectedItem();         
        album.setName(name.getText());
        listView.refresh();
    }
    /**
     * Displays a warning message to the user.
     * @param str the warning message to be displayed
     */
    public void alert(String str) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(str);
        alert.showAndWait();
    }
    /**
     * Switches the current window to the six.fxml window by loading the six.fxml file and setting the sixController as its controller.
     * The sixController is constructed with the current user object. A new Scene is created with the loaded root and the current stage is set with this Scene.
     * @param click The MouseEvent that triggers this method
     * @throws IOException If there is an error loading the six.fxml file
     */
    public void switchToSix(MouseEvent click) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/photopack/fxml files/six.fxml"));
        sixController sixcontroller = new sixController(user);
        loader.setController(sixcontroller);
        root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) name.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Logs out the current user and loads the login screen.
     * @throws IOException if there is an error loading the login screen FXML file
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     */
    public void logOut() throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/photopack/fxml files/login.fxml"));
        loginController login = new loginController(Photos.gapp.getUsers());
        loader.setController(login);
        root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) name.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Switches to the "four.fxml" file when the user double-clicks on an album in the list view, and passes the selected album to the controller of the "four.fxml" file.
     * @param click The mouse event of the double-click action
     * @param album The selected album in the list view
     * @throws IOException If the "four.fxml" file is not found or cannot be loaded
     */
    public void switchToFour(MouseEvent click, Album album) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/photopack/fxml files/four.fxml"));
        fourController sixcontroller = new fourController(user, album);
        loader.setController(sixcontroller);
        root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) name.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
