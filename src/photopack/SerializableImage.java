package photopack;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

/**
 * This class implements Serializable and represents an Image object that can be
 * serialized and deserialized.
 * The image is stored as a JavaFX Image object which is transient, meaning it
 * won't be serialized automatically.
 * Therefore, the class includes custom writeObject and readObject methods to
 * serialize and deserialize the image.
 */
public class SerializableImage implements Serializable {
    /**
     * A serializable image class representing a JavaFX image that can be serialized
     * and deserialized.
     * The image field is marked as transient to avoid it being serialized directly,
     * and instead is
     * converted to a BufferedImage using SwingFXUtils and written to the
     * ObjectOutputStream as a PNG.
     * The image can be retrieved using the getImage() method.
     */
    private transient Image image;

    /**
     * Constructs a SerializableImage object with the specified Image.
     * 
     * @param image the Image to be stored in the SerializableImage object.
     */
    public SerializableImage(Image image) {
        this.image = image;
    }

    /**
     * Custom serialization method to write the object to a stream.
     * The method first calls the defaultWriteObject method from the
     * ObjectOutputStream class.
     * Then it writes the image data to the output stream in PNG format using the
     * ImageIO class.
     * 
     * @param out the output stream to write the object to
     * @throws IOException if an I/O error occurs while writing to the stream
     */
    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.defaultWriteObject();
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", out);
    }

    /**
     * Deserializes the wrapped image from an input stream using the default object
     * serialization protocol.
     * 
     * @param in the input stream from which the image will be deserialized.
     * @throws IOException            if there is an error while reading from the
     *                                input stream.
     * @throws ClassNotFoundException if the class of the serialized object cannot
     *                                be found.
     */
    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        image = SwingFXUtils.toFXImage(ImageIO.read(in), null);
    }

    /**
     * Returns the deserialized JavaFX image contained in the SerializableImage
     * object.
     * 
     * @return The deserialized JavaFX image.
     */
    public Image getImage() {
        return image;
    }
}
