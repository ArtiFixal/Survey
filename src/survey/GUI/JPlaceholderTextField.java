package survey.GUI;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 * Input text field with functionality of a placeholder.
 * 
 * @author ArtiFixal
 */
public class JPlaceholderTextField extends JTextField{
    
	/**
	 * Text displayed as placeholder.
	 */
    public final String placeholderText;
    
	/**
	 * Indicates that placeholder text is being currently displayed.
	 */
    private boolean isTextPlaceholder;
    
    public JPlaceholderTextField() {
        this("Enter text...");
    }
	
	/**
	 * @param placeholderText Text which will be displayed as placeholder.
	 */
	public JPlaceholderTextField(String placeholderText) {
		super(placeholderText);
		this.placeholderText=placeholderText;
		isTextPlaceholder=true;
		addFocusListener(createFocusLostAction());
        addKeyListener(createPlaceholderAction());
	}
	
	/**
	 * Checks if placeholder text is being currently displayed.
	 * 
	 * @return True if placeholder is displayed, false otherwise.
	 */
	public boolean isTextPlaceholder()
	{
		return isTextPlaceholder;
	}
    
	/**
	 * Handles main placeholder functionality.
	 * 
	 * @return Action perfored on key press.
	 */
    private KeyAdapter createPlaceholderAction(){
        return new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                if(isTextPlaceholder&&(Character.isLetterOrDigit(e.getKeyChar())||e.getKeyCode()==KeyEvent.VK_BACK_SPACE))
                {
                    setText("");
                    isTextPlaceholder=false;
                }
                else if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE)
                {
                    if(getText().length()<=1)
                    {
                        isTextPlaceholder=true;
                        setText(placeholderText+placeholderText.charAt(placeholderText.length()-1));
                        
                    }
                }
            }
        };
    }
	
	/**
	 * Restores placeholder if text field is blank and lost user focus.
	 * 
	 * @return Action performed on text field focus lost.
	 */
	private FocusAdapter createFocusLostAction(){
		return new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(getText().isBlank())
				{
					setText(placeholderText);
					isTextPlaceholder=true;
				}
			}
		};
	}
}
