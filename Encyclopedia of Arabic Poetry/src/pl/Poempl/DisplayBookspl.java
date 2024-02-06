package pl.Poempl;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bll.IBLLFacade;
import dal.bookdal.Book;

/**
 * The DisplayBookspl class represents a graphical user interface for displaying
 * books and their poems.
 */
public class DisplayBookspl {

    private static final Logger logger = LogManager.getLogger(DisplayBookspl.class);

    private IBLLFacade bllFacade;
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel statusLabel;
    private JFrame mainFrame; // Store the reference to the main frame

    /**
     * Constructs a DisplayBookspl instance.
     *
     * @param bllFacade The business logic layer facade.
     */
    public DisplayBookspl(IBLLFacade bllFacade) {
        this.bllFacade = bllFacade;

        mainFrame = new JFrame("Book Display");
        mainFrame.setSize(600, 400);
        mainFrame.setLayout(new FlowLayout());
        mainFrame.getContentPane().setBackground(new Color(30, 31, 35)); // GitHub dark mode background color


        tableModel = new DefaultTableModel(new Object[] { "Book Title", "Author", "Publish Date", "Death Date" }, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        statusLabel = new JLabel("");

        mainFrame.add(scrollPane);
        mainFrame.add(statusLabel);

      
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

   
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
             
                onClose();
            }
        });

       
        JPanel buttonPanel = new JPanel();
        JButton customButton = new JButton("Custom Button");
        customizeButton(customButton);
        buttonPanel.add(customButton);
        mainFrame.add(buttonPanel);

        mainFrame.setVisible(true);

        try {
            // Display books when the screen opens
            displayBooks();

            // Add a listener to the table selection so that when a book is selected, it
            // displays the poems
            table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow >= 0) {
                           
                            String selectedBookTitle = (String) tableModel.getValueAt(selectedRow, 0);
                           
                            displayPoems(selectedBookTitle);
                        }
                    }
                }
            });

          
            customButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                  
                    JOptionPane.showMessageDialog(mainFrame, "Custom Button Clicked!");
                }
            });
        } catch (Exception ex) {
            logger.error("Error occurred while initializing DisplayBookspl.", ex);
        }
    }

    /**
     * Displays the list of books in the table.
     */
    public void displayBooks() {
        try {
            tableModel.setRowCount(0);
            List<Book> books = bllFacade.selectBooks();

            for (Book book : books) {
                Object[] rowData = new Object[4];
                rowData[0] = (book.getTitle() != null) ? book.getTitle() : "N/A";
                rowData[1] = (book.getAuthor() != null) ? book.getAuthor() : "N/A";
                rowData[2] = (book.getPublishDate() != null) ? book.getPublishDate().toString() : "N/A";
                rowData[3] = (book.getDeathDate() != null) ? book.getDeathDate().toString() : "N/A";

                tableModel.addRow(rowData);
            }
        } catch (Exception ex) {
            logger.error("Error occurred while displaying books.", ex);
        }
    }

    /**
     * Displays the poems for the specified book title.
     *
     * @param bookTitle The title of the book for which to display poems.
     */
    public void displayPoems(String bookTitle) {
        try {
            new DisplayPoemspl(bllFacade, bookTitle);

            // Dispose of the current frame (close it)
            mainFrame.dispose();
        } catch (Exception ex) {
            logger.error("Error occurred while displaying poems.", ex);
        }
    }

    /**
     * Handles the closing logic.
     */
    private void onClose() {
     
        new PoemPL(bllFacade);
        
        mainFrame.dispose();
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
