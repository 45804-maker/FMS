package furnituremanagementsystem2;

import java.util.ArrayList;
import java.util.List;

class InventoryManager {
    private List<FurnitureItem> inventory = new ArrayList<>();
    private final FileManager fileManager = new FileManager();

    public void addFurniture(FurnitureItem item) {
        inventory.add(item);
        fileManager.saveInventory(inventory);
    }

    public void removeFurniture(int id) {
        inventory.removeIf(item -> item.getId() == id);
        fileManager.saveInventory(inventory);
    }

    public void updateFurniture(int id, String newName, double newPrice, int newQuantity) {
        for (FurnitureItem item : inventory) {
            if (item.getId() == id) {
                item.setName(newName);
                item.setPrice(newPrice);
                item.setQuantity(newQuantity);
                fileManager.saveInventory(inventory);
                break;
            }
        }
    }

    public List<FurnitureItem> getInventory() {
        return inventory;
    }

    public void loadInventory() {
        this.inventory = fileManager.loadInventory();
    }

    public void clearInventory() {
        inventory.clear();
        fileManager.clearInventory();
    }
}
