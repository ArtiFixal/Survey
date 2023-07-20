package survey.GUI;

import javax.swing.text.JTextComponent;
import survey.GUI.Blocks.AnswerBlocks.AnswerBlock;
import survey.GUI.Blocks.SingleElementBlock;

/**
 * Utility class responsible for validation of user input it the GUI.
 * 
 * @author ArtiFixal
 */
public class Validator {
	
	private Validator()
	{
		throw new AssertionError("Not instantiable class");
	}
	
	public static boolean isEmpty(JTextComponent text)
	{
		return text.getText().isBlank();
	}
	
	public static boolean isEmptyOrDefault(JTextComponent text,String defaultText)
	{
		return isEmpty(text)||text.getText().equals(defaultText);
	}
	
	/**
	 * Checks if user selected any answer in block.
	 * 
	 * @param block
	 * @return Is anything selected?
	 */
	public static boolean validateBlock(AnswerBlock block)
	{
		boolean valid=true;
		if(!block.isAnythingSelected())
		{
			BaseGUI.setErrorOutline(block);
			valid=false;
		}
		else
		{
			BaseGUI.restorePreviousBorder(block);
		}
		return valid;
	}
	
	/**
	 * Checks if user entered anything in block {@code JTextComponent}.
	 *
	 * @param block
	 * @return Is input valid?
	 */
	public static boolean validateBlock(SingleElementBlock<? extends JTextComponent> block)
	{
		boolean valid=true;
		JTextComponent element=block.getElement();
		if(isEmpty(element))
		{
			BaseGUI.setErrorOutline(block);
			valid=false;
		}
		else
		{
			BaseGUI.restorePreviousBorder(block);
		}
		return valid;
	}
	
	/**
	 * Checks if user input in block {@code JTextComponent} is valid number.<br>
	 * 
	 * @param block Block in which we are checking user input.
	 * @param exceptedFloatingPointNumber Should number be interpered as floating point?
	 * @return Is input valid?
	 */
	public static boolean validateBlock(SingleElementBlock<? extends JTextComponent> block,boolean exceptedFloatingPointNumber)
	{
		boolean valid=true;
		if(!validateBlock(block))
		{
			JTextComponent element=block.getElement();
			try {
				if(exceptedFloatingPointNumber)
					Float.valueOf(element.getText());
				else
					Integer.valueOf(element.getText());
				BaseGUI.restorePreviousBorder(block);
			}catch(NumberFormatException e){
				valid=false;
				BaseGUI.setErrorOutline(block);
			}
		}
		else
			return false;
		return valid;
	}
	
}
