package dal;

import dal.Tokenize.ITokenizeDAO;
import dal.bookdal.IBookDAO;
import dal.poemdal.importPoem.IParsePoemDAO;
import dal.poemdal.manualAdd.IPoemDAO;
import dal.rootdal.IRootDao;
import dal.versedal.IVerseDAO;

public interface IDALFacade extends IRootDao, IParsePoemDAO, IBookDAO, IPoemDAO, IVerseDAO, ITokenizeDAO {

}
