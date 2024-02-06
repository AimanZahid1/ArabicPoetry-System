package dal.bookdal;

import java.util.Date;

public class Book {
	private String title;
	private String author;
	private Date publishDate;
	private Date deathDate;

	public Book(String title, String author, Date publishDate, Date deathDate) {
		this.title = title;
		this.author = author;
		this.publishDate = publishDate;
		this.deathDate = deathDate;
	}

	public void setTitle(String title) {
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public Date getDeathDate() {
		return deathDate;
	}
}