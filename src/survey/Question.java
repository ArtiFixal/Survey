package survey;

import java.util.ArrayList;

/**
 * Defines a {@code Survey} question, its type and stores answers to it.
 * Main part of the {@code Survey} class. 
 * 
 * @author ArtiFixal
 * @see Survey
 */
public class Question {
	/**
	 * Specifies max number of question answers.
	 */
	public static final byte MAX_ANSWERS=64;
	
	/**
	 * Array containing answers to this question.
	 */
    private ArrayList<String> answers;
	
	/**
	 * Question content.
	 */
    private final String content;
    
    /**
	 * Specifies type of field used by answers and thus question type itself. <p>
	 * 
     * Possible question types: <br>
     * 0 - multi choice (checkbox) <br>
     * 1 - single choice (radio) <br>
     * 2 - open-ended (input) <br>
     * 3 - single choice (combobox)
     */
    final private QuestionAnwserType questionType;
    
    public Question(String questionContent,QuestionAnwserType type, String... answer)
    {
        questionType=type;
        content=questionContent;
        if(answer.length>0)
        {
            answers.ensureCapacity(answer.length);
            for(int i=0;i<answer.length;i++)
                this.answers.add(answer[i]);
        }
        else
            this.answers=new ArrayList<>(4);
    }

    public Question(String questionContent,QuestionAnwserType questionType,ArrayList<String> answers) {
		this.questionType=questionType;
		content=questionContent;
		this.answers=answers;
    }
    
    public String getQuestionContent()
    {
        return content;
    }
    
    public int getAnswersQuantity()
    {
        return answers.size();
    }
    
    public String getAnswerN(int index)
    {
        return answers.get(index);
    }
    
    public ArrayList<String> getAnswers()
    {
        return answers;
    }
    
    public QuestionAnwserType getQuestionType()
    {
        return questionType;
    }
	
	public boolean addAnswer(String answer)
	{
		if(answers.size()<MAX_ANSWERS)
		{
			answers.add(answer);
			return true;
		}
		return false;
	}
}
