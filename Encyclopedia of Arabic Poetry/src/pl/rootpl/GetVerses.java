package pl.rootpl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import bll.IBLLFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A class for displaying verses associated with a root ID.
 */
public class GetVerses {
    private static final Logger logger = LogManager.getLogger(GetVerses.class);

    private IBLLFacade bll;
    private String selectedPoemName;

    /**
     * Constructs a GetVerses instance.
     *
     * @param bllf    The business logic layer facade.
     * @param root_id The root ID for which verses are to be displayed.
     */
    public GetVerses(IBLLFacade bllf, int root_id) {
        this.bll = bllf;

        // Create the main frame
        JFrame frame = new JFrame("Verses for Root ID: " + root_id);
        frame.setSize(800, 400); // Adjusted the width to accommodate the new column
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set custom background color
        frame.getContentPane().setBackground(new Color(30, 31, 35));

        // Create a table model with columns
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("First Verse");
        tableModel.addColumn("Second Verse");
        tableModel.addColumn("Poem Name"); // Added a column for Poem Name

        // Create a table with the model
        JTable versesTable = new JTable(tableModel);
        versesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow only single selection

        // Set column widths
        versesTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        versesTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        versesTable.getColumnModel().getColumn(2).setPreferredWidth(300);

        // Set custom font and foreground color for the table
        Font tableFont = new Font("Arial", Font.PLAIN, 14);
        versesTable.setFont(tableFont);
        versesTable.setForeground(Color.BLACK);

        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(versesTable);

        // Set custom border for the scroll pane
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 2));

        // Add the scroll pane to the frame
        frame.add(scrollPane, BorderLayout.CENTER);

        // Get verses and poem names from the BLL
        logger.debug("Root ID: {}", root_id);
        List<String[]> versesAndPoems = bll.getVersesByRoot(root_id);
        logger.debug("After calling getVersesByRoot");
        if (versesAndPoems != null) {
            for (String[] verseAndPoem : versesAndPoems) {
                Object[] rowData = new Object[3];
                rowData[0] = verseAndPoem[0];
                rowData[1] = verseAndPoem[1];
                rowData[2] = verseAndPoem[2];
                tableModel.addRow(rowData);
            }
        } else {
            logger.warn("The list of versesAndPoems is null.");
        }

      
        versesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { // Check if the event is an adjustment
                    int selectedRow = versesTable.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedPoemName = (String) tableModel.getValueAt(selectedRow, 2);
                        new VersesByPoem(bll, bll.PoemIdByTitle(selectedPoemName));
                        logger.info("Selected Poem Name: {}", selectedPoemName);
                    }
                }
            }
        });

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

    
        frame.setVisible(true);
    }

    /**
     * Gets the selected poem name.
     *
     * @return The selected poem name.
     */
    public String getSelectedPoemName() {
        return selectedPoemName;
    }
}
