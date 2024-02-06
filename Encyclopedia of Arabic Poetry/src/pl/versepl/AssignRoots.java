package pl.versepl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import bll.IBLLFacade;

/**
 * This class represents a window for assigning roots to a verse. It provides a
 * graphical user interface for users to select and assign roots to a verse.
 */
public class AssignRoots {

	private IBLLFacade bll;
	private int verseId;
	private JFrame frame;
	private JTextField verseTextField;
	private JComboBox<String> rootsDropdown;
	private DefaultListModel<String> selectedRootsListModel;
	private JList<String> selectedRootsList;
	private HashMap<String, String> tr;
	private JPanel selectedRootsPanel;

	/**
	 * Constructs an instance of the AssignRoots class.
	 *
	 * @param bllFacade The business logic layer facade for interacting with the
	 *                  data layer.
	 * @param verse     The ID of the verse to which roots are being assigned.
	 */
	public AssignRoots(IBLLFacade bllFacade, int verse) {
		this.bll = bllFacade;
		this.verseId = verse;
		tr = new HashMap<>();
		frame = new JFrame("Assign Roots - Verse ID: " + verseId);
		frame.setSize(400, 300);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());

		// Add verse text area at the center with margin from the top
		verseTextField = new JTextField("Verse: " + getVerseTextFromId(verseId));
		verseTextField.setEditable(false);
		verseTextField.setFont(new Font("Arial", Font.PLAIN, 16));
		verseTextField.setHorizontalAlignment(JTextField.CENTER);
		contentPanel.add(verseTextField, BorderLayout.NORTH);

		// Add a table displaying selected roots
		selectedRootsListModel = new DefaultListModel<>();
		selectedRootsList = new JList<>(selectedRootsListModel);
		JScrollPane scrollPane = new JScrollPane(selectedRootsList);
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		
		selectedRootsPanel = new JPanel();
		selectedRootsPanel.setLayout(new BoxLayout(selectedRootsPanel, BoxLayout.Y_AXIS));
		JScrollPane scrollPane1 = new JScrollPane(selectedRootsPanel);
		frame.add(scrollPane1, BorderLayout.EAST);

		
		rootsDropdown = new JComboBox<>(getrootsfromtoken());
		contentPanel.add(rootsDropdown, BorderLayout.SOUTH);

		
		frame.add(contentPanel, BorderLayout.CENTER);

		
		frame.setBackground(Color.WHITE);
		contentPanel.setBackground(Color.WHITE);
		selectedRootsPanel.setBackground(Color.WHITE);
		verseTextField.setFont(new Font("Arial", Font.BOLD, 12));
		verseTextField.setBackground(Color.LIGHT_GRAY);

		// Add item listener to JComboBox
		rootsDropdown.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					assignRoots();
				}
			}
		});

		
		JButton assignRootsButton1 = new JButton("Assign Roots");
		assignRootsButton1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					addRoots(tr, getSelectedRoots(), verseId);
				} catch (SQLException e1) {
					System.out.println("Error occurred while assigning roots.");
					e1.printStackTrace();
				}
			}
		});
		frame.add(assignRootsButton1, BorderLayout.SOUTH);

		// Center the frame on the screen
		Dimension dim = frame.getToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

		frame.setVisible(true);

		
		System.out.println("AssignRoots frame initialized for Verse ID: " + verseId);
	}

	private String getVerseTextFromId(int verseId) {
		List<String> verse = bll.getVersesFromId(verseId);

		if (verse != null && verse.size() == 2) {
			return verse.get(0) + " " + verse.get(1);
		} else {
			return "Verse not found";
		}
	}

	private String[] getrootsfromtoken() {
		ArrayList<String> tokens = bll.getTokensByVerseId(verseId);
		ArrayList<String> rootsList = new ArrayList<>();

		// Extract roots from tokens and filter based on specified conditions
		for (String token : tokens) {
			String rawRoot = net.oujda_nlp_team.AlKhalil2Analyzer.getInstance().processToken(token).getAllRootString();
			// Remove unwanted characters (:, -, space) from the rawRoot
			String cleanedRoot = rawRoot.replaceAll("[:-]", "");

			// Trim the cleaned root to remove leading and trailing spaces
			cleanedRoot = cleanedRoot.trim();

			// Check if the cleaned root is not empty and meets the condition (at most 4
			// letters)
			if (!cleanedRoot.isEmpty() && cleanedRoot.length() <= 4) {
				// Add the cleaned root to the list and map it to the token
				rootsList.add(cleanedRoot);
				tr.put(token, cleanedRoot);
			}
		}

		// Convert ArrayList to array
		String[] rootsArray = rootsList.toArray(new String[0]);
		
		for (String root:rootsArray) {System.out.print(root+"\n");}
		return rootsArray;
	}

	private void assignRoots() {
		String selectedRoot = (String) rootsDropdown.getSelectedItem();

		if (!selectedRootsListModel.contains(selectedRoot)) {
			selectedRootsListModel.addElement(selectedRoot);

			
			JPanel rootPanel = new JPanel();
			rootPanel.setBackground(Color.LIGHT_GRAY);
			rootPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

			
			JLabel rootLabel = new JLabel(selectedRoot);
			rootPanel.add(rootLabel);

			
			JButton removeButton = new JButton("X");
			removeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectedRootsListModel.removeElement(selectedRoot);
					selectedRootsPanel.remove(rootPanel);
					frame.revalidate();
					frame.repaint();
				}
			});
			rootPanel.add(removeButton);

			
			selectedRootsPanel.add(rootPanel);
			frame.revalidate();
			frame.repaint();
			// Log debug
			System.out.println("Root assigned: " + selectedRoot);
		} else {
			JOptionPane.showMessageDialog(frame, "Root already assigned.");
		}
	}

	private void addRoots(HashMap<String, String> tokens, ArrayList<String> selectedRoots, int verseId)
			throws SQLException {
		System.out.println("Entering addRoots method.");

		ArrayList<String> selectedTokens = new ArrayList<>();

		for (HashMap.Entry<String, String> entry : tokens.entrySet()) {
			String token = entry.getKey();
			String root = entry.getValue();

			if (selectedRoots.contains(root)) {
				selectedTokens.add(token);
			}
		}

		ArrayList<Integer> ids = bll.getidFromTokens(selectedTokens);

		if (ids.size() == selectedRoots.size()) {
			for (int i = 0; i < selectedRoots.size(); i++) {
				String root = selectedRoots.get(i);
				int id = ids.get(i);

				try {
					bll.addOrUpdateRoute(root, id, verseId, "verified");
					System.out.println("Root '" + root + "' assigned to Verse ID: " + verseId + " successfully.");
				} catch (SQLException e) {
					System.out.println("Error occurred while adding or updating route.");
					e.printStackTrace();
					throw e;
				}
			}

			JOptionPane.showMessageDialog(frame, "Roots assigned successfully!");
			frame.dispose();
			System.out.println("Roots assigned successfully for Verse ID: " + verseId);
		} else {
			System.out.println("Error: Number of roots and IDs do not match.");
		}

		System.out.println("Exiting addRoots method.");
	}

	private ArrayList<String> getSelectedRoots() {
		ArrayList<String> selectedRoots = new ArrayList<>();
		for (int i = 0; i < selectedRootsListModel.getSize(); i++) {
			selectedRoots.add(selectedRootsListModel.getElementAt(i));
		}
		return selectedRoots;
	}
}
