package furnituremanagementsystem2;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class CustomerScreen {
    private JFrame frame;
    private JTable inventoryTable;
    private InventoryManager inventoryManager;

    public CustomerScreen() {
        inventoryManager = new InventoryManager();
        inventoryManager.loadInventory();
    }

    public void createAndShowCustomerScreen() {
        frame = new JFrame("Customer - Furniture Inventory");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Inventory Table Panel
        inventoryTable = new JTable();
        JScrollPane tableScroll = new JScrollPane(inventoryTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Furniture Inventory"));

        frame.add(tableScroll, BorderLayout.CENTER);

        updateInventoryTable();
        frame.setVisible(true);
    }

    private void updateInventoryTable() {
        List<FurnitureItem> inventory = inventoryManager.getInventory();
        String[][] data = new String[inventory.size()][4];

        for (int i = 0; i < inventory.size(); i++) {
            FurnitureItem item = inventory.get(i);
            data[i][0] = String.valueOf(item.getId());
            data[i][1] = item.getName();
            data[i][2] = String.valueOf(item.getPrice());
            data[i][3] = String.valueOf(item.getQuantity());
        }

        String[] columnNames = {"ID", "Name", "Price", "Quantity"};
        inventoryTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }
}
