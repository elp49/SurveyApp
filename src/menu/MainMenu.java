package menu;

import java.util.ArrayList;

/**
 * The Main Menu of Survey App: used to determine the main action.
 */
public class MainMenu extends Menu {
    public static final String SURVEY = "Survey";
    public static final String TEST = "Test";
    public static final String QUIT = "Quit";

    public MainMenu() {
        prompt = "Main Menu";
        options = new ArrayList<>() {
            {
                add(SURVEY);
                add(TEST);
                add(QUIT);
            }
        };
    }
}
