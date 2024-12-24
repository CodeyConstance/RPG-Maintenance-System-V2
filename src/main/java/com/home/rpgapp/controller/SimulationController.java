package com.home.rpgapp.controller;

import com.home.rpgapp.RPGMaintenanceSystemMain;
import com.home.rpgapp.database.DatabaseHandler;
import com.home.rpgapp.model.Character;
import com.home.rpgapp.model.Item;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SimulationController {
    @FXML
    private ComboBox<Character> character1ComboBox;
    @FXML
    private ComboBox<Character> character2ComboBox;
    @FXML
    private TextArea simulationLog;
    @FXML
    private Label winnerLabel;
    @FXML
    private Label char1NameLabel;
    @FXML
    private Label char1ClassLabel;
    @FXML
    private Label char1LevelLabel;
    @FXML
    private Label char1HpLabel;
    @FXML
    private Label char1XpLabel;
    @FXML
    private Label char1GoldLabel;

    @FXML
    private Label char2NameLabel;
    @FXML
    private Label char2ClassLabel;
    @FXML
    private Label char2LevelLabel;
    @FXML
    private Label char2HpLabel;
    @FXML
    private Label char2XpLabel;
    @FXML
    private Label char2GoldLabel;

    private DatabaseHandler dbHandler;
    private Character character;
    private Random random = new Random();
    private Timer timer;
    private RPGMaintenanceSystemMain mainApp;
    public void setMainApp(RPGMaintenanceSystemMain mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void initialize() {
        dbHandler = new DatabaseHandler();
        // Populate ComboBoxes with characters from the database
        List<Character> characters = dbHandler.loadCharacterList();
        character1ComboBox.getItems().addAll(characters);
        character2ComboBox.getItems().addAll(characters);
    }

    @FXML
    public void displayChar1Stats() {
        Character character1 = character1ComboBox.getValue();
        if (character1 != null) {
            char1NameLabel.setText("Name: " + character1.getName());
            char1ClassLabel.setText("Class: " + character1.getCharClass());
            char1LevelLabel.setText("Level: " + character1.getLevel());
            char1HpLabel.setText("HP: " + character1.getHp());
            char1XpLabel.setText("XP: " + character1.getXp());
            char1GoldLabel.setText("Gold: " + character1.getGold());
        }
    }

    @FXML
    public void displayChar2Stats() {
        Character character2 = character2ComboBox.getValue();
        if (character2 != null) {
            char2NameLabel.setText("Name: " + character2.getName());
            char2ClassLabel.setText("Class: " + character2.getCharClass());
            char2LevelLabel.setText("Level: " + character2.getLevel());
            char2HpLabel.setText("HP: " + character2.getHp());
            char2XpLabel.setText("XP: " + character2.getXp());
            char2GoldLabel.setText("Gold: " + character2.getGold());
        }
    }


    @FXML
    public void startSimulation() {
        Character character1 = character1ComboBox.getValue();
        Character character2 = character2ComboBox.getValue();

        if (character1 == null || character2 == null) {
            showLog("Please select two characters to start the simulation.");
            return;
        }

        // Reset UI
        simulationLog.clear();
        winnerLabel.setText("");

        showLog("Starting battle between " + character1.getName() + " and " + character2.getName() + "!");
        startBattle(character1, character2);
    }

    private void startBattle(Character character1, Character character2) {
        final int[] char1Health = {character1.getHp()};
        final int[] char2Health = {character2.getHp()};

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int char1Attack = calculateAttack(character1);
                int char2Attack = calculateAttack(character2);

                char2Health[0] -= char1Attack;
                char1Health[0] -= char2Attack;

                Platform.runLater(() -> {
                    showLog(character1.getName() + " attacks " + character2.getName() + " for " + char1Attack + " damage.");
                    showLog(character2.getName() + " attacks " + character1.getName() + " for " + char2Attack + " damage.");

                    if (char1Health[0] <= 0 || char2Health[0] <= 0) {
                        determineWinner(character1, character2, char1Health[0], char2Health[0]);
                        timer.cancel();
                    }
                });
            }
        }, 0, 2000); // 3 seconds per turn (change the second parameter to adjust delay)
    }

    private int calculateAttack(Character character) {
        int baseAttack = character.getLevel() * 10; // Example base attack formula
        int randomFactor = random.nextInt(10); // Add randomness
        int itemBonus = calculateItemBonus(character);
        return baseAttack + randomFactor + itemBonus;
    }

    private int calculateItemBonus(Character character) {
        int bonus = 0;
        List<Item> inventory = dbHandler.getCharacterInventory(character.getCharacterId());
        for (Item item : inventory) {
            if (item.getClassRelevance().contains(character.getCharClass())) {
                bonus += parseEffectValue(item.getEffect());
            }
        }
        return bonus;
    }

    private int parseEffectValue(String effect) {
        // Example: "Increases attack by 15"
        String[] parts = effect.split(" ");
        for (String part : parts) {
            try {
                return Integer.parseInt(part);
            } catch (NumberFormatException ignored) {
            }
        }
        return 0;
    }

    private void determineWinner(Character character1, Character character2, int char1Health, int char2Health) {
        Platform.runLater(() -> {
            if (char1Health > char2Health) {
                winnerLabel.setText("Winner: " + character1.getName());
                showLog(character1.getName() + " wins the battle!");
            } else if (char2Health > char1Health) {
                winnerLabel.setText("Winner: " + character2.getName());
                showLog(character2.getName() + " wins the battle!");
            } else {
                winnerLabel.setText("It's a draw!");
                showLog("The battle ends in a draw!");
            }
        });
    }

    private void showLog(String message) {
        simulationLog.appendText(message + "\n");
    }

    @FXML
    public void handleBackToMainMenu(ActionEvent event) {
        if (mainApp != null) {
            mainApp.goBackToMainMenu();
        } else {
            System.out.println("MainApp is not set");
        }
    }
}
