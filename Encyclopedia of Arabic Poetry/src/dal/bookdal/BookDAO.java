package dal.bookdal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dal.ConnectionClass;

public class BookDAO implements IBookDAO {
	private static final Logger LOGGER = LogManager.getLogger(BookDAO.class);

	private Connection connection;

	public BookDAO() {
		connection = ConnectionClass.getInstance().getConnection();
	}

	public List<Book> selectBooks() {
		List<Book> books = new ArrayList<>();

		try {
			String sql = "SELECT book_Name, authorName, publishDate, authorDeathDate FROM book";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				String title = resultSet.getString("book_Name");
				String author = resultSet.getString("authorName");
				// Parse dates accordingly
				Date publishDate = resultSet.getDate("publishDate");
				Date deathDate = resultSet.getDate("authorDeathDate");

				Book book = new Book(title, author, publishDate, deathDate);
				books.add(book);
			}
		} catch (SQLException e) {
			LOGGER.error("Error occurred while selecting books: " + e.getMessage(), e);
		}

		return books;
	}

	public int getBookIdByTitle(String title) {
		int bookId = -1;
		try {
			String sql = "SELECT id FROM book WHERE book_Name = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, title);

			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				bookId = resultSet.getInt("id");
			}
		} catch (SQLException e) {
			LOGGER.error("Error occurred while getting book ID by title: " + e.getMessage(), e);
		}
		return bookId;
	}

	public void updateBook(String title, String author, Date publishDate, Date deathDate) {
		try {
			String sql = "UPDATE book SET authorName = '" + author + "', publishDate = '"
					+ new java.sql.Date(publishDate.getTime()) + "', authorDeathDate = '"
					+ new java.sql.Date(deathDate.getTime()) + "' WHERE book_Name = '" + title + "'";
			Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			LOGGER.error("Error occurred while updating book: " + e.getMessage(), e);
		}
	}

	public void deleteBook(String title) {
		try {
			String sql = "DELETE FROM book WHERE book_Name = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, title);
			statement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Error occurred while deleting book: " + e.getMessage(), e);
		}
	}

	public void insertBook(String title, String author, Date publishDate, Date deathDate) {
		try {
			String sql = "INSERT INTO book (book_Name, authorName, publishDate, authorDeathDate) VALUES ('" + title
					+ "', '" + author + "', '" + new java.sql.Date(publishDate.getTime()) + "', '"
					+ new java.sql.Date(deathDate.getTime()) + "')";
			Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			LOGGER.error("Error occurred while inserting book: " + e.getMessage(), e);
		}
	}
}