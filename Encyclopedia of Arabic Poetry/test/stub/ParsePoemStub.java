package stub;

import dal.poemdal.importPoem.IParsePoemDAO;

public class ParsePoemStub implements IParsePoemDAO {

	public ParsePoemStub() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean getPoemsData(String filePath, String bookName) {
	    return filePath != null && !filePath.isEmpty() && bookName != null && !bookName.isEmpty();
	}
}
