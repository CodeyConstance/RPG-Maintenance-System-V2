package com.home.rpgapp.model;

public class Item {
    private int itemId;
    private String name;
    private String type;
    private int quantity;
    private String effect;
    private int price;
    private String classRelevance;
    private String characterName;

    public Item() {}

    public Item(int itemId, String name, String type, int quantity, String effect, int price) {
        this.itemId = itemId;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.effect = effect;
        this.price = price;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getClassRelevance() {
        return classRelevance;
    }

    public void setClassRelevance(String classRelevance) {
        this.classRelevance = classRelevance;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }
}
