package menu;

import java.util.ArrayList;

/**
 * The Survey Menu of Survey App: used to determine the main survey action.
 */
public class SurveyMenu extends Menu {
    public static String CREATE = "Create a new Survey";
    public static String DISPLAY = "Display an existing Survey";
    public static String LOAD = "Load an existing Survey";
    public static String SAVE = "Save the current Survey";
    public static String TAKE = "Take the current Survey";
    public static String MODIFY = "Modify the current Survey";
    public static String TABULATE = "Tabulate a survey";

    SurveyMenu() {
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
