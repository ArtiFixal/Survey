package survey;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Stores data like name and questions. 
 * Each {@code Question} stores answers to itself.
 * 
 * @author ArtiFixal
 * @see Question
 */
public class Survey {
	/**
	 * Stores survey question.
	 */
    private final ArrayList<Question> questions;
	
	/**
	 * Survey name
	 */
	private final String name;
    
	public Survey(String name)
	{
		this.name=name;
		questions=new ArrayList<>(6);
	}
	
    public Survey(String name,ArrayList<Question> questions)
    {
        this.questions=questions;
		this.name=name;
    }
	
    public String getName() {
        return name;
    }
    
    public ArrayList<Question> getQuestions() {
        return questions;
    }
    
    public Question getQuestionN(int index)
    {
        return questions.get(index);
    }
	
	public void addQuestion(Question q)
	{
		questions.add(q);
	}
	
	public void addQuestion(Collection<Question> c)
	{
		c.forEach((q) -> {
			questions.add(q);
		});
	}
}
