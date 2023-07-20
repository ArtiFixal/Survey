package survey.SaveMethods;

import java.io.File;
import survey.Survey;

/**
 * Interface responsible for the way a {@code Survey} data in the file is 
 * interpreted to be read and write. <br>
 * Uses strategy design pattern.
 * 
 * @author ArtiFixal
 */
public interface SurveySaveMethod extends FileExtension{
	/**
	 * Writes {@code Survey} to a file of specific type.
	 * 
	 * @param s Survey to write.
	 * @throws Exception Any error occurred during write into a file.
	 */
	public void write(Survey s) throws Exception;
	
	/**
	 * Reads {@code Survey} from the given file.
	 * 
	 * @param pathToSurvey Path leading to a file containing survey.
	 * @return Survey read from file.
	 * 
	 * @throws Exception Any error occurred during reading of a file.
	 */
	public Survey read(File pathToSurvey) throws Exception;
	
	/**
	 * Tests if given file with {@code Survey} is readable by this save method.
	 * 
	 * @param pathToSurvey File supposed to be survey.
	 * @return True if file is readable by this method, false otherwise.
	 */
	public boolean testSurveyFile(File pathToSurvey);
}
