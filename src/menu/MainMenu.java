package menu;

import java.util.ArrayList;
import java.util.List;

/**
 * The Main Menu of Survey App: used to determine the main action.
 */
public class MainMenu {
    public static final String PROMPT = "Main Menu";
    public static final String CREATE_SURVEY = "Create a new Survey";
    public static final String DISPLAY_SURVEY = "Display an existing Survey";
    public static final String LOAD_SURVEY = "Load an existing Survey";
    public static final String SAVE_SURVEY = "Save the current Survey";
    public static final String TAKE_SURVEY = "Take the current Survey";
    public static final String MODIFY_SURVEY = "Modifying the current Survey";
    public static final String TABULATE_SURVEY = "Tabulate a Survey";
    public static final String QUIT = "Quit";
    public static final List<String> OPTIONS = new ArrayList<>() {
        {
            add(CREATE_SURVEY);
            add(DISPLAY_SURVEY);
            add(LOAD_SURVEY);
            add(SAVE_SURVEY);
            add(TAKE_SURVEY);
            add(MODIFY_SURVEY);
            add(TABULATE_SURVEY);
            add(QUIT);
        }
    };
}
