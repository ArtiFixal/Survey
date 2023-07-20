package survey.SaveMethods;

/**
 * Interface used to specify used file extension.
 * Provides method to check if file extension is suported.
 * 
 * @author ArtiFixal
 */
public interface FileExtension {
	/**
	 * @return Used file extension.
	 */
	public String getFileExtension();
	
	/**
	 * Checks if given file extension is suported.
	 * 
	 * @param fileExtension Extension to check.
	 * @return True if is suported, false otherwise.
	 */
	public boolean isFileExtensionSuported(String fileExtension);
}
