package survey.GUI;

import javax.swing.JComponent;

/**
 * Callback used to update GUI.
 *
 * @author ArtiFixal
 */
public interface Updateable {
	
	/**
	 * Adds given element to the GUI.
	 * 
	 * @param element Which element to add.
	 */
	public void addComponent(JComponent element);
	
	/**
	 * Removes given element from GUI.
	 * 
	 * @param element Which element to remove.
	 */
	public void removeComponent(JComponent element);
}
