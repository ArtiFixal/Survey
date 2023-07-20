package survey;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import survey.SaveMethods.AsTxtFile;
import survey.SaveMethods.AsZipFile;
import survey.SaveMethods.ResultSaveMethod;

/**
 * Performs all kind of read/write operations related to survey results and files.
 * 
 * @author ArtiFixal
 */
public class ResultIO {
	/**
	 * Stores survey results directories and those store results.
	 */
	public static final File DEFAULT_RESULTS_DIR=new File("results");
	
	/**
	 * Method in which survey result will be written to a file.<br>
	 * Uses strategy design pattern.
	 */
	private ResultSaveMethod resultMethod;
	
	public ResultIO(ResultSaveMethod resultMethod) {
		if(!DEFAULT_RESULTS_DIR.exists())
			DEFAULT_RESULTS_DIR.mkdir();
		this.resultMethod=resultMethod;
	}
	
	public void setResultSaveMethod(ResultSaveMethod newMethod) {
		resultMethod=newMethod;
	}
	
	/**
	 * Writes survey result to a file, located in:<br>
	 * {@link DEFAULT_RESULTS_DIR}/<i>[survey name]</i>/result[X]<br>
	 * where X is result number.
	 * 
	 * @param r Survey result to save
	 * @param whichSurveyResult Survey to which result belongs.
	 * 
	 * @throws Exception Any exception that occurred during save.
	 * @see #DEFAULT_RESULTS_DIR
	 */
	public void writeResult(SurveyResult r,Survey whichSurveyResult) throws Exception
	{
		resultMethod.writeResult(r,whichSurveyResult);
	}
	
	/**
	 * Selects on the basis of given file extension {@code SurveyResultSaveMethod} 
	 * able to read this file.
	 * 
	 * @param extension File extension used by file.
	 * @return Method able to read that file extension.
	 * 
	 * @throws UnsupportedOperationException If there aren't any methods able to
	 * read that file extension.
	 */
	private ResultSaveMethod getSupportedResultSaveMethod(String extension)
	{
		return switch(extension) {
			case ".zip" -> new AsZipFile();
			case ".txt" -> new AsTxtFile();
			default -> throw new UnsupportedOperationException("Can't read survey result with extension: "+extension);
		};
	}
	
	public static ResultSaveMethod interpertResultSaveMethodFromNumber(int number)
	{
		return (ResultSaveMethod)SurveyIO.getSupportedSaveMethods()[number];
	}
	
	/**
	 * Reads {@code SurveyResult} from a given file.
	 * 
	 * @param pathToResult File from where result will be read.
	 * @return Result read from a file.
	 * 
	 * @throws Exception Any error that occurred during reading of a file.
	 */
	public SurveyResult readResult(File pathToResult) throws Exception
	{
		String extension=SurveyIO.getFileExtension(pathToResult);
		if(!extension.equals(resultMethod.getFileExtension()))
		{
			return getSupportedResultSaveMethod(extension).readResult(pathToResult);
		}
		return resultMethod.readResult(pathToResult);
	}
	
	/**
	 * Tests if given file supposed to contain {@code SurveyResult} is readable.
	 * 
	 * @param pathToResult File to be tested.
	 * @return True if file is readable, false otherwise.
	 */
	public boolean testResultFile(File pathToResult)
	{
		String extension=SurveyIO.getFileExtension(pathToResult);
		if(!extension.equals(resultMethod.getFileExtension()))
		{
			return getSupportedResultSaveMethod(extension).testResultFile(pathToResult);
		}
		return resultMethod.testResultFile(pathToResult);
	}
	
	/**
	 * Reads all possible results related to the given {@code Survey}.
	 * 
	 * @param whichSurveyResults Results of this survey will be read.
	 * @return All valid results related to survey.
	 * 
	 * @throws NoResultsException If there aren't any results.
	 * @throws InterruptedException If invoke of tasks was interupted.
	 * @throws RejectedExecutionException If taks couldn't be scheduled.
	 */
	public ArrayList<SurveyResult> readResults(Survey whichSurveyResults) throws NoResultsException,RejectedExecutionException,InterruptedException
	{
		File resultsDir=new File(DEFAULT_RESULTS_DIR+"/"+whichSurveyResults.getName()+"/");
		File[] resultFiles=resultsDir.listFiles();
		if(resultFiles==null||resultFiles.length==0)
			throw new NoResultsException(whichSurveyResults);
		ExecutorService tp=Executors.newFixedThreadPool(SurveyIO.getEffectiveThreadNumber(resultFiles.length));
		ArrayList<Callable<SurveyResult>> tasks=new ArrayList<>();
		for(int i=0;i<resultFiles.length;i++)
		{
			tasks.add(createReadResultTask(resultFiles[i]));
		}
		ArrayList<SurveyResult> results=new ArrayList<>();
		for(Future<SurveyResult> r:tp.invokeAll(tasks))
		{
			try{
				results.add(r.get());
			}catch(InterruptedException|ExecutionException e){
				// Do nothing
			}
		}
		tp.shutdown();
		return results;
	}
	
	private Callable<SurveyResult> createReadResultTask(File result)
	{
		return () -> readResult(result);
	}
}
