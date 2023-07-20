package survey.GUI.Blocks;

import java.util.ArrayList;
import javax.swing.JComponent;

/**
 * Block which displays and stores multiple elements of the same type in
 * {@code ArrayList}. But its not only limited to them, since all elements of
 * {@code Component} type can be added to the layout however without their 
 * refference being stored in the {@code ArrayList}.
 * 
 * @author ArtiFixal
 * @param <T> Type of stored elements.
 */
public class MultiElementBlock<T extends JComponent> extends Block{
	/**
	 * Stores displayed elements.
	 */
	public ArrayList<T> elements;
	
	public MultiElementBlock() {
		super();
		elements=new ArrayList<>(0);
	}
	
	public MultiElementBlock(String topLabelText){
		this();
		createLabel(topLabelText);
	}
	
	public MultiElementBlock(int elementQuantity){
		super();
		createNewArray(elementQuantity);
	}
	
	public MultiElementBlock(String topLabelText,int elementQuantity){
		super(topLabelText);
		createNewArray(elementQuantity);
	}

	public ArrayList<T> getStoredElements() {
		return elements;
	}
	
	public T getStoredNElement(int index)
	{
		return elements.get(index);
	}
	
	private void createNewArray(int elementQuantity)
	{
		if(elementQuantity<0)
			throw new IllegalArgumentException("Illegal array size: "+elementQuantity);
		elements=new ArrayList<>(elementQuantity);
	}
	
	/**
	 * Stores and adds new element to GUI.
	 * 
	 * @param el Element to add.
	 */
	public void addNewElement(T el)
	{
		addNewElement(el,false);
	}
	
	/**
	 * Stores and adds new element to GUI.
	 * 
	 * @param el Element to add.
	 * @param createSeparator Defines if element below should be separated
	 */
	public void addNewElement(T el,boolean createSeparator)
	{
		elements.add(el);
		addElement(el);
		if(createSeparator)
			createSeparator();
	}
}
