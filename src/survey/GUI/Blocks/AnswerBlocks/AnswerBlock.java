package survey.GUI.Blocks.AnswerBlocks;

import javax.swing.JToggleButton;
import survey.GUI.Blocks.MultiElementBlock;

/**
 * Block which stores answers to the {@code Survey} single {@code Question}.
 * 
 * @author ArtiFixal
 * @param <T> Answer field type
 * 
 * @see survey.Survey
 * @see survey.Question
 */
public class AnswerBlock<T extends JToggleButton> extends MultiElementBlock<T> implements Answerable<Long>{
	
	public AnswerBlock(){
		super();
	}
	
	public AnswerBlock(String topLabelText){
		super(topLabelText);
	}
	
	public AnswerBlock(int answerQuantity){
		super(answerQuantity);
	}
	
	public AnswerBlock(String topLabelText,int answerQuantity){
		super(topLabelText,answerQuantity);
	}
	
	public boolean isAnythingSelected()
	{
		return getAnswer()>0;
	}

	@Override
	public Long getAnswer() {
		long answer=0;
		for(int i=0;i<elements.size();i++)
		{
			// Set bits to 1 where answer is selected
			if(elements.get(i).isSelected())
				answer|=(1l<<i);
		}
		return answer;
	}
}
