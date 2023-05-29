package photopack;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * This class represents the controller for the 6th screen of the PhotoAlbum application.
 * It is responsible for handling the user's search of photos by tag and date, displaying the search results, and creating new albums from the search results.
 * It also allows the user to navigate back to the 3rd screen of the application.
 * @author Joey Zheng jz813 JunFeng Wang jw1397
 */
public class sixController implements Initializable{
    /**
     * User object for the current user of the application.
     */
    private User user; 
    /**
     * Set of all photos owned by the current user.
     */
    private HashSet<Photo> allPhotos = new HashSet<Photo>();
    /**
     *  Root node of the scene
     */
    private Parent root;
    /**
     * JavaFX choice box for tag type 1 selection.
     */
    @FXML
    private ChoiceBox<String> tagType1;
    /**
     * JavaFX choice box for tag type 2 selection.
     */
    @FXML
    private ChoiceBox<String> tagType2; 
    /**
     *  JavaFX choice box for AND/OR operator selection.
     */
    @FXML
    private ChoiceBox<String> andOr; 
    /**
     * JavaFX text field for tag value 1 input.
     */
    @FXML 
    private TextField tagVal1; 
    /**
     * JavaFX text field for tag value 2 input.
     */
    @FXML 
    private TextField tagVal2; 
    /**
     *  JavaFX date picker for start date selection.
     */
    @FXML
    private DatePicker from; 
    /**
     *  JavaFX date picker for end date selection.
     */
    @FXML
    private DatePicker to; 
    /**
     * JavaFX text field for new album name input.
     */
    @FXML
    private TextField albumName;
    /**
     * JavaFX list view for displaying search results.
     */
    @FXML
    private ListView<Photo> listView = new ListView<Photo>();
    /**
     * Constructs a new sixController object with the specified User.
     * @param user the user associated with this sixController
     */
    public sixController(User user){
        this.user = user; 
    }
    /**
     * Initializes the controller by retrieving all photos from the user's albums and populating the choice boxes and the AND/OR choice box with options.
     * @param location The URL location of the FXML file.
     * @param resources The resources associated with the FXML file.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<Album> arr = user.getAlbums();
        HashSet<String> set = new HashSet<String>();
        for (int i = 0; i < arr.size(); i++){
            Album album = arr.get(i); 
            ArrayList<Photo> photoArr = album.getAlbum();
            for(int j = 0; j < photoArr.size(); j++){
                Photo photo = photoArr.get(j);
                allPhotos.add(photo); 
                ArrayList<String> TagArr = photo.getKeys();
                for(int k = 0; k < TagArr.size(); k++){
                    set.add(TagArr.get(k));
                }
            }
        }
        tagType1.getItems().add(null);
        tagType1.getItems().addAll(set);
        tagType2.getItems().add(null);
        tagType2.getItems().addAll(set);
        andOr.getItems().add("AND");
        andOr.getItems().add("OR");
        andOr.getSelectionModel().selectFirst();
    }
    /**
     * Searches the set of all photos based on the selected tags and date range, and displays the results in the ListView.
     * If any required fields are missing, an alert message is displayed.
     */
    public void search(){
        ArrayList<Photo> selectedPhotos = new ArrayList<Photo>();
        LocalDate toDate = to.getValue(); 
        LocalDate fromDate = from.getValue(); 
        Calendar toCal = Calendar.getInstance();
        Calendar fromCal = Calendar.getInstance();
        if(toDate != null){
            toCal.set(toDate.getYear(), toDate.getMonthValue() - 1, toDate.getDayOfMonth());
            toCal.set(Calendar.HOUR_OF_DAY, 0);
            toCal.set(Calendar.MINUTE, 0);
            toCal.set(Calendar.SECOND, 0);
            toCal.set(Calendar.MILLISECOND, 0);
        }
        if(fromDate != null){
            fromCal.set(fromDate.getYear(), fromDate.getMonthValue() - 1, fromDate.getDayOfMonth());
            fromCal.set(Calendar.HOUR_OF_DAY, 0);
            fromCal.set(Calendar.MINUTE, 0);
            fromCal.set(Calendar.SECOND, 0);
            fromCal.set(Calendar.MILLISECOND, 0);
        }
        for (Photo item : allPhotos) {
            Calendar cal = (Calendar) item.getCal().clone(); 
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            if(!(toDate == null || fromDate == null)){
                int compare1 = cal.compareTo(toCal);
                int compare2 = cal.compareTo(fromCal);
                if(!(compare1 == 0 || compare2 == 0 || (compare2 > 0 && compare1 < 0))) continue;
            }
            else if(fromDate != null){
                int compare = cal.compareTo(fromCal);
                if(compare != 0) continue; 
            }
            String tagT1 = tagType1.getValue();
            String tagT2 = tagType2.getValue(); 
            String tagV1 = tagVal1.getText();
            String tagV2 = tagVal2.getText();
            if((tagT1 != null && tagV1.equals("")) || (tagT2 != null && tagV2.equals(""))){
                alert("Please select a tagValue or set TagType to nothing");
                break;
            }
            if(tagT1 == null && !tagV1.equals("")){
                alert("Please select a tag for TagType1");
                break;
            }
            if(tagT2 == null && !tagV2.equals("")){
                alert("Please select a tag for TagType2");
                break;
            }
            String ao = andOr.getValue(); 
            if(ao.equals("AND")){
                if(tagT1 != null){
                    if(!item.containsTagValue(tagT1, tagV1)) continue;
                }
                if(tagT2 != null){
                    if(!item.containsTagValue(tagT2, tagV2)) continue;
                }
                if(tagT1 != null && tagT2 != null){
                    if(!item.containsTagValue(tagT1, tagV1) || !item.containsTagValue(tagT2, tagV2)) continue;
                }
            }
            if(ao.equals("OR")){
                if(tagT1 != null && tagT2 != null){
                    if(!item.containsTagValue(tagT1, tagV1) && !item.containsTagValue(tagT2, tagV2)) continue;
                }
                else if(tagT1 != null){
                    if(!item.containsTagValue(tagT1, tagV1)) continue;
                }
                else if(tagT2 != null){
                    if(!item.containsTagValue(tagT2, tagV2)) continue;
                }
            }
            if(fromDate == null && tagT1 == null && tagT2 == null) break; 
            selectedPhotos.add(item);
        }
        ObservableList<Photo> items = FXCollections.observableList(selectedPhotos);
        listView.setItems(items);
        resize();
    }

    /**
     * Resizes the photos in the listView and sets up a custom cell factory that shows the image, caption, and sets the font size.
     */
    public void resize(){
        listView.setCellFactory(param -> new ListCell<Photo>() {
            private ImageView imageView = new ImageView();
            @Override
            public void updateItem(Photo pic, boolean empty) {
                super.updateItem(pic, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try  {
                        FileInputStream fileIn = new FileInputStream(pic.getPath());
                        ObjectInputStream in = new ObjectInputStream(fileIn);
                        SerializableImage serializableImage = (SerializableImage) in.readObject();
                        in.close();
                        fileIn.close();
                        Image deserializedImage = serializableImage.getImage();
                        
                        imageView.setImage(deserializedImage);
                    } catch (Exception e) {
                        try {
                            Image image = new Image(pic.getPath());
                        imageView.setImage(image);
                        } catch (Exception p) {   
                        }
                    }
                    imageView.setFitHeight(150);
                    imageView.setFitWidth(200);
                    setText(pic.getCaption());
                    setFont(Font.font(20));
                    setGraphic(imageView);
                }
            }
        });
    }
    
    /**
     * Displays a warning dialog box with the given message.
     * @param str the message to display in the dialog box
     */
    public void alert(String str) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(str);
        alert.showAndWait();
    }
    /**
     * Creates a new album with the name and photos specified in the UI input fields.
     * If the name field is empty or the list of photos is empty, an alert is displayed.
     * If an album with the same name already exists, an alert is displayed and the album is not created.
     * Otherwise, a new album is created with the specified name and photos, and added to the user's list of albums.
     */
    public void createAlbum(){
        if(albumName.getText().equals("")){
            alert("Please input a name");
            return;
        }
        if(listView.getItems().isEmpty()){
            alert("List is empty");
            return;
        }
        if(user.duplicate(albumName.getText())){
            alert("Album name exist");
            return;
        }
        ObservableList<Photo> observableList = listView.getItems();
        ArrayList<Photo> arrayList = new ArrayList<>(observableList);
        Album newAlbum = new Album(albumName.getText(), arrayList); 
        if(user.duplicate(newAlbum.getName())){
            alert("Name of album already exist");
            return; 
        }
        user.addAlbum(newAlbum);
        alert("Album created");
        albumName.setText("");
    }
    /**
     * Switches to the "Three" view when the user clicks on a certain element.
     * @param click MouseEvent object representing the user's click.
     * @throws IOException if the "Three" view FXML file cannot be loaded.
     */
    public void switchToThree(MouseEvent click) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/photopack/fxml files/three.fxml"));
        threeController threecontroller = new threeController(user);
        loader.setController(threecontroller);
        root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) tagVal1.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
