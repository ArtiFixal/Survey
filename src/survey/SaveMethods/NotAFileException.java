package survey.SaveMethods;

import java.io.File;

/**
 * Exception thrown when file supposed to be a regular file but found 
 * directory instead.
 * 
 * @author ArtiFixal
 */
public class NotAFileException extends RuntimeException{

	public NotAFileException() {
		super();
	}
	
	public NotAFileException(String msg)
	{
		super(msg);
	}
	
	public NotAFileException(File f)
	{
		super("Excepted to be a regular file but directory found: "+f.getPath());
	}
	
}
