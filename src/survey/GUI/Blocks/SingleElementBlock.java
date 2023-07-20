package survey.GUI.Blocks;

import javax.swing.JComponent;

/**
 * Block which displays and stores single element of given type. But its not 
 * only limited to them, since all elements of {@code Component} type can be 
 * added to the layout however without their refference being stored.
 * 
 * @author ArtiFixal
 * @param <T> Type of stored element.
 */
public class SingleElementBlock<T extends JComponent> extends Block{
	/**
	 * Stored element
	 */
	public T element;
	
	public SingleElementBlock(String labelText){
		super(labelText);
	}
	
	public SingleElementBlock(T element) {
		super();
		this.element=element;
		addElement(element);
	}

	public SingleElementBlock(T element,String labelText) {
		super(labelText);
		this.element=element;
		addElement(element);
	}
	
	public T getElement()
	{
		return element;
	}
}
