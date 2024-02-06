package pl.rootpl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import bll.IBLLFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The VersesByPoem class represents a graphical user interface for displaying
 * verses associated with a specific poem.
 */
public class VersesByPoem {

	private static final Logger logger = LogManager.getLogger(VersesByPoem.class);

	private IBLLFacade bll;
	private int poemId;

	/**
	 * Constructs a VersesByPoem instance.
	 *
	 * @param bll    The business logic layer facade.
	 * @param poemId The ID of the poem for which verses will be displayed.
	 */
	public VersesByPoem(IBLLFacade bll, int poemId) {
		this.bll = bll;
		this.poemId = poemId;

		// Create the main frame
		JFrame frame = new JFrame("Verses for Poem ID: " + poemId);
		frame.setSize(800, 400);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Create a table model with columns
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn("First Verse");
		tableModel.addColumn("Second Verse");

		// Create a table with the model
		JTable versesTable = new JTable(tableModel);
		versesTable.setFont(new Font("Arial", Font.PLAIN, 14));

		// Create a scroll pane for the table
		JScrollPane scrollPane = new JScrollPane(versesTable);

		// Add the scroll pane to the frame
		frame.add(scrollPane, BorderLayout.CENTER);

		try {
			// Get verses for the specified poem ID from the BLL
			List<String[]> verses = bll.getVersesByPoem(poemId);
			if (verses != null) {
				for (String[] verseData : verses) {
					Object[] rowData = new Object[2];

					rowData[0] = verseData[0]; // First Verse
					rowData[1] = verseData[1]; // Second Verse

					tableModel.addRow(rowData);
				}
			} else {
				logger.warn("The list of verses is null for Poem ID: {}", poemId);
			}
		} catch (Exception ex) {
			logger.error("Error occurred while fetching verses for Poem ID: {}", poemId, ex);
		}

	
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

		frame.setVisible(true);
	}
}
