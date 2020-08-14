package menu;

import java.util.ArrayList;
import java.util.List;

/**
 * The Delete Menu: used to determine if a user wishes to delete something.
 */
public class DeleteMenu extends Menu {
    public static final String PROMPT = "Would you like to delete it now?";
    public static final String DELETE = "Yes, delete it";
    public static final String KEEP = "No, keep it";
    public static final List<String> OPTIONS = new ArrayList<>() {
        {
            add(DELETE);
            add(KEEP);
        }
    };
}
