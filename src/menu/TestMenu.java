package menu;

import java.util.ArrayList;

/**
 * The Test Menu of Survey App: used to determine the main test action.
 */
public class TestMenu extends SurveyMenu {
    public static String DISPLAY_WITH_ANSWERS = "Display an existing Test with correct answers";
    public static String GRADE = "Grade a Test";

    TestMenu() {
        CREATE = "Create a new Test";
        DISPLAY = "Display an existing Test without correct answers";
        LOAD = "Load an existing Test";
        SAVE = "Save the current Test";
        TAKE = "Take the current Test";
        MODIFY = "Modify the current Test";
        TABULATE = "Tabulate a Test";
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
