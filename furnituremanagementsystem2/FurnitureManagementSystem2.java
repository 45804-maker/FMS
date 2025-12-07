package furnituremanagementsystem2;

import javax.swing.SwingUtilities;

public class FurnitureManagementSystem2 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SelectionScreen().createAndShowSelectionScreen());
    }
}
