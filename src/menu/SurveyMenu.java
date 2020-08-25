package menu;

import java.util.ArrayList;

/**
 * The Survey Menu of Survey App: used to determine the main survey action.
 */
public class SurveyMenu extends Menu {
    public static final String CREATE = "Create a new Survey";
    public static final String DISPLAY = "Display an existing Survey";
    public static final String LOAD = "Load an existing Survey";
    public static final String SAVE = "Save the current Survey";
    public static final String TAKE = "Take the current Survey";
    public static final String MODIFY = "Modify the current Survey";
    public static final String TABULATE = "Tabulate a survey";
    public static final String RETURN = "Return to main menu";

    public SurveyMenu() {
        prompt = "Survey Menu";
        options = new ArrayList<>() {
            {
                add(CREATE);
                add(DISPLAY);
                add(LOAD);
                add(SAVE);
                add(TAKE);
                add(MODIFY);
                add(TABULATE);
                add(RETURN);
            }
        };
    }
}
