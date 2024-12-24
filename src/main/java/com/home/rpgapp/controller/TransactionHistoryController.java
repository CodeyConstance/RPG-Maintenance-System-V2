package com.home.rpgapp.controller;

import com.home.rpgapp.RPGMaintenanceSystemMain;
import com.home.rpgapp.database.DatabaseHandler;
import com.home.rpgapp.model.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TransactionHistoryController {

    @FXML
    private TableView<Transaction> tblTransactions;

    @FXML
    private TableColumn<Transaction, Integer> colTransactionID;

    @FXML
    private TableColumn<Transaction, Integer> colCharacterID;

    @FXML
    private TableColumn<Transaction, String> colCharacterName;

    @FXML
    private TableColumn<Transaction, String> colItemName;

    @FXML
    private TableColumn<Transaction, Integer> colQuantity;

    @FXML
    private TableColumn<Transaction, Integer> colTotalCost;

    @FXML
    private TableColumn<Transaction, Integer> colTransactionType;

    @FXML
    private TableColumn<Transaction, String> colTransactionDate;

    private DatabaseHandler dbHandler;
    private final ObservableList<Transaction> transactionList = FXCollections.observableArrayList();
    private RPGMaintenanceSystemMain mainApp;
    public void setMainApp(RPGMaintenanceSystemMain mainApp) {
        this.mainApp = mainApp;
    }


    public void initialize() {
        dbHandler = new DatabaseHandler();

        // Map each column to the corresponding field in the Transaction model
        colTransactionID.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
        colCharacterID.setCellValueFactory(new PropertyValueFactory<>("characterID"));
        colCharacterName.setCellValueFactory(new PropertyValueFactory<>("characterName"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colTotalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colTransactionType.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        colTransactionDate.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

        loadTransactionData();
    }

    private void loadTransactionData() {
        transactionList.clear();
        transactionList.addAll(dbHandler.loadTransactionList());
        tblTransactions.setItems(transactionList);
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
