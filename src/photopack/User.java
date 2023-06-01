package photopack;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The User class represents a user in a photo application, with a username and
 * a collection of albums.
 * Each User object has a unique username and an ArrayList of albums that the
 * user has created.
 * This class also implements the Serializable interface to allow objects of
 * this class to be serialized.
 */
public class User implements Serializable {
    /**
     * The username of the User object
     */
    private String username;
    /**
     * The ArrayList of Album objects created by the User object
     */
    private ArrayList<Album> albums;
    /**
     * A constant field that stores the serialVersionUID for the class
     */
    static final long serialVersionUID = 1L;

    /**
     * Constructs a User object with a given username and an ArrayList of Album
     * objects.
     * 
     * @param username the username of the User object
     * @param album    the ArrayList of Album objects created by the User object
     */
    public User(String username, ArrayList<Album> album) {
        this.username = username;
        this.albums = album;
    }

    /**
     * Sets the username of the User object.
     * 
     * @param username the new username of the User object
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the username of the User object.
     * 
     * @return the username of the User object
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the ArrayList of Album objects of the User object.
     * 
     * @param album the new ArrayList of Album objects of the User object
     */
    public void setAlbums(ArrayList<Album> album) {
        this.albums = album;
    }

    /**
     * Returns the ArrayList of Album objects of the User object.
     * 
     * @return the ArrayList of Album objects of the User object
     */
    public ArrayList<Album> getAlbums() {
        return this.albums;
    }

    /**
     * Adds an Album object to the ArrayList of Album objects of the User object.
     * 
     * @param album the Album object to be added
     */
    public void addAlbum(Album album) {
        this.albums.add(album);
    }

    /**
     * Deletes an Album object from the ArrayList of Album objects of the User
     * object.
     * 
     * @param album the Album object to be deleted
     */
    public void deleteAlbum(Album album) {
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i) == album)
                albums.remove(i);
        }
    }

    /**
     * Checks if the name of an Album object already exists in the ArrayList of
     * Album objects of the User object.
     * 
     * @param str the name of the Album object to be checked for duplicates
     * @return true if a duplicate name exists, false otherwise
     */
    public boolean duplicate(String str) {
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).getName().equals(str))
                return true;
        }
        return false;
    }

    /**
     * Returns a String representation of the User object.
     * 
     * @return a String representation of the User object
     */
    public String toString() {
        return username + " ";
    }

}
