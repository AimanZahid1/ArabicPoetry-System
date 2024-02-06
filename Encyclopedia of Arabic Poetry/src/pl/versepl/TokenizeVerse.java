package pl.versepl;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bll.IBLLFacade;

/**
 * This class represents a window for tokenizing and displaying verses. It
 * provides a graphical user interface for users to split verses into tokens and
 * save them to a database.
 */
public class TokenizeVerse {

    private static final Logger LOGGER = LogManager.getLogger(TokenizeVerse.class);

    private JFrame frame;
    private JTextField firstVerseField;
    private JTextField secondVerseField;
    private JTextArea splitsTextArea;
    private IBLLFacade bllFacade;
    private String selectedFirstVerse;
    private String selectedSecondVerse;
    private int poemid;

    /**
     * Constructs an instance of the TokenizeVerse class.
     *
     * @param bllFacade           The business logic layer facade for interacting
     *                            with the data layer.
     * @param selectedFirstVerse  The selected first verse.
     * @param selectedSecondVerse The selected second verse.
     * @param poemid              The ID of the poem.
     */
    public TokenizeVerse(IBLLFacade bllFacade, String selectedFirstVerse, String selectedSecondVerse, int poemid) {
        this.poemid = poemid;
        this.bllFacade = bllFacade;
        this.selectedFirstVerse = selectedFirstVerse;
        this.selectedSecondVerse = selectedSecondVerse;

        frame = new JFrame("Tokenize Verse");
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        JLabel firstVerseLabel = new JLabel("First Verse:");
        firstVerseField = new JTextField(20);
        JLabel secondVerseLabel = new JLabel("Second Verse:");
        secondVerseField = new JTextField(20);

        // Set the selected verses as the default text in the text fields
        firstVerseField.setText(selectedFirstVerse);
        secondVerseField.setText(selectedSecondVerse);

        inputPanel.add(firstVerseLabel);
        inputPanel.add(firstVerseField);
        inputPanel.add(secondVerseLabel);
        inputPanel.add(secondVerseField);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(splitPane, BorderLayout.CENTER);

        JButton splitButton = new JButton("Split Verses");
        JButton saveWordsButton = new JButton("Save Words");

        splitsTextArea = new JTextArea();
        splitsTextArea.setEditable(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(splitButton);
        buttonPanel.add(saveWordsButton);

        splitPane.setTopComponent(buttonPanel);
        splitPane.setBottomComponent(splitsTextArea);

        splitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tokenizeAndDisplay();
            }
        });

        saveWordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveWordsToDatabase();
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void tokenizeAndDisplay() {
        String firstVerse = firstVerseField.getText();
        String secondVerse = secondVerseField.getText();

        try {
            StringTokenizer tokenizer = new StringTokenizer(firstVerse + " " + secondVerse);
            StringBuilder tokenizedVerses = new StringBuilder("");

            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                System.out.print(token);
                tokenizedVerses.append(token).append("\n");
            }

            splitsTextArea.setText(tokenizedVerses.toString());

            // Update the verse in the database with the tokenized content
            String tokenizedContent = tokenizedVerses.toString().trim();
            bllFacade.updateVerse(poemid, selectedFirstVerse, selectedSecondVerse, firstVerse, secondVerse);
            selectedFirstVerse = firstVerse;
            selectedSecondVerse = secondVerse;
            System.out.print(selectedFirstVerse + selectedSecondVerse);
            LOGGER.info("Verse updated successfully.");
        } catch (Exception e) {
            LOGGER.error("Error occurred during tokenization: " + e.getMessage(), e);
        }
    }

    private void saveWordsToDatabase() {
        try {
            StringTokenizer tokenizer = new StringTokenizer(splitsTextArea.getText());
            ArrayList<String> tokens = new ArrayList<>();

            while (tokenizer.hasMoreTokens()) {
                tokens.add(tokenizer.nextToken());
            }

            int verseId = bllFacade.getVerseIdFromTitle(selectedFirstVerse, selectedSecondVerse);
            if (verseId != -1) {
                bllFacade.insertToken(tokens, verseId);
                frame.dispose();
                new VersePL(bllFacade, poemid);

                LOGGER.info("Tokens inserted successfully!");
            } else {
                LOGGER.error("Verse ID not found for title: " + selectedFirstVerse + ", " + selectedSecondVerse);
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred during saving words to the database: " + e.getMessage(), e);
        }
    }
}
