package pl.Poempl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bll.IBLLFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The UpdatePoempl class represents a graphical user interface for updating
 * poems.
 */
public class UpdatePoempl {
    private static final Logger logger = LogManager.getLogger(UpdatePoempl.class);

    private IBLLFacade bllFacade;
    private JFrame updatePoemFrame;
    private JTextField oldPoemTextField;
    private JTextField newPoemTextField;

    /**
     * Constructs an UpdatePoempl instance.
     *
     * @param bllFacade   The business logic layer facade.
     * @param bookTitle   The title of the book containing the poem.
     * @param oldPoemName The name of the poem to be updated.
     */
    public UpdatePoempl(IBLLFacade bllFacade, String bookTitle, String oldPoemName) {
        this.bllFacade = bllFacade;
        updatePoemFrame = new JFrame("Update Poem");
        updatePoemFrame.setSize(400, 200);
        updatePoemFrame.setLayout(new BorderLayout());
        // Set background color to GitHub dark mode
        updatePoemFrame.getContentPane().setBackground(new Color(30, 31, 35));

        // Create panel to hold components
        JPanel panel = new JPanel();

        JLabel oldPoemLabel = new JLabel("Old Poem Name:");
        oldPoemTextField = new JTextField(oldPoemName);
        oldPoemTextField.setEditable(false);

        JLabel newPoemLabel = new JLabel("New Poem Name:");
        newPoemTextField = new JTextField(20);

        JButton updateButton = new JButton("Update");

        // Customize button appearance
        updateButton.setBackground(new Color(28, 32, 36));
        updateButton.setForeground(Color.WHITE);
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        updateButton.setFont(buttonFont);

        panel.add(oldPoemLabel);
        panel.add(oldPoemTextField);
        panel.add(newPoemLabel);
        panel.add(newPoemTextField);
        panel.add(updateButton);

        updatePoemFrame.add(panel, BorderLayout.CENTER);

        // Add ActionListener for the Update button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String newPoemName = newPoemTextField.getText();

                    if (newPoemName.trim().isEmpty()) {
                        logger.warn("Attempted to update poem with empty new poem name.");
                        System.out.println("Please provide a new poem name.");
                        return;
                    }

                    bllFacade.updatePoem(bllFacade.getBookIdByTitle(bookTitle), oldPoemName, newPoemName);
                    logger.info("Updated poem successfully. Book Title: {}, Old Poem Name: {}, New Poem Name: {}",
                            bookTitle, oldPoemName, newPoemName);
                    System.out.println("Update button clicked");
                    System.out.println("Book Title: " + bookTitle);
                    System.out.println("Old Poem Name: " + oldPoemName);
                    System.out.println("New Poem Name: " + newPoemName);
                    updatePoemFrame.dispose();
                    new DisplayPoemspl(bllFacade, bookTitle);
                } catch (Exception ex) {
                    logger.error("Error occurred while updating poem.", ex);
                }
            }
        });

        updatePoemFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new DisplayPoemspl(bllFacade, bookTitle);
            }
        });

        updatePoemFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        updatePoemFrame.setVisible(true);
    }
}
