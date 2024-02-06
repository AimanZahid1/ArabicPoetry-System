package pl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import bll.BLLFacade;
import bll.IBLLFacade;
import dal.DALFacade;
import dal.IDALFacade;
import dal.Tokenize.ITokenizeDAO;
import dal.Tokenize.TokenizeDAO;
import dal.bookdal.BookDAO;
import dal.bookdal.IBookDAO;
import dal.poemdal.importPoem.IParsePoemDAO;
import dal.poemdal.importPoem.ParsePoemDAO;
import dal.poemdal.manualAdd.IPoemDAO;
import dal.poemdal.manualAdd.PoemDAO;
import dal.rootdal.IRootDao;
import dal.rootdal.RootDAO;
import dal.versedal.IVerseDAO;
import dal.versedal.VerseDAO;
import pl.Poempl.PoemPL;
import pl.bookpl.BookPL;
import pl.rootpl.RootPL;

public class EncyclopediaofArabicPoetryGUI {
    private IBLLFacade bllFacade;

    public EncyclopediaofArabicPoetryGUI(IBLLFacade bllFacade) {
        this.bllFacade = bllFacade;

        // Set the look and feel to Nimbus (if available)
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create the main frame
        JFrame mainFrame = new JFrame("Encyclopedia of Arabic Poetry");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.getContentPane().setBackground(new Color(46, 52, 58));

        // Set the layout manager to GridBagLayout
        mainFrame.setLayout(new GridBagLayout());

        // Create GridBagConstraints for button placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Set default button size
        Dimension buttonSize = new Dimension(200, 50);

        JButton bookButton = new JButton("Manage Books");
        customizeButton(bookButton, buttonSize);
        bookButton.addActionListener(e -> new BookPL(bllFacade));

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainFrame.add(bookButton, gbc);

        JButton rootButton = new JButton("Manage Roots");
        customizeButton(rootButton, buttonSize);
        rootButton.addActionListener(e -> new RootPL(bllFacade).setVisible(true));

        gbc.gridx = 1;
        gbc.gridy = 0;
        mainFrame.add(rootButton, gbc);

        JButton poemButton = new JButton("Manage Poems");
        customizeButton(poemButton, buttonSize);
        poemButton.addActionListener(e -> new PoemPL(bllFacade).setVisible(true));

        gbc.gridx = 2;
        gbc.gridy = 0;
        mainFrame.add(poemButton, gbc);

        // Set buttons color to white
        bookButton.setForeground(Color.WHITE);
        rootButton.setForeground(Color.WHITE);
        poemButton.setForeground(Color.WHITE);

        mainFrame.setSize(500, 500);
        mainFrame.setVisible(true);
    }

    private void customizeButton(JButton button, Dimension size) {
        button.setPreferredSize(size);
        button.setBackground(new Color(28, 32, 36));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
    }

    public static void main(String[] args) {
        IRootDao root = new RootDAO();
        IBookDAO book = new BookDAO();
        IPoemDAO poemDAO = new PoemDAO();
        IParsePoemDAO parsepoem = new ParsePoemDAO();
        IVerseDAO verseDAO = new VerseDAO();
        ITokenizeDAO token = new TokenizeDAO();
        IDALFacade dalFacade = new DALFacade(root, parsepoem, book, poemDAO, verseDAO, token);
        IBLLFacade bllFacade = new BLLFacade(dalFacade);
        SwingUtilities.invokeLater(() -> new EncyclopediaofArabicPoetryGUI(bllFacade));
    }
}
