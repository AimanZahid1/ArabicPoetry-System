package dal.versedal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dal.ConnectionClass;

/**
 * The VerseDAO class implements the IVerseDAO interface and provides methods to
 * interact with the data access layer for managing verses in the Encyclopedia
 * of Arabic Poetry application.
 */
public class VerseDAO implements IVerseDAO {
	private static final Logger LOGGER = LogManager.getLogger(VerseDAO.class);

	Connection connection;

	/**
	 * Constructor for creating an instance of VerseDAO. Initializes the database
	 * connection.
	 */
	public VerseDAO() {
		connection = ConnectionClass.getInstance().getConnection();
	}

	public void addVerse(int poemId, String firstVerse, String secondVerse) {
		try (PreparedStatement pStatement = connection
				.prepareStatement("INSERT INTO verses (poem_id, firstVerse, secondVerse) VALUES (?, ?, ?)")) {
			pStatement.setInt(1, poemId);
			pStatement.setString(2, firstVerse);
			pStatement.setString(3, secondVerse);
			pStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Error occurred while adding verse: " + e.getMessage(), e);
		}
	}

	public void updateVerse(int poemId, String firstVerse, String secondVerse, String firstUpdated,
			String secondUpdated) {
		try (PreparedStatement pStatement = connection.prepareStatement(
				"UPDATE verses SET firstVerse = ?, secondVerse = ? WHERE poem_id = ? AND firstVerse = ? AND secondVerse = ?")) {
			pStatement.setString(1, firstUpdated);
			pStatement.setString(2, secondUpdated);
			pStatement.setInt(3, poemId);
			pStatement.setString(4, firstVerse);
			pStatement.setString(5, secondVerse);
			pStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Error occurred while updating verse: " + e.getMessage(), e);
		}
	}

	public void deleteVerse(int poemId, String firstVerse, String secondVerse) {
		try (PreparedStatement pStatement = connection
				.prepareStatement("DELETE FROM verses WHERE poem_id = ? AND firstVerse = ? AND secondVerse = ?")) {
			pStatement.setInt(1, poemId);
			pStatement.setString(2, firstVerse);
			pStatement.setString(3, secondVerse);

			int rowsAffected = pStatement.executeUpdate();

			if (rowsAffected > 0) {
				LOGGER.info("Verse deleted successfully.");
			} else {
				LOGGER.warn("Verse not found in the database.");
			}
		} catch (SQLException e) {
			LOGGER.error("Error occurred while deleting verse: " + e.getMessage(), e);
		}
	}

	public List<String[]> getVersesByPoem(int poemId) {
		List<String[]> verses = new ArrayList<>();
		try (PreparedStatement pStatement = connection
				.prepareStatement("SELECT firstVerse, secondVerse FROM verses WHERE poem_id = ?")) {
			pStatement.setInt(1, poemId);
			ResultSet resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				String firstVerse = resultSet.getString("firstVerse");
				String secondVerse = resultSet.getString("secondVerse");
				verses.add(new String[] { firstVerse, secondVerse });
			}
		} catch (SQLException e) {
			LOGGER.error("Error occurred while retrieving verses: " + e.getMessage(), e);
		}
		return verses;
	}

	public int getVerseIdFromTitle(String firstVerse, String secondVerse) {
		int verseId = -1; // Default value if not found
		try (PreparedStatement pStatement = connection
				.prepareStatement("SELECT id FROM verses WHERE firstVerse = ? AND secondVerse = ?")) {
			pStatement.setString(1, firstVerse);
			pStatement.setString(2, secondVerse);
			ResultSet resultSet = pStatement.executeQuery();
			if (resultSet.next()) {
				verseId = resultSet.getInt("id");
			}
		} catch (SQLException e) {
			LOGGER.error("Error occurred while getting verse ID: " + e.getMessage(), e);
		}
		return verseId;
	}

	public List<String> getVersesFromId(int verseId) {
		List<String> verses = new ArrayList<>();

		try (PreparedStatement pStatement = connection
				.prepareStatement("SELECT firstVerse, secondVerse FROM verses WHERE id = ?")) {

			pStatement.setInt(1, verseId);
			ResultSet resultSet = pStatement.executeQuery();

			if (resultSet.next()) {
				String firstVerse = resultSet.getString("firstVerse");
				String secondVerse = resultSet.getString("secondVerse");

				verses.add(firstVerse);
				verses.add(secondVerse);
			} else {
				// Verse ID not found
				return null;
			}
		} catch (SQLException e) {
			LOGGER.error("Error occurred while getting verses from ID: " + e.getMessage(), e);
		}

		return verses;
	}

	public List<String[]> getVersesByRoot(int rootId) {
		List<String[]> versesList = new ArrayList<>();

		try (PreparedStatement pStatement = connection
				.prepareStatement("SELECT v1.firstVerse AS first_verse, v1.secondVerse AS second_verse, p.poem_Title "
						+ "FROM rootsofverses rov " + "JOIN Verses v1 ON rov.verseId = v1.id "
						+ "JOIN poem p ON v1.poem_id = p.id " + "WHERE rov.root_id = ?")) {

			pStatement.setInt(1, rootId);

			try (ResultSet resultSet = pStatement.executeQuery()) {
				while (resultSet.next()) {
					String[] verseData = new String[3];
					verseData[0] = resultSet.getString("first_verse");
					verseData[1] = resultSet.getString("second_verse");
					verseData[2] = resultSet.getString("poem_Title");
					versesList.add(verseData);
				}
			}

		} catch (SQLException e) {
			LOGGER.error("Error occurred while getting verses by root: " + e.getMessage(), e);
		}

		return versesList;
	}
}
