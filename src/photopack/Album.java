package photopack;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * The "Album" class represents a collection of photos stored in an ArrayList.
 * It implements the Serializable interface and contains methods to add, delete,
 * and check for duplicate photos.
 * It also has methods to retrieve and modify the name of the album and its
 * photo collection.
 */

public class Album implements Serializable {
    /**
     * The private instance variable "album" is an ArrayList of Photo objects
     * representing the collection of photos stored in an Album.
     */
    private ArrayList<Photo> album;
    /**
     * The private instance variable "name" is an String representing the name of an
     * Album.
     */
    private String name;
    /**
     * The "serialVersionUID" is a static final variable used to ensure version
     * control compatibility of serialized objects.
     * It is used to identify a specific version of the class that was used during
     * the serialization process.
     * This value is set to 1L in this class.
     */
    static final long serialVersionUID = 1L;

    /**
     * This constructor creates a new Album object with a specified name and an
     * ArrayList of Photo objects representing its collection of photos.
     * The "name" parameter represents the name of the album, and the "photo"
     * parameter represents the collection of photos in the album.
     * This constructor initializes the "album" and "name" instance variables with
     * the provided parameters.
     * 
     * @param name  the name of the album
     * @param photo an ArrayList of Photo objects to be added to the album
     */
    public Album(String name, ArrayList<Photo> photo) {
        this.album = photo;
        this.name = name;
    }

    /**
     * This constructor creates a new Album object with a specified name.
     * The "name" parameter represents the name of the album.
     * This constructor initializes an empty ArrayList for the "album" instance
     * variable and sets the "name" instance variable to the provided parameter.
     * 
     * @param name the name of the album
     */
    public Album(String name) {
        this.album = new ArrayList<Photo>();
        this.name = name;
    }

    /**
     * This method sets the name of the Album object.
     * The "name" parameter represents the new name for the album.
     * It updates the "name" instance variable with the provided parameter.
     * 
     * @param name the new name of the album
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method retrieves the name of the Album object.
     * It returns the current value of the "name" instance variable.
     * 
     * @return the name of the album
     */
    public String getName() {
        return this.name;
    }

    /**
     * This method sets the collection of photos for the Album object.
     * The "photo" parameter represents the new ArrayList of Photo objects to be set
     * as the album's collection of photos.
     * It updates the "album" instance variable with the provided parameter.
     * 
     * @param photo the ArrayList of Photo objects to set as the new album list
     */
    public void setAlbum(ArrayList<Photo> photo) {
        this.album = photo;
    }

    /**
     * This method retrieves the collection of photos of the Album object.
     * It returns the current value of the "album" instance variable, which is an
     * ArrayList of Photo objects representing the collection of photos in the
     * album.
     * 
     * @return the ArrayList of Photo objects in the album
     */
    public ArrayList<Photo> getAlbum() {
        return this.album;
    }

    /**
     * This method adds a new Photo object to the collection of photos in the Album
     * object.
     * The "photo" parameter represents the new Photo object to be added to the
     * album's collection of photos.
     * It adds the provided Photo object to the "album" ArrayList instance variable.
     * 
     * @param photo the Photo object to add to the album list
     */
    public void addPhoto(Photo photo) {
        this.album.add(photo);
    }

    /**
     * This method removes a specific Photo object from the collection of photos in
     * the Album object.
     * The "photo" parameter represents the Photo object to be removed from the
     * album's collection of photos.
     * It searches through the "album" ArrayList instance variable for the provided
     * Photo object and removes it from the collection if it is found.
     * 
     * @param photo the Photo object to remove from the album list
     */
    public void deletePhoto(Photo photo) {
        for (int i = 0; i < album.size(); i++) {
            if (album.get(i) == photo)
                album.remove(i);
        }
    }

    /**
     * This method checks whether the album's collection of photos contains a Photo
     * object with a specific caption.
     * The "str" parameter represents the caption to be searched for.
     * It searches through the "album" ArrayList instance variable for a Photo
     * object with a matching caption and returns true if found, otherwise false.
     * 
     * @param str the caption to search for
     * @return true if a photo with the given caption exists in the album, false
     *         otherwise
     */
    public boolean duplicate(String str) {
        for (int i = 0; i < album.size(); i++) {
            if (album.get(i).getCaption().equals(str))
                return true;
        }
        return false;
    }

    /**
     * This method checks whether the album's collection of photos contains a
     * specific Photo object.
     * The "photo" parameter represents the Photo object to be searched for.
     * It searches through the "album" ArrayList instance variable for the provided
     * Photo object and returns true if found, otherwise false.
     * 
     * @param photo the Photo object to search for
     * @return true if the given photo exists in the album, false otherwise
     */
    public boolean duplicatePhoto(Photo photo) {
        for (int i = 0; i < album.size(); i++) {
            if (album.get(i) == photo)
                return true;
        }
        return false;
    }

    /**
     * This method returns a string representation of the Album object.
     * It includes the name of the album, the number of photos in its collection,
     * and the earliest and latest dates of the photos.
     * If the album's collection of photos is empty, it returns a string with only
     * the album's name and the number of photos.
     */
    public String toString() {

        int size = album.size();
        if (size == 0)
            return (name + " \n" + "#ofPhotos: " + size);

        Calendar EDate = album.get(0).getCal();
        Calendar LDate = album.get(0).getCal();

        for (int i = 1; i < size; i++) {
            Photo photo = album.get(i);
            Calendar currentDate = photo.getCal();
            if (currentDate.compareTo(LDate) > 0) {
                LDate = currentDate;
            }
            if (currentDate.compareTo(EDate) < 0) {
                EDate = currentDate;
            }
        }
        return (name + " \n" + "#ofPhotos: " + size + "\nEarliest date: " + EDate.getTime() + " \nLatest date: "
                + LDate.getTime());
    }
}
