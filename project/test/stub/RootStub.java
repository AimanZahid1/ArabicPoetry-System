package stub;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dal.rootdal.IRootDao;
import dal.rootdal.RootOfTokensTO;
import dal.rootdal.RootOfVerseTO;
import dal.rootdal.RootTO;

public class RootStub implements IRootDao {
    public HashMap<RootTO, ArrayList<Object[]>> roots = new HashMap<>();

    public RootStub() {
        // Add sample Arabic data
        addToRootsMap("جذر1", 201, 101, "verified");
        addToRootsMap("جذر2", 202, 102, "verified");
    }

    private void addToRootsMap(String root, int tokenid, int verseId, String status) {
        int rid = generateUniqueId();
        RootTO rootTO = new RootTO(rid, root);
        int RootOfVerseid = generateUniqueId();
        RootOfVerseTO rootOfVersesTO = new RootOfVerseTO(verseId, rid, RootOfVerseid, status);
        int rot = RootOfVerseid + 1;
        RootOfTokensTO rootOfTokensTO = new RootOfTokensTO(rot, rid, rot);
        addToMap(rootTO, rootOfVersesTO, rootOfTokensTO);
    }

    private int generateUniqueId() {
        return roots.size() + 1;
    }

    private void addToMap(RootTO rootTO, RootOfVerseTO rootOfVersesTO, RootOfTokensTO rootOfTokensTO) {
        if (roots.containsKey(rootTO)) {
            roots.get(rootTO).add(new Object[]{rootOfVersesTO, rootOfTokensTO});
        } else {
            ArrayList<Object[]> dataList = new ArrayList<>();
            dataList.add(new Object[]{rootOfVersesTO, rootOfTokensTO});
            roots.put(rootTO, dataList);
        }
    }

    // Implement other methods according to your requirements...

    @Override
    public boolean addRoute(String root, int tokenid, int verseId, String status) throws SQLException {
        // Check if all parameters are available
        if (root != null && !root.isEmpty() && verseId > 0 && tokenid > 0 && status != null && !status.isEmpty()) {
            addToRootsMap(root, tokenid, verseId, status);
            return true;  // Indicate that the insertion was successful
        } else {
            // Log an error or handle the invalid parameters as needed
            System.out.println("Invalid parameters for adding route.");
            return false;  // Indicate that the insertion was unsuccessful
        }
    }

    @Override
    public boolean delRoute(String root) throws SQLException {
        if (root != null && !root.isEmpty()) {
            // Iterate through the roots map to find the matching root_word
            for (Map.Entry<RootTO, ArrayList<Object[]>> entry : roots.entrySet()) {
                RootTO currentRoot = entry.getKey();
                if (root.equals(currentRoot.getRoot_word())) {
                    roots.remove(currentRoot);
                    return true;  
                }
            }
            System.out.println("Root not found for deletion.");
            return false;
        } else {
            System.out.println("Invalid root parameter for deletion.");
            return false;
        }
    }


    @Override
    public boolean updRoute(String root, String newRootWord, String status) throws SQLException {
        if (root != null && !root.isEmpty() && newRootWord != null && !newRootWord.isEmpty() && status != null && !status.isEmpty()) {
            // Iterate through the roots map to find the matching root_word
            for (Map.Entry<RootTO, ArrayList<Object[]>> entry : roots.entrySet()) {
                RootTO currentRoot = entry.getKey();
                if (root.equals(currentRoot.getRoot_word())) {
                    // Update the root_word and status
                    currentRoot.setRoot_word(newRootWord);

                    // Update the corresponding RootOfVerseTO and RootOfTokensTO
                    ArrayList<Object[]> dataList = entry.getValue();
                    for (Object[] data : dataList) {
                        if (data[0] instanceof RootOfVerseTO) {
                            RootOfVerseTO rootOfVerseTO = (RootOfVerseTO) data[0];
                            rootOfVerseTO.setStatus(status);
                        }
                    }

                    System.out.println("Route updated successfully.");
                    return true;
                }
            }
            System.out.println("Root not found for update.");
            return false;
        } else {
            System.out.println("Invalid parameters for route update.");
            return false;
        }
    }

    @Override
    public List<String[]> viewAllRouteWithStatus() throws SQLException {
        List<String[]> result = new ArrayList<>();

        for (Map.Entry<RootTO, ArrayList<Object[]>> entry : roots.entrySet()) {
            RootTO rootTO = entry.getKey();
            ArrayList<Object[]> dataList = entry.getValue();

            for (Object[] data : dataList) {
                RootOfVerseTO rootOfVerseTO = (RootOfVerseTO) data[0];

                // Create a string array with root_word and status information
                String[] routeInfo = {
                        "Root Word: " + rootTO.getRoot_word(),
                        "Status: " + rootOfVerseTO.getStatus()
                };

                result.add(routeInfo);
            }
        }

        return result;
    }

    @Override
    public int roodIdFromName(String name) {
        if (name != null && !name.isEmpty()) {
            // Iterate through the roots map to find the matching root_word
            for (Map.Entry<RootTO, ArrayList<Object[]>> entry : roots.entrySet()) {
                RootTO currentRoot = entry.getKey();
                if (name.equals(currentRoot.getRoot_word())) {
                    return currentRoot.getRootid();
                }
            }
            // Return a default value if the root name is not found
            System.out.println("Root not found for the given name: " + name);
            return -1;
        } else {
            System.out.println("Invalid root name parameter.");
            return -1;
        }
    }

    @Override
    public boolean addOrUpdateRoute(String root, int tokenid, int verseId, String status) throws SQLException {
        if (root != null && !root.isEmpty() && verseId > 0 && tokenid > 0 && status != null && !status.isEmpty()) {
            // Check if the root is already present
            int rootId = roodIdFromName(root);
            if (rootId > 0) {
                // Root is already present, update the route
                return updRoute(root, root, status);
            } else {
                // Root is not present, add a new route
                return addRoute(root, tokenid, verseId, status);
            }
        } else {
            System.out.println("Invalid parameters for adding or updating route.");
            return false;
        }
    }

}
