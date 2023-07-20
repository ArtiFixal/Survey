package survey.GUI.Blocks.AnswerBlocks;

import javax.swing.JComponent;
import survey.GUI.Blocks.SingleElementBlock;

/**
 * Block which stores single answer to the {@code Survey} {@code Question}.
 * 
 * @author ArtiFixal
 * @param <T> Type of stored answer.
 * 
 * @see survey.Survey
 * @see survey.Question
 */
public class SingleElementAnswerBlock<T extends JComponent> extends SingleElementBlock<T> implements Answerable<T>{
	
	public SingleElementAnswerBlock(T element) {
		super(element);
	}
	
	public SingleElementAnswerBlock(T element,String labelText)
	{
		super(element,labelText);
	}

	/**
	 * @return Element containing answer.
	 */
	@Override
	public T getAnswer() {
		return getElement();
	}
	
}
