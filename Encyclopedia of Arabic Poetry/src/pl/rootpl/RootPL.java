package pl.rootpl;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bll.IBLLFacade;

public class RootPL extends JFrame {
    private static final Logger logger = LogManager.getLogger(RootPL.class);

    private IBLLFacade bllFacade;
    private JTable rootsTable;
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton viewAllVersesButton;

    /**
     * Constructs a RootPL instance.
     *
     * @param bllFacade The business logic layer facade.
     */
    public RootPL(IBLLFacade bllFacade) {
        this.bllFacade = bllFacade;

        setTitle("Root Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());

        buttonPanel = new JPanel();
        updateButton = new JButton("Update Root");
        deleteButton = new JButton("Delete Root");
        viewAllVersesButton = new JButton("View All Verses");

        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewAllVersesButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Root Name");
        tableModel.addColumn("Status"); // Added "Status" column
        rootsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(rootsTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        // Add ActionListener for the Update Root button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openUpdateRootScreen();
            }
        });

        // Add ActionListener for the Delete Root button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDeleteRootScreen();
            }
        });

        // Add ActionListener for the View All Verses button
        viewAllVersesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openViewAllVersesScreen();
            }
        });

        // Customize UI colors and fonts
        customizeUI();

        // Display roots initially
        displayRoots();

        setVisible(true);
    }

    private void customizeUI() {
        // Set the background color for the main panel
        mainPanel.setBackground(new Color(36, 41, 46));

        // Set the background color and font for the buttonPanel
        buttonPanel.setBackground(new Color(36, 41, 46));
        updateButton.setBackground(new Color(28, 32, 36));
        deleteButton.setBackground(new Color(28, 32, 36));
        viewAllVersesButton.setBackground(new Color(28, 32, 36));
        updateButton.setForeground(Color.WHITE);
        deleteButton.setForeground(Color.WHITE);
        viewAllVersesButton.setForeground(Color.WHITE);
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        updateButton.setFont(buttonFont);
        deleteButton.setFont(buttonFont);
        viewAllVersesButton.setFont(buttonFont);

        // Set the background color and font for the rootsTable
        rootsTable.setBackground(new Color(46, 52, 58));
        rootsTable.setForeground(Color.WHITE);
        Font tableFont = new Font("Arial", Font.PLAIN, 14);
        rootsTable.setFont(tableFont);
        rootsTable.getTableHeader().setFont(tableFont);
    }

    private void displayRoots() {
        DefaultTableModel tableModel = (DefaultTableModel) rootsTable.getModel();
        tableModel.setRowCount(0);

        try {
            List<String[]> rootsWithStatus = bllFacade.viewAllRouteWithStatus();
            if (rootsWithStatus != null) {
                for (String[] rootWithStatus : rootsWithStatus) {
                    Object[] rowData = new Object[2];
                    rowData[0] = rootWithStatus[0]; // Root Name
                    rowData[1] = rootWithStatus[1]; // Status
                    tableModel.addRow(rowData);
                }
            } else {
                logger.warn("The list of rootsWithStatus is null.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while displaying roots.", e);
        }
    }

	
	/**
	 * Opens the Update Root screen.
	 */
	private void openUpdateRootScreen() {
		int selectedRow = rootsTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a root from the table.");
			return;
		}

		String selectedRootName = (String) rootsTable.getValueAt(selectedRow, 0);

		UpdateRoot updateRootFrame = new UpdateRoot(bllFacade, selectedRootName);
		updateRootFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		updateRootFrame.setVisible(true);
		refreshRoots();
		dispose();
		System.out.println("Update operation performed. Table refreshed.");
	}

	/**
	 * Opens the Delete Root screen.
	 */
	private void openDeleteRootScreen() {
		int selectedRow = rootsTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a root from the table.");
			return;
		}

		String selectedRootName = (String) rootsTable.getValueAt(selectedRow, 0);
		DeleteRoot deleteRootFrame = new DeleteRoot(bllFacade, selectedRootName);
		deleteRootFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		deleteRootFrame.setVisible(true);

		// Refresh the table after deleting
		dispose();
		refreshRoots();
		System.out.println("Delete operation performed. Table refreshed.");
	}

	/**
	 * Opens the View All Verses screen.
	 */
	private void openViewAllVersesScreen() {
		int selectedRow = rootsTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a root from the table.");
			return;
		}

		String selectedRootName = (String) rootsTable.getValueAt(selectedRow, 0);
		System.out.print(selectedRootName + "id is " + bllFacade.roodIdFromName(selectedRootName));
		new GetVerses(bllFacade, bllFacade.roodIdFromName(selectedRootName));
	}

    /**
     * Refreshes the roots table.
     */
    public void refreshRoots() {
        displayRoots();
    }
}
