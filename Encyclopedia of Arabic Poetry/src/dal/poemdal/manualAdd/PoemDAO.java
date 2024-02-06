package dal.poemdal.manualAdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dal.ConnectionClass;

public class PoemDAO implements IPoemDAO {
	private static final Logger LOGGER = LogManager.getLogger(PoemDAO.class);

	private Connection connection;

	public PoemDAO() {
		connection = ConnectionClass.getInstance().getConnection();
	}

	public void updatePoem(int bookId, String previousTitle, String newTitle) {
		String sql = "UPDATE poem SET poem_Title = '" + newTitle + "' WHERE book_id = " + bookId + " AND poem_Title = '"
				+ previousTitle + "'";

		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			LOGGER.error("Error occurred while updating poem: " + e.getMessage(), e);
		}
	}

	public String getBookTitleByPoemId(int poemId) {
		String bookTitle = null; 
		String sql = "SELECT b.book_Name " + "FROM poem p " + "JOIN book b ON p.book_id = b.id " + "WHERE p.id = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, poemId);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				bookTitle = resultSet.getString("book_Name");
			}
		} catch (SQLException e) {
			LOGGER.error("Error occurred while getting book title by poem ID: " + e.getMessage(), e);
		}

		return bookTitle;
	}

	public int getPoemIdByTitle(int bookId, String poemTitle) {
		int poemId = -1; // Default value if not found

		// Sanitize and escape the input
		String sanitizedTitle = poemTitle.replace("'", "''");

		try (Statement statement = connection.createStatement()) {
		
			String sql = "SELECT id FROM poem WHERE book_id = " + bookId + " AND poem_Title = '" + sanitizedTitle + "'";

			ResultSet resultSet = statement.executeQuery(sql);

			if (resultSet.next()) {
				poemId = resultSet.getInt("id");
			}
		} catch (SQLException e) {
			LOGGER.error("Error occurred while getting poem ID by title: " + e.getMessage(), e);
		}

		return poemId;
	}

	public int PoemIdByTitle(String poemTitle) {
		int poemId = -1; // Default value if not found

		// Sanitize and escape the input
		String sanitizedTitle = poemTitle.replace("'", "''");

		try (PreparedStatement statement = connection.prepareStatement("SELECT id FROM poem WHERE poem_Title = ?")) {
			statement.setString(1, sanitizedTitle);

			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				poemId = resultSet.getInt("id");
			}
		} catch (SQLException e) {
			LOGGER.error("Error occurred while getting poem ID by title: " + e.getMessage(), e);
		}

		return poemId;
	}

	public List<String> viewPoemsByBook(int bookId) {
		List<String> poems = new ArrayList<>();
		String sql = "SELECT poem_Title FROM poem WHERE book_id = " + bookId;
		try (Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				poems.add(resultSet.getString("poem_Title"));
			}
		} catch (SQLException e) {
			LOGGER.error("Error occurred while viewing poems by book: " + e.getMessage(), e);
		}
		return poems;
	}


	public void addPoem(int bookId, String poemTitle) {
		String sql = "INSERT INTO poem ( book_id,poem_Title) VALUES (" + bookId + ", '" + poemTitle + "')";
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			LOGGER.error("Error occurred while adding poem: " + e.getMessage(), e);
		}
	}

	public void deletePoem(int bookId, String poemTitle) {
		// First, retrieve the poem ID
		int poemId = getPoemIdByTitle(bookId, poemTitle);

		// Check if the poem exists
		if (poemId != -1) {
			// Delete associated verses first
			deleteVersesForPoem(poemId);

			// Then, delete the poem
			String sql = "DELETE FROM poem WHERE book_id = " + bookId + " AND poem_Title = '" + poemTitle + "'";
			try (Statement statement = connection.createStatement()) {
				statement.executeUpdate(sql);
			} catch (SQLException e) {
				LOGGER.error("Error occurred while deleting poem: " + e.getMessage(), e);
			}
		}
	}

	private void deleteVersesForPoem(int poemId) {
		String sql = "DELETE FROM verses WHERE poem_id = " + poemId;
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			LOGGER.error("Error occurred while deleting verses for poem: " + e.getMessage(), e);
		}
	}
}
