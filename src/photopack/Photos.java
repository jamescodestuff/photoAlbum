package photopack;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import javafx.stage.Stage;

/**
 * The class represents the main application class for a JavaFX photo app.
 * It extends the Application class from the JavaFX library, and provides a
 * start method to set up the primary stage of the application
 * and a main method to launch the application. The class also contains a static
 * ArrayList of Photo objects named "pictures".
 */
public class Photos extends Application {
    /**
     * A static ArrayList representing the list of photos in the application.
     * This static variable is used to store the list of Photo objects in the
     * application.
     * It is initialized as an empty ArrayList and can be accessed from other
     * classes and methods using the App.pictures variable.
     */
    public static ArrayList<Photo> pictures = new ArrayList<Photo>();

    /**
     * a Parent object representing the root of the scene
     */
    Parent root;

    /**
     * A static variable representing the UserApp instance of the application.
     * This static variable is used to access the UserApp instance of the
     * application from
     * other classes and methods. It is set to null by default and is initialized
     * when the
     * application is launched.
     */
    public static UserApp gapp;

    /**
     * This method is called when the JavaFX application is launched. It sets up the
     * primary stage for the
     * application by loading the login.fxml file using an FXMLLoader and setting
     * the scene for the primary stage.
     * 
     * @param primaryStage the primary stage for the application, represented as a
     *                     Stage object
     * @throws Exception if an error occurs while loading the login.fxml file
     */
    public void start(Stage primaryStage) throws Exception {
        gapp = UserApp.readApp();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/photopack/fxml files/login.fxml"));
        loginController loginController = new loginController(gapp.getUsers());
        loader.setController(loginController);
        root = loader.load();
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * This method is called when the application should stop, and it writes the
     * state of the UserApp to a file using the writeApp() method.
     */
    @Override
    public void stop() throws Exception {
        UserApp.writeApp(gapp);
    }

    /**
     * This method is the starting point for the JavaFX application.
     * 
     * @param args the command line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }

}