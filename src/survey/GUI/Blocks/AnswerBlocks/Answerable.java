package survey.GUI.Blocks.AnswerBlocks;

/**
 * Interface used in {@code Block} containers holding answers to survey questions.
 * Primary purpose of this interface is to indicate that block which uses it
 * will return answer of specified type.
 * 
 * @author ArtiFixal
 * @param <T> Answer type
 * 
 * @see survey.GUI.Blocks.Block
 * @see survey.GUI.Blocks.AnswerBlock
 * @see survey.GUI.Blocks.SingleElementAnswerBlock
 */
public interface Answerable<T> {
	
	/**
	 * @return Answer to the question.
	 */
	public T getAnswer();
}
