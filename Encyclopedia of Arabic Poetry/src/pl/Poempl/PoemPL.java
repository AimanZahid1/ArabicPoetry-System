package pl.Poempl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bll.IBLLFacade;

public class PoemPL extends JFrame {

    private static final Logger logger = LogManager.getLogger(PoemPL.class);
    private IBLLFacade bllFacade;

    public PoemPL(IBLLFacade bllFacade) {
        this.bllFacade = bllFacade;
        setTitle("Poem Management");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        getContentPane().setBackground(new Color(30, 31, 35));

        // Create a panel with a FlowLayout to center the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 150)); // Increased vertical gap to 30

        // Decrease the button text size even more
        Dimension buttonSize = new Dimension(200, 50);
        Font buttonFont = new Font("Arial", Font.PLAIN, 15); // Further decreased font size

        JButton manualAddButton = new JButton("Manual Manage Poems");
        manualAddButton.setPreferredSize(buttonSize);
        manualAddButton.setFont(buttonFont);
        manualAddButton.setBackground(new Color(28, 32, 36)); // Set button color
        manualAddButton.setForeground(Color.WHITE); // Set text color
        manualAddButton.setMargin(new Insets(10, 10, 10, 10)); // Increased margin
        buttonPanel.add(manualAddButton);

        // Add margin between buttons
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0))); // Adjust the width as needed

        JButton importButton = new JButton("Import Poems");
        importButton.setPreferredSize(buttonSize);
        importButton.setFont(buttonFont);
        importButton.setBackground(new Color(28, 32, 36)); // Set button color
        importButton.setForeground(Color.WHITE); // Set text color
        importButton.setMargin(new Insets(20, 20, 20, 20)); // Increased margin
        buttonPanel.add(importButton);

        add(buttonPanel);

        manualAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new DisplayBookspl(bllFacade);
                    logger.debug("Opened DisplayBookspl for manual poem addition.");
                } catch (Exception ex) {
                    logger.error("Error occurred while opening DisplayBookspl for manual poem addition.", ex);
                }
            }
        });

        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new ImportPoem(bllFacade).setVisible(true);
                    logger.debug("Opened ImportPoem for poem import.");
                } catch (Exception ex) {
                    logger.error("Error occurred while opening ImportPoem for poem import.", ex);
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });
    }

    private void onClose() {
        dispose();
    }
}
