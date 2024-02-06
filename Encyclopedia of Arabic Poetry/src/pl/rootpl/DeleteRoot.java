package pl.rootpl;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bll.IBLLFacade;

/**
 * A JFrame class for deleting a root.
 */
public class DeleteRoot extends JFrame {
    private static final Logger logger = LogManager.getLogger(DeleteRoot.class);

    private JTextField rootIdTextField;
    private IBLLFacade bllFacade;

    /**
     * Constructs a DeleteRoot instance.
     *
     * @param bll  The business logic layer facade.
     * @param name The name of the root to be deleted.
     */
    public DeleteRoot(IBLLFacade bll, String name) {
        this.bllFacade = bll;
        setTitle("Delete Root");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        rootIdTextField = new JTextField(20);
        rootIdTextField.setText(name);

        panel.add(rootIdTextField);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    bllFacade.deleteroute(name);
                    dispose();
                    new RootPL(bll);
                    logger.info("Deleted root successfully. Root Name: {}", name);
                } catch (NumberFormatException ex) {
                    logger.warn("Error occurred while deleting root. Invalid format.", ex);
                } catch (SQLException ex) {
                    logger.error("Error occurred while deleting root.", ex);
                    ex.printStackTrace();
                }
            }
        });

        panel.add(deleteButton);
        add(panel);

        // Customize UI colors and fonts
        customizeUI();
    }

    private void customizeUI() {
        // Set the background color for the panel
        getContentPane().setBackground(new Color(36, 41, 46));

        // Set the background color and font for the text field
        rootIdTextField.setBackground(new Color(46, 52, 58));
        rootIdTextField.setForeground(Color.WHITE);
        Font textFieldFont = new Font("Arial", Font.PLAIN, 14);
        rootIdTextField.setFont(textFieldFont);

        // Set the background color and font for the button
        JButton deleteButton = (JButton) ((JPanel) getContentPane().getComponent(0)).getComponent(1);
        deleteButton.setBackground(new Color(28, 32, 36));
        deleteButton.setForeground(Color.WHITE);
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        deleteButton.setFont(buttonFont);
    }
}
