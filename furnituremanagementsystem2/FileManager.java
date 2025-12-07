package furnituremanagementsystem2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

class FileManager {
    // Path to the file where inventory will be saved
    private final String FILE_PATH = "inventory.dat";

    // Method to save the inventory to the file
    public void saveInventory(List<FurnitureItem> inventory) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(inventory);  // Serialize the inventory list
        } catch (IOException e) {
            // Handle the error (display a message to the user)
            JOptionPane.showMessageDialog(null, "Error saving inventory: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to load the inventory from the file
    public List<FurnitureItem> loadInventory() {
        List<FurnitureItem> inventory = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            inventory = (List<FurnitureItem>) ois.readObject();  // Deserialize the list
        } catch (FileNotFoundException e) {
            // File not found - Return empty list as no data is available
            // JOptionPane.showMessageDialog(null, "No inventory file found. Starting with empty inventory.", "Notice", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | ClassNotFoundException e) {
            // Handle general IO or class not found errors
            JOptionPane.showMessageDialog(null, "Error loading inventory: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return inventory;
    }

    // Method to clear the inventory from the file (empty the file)
    public void clearInventory() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(new ArrayList<FurnitureItem>());  // Clear the file by writing an empty list
        } catch (IOException e) {
            // Handle any I/O errors while clearing the inventory
            JOptionPane.showMessageDialog(null, "Error clearing inventory: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
