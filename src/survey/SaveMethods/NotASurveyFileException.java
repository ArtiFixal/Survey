package survey.SaveMethods;

import java.io.File;

/**
 * Exception thrown when data in file supposed to contain {@code Survey} 
 * is unredable.
 * 
 * @author ArtiFixal
 */
public class NotASurveyFileException extends Exception{

	public NotASurveyFileException() {
		super();
	}
	
	public NotASurveyFileException(String msg) {
		super(msg);
	}
	
	public NotASurveyFileException(File f) {
		super("Supposed to be a survey file but it's unreadable: "+f.getPath());
	}
}
