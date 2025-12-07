package furnituremanagementsystem2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class StoreManagerScreen {
    private JFrame frame;
    private JTextField idField, nameField, priceField, quantityField;
    private JButton addButton, removeButton, updateButton, clearButton, viewButton;
    private JTable inventoryTable;
    private InventoryManager inventoryManager;

    public StoreManagerScreen() {
        inventoryManager = new InventoryManager();
        inventoryManager.loadInventory();
    }

    public void createAndShowManagerScreen() {
        frame = new JFrame("Store Manager - Furniture Management");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Input Fields Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Furniture Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        inputPanel.add(new JLabel("ID:"), gbc);
        idField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(idField, gbc);

        inputPanel.add(new JLabel("Name:"), gbc);
        nameField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(nameField, gbc);

        inputPanel.add(new JLabel("Price:"), gbc);
        priceField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(priceField, gbc);

        inputPanel.add(new JLabel("Quantity:"), gbc);
        quantityField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(quantityField, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5, 10, 10));

        addButton = new JButton("Add");
        buttonPanel.add(addButton);

        removeButton = new JButton("Remove");
        buttonPanel.add(removeButton);

        updateButton = new JButton("Update");
        buttonPanel.add(updateButton);

        clearButton = new JButton("Clear");
        buttonPanel.add(clearButton);

        viewButton = new JButton("View Inventory");
        buttonPanel.add(viewButton);

        // Inventory Table Panel
        inventoryTable = new JTable();
        JScrollPane tableScroll = new JScrollPane(inventoryTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Furniture Inventory"));

        // Add Components to Frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(tableScroll, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(e -> addFurniture());
        removeButton.addActionListener(e -> removeFurniture());
        updateButton.addActionListener(e -> updateFurniture());
        clearButton.addActionListener(e -> clearInventory());
        viewButton.addActionListener(e -> updateInventoryTable());

        frame.setVisible(true);
    }

    private void addFurniture() {
        if (validateInputs()) {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());

            FurnitureItem item = new FurnitureItem(id, name, price, quantity);
            inventoryManager.addFurniture(item);
            updateInventoryTable();
        }
    }

    private void removeFurniture() {
        try {
            int id = Integer.parseInt(idField.getText());
            inventoryManager.removeFurniture(id);
            updateInventoryTable();
        } catch (NumberFormatException ex) {
            showError("Please enter a valid ID.");
        }
    }

    private void updateFurniture() {
        try {
            int id = Integer.parseInt(idField.getText());
            String newName = nameField.getText();
            double newPrice = Double.parseDouble(priceField.getText());
            int newQuantity = Integer.parseInt(quantityField.getText());

            inventoryManager.updateFurniture(id, newName, newPrice, newQuantity);
            updateInventoryTable();
        } catch (NumberFormatException ex) {
            showError("Please enter valid values for ID, Name, Price, and Quantity.");
        }
    }

    private void clearInventory() {
        int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to clear all inventory?", "Clear Inventory", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            inventoryManager.clearInventory();
            updateInventoryTable();
        }
    }

    private boolean validateInputs() {
        try {
            Integer.parseInt(idField.getText());
            Double.parseDouble(priceField.getText());
            Integer.parseInt(quantityField.getText());
            return true;
        } catch (NumberFormatException ex) {
            showError("Invalid input! Please make sure all fields are filled correctly.");
            return false;
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
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
