package bll.poembll;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import dal.IDALFacade;
import dal.poemdal.importPoem.ParsePoemDAO.Poems;

/**
 * The ImportPoemBO class implements the IImportPoemBO interface to handle
 * importing and managing poems.
 */
public class ImportPoemBO {

	private static final Logger LOGGER = LogManager.getLogger(ImportPoemBO.class);
	private IDALFacade dalFacade;

	/**
	 * Constructs an ImportPoemBO object with a DAL facade.
	 *
	 * @param df The data access layer facade to be used.
	 */
	public ImportPoemBO(IDALFacade df) {
		try {

			this.dalFacade = df;

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * Imports poems from a file.
	 *
	 * @param filePath The file path from which poems are imported.
	 * @param bookName The name of the book associated with the poems.
	 * @return A message indicating the result of the import operation.
	 */
	public boolean importPoems(String filePath, String bookName) {
		try {
			return dalFacade.getPoemsData(filePath, bookName);
		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}
	}

}