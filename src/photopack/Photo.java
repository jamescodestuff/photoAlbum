package photopack;

 
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The Photo class represents a single photo in the photo management system. It stores information about the photo such as its path, caption, date, and tags. The date is stored as a Calendar object and can be accessed as either a Date or a Calendar.
 * The tags are stored as a Map of tag types to lists of tag values.
 * The Photo class is serializable, allowing it to be easily saved and loaded from disk.
 */
public class Photo implements Serializable{
    /**
     * A Calendar object representing the date/time when the photo was taken.
     */
    private Calendar date; 
    /**
     *  A String representing the caption/description of the photo.
     */
    private String caption; 
    /**
     * A String representing the file path of the photo on the local file system.
     */
    private String path;
    /**
     * A long value representing the version of the serialization protocol.
     */
    static final long serialVersionUID = 1L;
    /**
     * A Map object representing the tags associated with the photo. The keys of the map represent the tag types and the values represent the tag values 
     */
    private Map<String, List<String>> tags;
    
    /**
     * Creates a new Photo object with the given path, date, and caption.
     * Initializes the tags map with an empty HashMap.
     * @param path The file path of the photo
     * @param date The date of photo creation
     * @param caption The caption for the photo
     */
    public Photo(String path, Calendar date, String caption){
        this.path = path;
        this.date = date;
        this.caption = caption;
        this.tags = new HashMap<>();
    }
    /**
     * Creates a new Photo object with the given path, date, caption, and tags.
     * @param path The file path of the photo
     * @param date The date of photo creation
     * @param caption The caption for the photo
     * @param tags The map of tags associated with the photo
    */
    public Photo(String path, Calendar date, String caption, Map<String, List<String>> tags){
        this.path = path;
        this.date = date;
        this.caption = caption;
        this.tags = tags;
    }
    /**
     * Returns the date of the photo as a Date object.
     * @return The date of the photo as a Date object
     */
    public Date getDate() {
        return this.date.getTime();
    }
     /**
     * Returns the date of the photo as a calender object.
     * @return The date of the photo as a calender object
     */
    public Calendar getCal(){
        return this.date;
    }
    /**
     * Sets the date of the photo to the given Calendar object.
     * @param date The new date to set for the photo
     */
    public void setDate(Calendar date) {
        this.date = date;
    }
    /**
     * Returns the caption for the photo.
     * @return The caption for the photo
     */
    public String getCaption(){
        return this.caption;
    }
    /**
     * Sets the caption for the photo to the given String.
     * @param caption The new caption to set for the photo
     */
    public void setCaption(String caption){
        this.caption = caption;
    }
    /**
     * Returns the file path of the photo.
     * @return The file path of the photo
     */
    public String getPath() {
        return this.path;
    }
    /**
     * Sets the file path of the photo to the given path.
     * @param path The new file path to set for the photo
     */
    public void setThumbnail(String path){
        this.path = path;
    }
    /**
     * Checks if the given tag type and value are associated with the photo.
     * @param tagType The tag type to check for
     * @param tagValue The tag value to check for
     * @return true if the photo contains the given tag type and value, false otherwise
     */
    public boolean containsTagValue(String tagType, String tagValue){
        if (tags.containsKey(tagType)) {
            List<String> tagValues = tags.get(tagType);
            for (int i = 0; i < tagValues.size(); i++){
                if(tagValues.get(i).equals(tagValue)) return true; 
            }
            return false;
        } 
        return false;
    }
    /**
     * Adds a tag with the given tag type and tag value to the photo's tag map.
     * If the tag type already exists for the photo and the tag value is already associated with it,the method returns false and does not add the tag.
     * If the tag type does not exist for the photo, a new entry is created in the tag map and the tag value is associated with it.
     * @param tagType The type of tag to add.
     * @param tagValue The value of the tag to add.
     * @return true if the tag was successfully added, false otherwise.
     */
    public boolean addTag(String tagType, String tagValue) {
        if (tags.containsKey(tagType)) {
            List<String> tagValues = tags.get(tagType);
            for (int i = 0; i < tagValues.size(); i++){
                if(tagValues.get(i).equals(tagValue)) return false; 
            }
            tagValues.add(tagValue);
        } else {
            List<String> tagValues = new ArrayList<>();
            tagValues.add(tagValue);
            tags.put(tagType, tagValues);
        }
        return true;
    }
    /**
     * Removes the given tag value from the list of values associated with the given tag type.
     * If the tag type or value is not found, does nothing.
     * @param tagType the type of the tag to delete the value from
     * @param tagValue the value of the tag to delete
     */
    public void deleteTag(String tagType, String tagValue) {
        if (tags.containsKey(tagType)) {
            List<String> tagValues = tags.get(tagType);
            for (int i = 0; i < tagValues.size(); i++){
                if(tagValues.get(i).equals(tagValue)) tagValues.remove(i);
            }
        } 
    }
    /**
     * Adds a new tag type to the photo's map of tags.
     * @param tagType the new tag type to be added
     * @return true if the tag type was successfully added, false if it already existed in the map
     */
    public boolean addTagType(String tagType){
        if (tags.containsKey(tagType)) return false; 
        List<String> tagValues = new ArrayList<>();
        tags.put(tagType, tagValues);
        return true; 
    }

    /**
     * Deletes the specified tag type and all associated tag values from the photo's tags map.
     * @param tagType the tag type to delete
     */
    public void deleteTagType(String tagType) {
        Map<String, List<String>> tagsCopy = new HashMap<>(tags);
        for (String key : tagsCopy.keySet()) {
            if (key.equals(tagType)) {
                tags.remove(key);
            }
        }
    }
    /**
     * Returns a List of tag values associated with the given tag type
     * @param tagType the tag type to get the tag values for
     * @return a List of tag values associated with the given tag type
     */
    public List<String> getTagValues(String tagType) {
        return tags.get(tagType);
    }
    /**
     * Returns an ArrayList of all the tag types in the photo's tags map.
     * @return ArrayList of tag types
     */
    public ArrayList<String> getKeys(){
        ArrayList<String> keysList = new ArrayList<>();
        Iterator<String> keyIterator = tags.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            keysList.add(key);
        }
        return keysList;
    }
    /**
     * Returns a Map representing the tags of the Photo, where the keys are tag types and the values are lists of tag values.
     * @return a Map representing the tags of the Photo
     */
    public Map<String, List<String>> getTags(){
        return this.tags;
    }
    /**
     * Returns a string representation of this photo.
     * @return the photo's caption
     */
    public String toString(){
        return caption;
    }
    




}
