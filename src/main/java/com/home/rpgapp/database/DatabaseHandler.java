package com.home.rpgapp.database;

import com.home.rpgapp.model.Character;
import com.home.rpgapp.model.Item;
import com.home.rpgapp.model.Transaction;
import com.home.rpgapp.model.TransactionType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private Connection connection;

    // Establishing the connection to the MySQL database
    public Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/rpg_maintenance_system";
            String user = "root";
            String password = "";

            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to MySQL");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }


    // Method to insert a new character into the database
    public void insertCharacter(String name, String characterClass, int level, int hp, int xp) {
        Connection connection = getConnection();
        String query = "INSERT INTO characters (name, char_class, level, hp, xp) VALUES (?, ?, ?, ?, ?)";
        boolean isInserted = false;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, characterClass);
            stmt.setInt(3, level);
            stmt.setInt(4, hp);
            stmt.setInt(5, xp);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Character added successfully.");
                isInserted = true;
            } else {
                System.out.println("Add new character failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Ensure the connection is closed
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to retrieve the list of characters from the database
    public ObservableList<Character> loadCharacterList() {
        ObservableList<Character> characterList = FXCollections.observableArrayList();
        Connection connection = getConnection();

        String query = "SELECT * FROM characters";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("character_id");
                String name = resultSet.getString("name");
                String charClass = resultSet.getString("char_class");
                int level = resultSet.getInt("level");
                int hp = resultSet.getInt("hp");
                int xp = resultSet.getInt("xp");

                // Create a new Character object
                Character character = new Character(id, name, charClass, level, hp, xp, null);

                // Add the character to the list
                characterList.add(character);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return characterList;
    }

    public Character getCharacterByName(String name) {
        Character character = null;
        Connection connection = getConnection();

        String query = "SELECT * FROM characters WHERE name = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("character_id");
                String charClass = resultSet.getString("char_class");
                int level = resultSet.getInt("level");
                int hp = resultSet.getInt("hp");
                int xp = resultSet.getInt("xp");

                // Create a new Character object
                character = new Character(id, name, charClass, level, hp, xp, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return character;
    }

    public List<Item> getCharacterInventory(int characterId) {
        List<Item> inventory = new ArrayList<>();
        String query = "SELECT i.item_id, i.name, i.type, i.effect, i.price, i.class_relevance, ci.quantity "
                + "FROM character_items ci "
                + "JOIN items i ON ci.item_id = i.item_id "
                + "WHERE ci.character_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, characterId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Item item = new Item();
                item.setItemId(rs.getInt("item_id"));
                item.setName(rs.getString("name"));
                item.setType(rs.getString("type"));
                item.setEffect(rs.getString("effect"));
                item.setPrice(rs.getInt("price"));
                item.setClassRelevance(rs.getString("class_relevance"));
                item.setQuantity(rs.getInt("quantity"));

                inventory.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventory;
    }


    // Method to update a character in the database
    public boolean updateCharacter(Character character) {
        String query = "UPDATE characters SET name = ?, char_class = ?, level = ?, hp = ?, xp = ? WHERE character_id = ?";
        Connection connection = getConnection();
        boolean isUpdated = false;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, character.getName());
            stmt.setString(2, character.getCharClass());
            stmt.setInt(3, character.getLevel());
            stmt.setInt(4, character.getHp());
            stmt.setInt(5, character.getXp());
            stmt.setInt(6, character.getCharacterId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Character updated successfully.");
                isUpdated = true;
            } else {
                System.out.println("Character update failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return isUpdated;
    }

    // Method to delete a character from the database
    public void deleteCharacter(Character character) {
        String query = "DELETE FROM characters WHERE character_id = ?";
        Connection connection = getConnection();
        boolean isDeleted = false;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, character.getCharacterId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Character deleted successfully.");
                isDeleted = true;
            } else {
                System.out.println("Character deletion failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // ========================== ITEM METHODS ==========================

    // Method to insert a new item into the database
    public void insertItem(String name, String type, int quantity, String effect, int price) {
        Connection connection = getConnection();
        String query = "INSERT INTO items (name, type, quantity, effect, price) VALUES (?, ?, ?, ?, ?)";
        boolean isInserted = false;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, type);
            stmt.setInt(3, quantity);
            stmt.setString(4, effect);
            stmt.setInt(5, price);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Item added successfully.");
                isInserted = true;
            } else {
                System.out.println("Add new item failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to load item list from the database (returns the list of items instead of updating the view)
    public List<Item> loadItemList() {
        List<Item> itemList = new ArrayList<>();
        Connection connection = getConnection();

        String query = "SELECT * FROM items";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("item_id");
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");
                int quantity = resultSet.getInt("quantity");
                String effect = resultSet.getString("effect");
                int price = resultSet.getInt("price");

                // Create a new Item object
                Item item = new Item(id, name, type, quantity, effect, price);

                // Add the item to the list
                itemList.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return itemList; // Return the list of items
    }

    // Method to update an item in the database
    public void updateItem(Item item) {
        String query = "UPDATE items SET name = ?, type = ?, quantity = ?, effect = ?, price = ? WHERE item_id = ?";
        Connection connection = getConnection();
        boolean isUpdated = false;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getType());
            stmt.setInt(3, item.getQuantity());
            stmt.setString(4, item.getEffect());
            stmt.setInt(5, item.getPrice());
            stmt.setInt(6, item.getItemId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Item updated successfully.");
                isUpdated = true;
            } else {
                System.out.println("Item update failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to delete an item from the database
    public void deleteItem(Item item) {
        String query = "DELETE FROM items WHERE item_id = ?";
        Connection connection = getConnection();
        boolean isDeleted = false;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, item.getItemId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Item deleted successfully.");
                isDeleted = true;
            } else {
                System.out.println("Item deletion failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    // Method to assign all item assigned to a specific character
    public boolean assignItemToCharacter(int characterId, int itemId) {
        String checkQuery = "SELECT COUNT(*) FROM character_items WHERE character_id = ? AND item_id = ?";
        String insertQuery = "INSERT INTO character_items (character_id, item_id) VALUES (?, ?)";
        Connection connection = getConnection();
        boolean isAssigned = false;

        try {
            // Check if the item is already assigned to the character
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, characterId);
                checkStmt.setInt(2, itemId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) {
                        System.out.println("Item is already assigned to the character.");
                        return false; // Item is already assigned
                    }
                }
            }

            // Proceed to assign the item
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, characterId);
                insertStmt.setInt(2, itemId);
                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Item assigned to character successfully.");
                    isAssigned = true;
                } else {
                    System.out.println("Item assignment failed.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return isAssigned;
    }

    // Method to get unassigned item assigned to a specific character (items that's not on a specific character's inventory, yet.)
    public List<Item> getUnassignedItemsForCharacter(int characterId) {
        List<Item> unassignedItems = new ArrayList<>();
        String query = "SELECT * FROM items WHERE item_id NOT IN (SELECT item_id FROM character_items WHERE character_id = ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, characterId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Item item = new Item();
                item.setItemId(rs.getInt("item_id"));
                item.setName(rs.getString("name"));
                item.setType(rs.getString("type"));
                item.setQuantity(rs.getInt("quantity"));
                item.setEffect(rs.getString("effect"));
                unassignedItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return unassignedItems;
    }

    // Method to retrieve all items assigned to a specific character
    public List<Item> getCharacterItems(int characterId) {
        String query = "SELECT i.item_id, i.name AS item_name, i.type, ci.quantity, i.price, c.name AS character_name " +
                "FROM items i " +
                "JOIN character_items ci ON i.item_id = ci.item_id " +
                "JOIN characters c ON ci.character_id = c.character_id " +
                "WHERE ci.character_id = ?";
        List<Item> characterItems = new ArrayList<>();

        Connection connection = getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, characterId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("item_id");
                String itemName = resultSet.getString("item_name");
                String type = resultSet.getString("type");
                int quantity = resultSet.getInt("quantity");
                int price = resultSet.getInt("price");
                String characterName = resultSet.getString("character_name");

                // Add character name to the item
                Item item = new Item(id, itemName, type, quantity, null, price);
                item.setCharacterName(characterName);
                characterItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return characterItems;
    }

    public int getCharacterItemQuantity(int characterId, int itemId) {
        String query = "SELECT quantity FROM character_items WHERE character_id = ? AND item_id = ?";
        Connection connection = getConnection();

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, characterId);
            ps.setInt(2, itemId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantity");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getAvailableItemQuantity(int itemId) {
        String query = "SELECT quantity FROM items WHERE item_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void assignOrIncrementItem(int characterId, int itemId, int quantity) {
        Connection connection = getConnection();

        String query = "INSERT INTO character_items (character_id, item_id, quantity) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, characterId);
            ps.setInt(2, itemId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateCharacterItemQuantity(int characterId, int itemId, int newQuantity) {
        if (newQuantity <= 0) {
            // Remove the item from inventory if quantity is zero
            String query = "DELETE FROM character_items WHERE character_id = ? AND item_id = ?";
            Connection connection = getConnection();

            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, characterId);
                ps.setInt(2, itemId);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Update the quantity
            String updateSql = "UPDATE character_items SET quantity = ? WHERE character_id = ? AND item_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(updateSql)) {
                ps.setInt(1, newQuantity);
                ps.setInt(2, characterId);
                ps.setInt(3, itemId);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deductItemQuantity(int itemId, int quantity) {
        String query = "UPDATE items SET quantity = quantity - ? WHERE item_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, itemId);
            stmt.executeUpdate();
            System.out.println("Item quantity updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get total gold of a specific character
    public int getCharacterGold(int characterId) {
        String query = "SELECT gold FROM characters WHERE character_id = ?";
        int gold = 0;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, characterId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                gold = rs.getInt("gold");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return gold;
    }

    // Update gold for a character
    public void updateCharacterGold(int characterId, int gold) {
        String query = "UPDATE characters SET gold = ? WHERE character_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, gold);
            stmt.setInt(2, characterId);
            stmt.executeUpdate();
            System.out.println("Gold updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ========================== TRANSACTION METHODS ==========================

    // Record a transaction
    public void recordTransaction(int characterId, int itemId, String type, int quantity, int totalCost) {
        String query = "INSERT INTO transactions (character_id, item_id, transaction_type, quantity, total_cost) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, characterId);
            stmt.setInt(2, itemId);
            stmt.setString(3, type);
            stmt.setInt(4, quantity);
            stmt.setInt(5, totalCost);
            stmt.executeUpdate();
            System.out.println("Transaction recorded successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> loadTransactionList() {
        List<Transaction> transactionList = new ArrayList<>();
        Connection connection = getConnection();

        String query =
                "SELECT t.transaction_id, t.character_id, c.name AS char_name, " +
                        "       t.item_id, i.name AS item_name, " +
                        "       t.quantity, t.total_cost, t.transaction_date, " +
                        "       t.transaction_type " +
                        "FROM transactions t " +
                        "JOIN characters c ON t.character_id = c.character_id " +
                        "JOIN items i ON t.item_id = i.item_id";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int transactionId     = resultSet.getInt("transaction_id");
                int characterId       = resultSet.getInt("character_id");
                String characterName  = resultSet.getString("char_name");
                int itemId            = resultSet.getInt("item_id");
                String itemName       = resultSet.getString("item_name");
                int quantity          = resultSet.getInt("quantity");
                int totalCost         = resultSet.getInt("total_cost");
                String transactionDate= resultSet.getString("transaction_date");
                String transTypeStr   = resultSet.getString("transaction_type");

                // Convert to enum
                TransactionType transType = TransactionType.valueOf(transTypeStr);

                Transaction transaction = new Transaction(
                        transactionId,
                        characterId,
                        characterName,
                        itemId,
                        itemName,
                        quantity,
                        totalCost,
                        transactionDate,
                        transType
                );
                transactionList.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return transactionList;
    }

}