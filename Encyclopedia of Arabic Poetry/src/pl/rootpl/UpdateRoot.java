package pl.rootpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bll.IBLLFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The UpdateRoot class represents the graphical user interface for updating a
 * root.
 */
public class UpdateRoot extends JFrame {
	private static final Logger logger = LogManager.getLogger(UpdateRoot.class);

	private JTextField newRootTextField;
	private IBLLFacade bllFacade;
	private String initialRootName;

	/**
	 * Constructs an UpdateRoot instance.
	 *
	 * @param bll             The business logic layer facade.
	 * @param initialRootName The initial root name.
	 */
	public UpdateRoot(IBLLFacade bll, String initialRootName) {
		this.bllFacade = bll;
		this.initialRootName = initialRootName;

		setTitle("Update Root");
		setSize(400, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create a JPanel to hold the components
		JPanel panel = new JPanel();
		JLabel newRootLabel = new JLabel("Enter the new root:");
		panel.add(newRootLabel);

		newRootTextField = new JTextField(20);
		// Set the initial root name in the text field
		newRootTextField.setText(initialRootName);
		panel.add(newRootTextField);

		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// Get the new root name from the text field
					String newRoot = newRootTextField.getText();
					bllFacade.updateroute(initialRootName, newRoot, "verified");
					dispose();
					new RootPL(bll);
					// Log success message
					logger.info("Root updated successfully - Initial Root: {}, New Root: {}", initialRootName, newRoot);
				} catch (SQLException | NumberFormatException ex) {
					// Log error
					logger.error("Error occurred while updating root.", ex);
				}
			}
		});
		panel.add(updateButton);
		add(panel);
	}

	/**
	 * Gets the new root name.
	 *
	 * @return The new root name.
	 */
	public String getNewRootName() {
		return newRootTextField.getText();
	}
}
