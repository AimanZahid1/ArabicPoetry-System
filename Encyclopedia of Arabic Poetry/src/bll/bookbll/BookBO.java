package bll.bookbll;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dal.IDALFacade;

/**
 * Custom exception class extending IllegalArgumentException. Used for specific
 * exceptions related to book operations.
 */
class CustomIllegalArgumentException extends IllegalArgumentException {

	/**
	 * Constructs a CustomIllegalArgumentException with the specified detail
	 * message.
	 *
	 * @param message The detail message (which is saved for later retrieval by the
	 *                getMessage() method)
	 */
	public CustomIllegalArgumentException(String message) {
		super(message);
	}
}

/**
 * The BookBO class implements the IBookBO interface and handles book-related
 * operations.
 */
public class BookBO {

	private IDALFacade dalFacade;
	private static final Logger logger = LogManager.getLogger(BookBO.class);

	/**
	 * Constructs a BookBO object with the specified IDALFacade instance.
	 *
	 * @param dalFacade The data access facade instance to interact with the data
	 *                  layer.
	 */
	public BookBO(IDALFacade dalFacade) {
		this.dalFacade = dalFacade;
	}

	/**
	 * Retrieves a list of books.
	 *
	 * @return A list of books retrieved from the data layer.
	 */
	public List<dal.bookdal.Book> selectBooks() {
		try {
			return dalFacade.selectBooks();
		} catch (Exception e) {
			logger.error("Error selecting books", e);
			return null;
		}
	}

	public int getBookIdByTitle(String bookTitle) {
		try {
			return dalFacade.getBookIdByTitle(bookTitle);
		} catch (Exception e) {

			e.printStackTrace();

			return -1;
		}
	}

	/**
	 * Updates book details.
	 *
	 * @param title       The title of the book.
	 * @param author      The author of the book.
	 * @param publishDate The publish date of the book.
	 * @param deathDate   The death date of the author (if applicable).
	 * @throws CustomIllegalArgumentException if an error occurs during book update
	 *                                        or if input parameters are invalid.
	 */
	public void updateBook(String title, String author, Date publishDate, Date deathDate)
			throws CustomIllegalArgumentException {
		if (title != null) {
			try {
				dalFacade.updateBook(title, author, publishDate, deathDate);
				logger.info("Book updated successfully: {}", title);
			} catch (Exception e) {
				logger.error("Error updating book: {}", title, e);
				throw new CustomIllegalArgumentException("Error updating book: " + e.getMessage());
			}
		} else {
			throw new CustomIllegalArgumentException("Invalid input for updating a book.");
		}
	}

	/**
	 * Deletes a book.
	 *
	 * @param title The title of the book to be deleted.
	 * @throws CustomIllegalArgumentException if an error occurs during book
	 *                                        deletion or if the title is not
	 *                                        provided.
	 */
	public void deleteBook(String title) throws CustomIllegalArgumentException {
		if (title != null) {
			try {
				dalFacade.deleteBook(title);
				logger.info("Book deleted successfully: {}", title);
			} catch (Exception e) {
				logger.error("Error deleting book: {}", title, e);
				throw new CustomIllegalArgumentException("Error deleting book: " + e.getMessage());
			}
		} else {
			throw new CustomIllegalArgumentException("Title must be provided for deleting a book.");
		}
	}

	/**
	 * Inserts a new book.
	 *
	 * @param title       The title of the new book.
	 * @param author      The author of the new book.
	 * @param publishDate The publish date of the new book.
	 * @param deathDate   The death date of the author (if applicable) of the new
	 *                    book.
	 * @throws CustomIllegalArgumentException if an error occurs during book
	 *                                        insertion or if input parameters are
	 *                                        invalid.
	 */
	public void insertBook(String title, String author, Date publishDate, Date deathDate)
			throws CustomIllegalArgumentException {
		if (title != null) {
			try {
				dalFacade.insertBook(title, author, publishDate, deathDate);
				logger.info("Book inserted successfully: {}", title);
			} catch (Exception e) {
				logger.error("Error inserting book: {}", title, e);
				throw new CustomIllegalArgumentException("Error inserting book: " + e.getMessage());
			}
		} else {
			throw new CustomIllegalArgumentException("Invalid input for inserting a book.");
		}
	}

}