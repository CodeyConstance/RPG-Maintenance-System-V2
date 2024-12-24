package com.home.rpgapp.model;

public class Transaction {
    private int transactionID;
    private int characterID;
    private String characterName;
    private String itemName;
    private int itemID;
    private int quantity;
    private int totalCost;
    private String transactionDate;
    private TransactionType transactionType;

    //constructors to insert transactions
    public Transaction(int transactionID, int characterID, int itemID, int quantity, int totalCost, String transactionDate) {
        this.transactionID = transactionID;
        this.characterID = characterID;
        this.itemID = itemID;
        this.quantity = quantity;
        this.totalCost = totalCost;
        this.transactionDate = transactionDate;
    }

    //constructors to list all transactions
    public Transaction(int transactionID, int characterID, String characterName,
                       int itemID, String itemName,
                       int quantity, int totalCost, String transactionDate, TransactionType transactionType) {
        this.transactionID = transactionID;
        this.characterID = characterID;
        this.characterName = characterName;
        this.itemID = itemID;
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalCost = totalCost;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public int getCharacterID() {
        return characterID;
    }

    public void setCharacterID(int characterID) {
        this.characterID = characterID;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}