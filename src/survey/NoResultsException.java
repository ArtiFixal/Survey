package survey;

/**
 * Exception thrown when there is no results to a given survey.
 * 
 * @author ArtiFixal
 */
public class NoResultsException extends Exception{
	public Survey s;
	
	public NoResultsException() {
		super();
	}
	
	public NoResultsException(String msg){
		super(msg);
	}
	
	public NoResultsException(Survey s)
	{
		super("Survey: "+s.getName()+" has not results yet.");
		this.s=s;
	}
	
	public Survey getSurvey()
	{
		return s;
	}
}
