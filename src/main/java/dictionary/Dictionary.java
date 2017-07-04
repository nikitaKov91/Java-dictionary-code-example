package dictionary;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Kovalenko Nikita on 03.07.2017.
 */
public class Dictionary {

    private static final Dictionary instance = new Dictionary();

    private ConcurrentMap<String, Set<String>> words = new ConcurrentHashMap<>();

    // singleton realization
    public static Dictionary getInstance() {
        return instance;
    }

    // private constructor for singleton realization
    private Dictionary () {

    }

    /**
     * method for dictionary operations
     * @param args - 1st element contains operation,
     *             the rest are parameters for it
     * @return result of operation
     */
    public String operation(String[] args) {
        switch (args[0]) {
            case "get":
                return getWord(args);
            case "add":
                return addWord(args);
            case "delete":
                return delWord(args);
            default:
                return "<Unknown operation: " + args[0] + ". Allowed operations: get, add, delete>";
        }
    }

    /**
     * get all meanings of the word
     * @param args - 1st element contains word for access
     * @return string with all meanings/error/absent message
     */
    private String getWord(String[] args) {
        if (args.length < 2) {
            return "<Not enough arguments for operation get. Format for operation: get %word%>";
        }
        String word = args[1];
        if (!words.containsKey(word)) {
            return "<Word \"" + word + "\" is absent in the dictionary>";
        } else {
            StringBuilder sb = new StringBuilder();
            for (String meaning : words.get(word)) {
                sb.append(sb.length() > 0 ? "\n" + meaning : meaning);
            }
            return sb.toString();
        }
    }

    /**
     * add meanings of word to dictionary
     * @param args 1st element contains word for adding,
     *             the rest are meanings for it
     * @return string with success/error
     */
    private String addWord(String[] args) {
        if (args.length < 3) {
            return "<Not enough arguments for operation add. Format for operation: " +
                    "add %word% %meaning1% [%meaning2%...]>";
        }
        String word = args[1];
        // word is absent - just add it
        if (!words.containsKey(word)) {
            // create set of meanings
            Set<String> meanings = new HashSet<>();
            for (int i = 2; i < args.length; i++) {
                meanings.add(args[i]);
            }
            // create a synchronized set
            Set<String> synMeanings = Collections.synchronizedSet(meanings);
            words.put(word, synMeanings);
            return "<Word \"" + word + "\" was successfully added to dictionary>";
        // word is in the dictionary - add absent meanings
        } else {
            Set<String> meanings = words.get(word);
            boolean isAdd = false;
            for (int i = 2; i < args.length; i++) {
                if (!meanings.contains(args[i])) {
                    isAdd = true;
                    meanings.add(args[i]);
                }
            }
            if (isAdd) {
                return "<Specified meanings of word \"" + word + "\" were successfully added to dictionary>";
            } else {
                return "<All specified meanings of the word \"" + word + "\" are already present in the dictionary>";
            }
        }
    }

    /**
     * delete meanings of word from dictionary
     * @param args 1st element contains word for delete,
     *             the rest are meanings for delete
     * @return string with success/error/absent message
     */
    private String delWord(String[] args) {
        if (args.length < 3) {
            return "<Not enough arguments for operation delete. Format for operation: " +
                    "delete %word% %meaning1% [%meaning2%...]>";
        }
        String word = args[1];
        if (!words.containsKey(word)) {
            return "<Word \"" + word + "\" is absent in the dictionary>";
        } else {
            Set<String> meanings = words.get(word);
            boolean isDelete = false;
            for (String meaning : args) {
                if (meanings.contains(meaning)) {
                    meanings.remove(meaning);
                    isDelete = true;
                }
            }
            if (isDelete) {
                if (meanings.size() == 0) {
                    words.remove(word);
                }
                return "<Specified meanings of word \"" + word + "\" successfully removed>";
            } else {
                return "<All of specified meanings of word \"" + word + "\" don't exist in the dictionary>";
            }
        }
    }

}
