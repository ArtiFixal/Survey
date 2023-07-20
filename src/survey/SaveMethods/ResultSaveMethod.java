package survey.SaveMethods;

import java.io.File;
import survey.Survey;
import survey.SurveyResult;

/**
 * Interface responsible for the way a {@code SurveyResult} data in the file is 
 * interpreted to be read and write. <br>
 * Uses strategy design pattern.
 * 
 * @author ArtiFixal
 */
public interface ResultSaveMethod extends FileExtension{
	/**
	 * Writes {@code SurveyResult} to a file of specific type.
	 * 
	 * @param r Result to write.
	 * @param s Survey related to this result.
	 * 
	 * @throws Exception Any error occurred during write into a file.
	 */
	public void writeResult(SurveyResult r,Survey s) throws Exception;
	
	/**
	 * Reads {@code SurveyResult} from the given file.
	 * 
	 * @param pathToResult Path leading to a file containing result.
	 * @return Result read from file.
	 * 
	 * @throws Exception Any error occurred during reading of a file.
	 */
	public SurveyResult readResult(File pathToResult) throws Exception;
	
	/**
	 * Tests if given file with {@codde SurveyResult} is readable by this save 
	 * method.
	 * 
	 * @param pathToResult File supposed to be survey result.
	 * @return True if file is readable by this method, false otherwise.
	 */
	public boolean testResultFile(File pathToResult);
}
