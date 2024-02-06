package stub;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import dal.bookdal.Book;
import dal.bookdal.IBookDAO;

public class BookStub implements  IBookDAO {

    private HashMap<Integer, Book> bookData = new HashMap<>();

    public BookStub() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date publishDate = dateFormat.parse("2022-01-01");
        Date deathDate = dateFormat.parse("2023-12-31");

        createSampleBook("عندما أصبحنا أحرارًا", "أحمد مطر", "2022-02-15", "2023-10-31");
        createSampleBook("القاتلة الطائرة", "هاربر لي", "2021-10-05", "2024-05-15");
        createSampleBook("1984", "جورج أورويل", "2023-05-20", "2025-12-01");
        createSampleBook("فخر وتحامل", "جين أوستن", "2020-12-10", "2022-06-30");
        createSampleBook("الصياد في الشوفان", "جي. د. سالينجر", "2022-06-01", "2024-03-12");
        createSampleBook("سيد الخواتم", "ج. ر. ر. تولكين", "2023-08-25", "2027-02-28");
        createSampleBook("هاري بوتر وحجر الساحر", "ج. ك. رولينج", "2021-03-15", "2024-11-20");
        createSampleBook("الهوبيت", "ج. ر. ر. تولكين", "2022-12-05", "2026-09-18");
        createSampleBook("عالم جديد شجاع", "ألدوس هكسلي", "2023-04-30", "2025-11-10");
        createSampleBook("جريمة وعقوبة", "فيودور دوستويفسكي", "2022-09-12", "2024-08-02");
    }

    private void createSampleBook(String title, String author, String publishDateStr, String deathDateStr)
            throws ParseException {
        Date publishDate = new SimpleDateFormat("yyyy-MM-dd").parse(publishDateStr);
        Date deathDate = new SimpleDateFormat("yyyy-MM-dd").parse(deathDateStr);
        Book book = new Book(title, author, publishDate, deathDate);
        int id = generateUniqueId();
        
        bookData.put(id, book);
    }

    private int generateUniqueId() {
        return bookData.size() + 1;
    }

    @Override
    public List<Book> selectBooks() {
        return new java.util.ArrayList<>(bookData.values());
    }

    @Override
    public int getBookIdByTitle(String bookTitle) {
        for (java.util.Map.Entry<Integer, Book> entry : bookData.entrySet()) {
            if (entry.getValue().getTitle().equalsIgnoreCase(bookTitle)) {
                return entry.getKey();
            }
        }
        return -1; // Return -1 if not found
    }

    @Override
    public void updateBook(String title, String author, Date publishDate, Date deathDate) {
        for (java.util.Map.Entry<Integer, Book> entry : bookData.entrySet()) {
            if (entry.getValue().getTitle().equalsIgnoreCase(title)) {
                Book book = entry.getValue();
                book.setTitle(title);
                book.setAuthor(author);
                book.setPublishDate(publishDate);
                book.setDeathDate(deathDate);
                break; // Assuming titles are unique, exit loop after finding the book
            }
        }
    }

    @Override
    public void deleteBook(String title) {
        // Remove the book with the given title
        bookData.entrySet().removeIf(entry -> entry.getValue().getTitle().equalsIgnoreCase(title));
    }

    @Override
    public void insertBook(String title, String author, Date publishDate, Date deathDate) {
        // Creating a new book
        Book newBook = new Book(title, author, publishDate, deathDate);

        // Generating a unique ID
        int id = generateUniqueId();

        // Inserting the new book into the HashMap
        bookData.put(id, newBook);
    }
}
