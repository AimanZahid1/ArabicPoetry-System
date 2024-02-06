package dal.bookdal;

import java.util.Date;
import java.util.List;

public interface IBookDAO {

	public void updateBook(String title, String author, Date publishDate, Date deathDate);

	public void deleteBook(String title);

	void insertBook(String title, String author, Date publishDate, Date deathDate);

	public List<Book> selectBooks();

	public int getBookIdByTitle(String bookTitle);

}
