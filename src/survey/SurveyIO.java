package survey;

import survey.SaveMethods.SurveySaveMethod;
import survey.SaveMethods.AsZipFile;
import java.io.File;
import survey.SaveMethods.AsTxtFile;

/**
 * Performs all kind of read/write operations related to surveys and files.
 * 
 * @author ArtiFixal
 */
public class SurveyIO {
	/**
	 * Directory which stores all available surveys.
	 */
	public static final File DEFAULT_SAVE_DIR=new File("surveys");
	
	/**
	 * Method in which survey will be written to a file.<br>
	 * Uses strategy design pattern.
	 */
	private SurveySaveMethod surveyMethod;
	
	public SurveyIO(){
		this(new AsZipFile());
	}
	
	/**
	 * @param surveyMethod Method in which survey will be saved.
	 */
	public SurveyIO(SurveySaveMethod surveyMethod) {
		if(!DEFAULT_SAVE_DIR.exists())
			DEFAULT_SAVE_DIR.mkdir();
		this.surveyMethod=surveyMethod;
	}
	
	public SurveySaveMethod getSurveySaveMethod() {
		return surveyMethod;
	}

	public void setSurveySaveMethod(SurveySaveMethod newMethod) {
		surveyMethod=newMethod;
	}
	
	/**
	 * Based on quantity of tasks returns thread number which will be used to 
	 * run these tasks.
	 * 
	 * @param tasksQuantity Number of tasks to do.
	 * @return Number of threads.
	 */
	public static int getEffectiveThreadNumber(int tasksQuantity)
	{
		return (tasksQuantity>Runtime.getRuntime().availableProcessors())?
				Runtime.getRuntime().availableProcessors():tasksQuantity;
	}
	
	/**
	 * Removes file extension from given string.
	 * 
	 * @param from String containing file extension.
	 * @return String without file extension.
	 */
	public static String removeFileExtension(String from)
	{
		String name=from;
		int pos=name.lastIndexOf(".");
		if(pos!=-1)
		{
			name=name.substring(0,pos);
		}
		return name;
	}
	
	/**
	 * Retrieves file extension from given file path.
	 * 
	 * @param path Path to a file.
	 * @return File extension.
	 */
	public static String getFileExtension(String path)
	{
		return path.substring(path.lastIndexOf('.'));
	}
	
	/**
	 * Retrieves file extension from given file path.
	 * 
	 * @param path Path to a file.
	 * @return File extension.
	 */
	public static String getFileExtension(File path)
	{
		return getFileExtension(path.getName());
	}

	/**
	 * Writes given {@code survey} to a file. located in:<br>
	 * {@link DEFAULT_SAVE_DIR}/<i>[survey name]</i>/result[X]<br>
	 * 
	 * @param s Survey to write.
	 * 
	 * @throws Exception Any error that occurred during write into a file.
	 * @see #DEFAULT_SAVE_DIR
	 */
	public void write(Survey s) throws Exception
	{
		surveyMethod.write(s);
	}
	
	/**
	 * Selects on the basis of given file extension {@code SurveySaveMethod} 
	 * able to read this file.
	 * 
	 * @param extension File extension used by file.
	 * @return Method able to read that file extension.
	 * 
	 * @throws UnsupportedOperationException If there aren't any methods able to
	 * read that file extension.
	 */
	private SurveySaveMethod getSupportedSurveySaveMethod(String extension) throws UnsupportedOperationException
	{
		return switch(extension) {
			case ".zip" -> new AsZipFile();
			case ".txt" -> new AsTxtFile();
			default -> 
				throw new UnsupportedOperationException("Can't read survey with extension: "+extension);
		};
	}
	
	public static SurveySaveMethod[] getSupportedSaveMethods()
	{
		final SurveySaveMethod[] methods=new SurveySaveMethod[]{
			new AsZipFile(),
			new AsTxtFile()
		};
		return methods;
	}
	
	public static SurveySaveMethod interpertSurveySaveMethodFromNumber(int number)
	{
		return getSupportedSaveMethods()[number];
	}
	
	/**
	 * Reads {@code Survey} from a given file.
	 * 
	 * @param pathToSurvey File from where survey will be read.
	 * @return Survey read from a file.
	 * 
	 * @throws Exception Any error that occurred during reading of a file.
	 */
	public Survey read(File pathToSurvey) throws Exception
	{
		String extension=getFileExtension(pathToSurvey);
		if(!surveyMethod.isFileExtensionSuported(extension))
		{
			return getSupportedSurveySaveMethod(extension).read(pathToSurvey);
		}
		return surveyMethod.read(pathToSurvey);
	}
	
	/**
	 * Tests if given file supposed to contain {@code Survey} is readable.
	 * 
	 * @param pathToSurvey File to be tested.
	 * @return True if file is readable, false otherwise.
	 */
	public boolean testSurveyFile(File pathToSurvey)
	{
		String extension=getFileExtension(pathToSurvey);
		if(!surveyMethod.isFileExtensionSuported(extension))
		{
			return getSupportedSurveySaveMethod(extension).testSurveyFile(pathToSurvey);
		}
		return surveyMethod.testSurveyFile(pathToSurvey);
	}
}
