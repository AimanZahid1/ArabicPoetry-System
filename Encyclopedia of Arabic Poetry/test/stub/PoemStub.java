package stub;

import java.util.ArrayList;
import java.util.List;

import dal.poemdal.manualAdd.IPoemDAO;
import dal.poemdal.manualAdd.Poem;

public class PoemStub implements IPoemDAO {

    private List<Poem> poemData = new ArrayList<>();

    public PoemStub() {
        // Add some temporary data for testing
        addPoem( 1, "قصيدة جميلة ");
        addPoem( 1,  "قصيدة مفكرة ");
        addPoem( 2,  "قصيدة ملهمة ");
        addPoem( 3, "قصيدة غامضة ");
        addPoem( 3, "قصيدة تأملية ");
        addPoem( 4,  "قصيدة جذابة ");
        addPoem( 4, "قصيدة مؤثرة ");
    }

    @Override
    public List<String> viewPoemsByBook(int bookId) {
        List<String> poems = new ArrayList<>();
        for (Poem poem : poemData) {
            if (poem.getBookId() == bookId) {
                poems.add(poem.getPoemTitle());
            }
        }
        return poems;
    }

    @Override
    public int getPoemIdByTitle(int bookId, String poemTitle) {
        for (Poem poem : poemData) {
            if (poem.getBookId() == bookId && poem.getPoemTitle().equalsIgnoreCase(poemTitle)) {
                return poem.getId();
            }
        }
        return 0; // Return 0 if not found
    }

    @Override
    public void addPoem(int bookId, String poemTitle) {
        // Add a new poem to the ArrayList
    	int id= generateUniqueId();
        Poem poem = new Poem(id, bookId, poemTitle);
        poemData.add(poem);
    }

    private int generateUniqueId() {
        return poemData.size() + 1;
    }
    @Override
    public void updatePoem(int bookId, String previousTitle, String newTitle) {
        // Update the title of a poem in the ArrayList
        for (Poem poem : poemData) {
            if (poem.getBookId() == bookId && poem.getPoemTitle().equalsIgnoreCase(previousTitle)) {
                poem.setPoemTitle(newTitle);
                break; // Assuming titles are unique, exit loop after updating the poem
            }
        }
    }

    @Override
    public void deletePoem(int bookId, String poemTitle) {
        poemData.removeIf(poem -> poem.getBookId() == bookId && poem.getPoemTitle().equalsIgnoreCase(poemTitle));
    }

    
    @Override
    //do not test this one 
    public String getBookTitleByPoemId(int poemId) {
        for (Poem poem : poemData) {
            if (poem.getId() == poemId) {
               
                return "Book Title: " + poem.getBookId();
            }
        }
        return null;
    }

    @Override
    public int PoemIdByTitle(String poemTitle) {
        for (Poem poem : poemData) {
            if (poem.getPoemTitle().equalsIgnoreCase(poemTitle)) {
                return poem.getId();
            }
        }
        return 0; // Return 0 if not found
    }
}
