package furnituremanagementsystem;

import java.io.*;
import java.util.*;




class FurnitureNotFoundException extends Exception {
    public FurnitureNotFoundException(String message) {
        super(message);
    }
}

class InsufficientStockException extends Exception {
    public InsufficientStockException(String message) {
        super(message);
    }
}


class Furniture {
    private final int id;
    private final String name;
    private int quantity;
    private final double price;

    public Furniture(int id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return String.format("%-5d | %-20s | Qty: %-5d | Rs. %.2f", id, name, quantity, price);
    }
}

class FurnitureManager {
    private final List<Furniture> furnitureList = new ArrayList<>();

    
    
    public void addFurniture(Furniture f) {
        furnitureList.add(f);
        System.out.println(" Furniture added successfully!");
    }

  
    public void viewAll() {
        System.out.println("\n--------------------------------------");
        System.out.println("         AVAILABLE FURNITURE");
        System.out.println("--------------------------------------");
        if (furnitureList.isEmpty()) {
            System.out.println("No furniture available.");
            return;
        }
        for (Furniture f : furnitureList)
            System.out.println(f);
    }

   
    public Furniture findFurnitureById(int id) throws FurnitureNotFoundException {
        for (Furniture f : furnitureList)
            if (f.getId() == id)
                return f;
        throw new FurnitureNotFoundException(" Furniture with ID " + id + " not found!");
    }

    
    public void sellFurniture(int id, int qty) throws InsufficientStockException, FurnitureNotFoundException {
        Furniture f = findFurnitureById(id);

        if (f.getQuantity() < qty)
            throw new InsufficientStockException(" Not enough stock to complete the sale!");

        f.setQuantity(f.getQuantity() - qty);
        System.out.println("✅ Sale successful! " + qty + " units of " + f.getName() + " sold.");
    }

    // Save data to file (Checked Exception)
    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Furniture f : furnitureList) {
                writer.write(f.getId() + "," + f.getName() + "," + f.getQuantity() + "," + f.getPrice());
                writer.newLine();
            }
            System.out.println("Furniture data saved successfully!");
        }
    }

   
    public void loadFromFile(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                furnitureList.add(new Furniture(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        Integer.parseInt(parts[2]),
                        Double.parseDouble(parts[3])
                ));
            }
            System.out.println("? Furniture data loaded successfully!");
        }
    }
}


public class FurnitureManagementSystem {

    
    public static int readInt(Scanner sc, String message) {
        while (true) {
            try {
                System.out.print(message);
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("⚠ Invalid input! Please enter a number.");
                sc.nextLine(); 
            }
        }
    }

    
    public static double readDouble(Scanner sc, String message) {
        while (true) {
            try {
                System.out.print(message);
                return sc.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println(" Invalid input! Please enter a valid number (e.g., 1000.50).");
                sc.nextLine(); 
            }
        }
    }

   
    public static String readString(Scanner sc, String message) {
        sc.nextLine();
        System.out.print(message);
        return sc.nextLine().trim();
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            FurnitureManager manager = new FurnitureManager();
            try {
                manager.loadFromFile("furniture_data.txt");
            } catch (IOException e) {
                System.out.println(" No previous data found. Starting fresh...");
            }
            int choice;
            do {
                System.out.println("\n======================================");
                System.out.println("        FURNITURE MANAGEMENT SYSTEM   ");
                System.out.println("======================================");
                System.out.println("1. Add Furniture");
                System.out.println("2. View All Furniture");
                System.out.println("3. Sell Furniture");
                System.out.println("4. Save and Exit");
                System.out.println("======================================");
                
                choice = readInt(sc, "Enter your choice: ");
                
                switch (choice) {
                    case 1 -> {
                        int id = readInt(sc, "Enter ID: ");
                        String name = readString(sc, "Enter Name: ");
                        int qty = readInt(sc, "Enter Quantity: ");
                        double price = readDouble(sc, "Enter Price (Rs): ");
                        manager.addFurniture(new Furniture(id, name, qty, price));
                    }
                    
                    case 2 -> manager.viewAll();
                    
                    case 3 -> {
                        int sellId = readInt(sc, "Enter Furniture ID to sell: ");
                        int sellQty = readInt(sc, "Enter Quantity to sell: ");
                        try {
                            manager.sellFurniture(sellId, sellQty);
                        } catch (FurnitureNotFoundException | InsufficientStockException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                    
                    case 4 -> {
                        try {
                            manager.saveToFile("furniture_data.txt");
                        } catch (IOException e) {
                            System.out.println("Failed to save data: " + e.getMessage());
                        } finally {
                            System.out.println("Exiting system... Goodbye!");
                        }
                    }
                    
                    default -> System.out.println("Invalid choice! Please select between 1–4.");
                }
                
            } while (choice != 4);
            }
}}
