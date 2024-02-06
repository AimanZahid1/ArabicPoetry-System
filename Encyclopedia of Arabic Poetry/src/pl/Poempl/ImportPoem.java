package pl.Poempl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import bll.IBLLFacade;
import dal.bookdal.Book;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The ImportPoem class represents a graphical user interface for importing
 * poems.
 */
public class ImportPoem extends JFrame {
	private static final Logger logger = LogManager.getLogger(ImportPoem.class);

	private JTextField filePathField;
	private JTable bookTable;
	private DefaultTableModel tableModel;
	private IBLLFacade bllfacade;

	/**
	 * Constructs an ImportPoem instance.
	 *
	 * @param bllfacade The business logic layer facade.
	 */
	public ImportPoem(IBLLFacade bllfacade) {
		this.bllfacade = bllfacade;
		setTitle("Import Poem");
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();

		// Create a label for file path
		JLabel filePathLabel = new JLabel("Select File:");
		panel.add(filePathLabel);

		// Create a text field for file path
		filePathField = new JTextField(20);
		panel.add(filePathField);

		// Create a button to open file dialog
		JButton selectFileButton = new JButton("Select File");
		panel.add(selectFileButton);

		// Create a button to confirm import
		JButton importButton = new JButton("Confirm Import");
		panel.add(importButton);

		// Create a table model with columns
		tableModel = new DefaultTableModel();
		tableModel.addColumn("Book Title");

		// Create a table with the model
		bookTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(bookTable);
		panel.add(scrollPane);

		selectFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser fileChooser = new JFileChooser();
					int result = fileChooser.showOpenDialog(ImportPoem.this);
					if (result == JFileChooser.APPROVE_OPTION) {
						File selectedFile = fileChooser.getSelectedFile();
						filePathField.setText(selectedFile.getAbsolutePath());
						logger.debug("Selected file: {}", selectedFile.getAbsolutePath());
					}
				} catch (Exception ex) {
					logger.error("Error occurred while selecting a file.", ex);
				}
			}
		});

		importButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int selectedRow = bookTable.getSelectedRow();

					if (selectedRow == -1) {
						JOptionPane.showMessageDialog(ImportPoem.this, "Please select a Book from the table.");
						return;
					}

					String selectedBookName = (String) tableModel.getValueAt(selectedRow, 0);
					String filePath = filePathField.getText();

					bllfacade.importPoems(filePath, selectedBookName);
					JOptionPane.showMessageDialog(ImportPoem.this, "Poem imported successfully!");
					logger.info("Poem imported successfully for book: {}", selectedBookName);
					new DisplayPoemspl(bllfacade, selectedBookName);
					displayBooks();
				} catch (NumberFormatException ex) {
					logger.error("Error occurred while importing a poem.", ex);
					JOptionPane.showMessageDialog(ImportPoem.this, "Error importing poem.");
				}
			}
		});

		add(panel);
		displayBooks();
	}

	/**
	 * Display books in the table.
	 */
	public void displayBooks() {
		try {
			tableModel.setRowCount(0);
			List<Book> books = bllfacade.selectBooks();
			for (Book book : books) {
				Object[] rowData = new Object[1];
				rowData[0] = (book.getTitle() != null) ? book.getTitle() : "N/A";
				tableModel.addRow(rowData);
			}
			logger.debug("Displayed books successfully.");
		} catch (Exception ex) {
			logger.error("Error occurred while displaying books.", ex);
		}
	}
}
