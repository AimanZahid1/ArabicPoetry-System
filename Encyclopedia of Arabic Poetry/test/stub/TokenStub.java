package stub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import dal.Tokenize.ITokenizeDAO;
import dal.Tokenize.TokenTO;
import dal.Tokenize.TokenTOVerseTO;

public class TokenStub implements ITokenizeDAO {

    // HashMap to store tokens and their associated verse information
    HashMap<TokenTO, ArrayList<TokenTOVerseTO>> tokenMap = new HashMap<>();

    public TokenStub() {
        // Add sample Arabic data
        insertTokens(new ArrayList<>(List.of("كلمة1", "كلمة2")), 101);
        insertTokens(new ArrayList<>(List.of("كلمة3", "كلمة4")), 102);
    }

    @Override
    public void insertTokens(ArrayList<String> tokens, int verseId) {
        // Iterate through the list of tokens
        for (String token : tokens) {
            // Create a new TokenTO object
            TokenTO newToken = new TokenTO(generateUniqueId(), token);

            // Check if the token is already present in the map
            if (tokenMap.containsKey(newToken)) {
                tokenMap.get(newToken).add(new TokenTOVerseTO(newToken.getId(), verseId));
            } else {
                // If the token is not present, create a new entry
                ArrayList<TokenTOVerseTO> verseList = new ArrayList<>();
                verseList.add(new TokenTOVerseTO(newToken.getId(), verseId));
                tokenMap.put(newToken, verseList);
            }
        }
    }

    @Override
    public boolean isVerseIdAvailable(int verseId) {
        for (ArrayList<TokenTOVerseTO> verseList : tokenMap.values()) {
            for (TokenTOVerseTO tokenTOVerseTO : verseList) {
                if (tokenTOVerseTO.getVerseid() == verseId) {
                    return true;  
                }
            }
        }
        return false; 
    }
    @Override
    public ArrayList<Integer> getidFromTokens(ArrayList<String> tokens) {
        ArrayList<Integer> ids = new ArrayList<Integer>();

        // Check if tokens are not empty before adding IDs
        if (!tokens.isEmpty()) {
            for (int i = 0; i < 20; i++) {
                ids.add(i);
            }
        }

        return ids;
    }


    private int generateUniqueId() {
        return tokenMap.size() + 1;
    }

	@Override
	public ArrayList<String> getTokensByVerseId(int verseId) {
		return null;
	}
}
