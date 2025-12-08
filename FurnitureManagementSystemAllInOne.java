package furnituremanagementsystem2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class FurnitureManagementSystemAllInOne {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::new);
    }

    // ---------------------- MAIN GUI ----------------------
    static class MainGUI {
        JFrame frame;
        JTabbedPane tabs;

        InventoryManager inventoryManager = new InventoryManager();

        public MainGUI() {
            inventoryManager.loadInventory();

            frame = new JFrame("Furniture Management System");
            frame.setSize(1000, 700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            tabs = new JTabbedPane();
            tabs.addTab("Home", createHomePanel());
            tabs.addTab("Store Manager", new StoreManagerPanel(inventoryManager).getPanel());
            tabs.addTab("Customer", new CustomerPanel(inventoryManager).getPanel());

            frame.add(tabs);
            frame.setVisible(true);
        }

        private JPanel createHomePanel() {
            JPanel panel = new JPanel(new BorderLayout());

            JLabel title = new JLabel("Welcome to Furniture Management System", SwingConstants.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 26));

            JLabel subtitle = new JLabel("Select a tab above to continue", SwingConstants.CENTER);
            subtitle.setFont(new Font("Arial", Font.PLAIN, 16));

            JPanel container = new JPanel(new GridLayout(2, 1));
            container.add(title);
            container.add(subtitle);

            panel.add(container, BorderLayout.CENTER);
            return panel;
        }
    }

    // ------------------ FURNITURE ITEM ----------------
    static class FurnitureItem implements Serializable {
        private static final long serialVersionUID = 1L;

        private final int id;
        private String name;
        private double price;
        private int quantity;

        public FurnitureItem(int id, String name, double price, int quantity) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    // -------------------- FILE MANAGER ----------------
    static class FileManager {
        private final String FILE_PATH = "inventory.dat";

        public void saveInventory(java.util.List<FurnitureItem> inventory) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
                oos.writeObject(inventory);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving inventory: " + e.getMessage());
            }
        }

        public java.util.List<FurnitureItem> loadInventory() {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
                return (java.util.List<FurnitureItem>) ois.readObject();
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }

        public void clearInventory() {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
                oos.writeObject(new ArrayList<FurnitureItem>());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error clearing inventory: " + e.getMessage());
            }
        }
    }

    // ------------------ INVENTORY MANAGER -------------
    static class InventoryManager {
        private java.util.List<FurnitureItem> inventory = new ArrayList<>();
        private final FileManager fileManager = new FileManager();

        public void loadInventory() {
            inventory = fileManager.loadInventory();
        }

        public java.util.List<FurnitureItem> getInventory() { return inventory; }

        public void addFurniture(FurnitureItem item) {
            inventory.add(item);
            fileManager.saveInventory(inventory);
        }

        public void removeFurniture(int id) {
            inventory.removeIf(item -> item.getId() == id);
            fileManager.saveInventory(inventory);
        }

        public void updateFurniture(int id, String name, double price, int qty) {
            for (FurnitureItem item : inventory) {
                if (item.getId() == id) {
                    item.setName(name);
                    item.setPrice(price);
                    item.setQuantity(qty);
                }
            }
            fileManager.saveInventory(inventory);
        }

        public void clearInventory() {
            inventory.clear();
            fileManager.clearInventory();
        }
    }

    // ------------------ CUSTOMER PANEL ----------------
    static class CustomerPanel {
        private JPanel panel;
        private JTable table;
        private InventoryManager manager;

        public CustomerPanel(InventoryManager manager) {
            this.manager = manager;

            panel = new JPanel(new BorderLayout());

            JLabel title = new JLabel("Customer View - Inventory", SwingConstants.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 22));

            table = new JTable();
            JScrollPane scroll = new JScrollPane(table);

            panel.add(title, BorderLayout.NORTH);
            panel.add(scroll, BorderLayout.CENTER);

            updateTable();
        }

        public JPanel getPanel() { return panel; }

        private void updateTable() {
            java.util.List<FurnitureItem> items = manager.getInventory();
            String[][] data = new String[items.size()][4];

            for (int i = 0; i < items.size(); i++) {
                FurnitureItem f = items.get(i);
                data[i][0] = f.getId() + "";
                data[i][1] = f.getName();
                data[i][2] = f.getPrice() + "";
                data[i][3] = f.getQuantity() + "";
            }

            table.setModel(new DefaultTableModel(
                    data,
                    new String[]{"ID", "Name", "Price", "Quantity"}
            ));
        }
    }

    // ------------------ STORE MANAGER PANEL -----------
    static class StoreManagerPanel {
        private JPanel panel;
        private JTable table;
        private JTextField idField, nameField, priceField, qtyField;
        private final InventoryManager manager;

        public StoreManagerPanel(InventoryManager manager) {
            this.manager = manager;

            panel = new JPanel(new BorderLayout());

            JLabel title = new JLabel("Store Manager Panel", SwingConstants.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 22));
            panel.add(title, BorderLayout.NORTH);

            // ---------------- FORM ----------------
            JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
            form.setBorder(BorderFactory.createTitledBorder("Furniture Details"));

            idField = new JTextField();
            nameField = new JTextField();
            priceField = new JTextField();
            qtyField = new JTextField();

            form.add(new JLabel("ID:")); form.add(idField);
            form.add(new JLabel("Name:")); form.add(nameField);
            form.add(new JLabel("Price:")); form.add(priceField);
            form.add(new JLabel("Quantity:")); form.add(qtyField);

            // ---------------- BUTTONS ----------------
            JPanel buttons = new JPanel(new GridLayout(1, 5, 10, 10));
            JButton add = new JButton("Add");
            JButton remove = new JButton("Remove");
            JButton update = new JButton("Update");
            JButton clear = new JButton("Clear All");
            JButton refresh = new JButton("Refresh");

            buttons.add(add);
            buttons.add(remove);
            buttons.add(update);
            buttons.add(clear);
            buttons.add(refresh);

            // ---------------- TABLE ----------------
            table = new JTable();
            JScrollPane scroll = new JScrollPane(table);

            panel.add(form, BorderLayout.NORTH);
            panel.add(scroll, BorderLayout.CENTER);
            panel.add(buttons, BorderLayout.SOUTH);

            add.addActionListener(e -> addItem());
            remove.addActionListener(e -> removeItem());
            update.addActionListener(e -> updateItem());
            clear.addActionListener(e -> clearAll());
            refresh.addActionListener(e -> updateTable());

            updateTable();
        }

        public JPanel getPanel() { return panel; }

        private void addItem() {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int qty = Integer.parseInt(qtyField.getText());

                manager.addFurniture(new FurnitureItem(id, name, price, qty));
                updateTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(panel, "Invalid Input!");
            }
        }

        private void removeItem() {
            try {
                int id = Integer.parseInt(idField.getText());
                manager.removeFurniture(id);
                updateTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(panel, "Enter valid ID!");
            }
        }

        private void updateItem() {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int qty = Integer.parseInt(qtyField.getText());

                manager.updateFurniture(id, name, price, qty);
                updateTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(panel, "Invalid Update!");
            }
        }

        private void clearAll() {
            if (JOptionPane.showConfirmDialog(panel, "Clear entire inventory?", "Confirm",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                manager.clearInventory();
                updateTable();
            }
        }

        private void updateTable() {
            java.util.List<FurnitureItem> items = manager.getInventory();
            String[][] data = new String[items.size()][4];

            for (int i = 0; i < items.size(); i++) {
                FurnitureItem f = items.get(i);
                data[i][0] = f.getId() + "";
                data[i][1] = f.getName();
                data[i][2] = f.getPrice() + "";
                data[i][3] = f.getQuantity() + "";
            }

            table.setModel(new DefaultTableModel(
                    data,
                    new String[]{"ID", "Name", "Price", "Quantity"}
            ));
        }
    }
}