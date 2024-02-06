package stub;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dal.versedal.IVerseDAO;
import dal.versedal.VerseTO;

public class VerseStub implements IVerseDAO {

    private List<VerseTO> verseData = new ArrayList<>();

    public VerseStub() throws SQLException {
        // Add some temporary data for testing
        addVerse(1, "أول بيت", "الثاني بيت");
        addVerse(2, "بيت 1", "بيت 2");
        addVerse(3, "البيت الأول", "البيت الثاني");
        addVerse(4, "بيت جميل", "بيت جذاب");
        addVerse(5, "البيت الأول", "البيت الثاني");
    }

    @Override
    public void addVerse(int poemId, String firstVerse, String secondVerse) throws SQLException {
        // Add a new verse to the list
        VerseTO verse = new VerseTO(poemId, firstVerse, secondVerse);
        verseData.add(verse);
    }

    @Override
    public void updateVerse(int poemId, String firstVerse, String secondVerse, String firstUpdated, String secondUpdated) {
        // Update the verses in the list
        for (VerseTO verse : verseData) {
            if (verse.getPoemId() == poemId && verse.getFirstVerse().equalsIgnoreCase(firstVerse)
                    && verse.getSecondVerse().equalsIgnoreCase(secondVerse)) {
                verse.setFirstVerse(firstUpdated);
                verse.setSecondVerse(secondUpdated);
                break; // Assuming verses are unique, exit loop after updating the verse
            }
        }
    }

    @Override
    public void deleteVerse(int poemId, String firstVerse, String secondVerse) {
        // Remove a verse from the list
        verseData.removeIf(verse -> verse.getPoemId() == poemId && verse.getFirstVerse().equalsIgnoreCase(firstVerse)
                && verse.getSecondVerse().equalsIgnoreCase(secondVerse));
    }

    @Override
    public List<String[]> getVersesByPoem(int poemId) {
        List<String[]> verses = new ArrayList<>();
        for (VerseTO verse : verseData) {
            if (verse.getPoemId() == poemId) {
                verses.add(new String[]{verse.getFirstVerse(), verse.getSecondVerse()});
            }
        }
        return verses;
    }

    @Override
    public int getVerseIdFromTitle(String firstVerse, String secondVerse) {
        for (VerseTO verse : verseData) {
            if (verse.getFirstVerse().equalsIgnoreCase(firstVerse) && verse.getSecondVerse().equalsIgnoreCase(secondVerse)) {
                return verse.getPoemId();
            }
        }
        return 0; // Return 0 if not found
    }

    @Override
    public List<String> getVersesFromId(int verseId) {
        List<String> verses = new ArrayList<>();
        for (VerseTO verse : verseData) {
            if (verse.getPoemId() == verseId) {
                verses.add(verse.getFirstVerse());
                verses.add(verse.getSecondVerse());
            }
        }
        return verses;
    }

    @Override
    public List<String[]> getVersesByRoot(int rootId) {
    	 List<String[]> verses = new ArrayList<>();

         verses.add(new String[]{"إذا الشَّعْبُ يَوْمَاً أَرَادَ الْحَيَــاةَ،"});
         verses.add(new String[]{"فَلَا بُدَّ أَنْ يَسْتَجِيبَ القَدَرُ"});
         verses.add(new String[]{"قالت لي روميّة وأنا أنظرها"});
         verses.add(new String[]{"أنتَ وإن طلبت العلا فلست منا"});
      
    	for(int i=0;i<20;i++) {
    	  if(rootId==i) {
    	return verses;
    	  }
      }
    	return Collections.emptyList();
    }
}
