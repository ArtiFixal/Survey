package survey.GUI.Blocks.AnswerBlocks;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

/**
 * Block which stores self exclusion answers to the {@code Survey} 
 * {@code Question}.
 * 
 * @author ArtiFixal
 * 
 * @see survey.Survey
 * @see survey.Question
 */
public class ExclusionAnswerBlock extends AnswerBlock<JRadioButton>{
	
	/**
	 * Exclusion group to which will belong new added buttons.
	 */
	ButtonGroup group;
	
	public ExclusionAnswerBlock() {
		super();
		group=new ButtonGroup();
	}
	
	public ExclusionAnswerBlock(String topLabelText){
		this();
		createLabel(topLabelText);
	}
	
	public ExclusionAnswerBlock(int answerQuantity) {
		super(answerQuantity);
		group=new ButtonGroup();
	}
	
	public ExclusionAnswerBlock(String topLabelText,int answerQuantity){
		super(topLabelText,answerQuantity);
		group=new ButtonGroup();
	}

	@Override
	public void addNewElement(JRadioButton el,boolean createSeparator) {
		super.addNewElement(el,createSeparator);
		group.add(el);
	}
}
