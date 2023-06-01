package photopack;

import java.io.*;
import java.util.ArrayList;

/**
 * The UserApp class represents an application for managing a collection of User
 * objects.
 * This class allows the addition of User objects to an ArrayList, and the
 * writing and reading of UserApp objects to and from a file.
 * Each UserApp object has an ArrayList of User objects and two constant fields
 * that represent the directory and file name for storing UserApp objects.
 */
public class UserApp implements Serializable {
    /**
     * An ArrayList representing the list of users in the application.
     * This private variable is used to store the list of User objects in the
     * application.
     * It is initialized as an empty ArrayList and can only be accessed and modified
     * within the UserApp class.
     */
    private ArrayList<User> users;

    /**
     * A static final variable representing the directory where the application data
     * is stored.
     * This static final variable is used to store the directory name where the
     * application data
     * is stored. It is set to "dat" by default and can be accessed from other
     * classes and methods using the UserApp.storeDir variable.
     */
    public static final String storeDir = "dat";
    /**
     * A static final variable representing the filename of the file where the user
     * data is stored.
     * This static final variable is used to store the filename of the file where
     * the user data is stored. It is set to "users.dat" by default
     * an be accessed from other classes and methods using the UserApp.storeFile 
     * ariable.
     */
    public static final String storeFile = "users.dat";

    /**
     * Constructs a UserApp object with an empty ArrayList of User objects
     */
    public UserApp() {
        users = new ArrayList<User>();
    }

    /**
     * Adds a User object to the ArrayList of User objects in the UserApp object.
     * 
     * @param p the User object to be added
     */
    public void addUser(User p) {
        users.add(p);
    }

    /**
     * Prints the String representation of each User object in the ArrayList of User
     * objects in the UserApp object.
     */
    public void writePoints() {
        for (User p : users) {
            System.out.println(p);
        }
    }

    /**
     * Returns the ArrayList of User objects in the UserApp object.
     * 
     * @return the ArrayList of User objects in the UserApp object
     */
    public ArrayList<User> getUsers() {
        return this.users;
    }

    /**
     * Writes a UserApp object to a file in the directory specified by the storeDir
     * constant and with the file name specified by the storeFile constant.
     * 
     * @param gapp the UserApp object to be written to the file
     * @throws IOException if there is an error writing to the file
     */
    public static void writeApp(UserApp gapp) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(storeDir + File.separator + storeFile));
        oos.writeObject(gapp);
    }

    /**
     * Reads a UserApp object from a file in the directory specified by the storeDir
     * constant and with the file name specified by the storeFile constant.
     * 
     * @return the UserApp object read from the file
     * @throws IOException            if there is an error reading from the file
     * @throws ClassNotFoundException if the class of the object read from the file
     *                                cannot be found
     */
    public static UserApp readApp()
            throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(storeDir + File.separator + storeFile));
        UserApp gapp = (UserApp) ois.readObject();
        return gapp;
    }

}