package pl.versepl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import bll.IBLLFacade;
import pl.Poempl.DisplayPoemspl;

/**
 * This class represents a window for managing verses in a poem. It provides a
 * graphical user interface for adding, updating, deleting, tokenizing, and
 * assigning roots to verses.
 */
public class VersePL {

    private static final Logger LOGGER = LogManager.getLogger(VersePL.class);

    private IBLLFacade bllF;
    private int poemid;
    private JTable table;

    /**
     * Constructs an instance of the VersePL class.
     *
     * @param bllFacade The business logic layer facade for interacting with the
     *                  data layer.
     * @param poemid    The ID of the poem containing the verses.
     */
    public VersePL(IBLLFacade bllFacade, int poemid) {
        this.bllF = bllFacade;
        this.poemid = poemid;

        JFrame frame = new JFrame("Verses Table - " + poemid);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set background color to GitHub dark mode
        frame.getContentPane().setBackground(new Color(30, 31, 35));

        JPanel buttonPanel = new JPanel();
        JButton backButton = createCustomButton("Back to Poems");
        JButton addButton = createCustomButton("Add");
        JButton updateButton = createCustomButton("Update");
        JButton deleteButton = createCustomButton("Delete");
        JButton tokenizeButton = createCustomButton("Tokenize");
        JButton assignRootsButton = createCustomButton("Assign Roots");

        buttonPanel.add(backButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(tokenizeButton);
        buttonPanel.add(assignRootsButton);

        frame.add(buttonPanel, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel(getVersesData(),
                new Object[]{"First Verse", "Second Verse"});
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new DisplayPoemspl(bllF, bllF.getBookTitleByPoemTitle(poemid));
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddVerse(bllFacade, poemid);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String firstVerse = (String) table.getValueAt(selectedRow, 0);
                    String secondVerse = (String) table.getValueAt(selectedRow, 1);
                    new UpdateVerse(bllFacade, firstVerse, secondVerse, poemid);
                } else {
                    LOGGER.warn("No row selected for update.");
                    JOptionPane.showMessageDialog(null, "Please select a row to update.");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String firstVerse = (String) table.getValueAt(selectedRow, 0);
                    String secondVerse = (String) table.getValueAt(selectedRow, 1);

                    try {
                        bllFacade.deleteVerse(poemid, firstVerse, secondVerse);
                        refreshTable();
                        LOGGER.info("Verse deleted successfully.");
                    } catch (Exception ex) {
                        LOGGER.error("Error occurred during verse deletion: " + ex.getMessage(), ex);
                        JOptionPane.showMessageDialog(null, "Error occurred during verse deletion.");
                    }
                } else {
                    LOGGER.warn("No row selected for deletion.");
                    JOptionPane.showMessageDialog(null, "Please select a row to delete.");
                }
            }
        });

        tokenizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String firstVerse = (String) table.getValueAt(selectedRow, 0);
                    String secondVerse = (String) table.getValueAt(selectedRow, 1);
                    new TokenizeVerse(bllFacade, firstVerse, secondVerse, poemid);
                    frame.dispose();
                } else {
                    LOGGER.warn("No row selected for tokenization.");
                    JOptionPane.showMessageDialog(null, "Please select a row to tokenize.");
                }
            }
        });

        assignRootsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String firstVerse = (String) table.getValueAt(selectedRow, 0);
                    String secondVerse = (String) table.getValueAt(selectedRow, 1);
                    try {
                        new AssignRoots(bllF, bllF.getVerseIdFromTitle(firstVerse, secondVerse));
                    } catch (Exception ex) {
                        LOGGER.error("Error occurred during root assignment: " + ex.getMessage(), ex);
                        JOptionPane.showMessageDialog(null, "Error occurred during root assignment.");
                    }
                } else {
                    LOGGER.warn("No row selected for root assignment.");
                    JOptionPane.showMessageDialog(null, "Please select a row to assign roots.");
                }
            }
        });
    }

    private Object[][] getVersesData() {
        List<String[]> verses = bllF.getVersesByPoem(poemid);
        Object[][] data = new Object[verses.size()][2];
        for (int i = 0; i < verses.size(); i++) {
            data[i] = verses.get(i);
        }
        return data;
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setDataVector(getVersesData(), new Object[]{"First Verse", "Second Verse"});
    }

    /**
     * Create a custom button with the specified text.
     *
     * @param buttonText The text to be displayed on the button.
     * @return The customized JButton.
     */
    private JButton createCustomButton(String buttonText) {
        JButton button = new JButton(buttonText);
        button.setBackground(new Color(28, 32, 36));
        button.setForeground(Color.WHITE);
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        button.setFont(buttonFont);
        return button;
    }
}
