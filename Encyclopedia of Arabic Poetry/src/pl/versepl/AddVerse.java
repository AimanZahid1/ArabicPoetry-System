package pl.versepl;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bll.IBLLFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class represents a window for adding verses to a poem. It provides a
 * graphical user interface for users to input and submit verses.
 */
public class AddVerse {

	private static final Logger logger = LogManager.getLogger(AddVerse.class);

	private List<String[]> versesList;
	private JFrame frame;
	private JPanel versePanel;
	private JButton plusButton;
	private IBLLFacade bllFacade;
	private int poemId;

	/**
	 * Constructs an instance of the AddVerse class.
	 *
	 * @param bll    The business logic layer facade for interacting with the data
	 *               layer.
	 * @param poemId The ID of the poem to which verses are being added.
	 */
	public AddVerse(IBLLFacade bll, int poemId) {
		this.bllFacade = bll;
		this.poemId = poemId;
		versesList = new ArrayList<>();

		frame = new JFrame("Add Verses - Poem ID: " + poemId);
		frame.setSize(400, 200);
		frame.setLayout(new BorderLayout());

		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

		plusButton = new JButton("+");
		buttonsPanel.add(plusButton);
		JButton addButton = new JButton("Add");
		buttonsPanel.add(addButton);

		frame.add(buttonsPanel, BorderLayout.NORTH);

		versePanel = new JPanel(new GridBagLayout());
		addVerseFields();
		frame.add(versePanel, BorderLayout.CENTER);

		plusButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addVerseFields();
				frame.revalidate();
				frame.repaint();
			}
		});

		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addVerse();
				for (String[] verse : versesList) {
					String firstVerse = verse[0];
					String secondVerse = verse[1];
					try {
						StringTokenizer tokenizer = new StringTokenizer(firstVerse + " " + secondVerse);
						StringBuilder tokenizedVerses = new StringBuilder("");

						ArrayList<String> tokens = new ArrayList<>();

						while (tokenizer.hasMoreTokens()) {
							String token = tokenizer.nextToken();
							tokenizedVerses.append(token).append("\n");
							tokens.add(token);
						}

						bllFacade.addVerse(poemId, firstVerse, secondVerse);
						int verseId = bllFacade.getVerseIdFromTitle(firstVerse, secondVerse);
						if (verseId != -1) {
							bllFacade.insertToken(tokens, verseId);

							ArrayList<Integer> ids = bllFacade.getidFromTokens(tokens);

							int i = 0;
							for (String token : tokens) {
								String rawRoot = net.oujda_nlp_team.AlKhalil2Analyzer.getInstance().processToken(token)
										.getAllRootString();
								String cleanedRoot = rawRoot.replaceAll("[:-]", "");

								cleanedRoot = cleanedRoot.trim();
								int id = ids.get(i);
								System.out.print(cleanedRoot);
								bll.addRoute(cleanedRoot, id, verseId, "Not verified");
								i = i + 1;
							}

						} else {
							logger.error("Verse ID not found for title: " + firstVerse + ", " + secondVerse);
						}

					} catch (SQLException e1) {
						logger.error("Error occurred while adding verses.", e1);
					}
				}

				versesList.clear();
				frame.dispose();
				new VersePL(bllFacade, poemId);
			}
		});

		// Open the frame in full screen
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);

		// Add a WindowListener to handle the window closing event
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				// Handle closing event as needed (dispose, exit application, etc.)
				frame.dispose();
			}
		});

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);

		logger.info("AddVerse frame initialized for Poem ID: " + poemId);
	}

	private void addVerseFields() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = versePanel.getComponentCount() / 2; // Two fields per row
		gbc.insets = new Insets(5, 5, 5, 5);

		JTextField firstVerseField = new JTextField(20);
		JTextField secondVerseField = new JTextField(20);

		versePanel.add(firstVerseField, gbc);
		gbc.gridx++;
		versePanel.add(secondVerseField, gbc);
	}

	private void addVerse() {
		List<String[]> currentVerses = new ArrayList<>();

		// Iterate through the versePanel to retrieve the text from each pair of fields
		for (int i = 0; i < versePanel.getComponentCount(); i += 2) {
			JTextField firstVerseField = (JTextField) versePanel.getComponent(i);
			JTextField secondVerseField = (JTextField) versePanel.getComponent(i + 1);

			String firstVerse = firstVerseField.getText();
			String secondVerse = secondVerseField.getText();

			if (!firstVerse.isEmpty() && !secondVerse.isEmpty()) {
				currentVerses.add(new String[] { firstVerse, secondVerse });
			} else {
				JOptionPane.showMessageDialog(frame, "Both verse fields must be filled.");
				logger.warn("Both verse fields must be filled.");
				return;
			}
		}

		// Store the current verses in the list
		versesList.addAll(currentVerses);

		addVerseFields();
		frame.revalidate();
		frame.repaint();
		logger.debug("Added verse fields.");
	}

	/**
	 * Gets the list of verses added through the AddVerse window.
	 *
	 * @return The list of verses, where each verse is represented as an array of
	 *         strings.
	 */
	public List<String[]> getVersesList() {
		return versesList;
	}
}
