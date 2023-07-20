package survey;

import java.util.ArrayList;

/**
 * Stores respondent data and his answers to {@code Survey}.
 * 
 * @author ArtiFixal
 * @see Survey
 */
public class SurveyResult {
	/**
	 * Person who took part in the survey.
	 */
	public User respondent;
	
	/**
	 * Stores answers to {@code Survey} questions.<p>
	 * 
	 * Possible answer types: <br>
	 * Long - answer to closed question.<br>
	 * String - answer to open-ended question.
	 */
	private final ArrayList<Object> answers;

	public SurveyResult(User respondent)
	{
		this.respondent=respondent;
		this.answers=new ArrayList<>();
	}
	
	public SurveyResult(boolean sex,Education edu,Voivodeship voi,AgeGroup age,Settlement livingIn) {
		respondent=new User(sex,edu,voi,age,livingIn);
		this.answers=new ArrayList<>();
	}

	public User getRespondent() {
		return respondent;
	}
	
	public void addAnswer(Object answer)
	{
		answers.add(answer);
	}

	public ArrayList getAnswers() {
		return answers;
	}
	
	public Object getNAnswer(int index)
	{
		return answers.get(index);
	}
	
	public boolean isAnswerString(Object answer)
	{
		return answer instanceof String;
	}
	
	public boolean isAnswerLong(Object answer)
	{
		return answer instanceof Long;
	}
	
	/**
	 * Checks if nth answer is {@code String} type answer.
	 * 
	 * @param index Which answer.
	 * @return True if it is, false otherwise.
	 */
	public boolean isNAnswerString(int index)
	{
		return isAnswerString(getNAnswer(index));
	}
	
	/**
	 * Checks if nth answer is {@code Long} answer.
	 * 
	 * @param index Which answer.
	 * @return True if it is, false otherwise.
	 */
	public boolean isNAnswerLong(int index)
	{
		return isAnswerLong(getNAnswer(index));
	}
	
	/**
	 * Since selected answer/s to a closed question is represented as long value
	 * with toggled bits equivalent to selected answer/s, we check if in the 
	 * given answer, the passed {@code int} representing nth question index
	 * is toggled.
	 * 
	 * @param answer Answer in which we test selection.
	 * @param answerBitIndex Nth bit corresponding to the index of answer
	 * we want to test the selection.
	 * 
	 * @return True if answer of given index is selected, fasle otherwise.
	 * 
	 * @throws NotAnAnswerOfTypeLongException If given answer is not type of
	 * long.
	 * @throws IndexOutOfBoundsException If index is negaitve or greater than
	 * {@link Question#MAX_ANSWERS}.
	 * 
	 * @see Question#MAX_ANSWERS
	 */
	public boolean isAnswerSelected(Object answer,int answerBitIndex) throws NotAnAnswerOfTypeLongException
	{
		if(!isAnswerLong(answer))
			throw new NotAnAnswerOfTypeLongException(answer,answerBitIndex);
		if(answerBitIndex<0)
			throw new IndexOutOfBoundsException("Index can't be negative number!");
		if(answerBitIndex>Question.MAX_ANSWERS)
			throw new IndexOutOfBoundsException("Answer max index equals: "+Question.MAX_ANSWERS);
		return isAnswerSelected((Long)answer,answerBitIndex);
	}
	
	/**
	 * Since selected answer/s to a closed question is represented as long value
	 * with toggled bits equivalent to selected answer/s, we check if in the 
	 * given answer, the passed {@code int} representing nth question index
	 * is toggled.
	 * 
	 * @param answer Answer in which we test selection.
	 * @param answerBitIndex Nth bit corresponding to the index of answer
	 * we want to test the selection.
	 * 
	 * @return True if answer of given index is selected, fasle otherwise.
	 * 
	 * @throws IndexOutOfBoundsException If index is negaitve or greater than
	 * {@link Question#MAX_ANSWERS}.
	 * 
	 * @see Question#MAX_ANSWERS
	 */
	public boolean isAnswerSelected(Long answer,int answerBitIndex)
	{
		if(answerBitIndex<0)
			throw new IndexOutOfBoundsException("Index can't be negative number!");
		if(answerBitIndex>Question.MAX_ANSWERS)
			throw new IndexOutOfBoundsException("Answer max index equals: "+Question.MAX_ANSWERS);
		// Allow unsigned
		return ((answer>>>(answerBitIndex)&1l)==1);
	}
}
