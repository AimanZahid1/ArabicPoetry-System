package dal.poemdal.importPoem;

import java.util.ArrayList;

import dal.poemdal.importPoem.ParsePoemDAO.Poems;

public interface IParsePoemDAO {
	public boolean getPoemsData(String filePath, String bookName);
}
