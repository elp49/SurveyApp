package menu;

import java.util.ArrayList;

/**
 * The Test Menu of Survey App: used to determine the main test action.
 */
public class TestMenu extends Menu {
    public static final String CREATE = "Create a new Test";
    public static final String DISPLAY = "Display an existing Test without correct answers";
    public static final String DISPLAY_WITH_ANSWERS = "Display an existing Test with correct answers";
    public static final String LOAD = "Load an existing Test";
    public static final String SAVE = "Save the current Test";
    public static final String TAKE = "Take the current Test";
    public static final String MODIFY = "Modify the current Test";
    public static final String TABULATE = "Tabulate a Test";
    public static final String GRADE = "Grade a Test";
    public static final String RETURN = "Return to main menu";

    public TestMenu() {
        prompt = "Test Menu";
        options = new ArrayList<>() {
            {
                add(CREATE);
                add(DISPLAY);
                add(DISPLAY_WITH_ANSWERS);
                add(LOAD);
                add(SAVE);
                add(TAKE);
                add(MODIFY);
                add(TABULATE);
                add(GRADE);
                add(RETURN);
            }
        };
    }
}
