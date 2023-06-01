package photopack;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * This class represents the controller for the admin.fxml file.
 * It implements the Initializable interface and contains methods to add and
 * delete users from the system, log out the current user,
 * display warning and confirmation dialogs.
 * The class also initializes a ListView with an ObservableList of all user
 * names in the system passed to the constructor.
 */

public class adminController implements Initializable {
    /**
     * The private instance variable "root" stores a reference to the root node of a
     * tree structure.
     * The data type of "root" is "Parent", which is a class representing a node in
     * a JavaFX scene graph.
     */
    private Parent root;
    /**
     * Represents a JavaFX ListView object used to display a list of strings.
     */
    @FXML
    private ListView<User> listView;
    /**
     * Represents a JavaFX TextField object used to display a user's Username.
     */
    @FXML
    private TextField Username;
    /**
     * Represents a JavaFX Button object used to add a user.
     */
    @FXML
    private Button addUserButton;
    /**
     * Represents a JavaFX Button object used to delete a user.
     */
    @FXML
    private Button deleteUserButton;
    /**
     * Represents a JavaFX Button object used to logout.
     */
    @FXML
    private Button logOutButton;
    /**
     * Represents a private int used for the size of the list.
     */
    private int listSize = 0;
    /**
     * Represents an arraylist of strings to display a list of users.
     */
    private ArrayList<User> users;

    /**
     * Initializes a new instance of the adminController class with the specified
     * list of users.
     * 
     * @param users an ArrayList of Strings containing the usernames of all users in
     *              the system
     */
    public adminController(ArrayList<User> users) {
        this.users = users;
    }

    /**
     * This method adds a user to the list of users displayed in the ListView. It
     * checks if the entered username already exists in the list,
     * displays a warning dialog if it does, and adds the username to the list if it
     * does not. It also clears the username TextField and
     * selects the newly added user in the ListView.
     */
    @FXML
    public void addUser() {
        if (Username.getText().equals(""))
            return;
        for (User username : listView.getItems()) {
            if (username.getUsername().equals(Username.getText())) {
                dupUserWarning();
                Username.clear();
                return;
            }
        }
        
        User newUser = new User(Username.getText(), new ArrayList<Album>());
        listView.getItems().add(newUser);
        Username.clear();
        listSize++;
    }

    /**
     * Deletes the currently selected user from the listView.
     * If user confirms the action, the method will remove the user from the list
     * and select the next available user.
     * If there is no next user, the method will select the first user in the list.
     */
    @FXML
    public void deleteUser() {
        if (confirmation()) {
            int index = listView.getSelectionModel().getSelectedIndex();
            listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
            listSize--;

            if (index == 0)
                listView.getSelectionModel().selectFirst();
            else if (listView.getSelectionModel().getSelectedIndex() == listSize - 1)
                ;
            else
                listView.getSelectionModel().selectNext();
        }
    }

    /**
     * This method logs out the current user and redirects to the login page by
     * loading the login.fxml file and setting the scene for the primary stage.
     * 
     * @throws IOException            if an error occurs while loading the
     *                                login.fxml file
     * @throws ClassNotFoundException if the Photos.gapp.getUsers() method cannot
     *                                find the Users class
     */
    @FXML
    public void logOut() throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/photopack/fxml files/login.fxml"));
        loginController login = new loginController(Photos.gapp.getUsers());
        loader.setController(login);
        root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) listView.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Displays a warning message using an Alert object with a title "Warning" and a
     * header "This User Already Exist!".
     * The method is used to notify the user that the user they are trying to add
     * already exists in the system.
     */
    private void dupUserWarning() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("This User Already Exist!");
        alert.showAndWait();
    }

    /**
     * Displays a confirmation dialog with "OK" and "Cancel" buttons and returns a
     * boolean value indicating whether the "OK" button was pressed or not.
     * 
     * @return a boolean value indicating whether the "OK" button was pressed or not
     */
    private boolean confirmation() {
        Boolean confirm = false;

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm your action");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK)
            confirm = true;
        return confirm;

    }

    /**
     * This method is called to initialize the controller after its root element has
     * been completely processed.
     * It sets up an ObservableList of Strings containing all the users passed to
     * the constructor and sets the items for the ListView to this list.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<User> items = FXCollections.observableList(users);
        listView.setItems(items);

    }
}
