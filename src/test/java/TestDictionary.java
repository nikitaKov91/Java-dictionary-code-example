import dictionary.Dictionary;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Коваленко Никита on 04.07.2017.
 */
public class TestDictionary {

    Dictionary dictionary;

    @Before
    public void setUp() throws Exception {
        dictionary = Dictionary.getInstance();
        String[] args = {"add", "man", "мужчина"};
        dictionary.operation(args);
    }

    @After
    public void tearDown() throws Exception {
        dictionary = null;
    }

    @Test
    public void testOperationAddSame() {
        String[] argsGet = {"get", "man"};
        assertEquals("мужчина", dictionary.operation(argsGet));
        String[] argsAdd = {"add", "man", "мужчина"};
        assertEquals("<All specified meanings of the word \"man\" are already present in the dictionary>",
                dictionary.operation(argsAdd));
    }

    @Test
    public void testOperationAddNew() {
        String[] argsGet = {"get", "man"};
        assertEquals("мужчина", dictionary.operation(argsGet));
        String[] argsAdd = {"add", "man", "мужчина", "человек"};
        assertEquals("<Specified meanings of word \"man\" were successfully added to dictionary>",
                dictionary.operation(argsAdd));
        String[] argsGetAfter = {"get", "man"};
        assertEquals("мужчина\nчеловек", dictionary.operation(argsGetAfter));
    }

    @Test
    public void testOperationGet() {
        String[] args = {"get", "new"};
        assertEquals("<Word \"new\" is absent in the dictionary>", dictionary.operation(args));
    }

    @Test
    public void testOperationDel() {
        String[] argsDel = {"delete", "man", "мужчина"};
        assertEquals("<Specified meanings of word \"man\" successfully removed>", dictionary.operation(argsDel));
        String[] argsGet = {"get", "man"};
        assertEquals("<Word \"man\" is absent in the dictionary>", dictionary.operation(argsGet));
    }

    @Test
    public void testOperationDelAbsent() {
        String[] args = {"delete", "man", "алло", "ау"};
        assertEquals("<All of specified meanings of word \"man\" don't exist in the dictionary>",
                dictionary.operation(args));
    }

    @Test
    public void testOperationDelAbsentWord() {
        String[] args = {"delete", "new", "Новый"};
        assertEquals("<Word \"new\" is absent in the dictionary>", dictionary.operation(args));
    }

}
