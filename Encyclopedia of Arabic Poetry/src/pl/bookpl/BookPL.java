package pl.bookpl;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdesktop.swingx.JXDatePicker;

import bll.IBLLFacade;
import dal.bookdal.Book;

public class BookPL extends JFrame {
    private static final Logger logger = LogManager.getLogger(BookPL.class);

    private DefaultTableModel tableModel;
    private JTable table;
    private SimpleDateFormat dateFormat;
    private IBLLFacade bllFacade;

    public BookPL(IBLLFacade bllFacade) {
        try {
            this.bllFacade = bllFacade;

            JFrame mainFrame = new JFrame("Book Management");
            mainFrame.setSize(600, 400);
            mainFrame.setLayout(new FlowLayout());
            mainFrame.getContentPane().setBackground(new Color(36, 41, 46));

            JButton addButton = new JButton("Add");
            JButton updateButton = new JButton("Update");
            JButton deleteButton = new JButton("Delete");
            JButton homeButton = new JButton("Home");

            customizeButton(addButton);
            customizeButton(updateButton);
            customizeButton(deleteButton);
            customizeButton(homeButton);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());
            buttonPanel.setBackground(new Color(36, 41, 46));
            buttonPanel.add(homeButton);
            buttonPanel.add(addButton);
            buttonPanel.add(updateButton);
            buttonPanel.add(deleteButton);

            mainFrame.add(buttonPanel);

            tableModel = new DefaultTableModel(new Object[] { "Title", "Author", "Publish Date", "Death Date" }, 0);
            table = new JTable(tableModel);
            table.setBackground(new Color(46, 52, 58));
            table.setForeground(Color.WHITE);
            JScrollPane scrollPane = new JScrollPane(table);
            mainFrame.add(scrollPane);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setVisible(true);

            homeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        mainFrame.dispose();
                    } catch (Exception ex) {
                        logger.error("Error occurred in Home button action.", ex);
                    }
                }
            });

            addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        openAddBookDialog();
                        // mainFrame.dispose(); // Do not dispose immediately to allow Add Book dialog to be shown
                    } catch (Exception ex) {
                        logger.error("Error occurred in Add button action.", ex);
                    }
                }
            });

            updateButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        openUpdateBookDialog();
                        // mainFrame.dispose(); // Do not dispose immediately to allow Update Book dialog to be shown
                    } catch (Exception ex) {
                        logger.error("Error occurred in Update button action.", ex);
                    }
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        openDeleteBookDialog();
                        // mainFrame.dispose(); // Do not dispose immediately to allow Delete Book dialog to be shown
                    } catch (Exception ex) {
                        logger.error("Error occurred in Delete button action.", ex);
                    }
                }
            });

            dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            displayBooks();

        } catch (Exception e) {
            logger.fatal("Fatal error during BookPL initialization.", e);
        }
    }

    public void displayBooks() {
        try {
            tableModel.setRowCount(0);
            List<Book> books = bllFacade.selectBooks();

            for (Book book : books) {
                Object[] rowData = new Object[4];
                rowData[0] = (book.getTitle() != null) ? book.getTitle() : "N/A";
                rowData[1] = (book.getAuthor() != null) ? book.getAuthor() : "N/A";
                rowData[2] = (book.getPublishDate() != null) ? dateFormat.format(book.getPublishDate()) : "N/A";
                rowData[3] = (book.getDeathDate() != null) ? dateFormat.format(book.getDeathDate()) : "N/A";

                tableModel.addRow(rowData);
            }
        } catch (Exception e) {
            logger.error("Error occurred while displaying books.", e);
        }

    }

    private void openAddBookDialog() {
        try {
            JFrame addFrame = new JFrame("Add Book");
            addFrame.setSize(400, 200);

            JLabel titleLabel = new JLabel("Title:");
            JTextField titleTextField = new JTextField(30);
            JLabel authorLabel = new JLabel("Author:");
            JTextField authorTextField = new JTextField(30);
            JLabel publishDateLabel = new JLabel("Publish Date:");
            JXDatePicker publishDatePicker = new JXDatePicker();
            publishDatePicker.setDate(new Date());
            publishDatePicker.setFormats(new SimpleDateFormat("dd-MM-yyyy"));

            JLabel deathDateLabel = new JLabel("Death Date:");
            JXDatePicker deathDatePicker = new JXDatePicker();
            deathDatePicker.setDate(new Date());
            deathDatePicker.setFormats(new SimpleDateFormat("dd-MM-yyyy"));

            JButton addButton = new JButton("Add Book");

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(6, 2));
            panel.add(titleLabel);
            panel.add(titleTextField);
            panel.add(authorLabel);
            panel.add(authorTextField);
            panel.add(publishDateLabel);
            panel.add(publishDatePicker);
            panel.add(deathDateLabel);
            panel.add(deathDatePicker);
            panel.add(new JLabel());
            panel.add(addButton);

            addFrame.add(panel);
            addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            addFrame.setVisible(true);

            addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        String title = titleTextField.getText();
                        String author = authorTextField.getText();
                        Date publishDate = publishDatePicker.getDate();
                        Date deathDate = deathDatePicker.getDate();

                        if (publishDate == null || deathDate == null) {
                            JOptionPane.showMessageDialog(null, "Please select both dates.");
                            return;
                        }

                        bllFacade.insertBook(title, author, publishDate, deathDate);
                        logger.debug("Book added: " + title);
                        addFrame.dispose();
                        displayBooks();
                    } catch (Exception ex) {
                        logger.error("Error occurred in Add button action.", ex);
                    }
                }
            });
        } catch (Exception ex) {
            logger.error("Error occurred while opening Add Book dialog.", ex);
        }
    }

    private void openUpdateBookDialog() {
        try {
            JFrame updateFrame = new JFrame("Update Book");
            updateFrame.setSize(400, 200);

            JLabel titleLabel = new JLabel("Selected Book:");
            JTextField titleTextField = new JTextField(30);
            titleTextField.setEditable(false);
            JLabel authorLabel = new JLabel("Author:");
            JTextField authorTextField = new JTextField(30);
            JLabel publishDateLabel = new JLabel("Publish Date:");
            JXDatePicker publishDatePicker = new JXDatePicker();
            publishDatePicker.setFormats(new SimpleDateFormat("dd-MM-yyyy"));
            JLabel deathDateLabel = new JLabel("Death Date:");
            JXDatePicker deathDatePicker = new JXDatePicker();
            deathDatePicker.setFormats(new SimpleDateFormat("dd-MM-yyyy"));

            JButton updateButton = new JButton("Update Book");
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(6, 2));
            panel.add(titleLabel);
            panel.add(titleTextField);
            panel.add(authorLabel);
            panel.add(authorTextField);
            panel.add(publishDateLabel);
            panel.add(publishDatePicker);
            panel.add(deathDateLabel);
            panel.add(deathDatePicker);
            panel.add(new JLabel());
            panel.add(updateButton);

            updateFrame.add(panel);
            updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            updateFrame.setVisible(true);

            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                titleTextField.setText((String) table.getValueAt(selectedRow, 0));
                authorTextField.setText((String) table.getValueAt(selectedRow, 1));

                try {
                    Date publishDate = dateFormat.parse((String) table.getValueAt(selectedRow, 2));
                    Date deathDate = dateFormat.parse((String) table.getValueAt(selectedRow, 3));
                    publishDatePicker.setDate(publishDate);
                    deathDatePicker.setDate(deathDate);
                } catch (ParseException ex) {
                    logger.error("Error occurred while parsing date in Update Book dialog.", ex);
                }
            }

            updateButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String title = titleTextField.getText();
                    String author = authorTextField.getText();
                    Date publishDate = publishDatePicker.getDate();
                    Date deathDate = deathDatePicker.getDate();

                    if (publishDate == null || deathDate == null) {
                        JOptionPane.showMessageDialog(null, "Please select both dates.");
                        return;
                    }

                    try {
                        bllFacade.updateBook(title, author, publishDate, deathDate);
                        updateFrame.dispose();
                        displayBooks();

                    } catch (Exception ex) {
                        logger.error("Error occurred while updating a book.", ex);
                    }
                }
            });
        } catch (Exception e) {
            logger.error("Error occurred while opening Update Book dialog.", e);
        }
    }

    private void openDeleteBookDialog() {
        try {
            JFrame deleteFrame = new JFrame("Delete Book");
            deleteFrame.setSize(400, 100);

            JLabel titleLabel = new JLabel("Selected Book:");
            JTextField titleTextField = new JTextField(30);
            titleTextField.setEditable(false);

            JButton deleteButton = new JButton("Delete Book");

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(2, 2));
            panel.add(titleLabel);
            panel.add(titleTextField);
            panel.add(new JLabel());
            panel.add(deleteButton);

            deleteFrame.add(panel);
            deleteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            deleteFrame.setVisible(true);

            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                titleTextField.setText((String) table.getValueAt(selectedRow, 0));
            }

            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        String title = titleTextField.getText();
                        bllFacade.deleteBook(title);
                        logger.debug("Book deleted: " + title);
                        deleteFrame.dispose();
                        displayBooks();
                    } catch (Exception ex) {
                        logger.error("Error occurred while deleting a book.", ex);
                    }
                }
            });
        } catch (Exception ex) {
            logger.error("Error occurred while opening Delete Book dialog.", ex);
        }
    }

    private void customizeButton(JButton button) {
        button.setBackground(new Color(28, 32, 36));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
    }

 
}
