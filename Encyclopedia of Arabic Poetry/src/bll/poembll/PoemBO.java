package bll.poembll;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dal.IDALFacade;

class CustomIllegalArgumentException extends IllegalArgumentException {
	public CustomIllegalArgumentException(String message) {
		super(message);
	}
}

public class PoemBO {
	private static final Logger LOGGER = LogManager.getLogger(PoemBO.class);

	private IDALFacade dalFacade;

	public PoemBO(IDALFacade dalFacade) {
		this.dalFacade = dalFacade;
	}

	public void updatePoem(int bookId, String previousTitle, String newTitle) {
		try {
			if (bookId > 0 && newTitle != null) {
				LOGGER.debug("Updating poem with bookId: {}, previousTitle: {}, newTitle: {}", bookId, previousTitle,
						newTitle);
				dalFacade.updatePoem(bookId, previousTitle, newTitle);
			} else {
				throw new CustomIllegalArgumentException("Invalid input for updating a poem.");
			}
		} catch (CustomIllegalArgumentException e) {
			LOGGER.error("CustomIllegalArgumentException: {}", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.error("Exception: {}", e.getMessage());
			e.printStackTrace();
		}
	}

	public List<String> viewPoemsByBook(int bookId) {
		try {
			LOGGER.debug("Viewing poems by bookId: {}", bookId);
			List<String> poems = dalFacade.viewPoemsByBook(bookId);
			return poems;
		} catch (Exception e) {
			LOGGER.error("Exception: {}", e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public int getPoemIdByTitle(int bookId, String poemTitle) {
		LOGGER.debug("Getting poem ID by bookId: {} and poemTitle: {}", bookId, poemTitle);
		return dalFacade.getPoemIdByTitle(bookId, poemTitle);
	}

	public void addPoem(int bookId, String poemTitle) {
		try {
			if (bookId > 0 && poemTitle != null) {
				LOGGER.debug("Adding poem with bookId: {}, poemTitle: {}", bookId, poemTitle);
				dalFacade.addPoem(bookId, poemTitle);
			} else {
				throw new CustomIllegalArgumentException("Invalid input for adding a poem.");
			}
		} catch (CustomIllegalArgumentException e) {
			LOGGER.error("CustomIllegalArgumentException: {}", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.error("Exception: {}", e.getMessage());
			e.printStackTrace();
		}
	}

	public void deletePoem(int bookId, String poemTitle) {
		try {
			if (bookId > 0) {
				LOGGER.debug("Deleting poem with bookId: {}, poemTitle: {}", bookId, poemTitle);
				dalFacade.deletePoem(bookId, poemTitle);
			} else {
				throw new CustomIllegalArgumentException("Poem ID must be provided for deleting a poem.");
			}
		} catch (CustomIllegalArgumentException e) {
			LOGGER.error("CustomIllegalArgumentException: {}", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.error("Exception: {}", e.getMessage());
			e.printStackTrace();
		}
	}

	public String getBookTitleByPoemTitle(int poemTitle) {
		try {
			LOGGER.debug("Getting book title by poem title: {}", poemTitle);
			return dalFacade.getBookTitleByPoemId(poemTitle);
		} catch (Exception e) {
			LOGGER.error("Error getting book title by poem title: {}", poemTitle, e);
			return null;
		}
	}

	public int PoemIdByTitle(String poemTitle) {
		try {
			LOGGER.debug("Getting poem ID by title: {}", poemTitle);
			return dalFacade.PoemIdByTitle(poemTitle);
		} catch (Exception e) {
			LOGGER.error("Error getting poem ID by title: {}", poemTitle, e);
			return -1;
		}
	}

}
