package photopack;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;


/**
 * This class represents a controller for the login.fxml file. The class
 * contains fields representing the Username TextField,
 * the loginButton, and an ArrayList of all user names in the system. The class
 * also contains a field named "root".
 * The loggingIn() method handles the logic for logging in to the system, while
 * the usernameWarning() method displays a warning dialog if the username is
 * empty.
 * The getUserList() method returns an ArrayList of Strings containing all user
 * names in the system.
 * @author Joey Zheng jz813 JunFeng Wang jw1397
 */

public class loginController implements Initializable {
    /**
     * Represents a TextField for the user to enter their username.
     */
    @FXML
    private TextField Username;
    /**
     * Represents a Button for the user to login.
     */
    @FXML
    private Button loginButton;
    /**
     * Represents an ArrayList of all user names in the system.
     */
    public ArrayList<User> allUsers = new ArrayList<User>();
    /**
     * The root node of the scene.
     */
    Parent root;

    /**
     * Constructs a loginController object with the specified list of users.
     * This constructor creates a new loginController object with the specified list
     * of users.
     * The allUsers list is set to the provided list of User objects.
     * 
     * @param users the ArrayList of User objects to set as the allUsers list
     */
    public loginController(ArrayList<User> users) {
        this.allUsers = users;
    }

    /**
     * Logs the user in by getting the username input from the text field. If the
     * input is empty, shows a warning and clears the text field. If the input is
     * "admin", switches to the admin view. If the input matches an existing user,
     * switches to their view. If the input does not match an existing user, creates
     * a new user with the input as username and switches to their view.
     * 
     * @throws IOException            if the FXML file for the next view cannot be
     *                                found or loaded.
     * @throws ClassNotFoundException if the User class cannot be found.
     */
    @FXML
    public void loggingIn() throws IOException, ClassNotFoundException {
        String usernameText = Username.getText();
        if (usernameText.trim().equals("")) {
            usernameWarning();
            Username.clear();
            return;
        } else if (usernameText.trim().equals("admin")) {

            switchToAdmin();
        } else {
            int index = -1;
            for (int i = 0; i < allUsers.size(); i++) {
                if (usernameText.trim().equals(allUsers.get(i).getUsername())) {
                    index = i;
                    break;
                }
            }
            if (index >= 0)
                switchToThree(allUsers.get(index));
            else {
                User newUser = new User(usernameText.trim(), new ArrayList<Album>());
                allUsers.add(newUser);
                switchToThree(newUser);
            }
        }
    }

    /**
     * This method displays a warning dialog with a message indicating that the
     * username cannot be empty.
     * The method creates an Alert object of type Warning, sets the title and header
     * text of the alert, and
     * shows the alert to the user using the showAndWait() method.
     */
    private void usernameWarning() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Username Cannot Be Empty!");
        alert.showAndWait();
    }

    /**
     * This method returns an ArrayList of Strings containing all user names in the
     * system.
     * 
     * @return an ArrayList of Strings containing all user names in the system.
     */
    public ArrayList<User> getUserList() {
        return allUsers;
    }

    /**
     * Switches to the "three" view, which displays the user's albums and allows
     * them to interact with them.
     * 
     * @param user the user whose albums will be displayed
     * @throws IOException if an error occurs during the loading of the "three" view
     */
    public void switchToThree(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/photopack/fxml files/three.fxml"));
        threeController threecontroller = new threeController(user);
        loader.setController(threecontroller);
        root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) Username.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switches to the admin screen.
     * Loads the admin.fxml file and sets the controller with the given list of
     * users.
     * Sets the root of the loader as the new scene for the current stage.
     * 
     * @throws IOException If there is an error loading the fxml file.
     */
    public void switchToAdmin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/photopack/fxml files/admin.fxml"));
        adminController admincontroller = new adminController(allUsers);
        loader.setController(admincontroller);
        root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) Username.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method is called by the FXMLLoader when initializing the controller. It
     * does not perform any operation.
     * 
     * @param location  the location used to resolve relative paths for the root
     *                  object or null if the location is not known
     * @param resources the resources used to localize the root object or null if
     *                  the root object was not localized
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
