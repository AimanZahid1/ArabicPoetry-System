package pl.rootpl;

import java.awt.Dimension;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import bll.IBLLFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The ViewRoots class represents a graphical user interface for displaying all
 * roots with their statuses.
 */
public class ViewRoots extends JFrame {
	private static final Logger logger = LogManager.getLogger(ViewRoots.class);

	private IBLLFacade bllfacade;
	private JTable table;

	/**
	 * Constructs a ViewRoots instance.
	 *
	 * @param bll The business logic layer facade.
	 */
	public ViewRoots(IBLLFacade bll) {
		this.bllfacade = bll;
		setTitle("View All Roots");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			List<String[]> roots = bllfacade.viewAllRouteWithStatus();

			Vector<String> columnNames = new Vector<>();
			columnNames.add("Root Name");
			columnNames.add("Status"); // Add a column for status

			Vector<Vector<Object>> data = new Vector<>();
			for (String[] rootData : roots) {
				Vector<Object> row = new Vector<>();
				row.add(rootData[0]); 
				row.add(rootData[1]); 
				data.add(row);
			}

			table = new JTable(data, columnNames);
			table.setPreferredScrollableViewportSize(new Dimension(600, 400));
			table.setFillsViewportHeight(true);

			JScrollPane scrollPane = new JScrollPane(table);
			add(scrollPane);

			logger.info("Successfully loaded and displayed roots data.");
		} catch (SQLException ex) {
			logger.error("Error occurred while fetching and displaying roots data.", ex);
		}
	}
}
