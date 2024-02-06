package bll.rootbll;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

import dal.IDALFacade;

public class RootBO {
	private static final Logger LOGGER = LogManager.getLogger(RootBO.class);

	private IDALFacade dllfacade;

	public RootBO(IDALFacade df) {
		this.dllfacade = df;
	}

	public boolean addRoute(String root, int tokenid, int verseId, String status) {
	    try {
	        LOGGER.debug("Adding route with root: {}, tokenid: {}, verseId: {}, status: {}", root, tokenid, verseId, status);
	        return dllfacade.addRoute(root, tokenid, verseId, status);
	    } catch (SQLException e) {
	        LOGGER.error("Error adding route with root: {}, tokenid: {}, verseId: {}, status: {}", root, tokenid, verseId, status, e);
	        throw new RuntimeException(e); // You may choose to handle or rethrow the exception
	    }
	}

	public boolean updateroute(String name, String root, String status) {
	    try {
	        LOGGER.debug("Updating route with name: {}, new root: {}, new status: {}", name, root, status);
	        return dllfacade.updRoute(name, root, status);
	    } catch (SQLException e) {
	        LOGGER.error("Error updating route with name: {}, new root: {}, new status: {}", name, root, status, e);
	        throw new RuntimeException(e); 
	    }
	}

	public boolean deleteroute(String name) {
	    try {
	        LOGGER.debug("Deleting route with name: {}", name);
	        return dllfacade.delRoute(name);
	    } catch (SQLException e) {
	        LOGGER.error("Error deleting route with name: {}", name, e);
	        throw new RuntimeException(e);
	    }
	}

	public List<String[]> viewAllRouteWithStatus() {
	    try {
	        LOGGER.debug("Viewing all routes with status");
	        return dllfacade.viewAllRouteWithStatus();
	    } catch (SQLException e) {
	        LOGGER.error("Error viewing all routes with status", e);
	        throw new RuntimeException(e);
	    }
	}

	public int roodIdFromName(String name) {
	    try {
	        LOGGER.debug("Getting root ID from name: {}", name);
	        return dllfacade.roodIdFromName(name);
	    } catch (Exception e) {
	        LOGGER.error("Error getting root ID from name: {}", name, e);
	        throw new RuntimeException(e);
	    }
	}

	public boolean addOrUpdateRoute(String root, int tokenid, int verseId, String status) {
	    try {
	        LOGGER.debug("Adding or updating route with root: {}, tokenid: {}, verseId: {}, status: {}", root, tokenid, verseId, status);
	        return dllfacade.addOrUpdateRoute(root, tokenid, verseId, status);
	    } catch (SQLException e) {
	        LOGGER.error("Error adding or updating route with root: {}, tokenid: {}, verseId: {}, status: {}", root, tokenid, verseId, status, e);
	        throw new RuntimeException(e);
	    }
	}

}
