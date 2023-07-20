package survey;

/**
 * Exception thrown when searched option wasn't found in config file.
 * 
 * @author ArtiFixal
 */
public class ConfigOptionNotFoundException extends RuntimeException{
	private String searchedOption;
	
	public ConfigOptionNotFoundException() {
		super();
	}
	
	public ConfigOptionNotFoundException(String searchedOption)
	{
		super("Option "+searchedOption+" not found in config file");
		this.searchedOption=searchedOption;
	}
	
	public String getSearchedOption()
	{
		return searchedOption;
	}
}
