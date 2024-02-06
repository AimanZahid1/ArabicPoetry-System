package pl.Poempl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import bll.IBLLFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.versepl.VersePL;

/**
 * The DisplayPoemspl class represents a graphical user interface for displaying
 * poems in a book.
 */
public class DisplayPoemspl {

    private static final Logger logger = LogManager.getLogger(DisplayPoemspl.class);

    private IBLLFacade bllFacade;
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel statusLabel;

    /**
     * Constructs a DisplayPoemspl instance.
     *
     * @param bllFacade The business logic layer facade.
     * @param bookTitle The title of the book for which poems will be displayed.
     */
    public DisplayPoemspl(IBLLFacade bllFacade, String bookTitle) {
        this.bllFacade = bllFacade;

        try {
            JFrame mainFrame = new JFrame("Poems in Book - " + bookTitle);
            mainFrame.setSize(600, 400);
            mainFrame.setLayout(new BorderLayout());
            // Set background color to GitHub dark mode
            mainFrame.getContentPane().setBackground(new Color(30, 31, 35));

            
            JPanel buttonPanel = new JPanel();

           
            JButton backButton = new JButton("Back to Books");
            customizeButton(backButton);
            buttonPanel.add(backButton);

          
            JButton addPoemButton = new JButton("Add Poem");
            customizeButton(addPoemButton);
            buttonPanel.add(addPoemButton);

            // Add Update button to the button panel
            JButton updateButton = new JButton("Update");
            customizeButton(updateButton);
            buttonPanel.add(updateButton);

            // Add Delete button to the button panel
            JButton deleteButton = new JButton("Delete");
            customizeButton(deleteButton);
            buttonPanel.add(deleteButton);

            // Add View Verses button to the button panel
            JButton viewVersesButton = new JButton("View Verses");
            customizeButton(viewVersesButton);
            buttonPanel.add(viewVersesButton);

            // Add the button panel to the top (NORTH)
            mainFrame.add(buttonPanel, BorderLayout.NORTH);

            tableModel = new DefaultTableModel(new Object[] { "Poem Title" }, 0);
            table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);

            statusLabel = new JLabel("");

            
            mainFrame.add(scrollPane, BorderLayout.CENTER);

            mainFrame.add(statusLabel, BorderLayout.SOUTH);

            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setVisible(true);

           
            displayPoems(bookTitle);

           
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        // Close the current frame (DisplayPoemspl)
                        mainFrame.dispose();

                        // Create and show the DisplayBookspl frame
                        new DisplayBookspl(bllFacade);
                    } catch (Exception ex) {
                        logger.error("Error occurred while navigating back to books.", ex);
                    }
                }
            });

            // Add ActionListener for the Add Poem button
            addPoemButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        new AddPoempl(bllFacade, bookTitle);
                        mainFrame.dispose();
                    } catch (Exception ex) {
                        logger.error("Error occurred while adding a poem.", ex);
                    }
                }
            });

            // Add ActionListener for the Update button
            updateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow != -1) {
                            String selectedPoemTitle = (String) table.getValueAt(selectedRow, 0);
                            new UpdatePoempl(bllFacade, bookTitle, selectedPoemTitle);
                            mainFrame.dispose();
                            logger.info("Update button clicked for poem: " + selectedPoemTitle);
                        } else {
                            statusLabel.setText("Please select a poem to update.");
                        }
                    } catch (Exception ex) {
                        logger.error("Error occurred while updating a poem.", ex);
                    }
                }
            });

            // Add ActionListener for the Delete button
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow != -1) {
                            String selectedPoemTitle = (String) table.getValueAt(selectedRow, 0);
                            new DeletePoempl(bllFacade, bookTitle, selectedPoemTitle);
                            mainFrame.dispose();
                            logger.info("Delete button clicked for poem: " + selectedPoemTitle);
                        } else {
                            statusLabel.setText("Please select a poem to delete.");
                        }
                    } catch (Exception ex) {
                        logger.error("Error occurred while deleting a poem.", ex);
                    }
                }
            });

            viewVersesButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow != -1) {
                            String selectedPoemTitle = (String) table.getValueAt(selectedRow, 0);
                            new VersePL(bllFacade, bllFacade.getPoemIdByTitle(bllFacade.getBookIdByTitle(bookTitle),
                                    selectedPoemTitle));
                            logger.info("View Verses button clicked for poem: " + selectedPoemTitle);
                        } else {
                            statusLabel.setText("Please select a poem to view verses.");
                        }
                    } catch (Exception ex) {
                        logger.error("Error occurred while viewing verses for a poem.", ex);
                    }
                }
            });
        } catch (Exception ex) {
            logger.error("Error occurred while initializing DisplayPoemspl.", ex);
        }
    }

    private void displayPoems(String bookTitle) {
        try {
            tableModel.setRowCount(0);

            int bookId = bllFacade.getBookIdByTitle(bookTitle);

            if (bookId != -1) {
                List<String> poems = bllFacade.viewPoemsByBook(bookId);

                for (String poemTitle : poems) {
                    tableModel.addRow(new Object[] { poemTitle });
                }
                statusLabel.setText("");
            } else {
                statusLabel.setText("Book not found with the specified title.");
            }
        } catch (Exception ex) {
            logger.error("Error occurred while displaying poems.", ex);
        }
    }

    /**
     * Customize the appearance of the button.
     *
     * @param button The button to customize.
     */
    private void customizeButton(JButton button) {
        button.setBackground(new Color(28, 32, 36));
        button.setForeground(Color.WHITE);
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        button.setFont(buttonFont);
    }
}
