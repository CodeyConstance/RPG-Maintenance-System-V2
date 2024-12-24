package com.home.rpgapp.controller;

import com.home.rpgapp.RPGMaintenanceSystemMain;
import com.home.rpgapp.database.DatabaseHandler;
import com.home.rpgapp.model.Character;
import com.home.rpgapp.model.Item;
import com.home.rpgapp.controller.SessionState;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class CharacterController {

    // Buttons and forms
    @FXML
    private GridPane characterButtons; // The main menu buttons
    @FXML
    private Button submitButton; // Button used for submitting new or edited characters
    @FXML
    private Button backButton;  // Single back button used for all views

    @FXML
    private ListView<Character> characterListView; // ListView to display characters
    private ObservableList<Character> characterList;
    private boolean isEditMode = false; // Track whether we're editing or adding a new character
    private Character selectedCharacter; // The currently selected character to be edited
    @FXML
    private VBox addEditCharacterForm; // The form for adding/editing a character
    @FXML
    private TableView<Item> itemSelectionTableView; // TableView for displaying items
    @FXML
    private TableColumn<Item, String> itemNameColumn;
    @FXML
    private TableColumn<Item, Integer> itemQuantityColumn;
    @FXML
    private TableView<Item> characterInventoryTableView; // TableView for character inventory
    @FXML
    private TableColumn<Character, String> characterNameColumn;
    @FXML
    private TableColumn<Item, String> characterItemNameColumn;
    @FXML
    private TableColumn<Item, Integer> characterItemQuantityColumn;
    // Text fields for adding new character
    @FXML
    private TextField characterNameField;
    @FXML
    private TextField characterClassField;
    @FXML
    private TextField characterLevelField;
    @FXML
    private TextField characterHpField;
    @FXML
    private TextField characterXpField;

    // States for toggling visibility
    private boolean isListViewVisible = false;
    private boolean isAddFormVisible = false;
    private ObservableList<Item> itemList;
    private ObservableList<Item> characterInventoryList;
    private DatabaseHandler dbHandler;
    private RPGMaintenanceSystemMain mainApp;
    public void setMainApp(RPGMaintenanceSystemMain mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void initialize() {
        dbHandler = new DatabaseHandler();
        dbHandler.getConnection();
        characterListView.setVisible(false);
        characterListView.setManaged(false); // Ensure it's not shown by default
        characterButtons.setVisible(true);
        characterButtons.setManaged(true); // Ensure buttons are shown by default
        backButton.setVisible(false);
        backButton.setManaged(true);

        // Configure the table for viewing character inventory
        characterNameColumn.setCellValueFactory(new PropertyValueFactory<>("characterName"));
        characterItemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        characterItemQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        characterList = FXCollections.observableArrayList();
        characterInventoryList = FXCollections.observableArrayList();
        characterListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); // Users could assign single item only during selection

        // Add listener for character selection
        characterListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Store selection in SessionState
                SessionState.setSelectedCharacter(newValue);
                System.out.println("Selected character: " + newValue.getName());

            } else {
                    System.out.println("Invalid selection or empty list.");
                }

        });
    }

    @FXML
    public void toggleViewCharacterList(ActionEvent event) {
        isListViewVisible = !isListViewVisible;

        if (isListViewVisible) {
            // Show ListView, hide main buttons, show Back button in a new position
            characterButtons.setVisible(false);
            characterButtons.setManaged(false);
            characterListView.setVisible(true);
            characterListView.setManaged(true);

            backButton.setLayoutX(500);
            backButton.setLayoutY(440);
            backButton.setVisible(true);
            backButton.setManaged(true);

            loadCharactersIntoListView();

            enableCharacterSelection();
        } else {
            characterListView.setVisible(false);
            characterListView.setManaged(false);
            characterButtons.setVisible(true);
            characterButtons.setManaged(true);
            backButton.setVisible(false);
            backButton.setManaged(false);
        }
    }

    // Method to load character list
    public ObservableList<Character> loadCharactersIntoListView() {
        // Clear the current ListView and hidden item list
        characterListView.getItems().clear();
        characterList.clear();

        // Fetch the characters from the database
        List<Character> charactersFromDB = dbHandler.loadCharacterList();

        System.out.println("Total characters: " + charactersFromDB.size());

        // Store the characters in the hidden item list (assuming characterList is ObservableList<Character>)
        characterList.addAll(charactersFromDB);

        // Set the ListView items to be the list of characters
        characterListView.setItems(FXCollections.observableArrayList(charactersFromDB));

        System.out.println("Updated total characters in ListView: " + characterListView.getItems().size());

        return characterList;
    }


    @FXML
    public void toggleAddNewCharacter(ActionEvent event) {
        isEditMode = false;

        isAddFormVisible = !isAddFormVisible;

        if (isAddFormVisible) {
            // Hide main buttons, show form, move Back button to new position
            characterButtons.setVisible(false);
            characterButtons.setManaged(false);
            addEditCharacterForm.setVisible(true);
            addEditCharacterForm.setManaged(true);

            // Reset form fields to empty values
            clearFormFields();

            // Update the submit button text to set title to "Add Character"
            submitButton.setText("Add Character");

            // Move and show the back button for the form
            backButton.setLayoutX(500);
            backButton.setLayoutY(440);
            backButton.setVisible(true);
            backButton.setManaged(true);
        } else {
            // If returning to the main menu, hide the form and back button
            addEditCharacterForm.setVisible(false);
            addEditCharacterForm.setManaged(false);
            characterButtons.setVisible(true);
            characterButtons.setManaged(true);
            backButton.setVisible(false);
            backButton.setManaged(false);
        }
    }


    @FXML
    public void toggleAddEditCharacter(ActionEvent event) {
        isAddFormVisible = !isAddFormVisible;
        Character currentCharacter = SessionState.getSelectedCharacter();


        if (isAddFormVisible) {
            // Hide main buttons, show form, move Back button to new position
            characterButtons.setVisible(false);
            characterButtons.setManaged(false);
            addEditCharacterForm.setVisible(true);
            addEditCharacterForm.setManaged(true);

            // Position the back button for the edit form
            backButton.setLayoutX(500);
            backButton.setLayoutY(440);
            backButton.setVisible(true);
            backButton.setManaged(true);

            if (isEditMode && currentCharacter != null) {
                // Editing an existing character, update form and button text
                submitButton.setText("Update Character");
                populateFormFields(currentCharacter);
            } else {
                // Adding a new character
                submitButton.setText("Add Character");
                clearFormFields();
            }
        } else {
            // If returning to the main menu, hide the form and back button
            addEditCharacterForm.setVisible(false);
            addEditCharacterForm.setManaged(false);
            characterButtons.setVisible(true);
            characterButtons.setManaged(true);
            backButton.setVisible(false);
            backButton.setManaged(false);
        }
    }

    // Method to switch to edit mode and pre-fill character data
    @FXML
    public void toggleEditCharacter(ActionEvent event) {
        Character currentCharacter = SessionState.getSelectedCharacter();

        if (currentCharacter != null) {
            isEditMode = true;
            toggleAddEditCharacter(event);
        } else {
            showAlert("Please select a character to edit.");
        }
    }

    // Method to submit a new or edited character
    @FXML
    public void submitNewOrEditedCharacter(ActionEvent event) {
        Character currentCharacter = SessionState.getSelectedCharacter();
        String name = characterNameField.getText();
        String charClass = characterClassField.getText();
        int level = Integer.parseInt(characterLevelField.getText());
        int hp = Integer.parseInt(characterHpField.getText());
        int xp = Integer.parseInt(characterXpField.getText());

        if (isEditMode && currentCharacter != null) {
            // Update character in the database
            currentCharacter.setName(name);
            currentCharacter.setCharClass(charClass);
            currentCharacter.setLevel(level);
            currentCharacter.setHp(hp);
            currentCharacter.setXp(xp);
            dbHandler.updateCharacter(currentCharacter);
        } else {
            // Insert new character into the database
            dbHandler.insertCharacter(name, charClass, level, hp, xp);
        }

        clearFormFields();
        toggleAddEditCharacter(event); // Return to the main menu
    }

    @FXML
    public void deleteCharacter(ActionEvent event) {
        Character currentCharacter = SessionState.getSelectedCharacter();

        if (currentCharacter != null) {
            // Create confirmation alert
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Confirmation");
            confirmationAlert.setHeaderText("Are you sure you want to delete the character: " + currentCharacter.getName() + "?");
            confirmationAlert.setContentText("This action cannot be undone.");

            // Show confirmation and wait for user input
            ButtonType result = confirmationAlert.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                // User chose OK, proceed with deletion
                dbHandler.deleteCharacter(currentCharacter);
                loadCharactersIntoListView(); // Refresh the character list
            }
        } else {
            // Show alert if no character is selected
            showAlert("Please select a character to delete.");
        }
    }

    // Re-enable item selection after items are loaded
    private void enableCharacterSelection() {
        characterListView.setDisable(false);
    }

    // Method to load items from the database into the TableView
    public void loadItemsIntoTableView() {
        Character currentCharacter = SessionState.getSelectedCharacter();

        if (currentCharacter == null) {
            showAlert("Please select a character first.");
            return;
        }
        List<Item> unassignedItems = dbHandler.getUnassignedItemsForCharacter(currentCharacter.getCharacterId());
        itemList = FXCollections.observableArrayList(unassignedItems);
        itemSelectionTableView.setItems(itemList);
    }

    // Toggle visibility for viewing a character's inventory
    @FXML
    public void toggleViewCharacterInventory(ActionEvent event) {
        isListViewVisible = !isListViewVisible;

        if (isListViewVisible) {
            // Hide main buttons and display the inventory list
            characterButtons.setVisible(false);
            characterButtons.setManaged(false);
            characterInventoryTableView.setVisible(true);
            characterInventoryTableView.setManaged(true);
            backButton.setVisible(true);
            backButton.setManaged(true);

            loadCharacterInventory(); // Load the character's inventory
        } else {
            characterInventoryTableView.setVisible(false);
            characterInventoryTableView.setManaged(false);
            characterButtons.setVisible(true);
            characterButtons.setManaged(true);
            backButton.setVisible(false);
            backButton.setManaged(false);
        }
    }

    // Load the inventory for the selected character
    public void loadCharacterInventory() {
        Character currentCharacter = SessionState.getSelectedCharacter();
        if (currentCharacter != null) {
            characterInventoryTableView.getItems().clear();
            List<Item> characterItems = dbHandler.getCharacterItems(currentCharacter.getCharacterId());

            characterInventoryList.setAll(characterItems);
            characterInventoryTableView.setItems(characterInventoryList);
        } else {
            showAlert("Please select a character.");
        }
    }


    // Helper method to populate form fields with character data
    private void populateFormFields(Character character) {
        characterNameField.setText(character.getName());
        characterClassField.setText(character.getCharClass());
        characterLevelField.setText(Integer.toString(character.getLevel()));
        characterHpField.setText(Integer.toString(character.getHp()));
        characterXpField.setText(Integer.toString(character.getXp()));
    }

    // Helper method to clear form fields
    private void clearFormFields() {
        characterNameField.clear();
        characterClassField.clear();
        characterLevelField.clear();
        characterHpField.clear();
        characterXpField.clear();
    }

    @FXML
    public void handleBackToMenu(ActionEvent event) {
        // Hide character list if visible
        if (isListViewVisible) {
            characterListView.setVisible(false);
            characterListView.setManaged(false);
            characterListView.getItems().clear();
            isListViewVisible = false;
        }

        // Hide add/edit character form if visible
        if (isAddFormVisible) {
            addEditCharacterForm.setVisible(false);
            addEditCharacterForm.setManaged(false);
            isAddFormVisible = false;
        }

        // Hide character inventory table if visible
        if (characterInventoryTableView.isVisible()) {
            characterInventoryTableView.setVisible(false);
            characterInventoryTableView.setManaged(false);
            characterInventoryTableView.getItems().clear();
        }

        // Show the main menu buttons
        characterButtons.setVisible(true);
        characterButtons.setManaged(true);

        // Hide the back button
        backButton.setVisible(false);
        backButton.setManaged(false);
    }

    @FXML
    public void handleBackToMainMenu(ActionEvent event) {
        if (mainApp != null) {
            mainApp.goBackToMainMenu();
        } else {
            System.out.println("MainApp is not set");
        }
    }

    // Method to display alerts
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No Character Selected");
        alert.setContentText(message);
        alert.showAndWait();
    }
}