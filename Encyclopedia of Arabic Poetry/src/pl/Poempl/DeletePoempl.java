package pl.Poempl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import bll.IBLLFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The DeletePoempl class represents a graphical user interface for deleting
 * poems from a book.
 */
public class DeletePoempl {
    private static final Logger logger = LogManager.getLogger(DeletePoempl.class);

    private IBLLFacade bllFacade;
    private JFrame deletePoemFrame;
    private JTextField poemTitleTextField;

    /**
     * Constructs a DeletePoempl instance.
     *
     * @param bllFacade The business logic layer facade.
     * @param bookTitle The title of the book from which the poem will be deleted.
     * @param poemTitle The title of the poem to be deleted.
     */
    public DeletePoempl(IBLLFacade bllFacade, String bookTitle, String poemTitle) {
        this.bllFacade = bllFacade;
        deletePoemFrame = new JFrame("Delete Poem");
        deletePoemFrame.setSize(400, 300);
        deletePoemFrame.setLayout(new BorderLayout());

       
        deletePoemFrame.getContentPane().setBackground(new Color(255, 255, 255));

 
        JPanel panel = new JPanel();

        panel.setBorder(new EmptyBorder(50, 0, 0, 0));

        JLabel poemTitleLabel = new JLabel("Poem Title:");
        poemTitleTextField = new JTextField(poemTitle);
        poemTitleTextField.setEditable(false);

        JButton deleteButton = new JButton("Confirm Delete");
        customizeButton(deleteButton);

        panel.add(poemTitleLabel);
        panel.add(poemTitleTextField);
        panel.add(deleteButton);

        deletePoemFrame.add(panel, BorderLayout.CENTER);

     
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
                int confirm = javax.swing.JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete the poem?", "Confirmation",
                        javax.swing.JOptionPane.YES_NO_OPTION);
                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                  
                    try {
                        bllFacade.deletePoem(bllFacade.getBookIdByTitle(bookTitle), poemTitle);
                        logger.info("Poem deleted: Book Title - {}, Poem Title - {}", bookTitle, poemTitle);
                        deletePoemFrame.dispose();
                        new DisplayPoemspl(bllFacade, bookTitle);
                    } catch (Exception ex) {
                        logger.error("Error occurred while deleting a poem.", ex);
                    }
                }
            }
        });

        deletePoemFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new DisplayPoemspl(bllFacade, bookTitle);
            }
        });

        deletePoemFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        deletePoemFrame.setVisible(true);
    }

    private void customizeButton(JButton button) {
       
        button.setBackground(new Color(28, 32, 36));
        button.setForeground(Color.WHITE);
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        button.setFont(buttonFont);
   
        button.setMargin(new Insets(5, 5, 5, 5));
    }
}
