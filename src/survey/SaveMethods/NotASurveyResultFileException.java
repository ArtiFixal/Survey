package survey.SaveMethods;

import java.io.File;

/**
 * Exception thrown when data in file supposed to contain {@code SurveyResult} 
 * is unredable.
 * 
 * @author ArtiFixal
 */
public class NotASurveyResultFileException extends Exception{
	
	public NotASurveyResultFileException() {
		super();
	}
	
	public NotASurveyResultFileException(String msg) {
		super(msg);
	}
	
	public NotASurveyResultFileException(File f) {
		super("Supposed to be a survey result file but it's unreadable: "+f.getPath());
	}
}
