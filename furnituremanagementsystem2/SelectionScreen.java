package furnituremanagementsystem2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class SelectionScreen {
    public void createAndShowSelectionScreen() {
        JFrame frame = new JFrame("Select Role");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        JButton storeManagerButton = new JButton("Store Manager");
        JButton customerButton = new JButton("Customer");

        storeManagerButton.addActionListener(e -> {
            new StoreManagerScreen().createAndShowManagerScreen();
            frame.dispose(); // Close the selection screen
        });

        customerButton.addActionListener(e -> {
            new CustomerScreen().createAndShowCustomerScreen();
            frame.dispose(); // Close the selection screen
        });

        frame.add(storeManagerButton);
        frame.add(customerButton);

        frame.setVisible(true);
    }
}
