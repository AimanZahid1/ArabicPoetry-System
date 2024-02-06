package dal.poemdal.manualAdd;

public class Poem {
	private int id;
	private int bookId;
	private String poem_Title;

	public Poem(int id, int bookId, String poem_Title) {
		this.id = id;
		this.bookId = bookId;
		this.poem_Title = poem_Title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public void setPoemTitle(String poem_Title) {
		this.poem_Title = poem_Title;
	}

	public int getBookId() {
		return bookId;
	}

	public String getPoemTitle() {
		return poem_Title;
	}
}