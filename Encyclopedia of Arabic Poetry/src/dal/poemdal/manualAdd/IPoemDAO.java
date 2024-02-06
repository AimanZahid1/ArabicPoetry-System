package dal.poemdal.manualAdd;

import java.util.List;

public interface IPoemDAO {
	public List<String> viewPoemsByBook(int bookId);

	int getPoemIdByTitle(int bookId, String poemTitle);

	public void addPoem(int bookId, String poemTitle);

	public void updatePoem(int bookId, String previousTitle, String newTitle);

	public void deletePoem(int bookId, String poemTitle);

	public String getBookTitleByPoemId(int poemId);

	public int PoemIdByTitle(String poemTitle);

}
