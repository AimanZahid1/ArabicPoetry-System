package pl.versepl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import bll.IBLLFacade;

/**
 * This class represents a window for updating verses. It provides a graphical
 * user interface for users to update existing verses in a poem.
 */
public class UpdateVerse {

	private static final Logger LOGGER = LogManager.getLogger(UpdateVerse.class);

	private IBLLFacade bllFacade;
	private JFrame frame;
	private JTextField oldFirstVerseField;
	private JTextField oldSecondVerseField;
	private JTextField newFirstVerseField;
	private JTextField newSecondVerseField;
	private int poemId;

	/**
	 * Constructs an instance of the UpdateVerse class.
	 *
	 * @param bllFacade      The business logic layer facade for interacting with
	 *                       the data layer.
	 * @param oldFirstVerse  The old first verse to be updated.
	 * @param oldSecondVerse The old second verse to be updated.
	 * @param poemId         The ID of the poem containing the verse.
	 */
	public UpdateVerse(IBLLFacade bllFacade, String oldFirstVerse, String oldSecondVerse, int poemId) {
		this.bllFacade = bllFacade;
		this.poemId = poemId;

		frame = new JFrame("Update Verse");
		frame.setSize(400, 200);
		frame.setLayout(new GridLayout(5, 2));

		JLabel oldFirstVerseLabel = new JLabel("Old First Verse:");
		oldFirstVerseField = new JTextField(oldFirstVerse);
		oldFirstVerseField.setEditable(false);

		JLabel oldSecondVerseLabel = new JLabel("Old Second Verse:");
		oldSecondVerseField = new JTextField(oldSecondVerse);
		oldSecondVerseField.setEditable(false);

		JLabel newFirstVerseLabel = new JLabel("New First Verse:");
		newFirstVerseField = new JTextField();

		JLabel newSecondVerseLabel = new JLabel("New Second Verse:");
		newSecondVerseField = new JTextField();

		JButton updateButton = new JButton("Update");

		frame.add(oldFirstVerseLabel);
		frame.add(oldFirstVerseField);
		frame.add(oldSecondVerseLabel);
		frame.add(oldSecondVerseField);
		frame.add(newFirstVerseLabel);
		frame.add(newFirstVerseField);
		frame.add(newSecondVerseLabel);
		frame.add(newSecondVerseField);
		frame.add(new JLabel()); // Placeholder
		frame.add(updateButton);

		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateVerse();
				frame.dispose();
				new VersePL(bllFacade, poemId);
			}
		});

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private void updateVerse() {
		String oldFirstVerse = oldFirstVerseField.getText();
		String oldSecondVerse = oldSecondVerseField.getText();
		String newFirstVerse = newFirstVerseField.getText();
		String newSecondVerse = newSecondVerseField.getText();

		if (!newFirstVerse.isEmpty() && !newSecondVerse.isEmpty()) {
			try {
				
				bllFacade.updateVerse(poemId, oldFirstVerse, oldSecondVerse, newFirstVerse, newSecondVerse);
				LOGGER.info("Verse updated successfully.");
				JOptionPane.showMessageDialog(frame, "Verse updated successfully.");
			} catch (Exception ex) {
				LOGGER.error("Error occurred during verse update: " + ex.getMessage(), ex);
				JOptionPane.showMessageDialog(frame, "Error occurred during verse update.");
			} finally {
				frame.dispose(); // Close the window after updating
			}
		} else {
			JOptionPane.showMessageDialog(frame, "New verses cannot be empty.");
		}
	}
}
