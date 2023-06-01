package photopack;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class represents the controller for the slideshow view in the photo management application.
 * It implements the Initializable interface from JavaFX to initialize the view when it is loaded.
 * It contains methods for moving to the previous or next image in the slideshow, and a constructor for passing in the list of images to display.
 * The class utilizes an ImageView object to display the images, and keeps track of a counter to know which image to display next.
 */

public class slideshowController implements Initializable{
    /**
     * ImageView object for displaying the images in the slideshow.
     */
    @FXML ImageView imageView;
    /**
     * ArrayList containing all the images to be displayed in the slideshow.
     */
    private ArrayList<Image> listOfImages;
    /**
     *  Counter variable to keep track of the current image being displayed in the slideshow.
     */
    private int counter = 0; 
    /**
     * Parent object representing the root of the FXML hierarchy of the slideshow view.
     */
    Parent root;
   /**
    * Initializes a new instance of the {@link slideshowController} class.
    * @param listOfImages The list of images to display in the slideshow.
    */
    public slideshowController(ArrayList<Image> listOfImages){
        this.listOfImages = listOfImages; 
    }

    /**
     * This method updates the image in the imageView by decrementing the counter and retrieving the previous image from the listOfImages.
     * If an exception is caught, the counter is incremented to avoid an out of bounds exception.
     */
    public void left(){
        try {
            imageView.setImage(listOfImages.get(--counter));
        } catch (Exception e) {
            counter++;
        }
    }
    /**
     * Moves the slideshow to the next image in the list of images, if possible.
     * If not possible, the counter remains unchanged and the current image is displayed.
     */
    public void right(){
        try {
            imageView.setImage(listOfImages.get(++counter));
        } catch (Exception e) {
            counter--;
        }
    }

    /**
     * Initializes the slideshow by setting the image in the {@code imageView} to the first image in the {@code listOfImages}.
     * @param location the location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources the resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageView.setImage(listOfImages.get(counter));
    }
}
