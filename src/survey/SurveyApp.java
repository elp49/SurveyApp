package survey;

import java.util.ArrayList;
import java.util.List;

import io.SurveyInputReader;
import io.SurveyOutputWriter;
import io.ConsoleSurveyInputReader;
import io.ConsoleSurveyOutputWriter;

public class SurveyApp {
	public static SurveyInputReader in;
	public static SurveyOutputWriter out;

	public static void main(String[] args) {
		SurveyApp app = new SurveyApp();
		app.runConsoleApp();
	}

	public SurveyApp() { }

	public void runConsoleApp() {
		in = new ConsoleSurveyInputReader();
		out = new ConsoleSurveyOutputWriter();
		run();
	}

	private void run() {
		showMainMenu();
	}

	public static final String MAIN_MENU_PROMPT = "Main Menu";
	public static final List<String> MAIN_MENU_CHOICES = new ArrayList() {
		{
			add("Create a new Survey");
			add("Load an existing S1
		}
	};

	public void showMainMenu() {

	}
}
