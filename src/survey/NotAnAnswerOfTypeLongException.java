package survey;

/**
 * Exception thrown when answer type was excepted to be a Long, but found 
 * anything other than it.
 * 
 * @author ArtiFixal
 */
public class NotAnAnswerOfTypeLongException extends RuntimeException{

	public NotAnAnswerOfTypeLongException() {
		super();
	}
	
	public NotAnAnswerOfTypeLongException(String msg)
	{
		super(msg);
	}
	
	public NotAnAnswerOfTypeLongException(Object answer,int index)
	{
		super("Answer at index: "+index+" supposed to be long but found: "+
				answer.getClass().getSimpleName());
	}
}
