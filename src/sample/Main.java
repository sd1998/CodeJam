package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

public class Main extends Application{

    public static ArrayList<Animal> currentAnimals = new ArrayList<Animal>();
    private Scene currentLayout;
    private static Stage screen;
    boolean flag;
    ImageView errorImage,errorImage1,errorImage2;
    Label hungry = null;
    File imageFile;
    Image errorImg;
    ListView<String> currentAnimalList = new ListView<String>();
    String imagePath;

    @Override
    public void start(Stage primaryStage) throws Exception{
        screen = primaryStage;
        currentAnimals = DB.getCurrentAnimals();
        primaryStage.setTitle("Animal Data");
        currentAnimalList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Button addButton = new Button("Add");
        Button removeButton = new Button("Remove");
        Button exitButton = new Button("Exit");
        HBox buttonBox = new HBox(50);
        buttonBox.getChildren().addAll(addButton,removeButton,exitButton);
        VBox mainLayout = new VBox(20);
        mainLayout.getChildren().addAll(currentAnimalList,buttonBox);
        populateListView(currentAnimalList,currentAnimals);
        for(Animal animal : currentAnimals){
            initTimer(animal);
        }
        currentAnimalList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Scene scene = specificItemUI(currentAnimalList.getSelectionModel().getSelectedIndex());
                System.out.println(currentAnimalList.getSelectionModel().getSelectedIndex());
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
        addButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                Scene addAnimalScene = addAnimalGui();
                primaryStage.setScene(addAnimalScene);
                primaryStage.show();
            }
        });
        removeButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                Scene removeanimalScene = removeAnimalUI();
                primaryStage.setScene(removeanimalScene);
                primaryStage.show();
            }
        });
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
        currentLayout = new Scene(mainLayout,500,500);
        primaryStage.setScene(currentLayout);
        primaryStage.show();
    }

    public Scene removeAnimalUI(){
        ListView<String> multipleSelectListView = new ListView<String>();
        multipleSelectListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Button removeButton = new Button("Remove");
        Button backButton = new Button("Back");
        HBox buttonBox = new HBox(50);
        buttonBox.getChildren().addAll(removeButton,backButton);
        VBox removeLayout = new VBox(50);
        populateListView(multipleSelectListView,currentAnimals);
        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ObservableList<Integer> selectedIndex = multipleSelectListView.getSelectionModel().getSelectedIndices();
                ArrayList<Animal> toDelete = new ArrayList<Animal>();
                for(Integer integer : selectedIndex){
                    toDelete.add(currentAnimals.get(Integer.parseInt(integer.toString())));
                }
                for(Animal animal : toDelete){
                    currentAnimals.remove(animal);
                }
                toDelete.clear();
                multipleSelectListView.getItems().remove(0,multipleSelectListView.getItems().size());
                populateListView(multipleSelectListView,currentAnimals);
                DB.setCurrentAnimals(currentAnimals);
                currentAnimalList.getItems().remove(0,currentAnimalList.getItems().size());
                populateListView(currentAnimalList,currentAnimals);
            }
        });
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                screen.setScene(currentLayout);
                screen.show();
            }
        });
        removeLayout.getChildren().addAll(multipleSelectListView,buttonBox);
        Scene scene = new Scene(removeLayout,700,700);
        return scene;
    }

    public Scene specificItemUI(int selectedIndex){
        Animal animal = currentAnimals.get(selectedIndex);
        Label type = new Label("Type");
        Label currentStatus = new Label("Current Status");
        Label feedingTime = new Label("Feeding Time");
        Label type1 = new Label(animal.getAnimalType());
        Label currentStatus1 = new Label(""+animal.getCurrentStatus());
        Label feedingTime1 = new Label(""+animal.getFeedingTime());
        Button feedButton = new Button("Feed Animal");
        Button backButton = new Button("Back");
        ImageView posterView = new ImageView();
        File imageFile;
        Image image;
        if(! (animal.getImagePath() == null || animal.getImagePath().length() <= 0)){
            imageFile = new File(animal.getImagePath());
            image = new Image(imageFile.toURI().toString());
            posterView.setImage(image);
        }
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                screen.setScene(currentLayout);
                screen.show();
            }
        });
        feedButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentAnimals.get(selectedIndex).setCurrentStatus(currentAnimals.get(selectedIndex).getCurrentStatus() + 10);
                if(currentAnimals.get(selectedIndex).getCurrentStatus() > 50){
                    hungry.setText("Not Hungry");
                    hungry.setTextFill(Color.web("#00ff00"));
                }
                currentStatus1.setText(""+animal.getCurrentStatus());
                currentAnimals.get(selectedIndex).getFoodTimer().cancel();
                initTimer(currentAnimals.get(selectedIndex));
            }
        });
        if(animal.getCurrentStatus() <= 50){
            hungry = new Label("Hungry");
            hungry.setTextFill(Color.web("#ff0000"));
        }
        else{
            hungry = new Label("Not Hungry");
            hungry.setTextFill(Color.web("#00ff00"));
            feedButton.setDisable(true);
        }
        HBox typeBox = new HBox(50);
        typeBox.getChildren().addAll(type,type1);
        HBox currentStatusBox = new HBox(40);
        currentStatusBox.getChildren().addAll(currentStatus,currentStatus1);
        HBox feedingTimeBox = new HBox(40);
        feedingTimeBox.getChildren().addAll(feedingTime,feedingTime1);
        HBox hungryBox = new HBox(40);
        hungryBox.getChildren().addAll(hungry,feedButton,backButton);
        VBox specificItemLayout = new VBox(40);
        specificItemLayout.getChildren().addAll(typeBox,currentStatusBox,feedingTimeBox,hungryBox);
        HBox mainLayout = new HBox(50);
        mainLayout.getChildren().addAll(posterView,specificItemLayout);
        Scene scene = new Scene(mainLayout,700,700);
        return scene;
    }

    public Scene addAnimalGui(){
        javafx.scene.control.Label animalTypeLabel = new javafx.scene.control.Label("Enter Animal Type");
        javafx.scene.control.Label animalFeedingTimeLabel = new javafx.scene.control.Label("Enter Feeding Time");
        javafx.scene.control.Label animalCurrentStatusLabel = new javafx.scene.control.Label("Enter Current Hunger Status");
        javafx.scene.control.TextField animalTypeField = new javafx.scene.control.TextField();
        javafx.scene.control.TextField animalFeedingTimeField = new javafx.scene.control.TextField();
        javafx.scene.control.TextField animalCurrentStatusLabelField = new javafx.scene.control.TextField();
        errorImage = new ImageView();
        errorImage1 = new ImageView();
        errorImage2 = new ImageView();
        imageFile = new File("src/sample/sign-159285_1280.png`");
        errorImg = new Image(imageFile.toURI().toString());
        Button addAnimal = new Button("Add");
        Button backButton = new Button("Back");
        Button addImageButton = new Button("Add Image");
        addImageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                JFrame frame = new JFrame();
                JFileChooser fileChooser = new JFileChooser();
                int returnVal = fileChooser.showOpenDialog(frame);
                if(returnVal == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    imagePath = file.getPath();
                }
            }
        });
        addAnimal.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                validateInputs(animalTypeField,animalFeedingTimeField,animalCurrentStatusLabelField);
                if(flag) {
                    String type = animalTypeField.getText();
                    int feedingTime = Integer.parseInt(animalFeedingTimeField.getText());
                    int currentStatus = Integer.parseInt(animalFeedingTimeField.getText());
                    Animal addedAnimal = new Animal(type,feedingTime,currentStatus,false);
                    addedAnimal.setImagePath(imagePath);
                    currentAnimals.add(addedAnimal);
                    initTimer(addedAnimal);
                    animalTypeField.setText("");
                    animalCurrentStatusLabelField.setText("");
                    animalFeedingTimeField.setText("");
                }
            }
        });
        backButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                currentAnimalList.getItems().remove(0,currentAnimalList.getItems().size());
                populateListView(currentAnimalList, currentAnimals);
                DB.setCurrentAnimals(currentAnimals);
                screen.setScene(currentLayout);
                screen.show();
            }
        });
        HBox animalType = new HBox(20);
        HBox animalFeedingTime = new HBox(15);
        HBox animalCurrentStatus = new HBox(15);
        animalCurrentStatus.getChildren().addAll(animalCurrentStatusLabel,animalCurrentStatusLabelField,errorImage);
        animalType.getChildren().addAll(animalTypeLabel,animalTypeField,errorImage1);
        animalFeedingTime.getChildren().addAll(animalFeedingTimeLabel,animalFeedingTimeField,errorImage2);
        HBox buttonBox = new HBox(20);
        buttonBox.getChildren().addAll(addAnimal,addImageButton,backButton);
        VBox addAnimalLayout = new VBox();
        addAnimalLayout.getChildren().addAll(animalType,animalFeedingTime,animalCurrentStatus,buttonBox);
        Scene animalAddScene = new Scene(addAnimalLayout,700,700);
        return animalAddScene;
    }

    public static void initTimer(Animal animal){
        animal.getFoodTimer().schedule(new TimerTask() {
            @Override
            public void run() {
                joptionPane(animal.getAnimalType() + " is Hungry!!!!");
                animal.setHungerStatus(true);
                Random random = new Random();
                int status = random.nextInt(80)+1;
                while(true) {
                    if (animal.getCurrentStatus() - status >= 0) {
                        animal.setCurrentStatus(animal.getCurrentStatus() - status);
                        break;
                    }
                    else{
                        continue;
                    }
                }
                animal.setCurrentStatus(0);
                DB.setCurrentAnimals(currentAnimals);
            }
        },animal.getFeedingTime() * 1000);
    }

    public static void joptionPane(String message){
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,message);
    }

    public void validateInputs(TextField animalTypeField,TextField animalFeedingTimeField,TextField animalCurrentStatusField){
        if (animalTypeField.getText().length() < 1 || animalCurrentStatusField.getText().length() < 1 || animalFeedingTimeField.getText().length() < 1) {
            if(animalTypeField.getText().length() < 1) {
                errorImage.setImage(errorImg);
            }
            if(animalFeedingTimeField.getText().length() < 1) {
                errorImage1.setImage(errorImg);
            }
            if(animalCurrentStatusField.getText().length() < 1) {
                errorImage2.setImage(errorImg);
            }
            flag = false;
            return;
        }
        else {
            String type = animalTypeField.getText();
            int currentStatus = Integer.parseInt(animalCurrentStatusField.getText());
            int feedingTime = Integer.parseInt(animalFeedingTimeField.getText());
            if (isInt(currentStatus) && isInt(feedingTime)) {
                flag = true;
            }
            else {
                System.out.println(1);
                if(!isInt(currentStatus)) {
                    errorImage1.setImage(errorImg);
                }
                if(!isInt(feedingTime)) {
                    errorImage2.setImage(errorImg);
                }
                flag = false;
                return;
            }
        }
    }

    public void populateListView(ListView<String> currentAnimalList,ArrayList<Animal> currentAnimals){
        if(currentAnimalList != null) {
            for (Animal animal : currentAnimals) {
                currentAnimalList.getItems().add(animal.getAnimalType());
            }
        }
        return;
    }

    public boolean isInt(int value){
        if(value == (int) value){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
