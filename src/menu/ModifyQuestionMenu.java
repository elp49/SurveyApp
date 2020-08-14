package menu;

import java.util.ArrayList;
import java.util.List;

public class ModifyQuestionMenu extends Menu {
    public static final String MODIFY_PROMPT = "Do you wish to modify the prompt?";
    public static final String MODIFY_NUM_RESPONSES = "Do you wish to modify the number of responses?";
    public static final String MODIFY_CHOICES = "Do you wish to modify choices?";
    public static final String YES = "Yes";
    public static final String NO = "No";
    public static final List<String> OPTIONS = new ArrayList<>() {
        {
            add(YES);
            add(NO);
            add(RETURN);
        }
    };
}
