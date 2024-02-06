package pl.Poempl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bll.IBLLFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddPoempl {

    private static final Logger logger = LogManager.getLogger(AddPoempl.class);

    private IBLLFacade bllFacade;
    private JFrame addPoemFrame;
    private JTextField poemTextField;

    public AddPoempl(IBLLFacade bllFacade, String bookTitle) {
        this.bllFacade = bllFacade;

        addPoemFrame = new JFrame("Add Poem");
        addPoemFrame.setSize(400, 200);
        addPoemFrame.setLayout(new BorderLayout());

        
        addPoemFrame.getContentPane().setBackground(new Color(30, 31, 35));;

       
        JPanel panel = new JPanel();

        JLabel poemLabel = new JLabel("Poem Text:");
        poemTextField = new JTextField(20);

        JButton addPoemButton = new JButton("Add Poem");
        customizeButton(addPoemButton);

        panel.add(poemLabel);
        panel.add(poemTextField);
        panel.add(addPoemButton);

        addPoemFrame.add(panel, BorderLayout.CENTER);

        // Add ActionListener for the Add Poem button
        addPoemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String poemText = poemTextField.getText();

                if (poemText.trim().isEmpty() || bookTitle.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please provide both poem text and book ID.");
                    logger.warn("Empty poem text or book ID provided.");
                    return;
                }

                addPoem(bookTitle, poemText);
                // Clear the text field for the next poem
                poemTextField.setText("");
                // Display the poems again to reflect the changes
                new DisplayPoemspl(bllFacade, bookTitle);
            }
        });

        addPoemFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new DisplayPoemspl(bllFacade, bookTitle);
            }
        });

        addPoemFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addPoemFrame.setVisible(true);
    }

    private void addPoem(String bookTitle, String poemText) {
        try {
            bllFacade.addPoem(bllFacade.getBookIdByTitle(bookTitle), poemText);
            logger.info("Poem added for book: " + bookTitle);
            JOptionPane.showMessageDialog(null, "Poem added successfully!");
        } catch (Exception ex) {
            logger.error("Error occurred while adding a poem.", ex);
            JOptionPane.showMessageDialog(null, "An error occurred while adding the poem. Please check logs.");
        }
    }

    private void customizeButton(JButton button) {
       
        button.setBackground(new Color(28, 32, 36));
        button.setForeground(Color.WHITE);
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        button.setFont(buttonFont);
    }
}
