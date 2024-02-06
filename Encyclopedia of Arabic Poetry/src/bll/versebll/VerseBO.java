package bll.versebll;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dal.IDALFacade;

class CustomIllegalArgumentException extends IllegalArgumentException {

	public CustomIllegalArgumentException(String message) {
		super(message);
	}
}

public class VerseBO {
	private static final Logger LOGGER = LogManager.getLogger(VerseBO.class);

	private IDALFacade dalFacade;

	public VerseBO(IDALFacade dalFacade) {
		this.dalFacade = dalFacade;
	}

	public void addVerse(int poemId, String first, String snd) {
		try {
			LOGGER.debug("Adding verse with poemId: {}, first: {}, snd: {}", poemId, first, snd);
			dalFacade.addVerse(poemId, first, snd);
		} catch (CustomIllegalArgumentException e) {
			LOGGER.error("CustomIllegalArgumentException: {}", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.error("Exception: {}", e.getMessage());
			e.printStackTrace();
		}
	}

	public void updateVerse(int poemId, String firstVerse, String secondVerse, String firstUpdated,
			String secondUpdated) {
		try {
			LOGGER.debug(
					"Updating verse with poemId: {}, firstVerse: {}, secondVerse: {}, firstUpdated: {}, secondUpdated: {}",
					poemId, firstVerse, secondVerse, firstUpdated, secondUpdated);
			dalFacade.updateVerse(poemId, firstVerse, secondVerse, firstUpdated, secondUpdated);
		} catch (CustomIllegalArgumentException e) {
			LOGGER.error("CustomIllegalArgumentException: {}", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.error("Exception: {}", e.getMessage());
			e.printStackTrace();
		}
	}

	public void deleteVerse(int poemId, String First, String snd) {
		try {
			LOGGER.debug("Deleting verse with poemId: {}, First: {}, snd: {}", poemId, First, snd);
			if (poemId > 0) {
				dalFacade.deleteVerse(poemId, First, snd);
			} else {
				throw new CustomIllegalArgumentException("Verse ID must be provided for deleting a verse.");
			}
		} catch (CustomIllegalArgumentException e) {
			LOGGER.error("CustomIllegalArgumentException: {}", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.error("Exception: {}", e.getMessage());
			e.printStackTrace();
		}
	}

	public List<String[]> getVersesByPoem(int poemId) {
		try {
			LOGGER.debug("Getting verses by poemId: {}", poemId);
			List<String[]> verses = dalFacade.getVersesByPoem(poemId);
			return verses;
		} catch (Exception e) {
			LOGGER.error("Exception: {}", e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public int getVerseIdFromTitle(String firstVerse, String secondVerse) {
		try {
			LOGGER.debug("Getting verse ID from title with firstVerse: {}, secondVerse: {}", firstVerse, secondVerse);
			return dalFacade.getVerseIdFromTitle(firstVerse, secondVerse);
		} catch (Exception e) {
			LOGGER.error("Error while getting verse ID from title: {}", e.getMessage(), e);

			throw e;
		}
	}

	public List<String> getVersesFromId(int verseId) {
		try {
			LOGGER.debug("Getting verses from ID: {}", verseId);
			return dalFacade.getVersesFromId(verseId);
		} catch (Exception e) {
			LOGGER.error("Error while getting verses from ID: {}", e.getMessage(), e);

			throw e;
		}
	}

	public List<String[]> getVersesByRoot(int rootId) {
		try {
			LOGGER.debug("Getting verses by rootId: {}", rootId);
			return dalFacade.getVersesByRoot(rootId);
		} catch (Exception e) {
			LOGGER.error("Error while getting verses by rootId: {}", e.getMessage(), e);

			throw e;
		}
	}

}
