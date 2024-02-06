package bll.tokenize;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dal.IDALFacade;

public class TokenizeBO {
	private static final Logger LOGGER = LogManager.getLogger(TokenizeBO.class);

	private IDALFacade dalFacade;

	public TokenizeBO(IDALFacade dalFacade) {
		this.dalFacade = dalFacade;
	}

	public void insertToken(ArrayList<String> tokens, int verseid) {
	    try {
	        LOGGER.debug("Inserting tokens with verseId: {}, tokens: {}", verseid, tokens);
	        dalFacade.insertTokens(tokens, verseid);
	    } catch (Exception e) {
	        LOGGER.error("Error inserting tokens with verseId: {}, tokens: {}", verseid, tokens, e);
	    }
	}

	public boolean isVerseIdAvailable(int verseId) {
	    try {
	        LOGGER.debug("Checking if verseId is available: {}", verseId);
	        return dalFacade.isVerseIdAvailable(verseId);
	    } catch (Exception e) {
	        LOGGER.error("Error checking if verseId is available: {}", verseId, e);
	        return false;
	    }
	}

	public ArrayList<String> getTokensByVerseId(int verseId) {
	    try {
	        LOGGER.debug("Getting tokens by verseId: {}", verseId);
	        return dalFacade.getTokensByVerseId(verseId);
	    } catch (Exception e) {
	        LOGGER.error("Error getting tokens by verseId: {}", verseId, e);
	        return new ArrayList<>(); 
	    }
	}

	public ArrayList<Integer> getidFromTokens(ArrayList<String> tokens) {
	    try {
	        LOGGER.debug("Getting IDs from tokens: {}", tokens);
	        return dalFacade.getidFromTokens(tokens);
	    } catch (Exception e) {
	        LOGGER.error("Error getting IDs from tokens: {}", tokens, e);
	        return new ArrayList<>();
	    }
	}

}
