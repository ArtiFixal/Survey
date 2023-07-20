package survey.GUI.Blocks;

import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import survey.GUI.BaseGUI;

/**
 * Simple container diplaying elements row by row. Uses {@code GroupLayout}.
 * 
 * @author ArtiFixal
 */
public abstract class Block extends JPanel{
	/**
	 * This block layout.
	 */
	protected GroupLayout layout;
	
	/**
	 * Group responsible for aligning and sizeing its elements in horizontal
	 * direction. Elements should be added here.
	 */
	protected GroupLayout.ParallelGroup mainHorizontalGroup;
	
	/**
	 * 
	 */
	protected GroupLayout.SequentialGroup mainVerticalGroup;
	
	public Block() {
		initGroups();
	}
	
	public Block(String topLabelText){
		initGroups(topLabelText);
	}
	
	/**
	 * Creates {@code JLabel} and adds it into JPanel in new line.
	 * 
	 * @param labelText
	 * @return Reference to created label
	 */
	public final JLabel createLabel(String labelText)
	{
		JLabel text=new JLabel(labelText);
		mainHorizontalGroup.addComponent(text);
		mainVerticalGroup.addComponent(text)
				.addGap(BaseGUI.DEFAULT_VERTICAL_GAP);
		return text;
	}
	
	/**
	 * Creates {@code JSeparator} and adds it into JPanel.
	 * 
	 * @return Reference to created separator
	 */
	public final JSeparator createSeparator()
	{
		JSeparator separator=new JSeparator();
		mainHorizontalGroup.addComponent(separator);
		mainVerticalGroup.addComponent(separator);
				makeVerticalGap(BaseGUI.DEFAULT_VERTICAL_GAP);
		return separator;
	}
	
	/**
	 * Creates and sets basic layout.
	 */
	private void initGroups()
	{
		layout=new GroupLayout(this);
		setLayout(layout);
		mainHorizontalGroup=layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		mainVerticalGroup=layout.createSequentialGroup();
		layout.setHorizontalGroup(mainHorizontalGroup);
		layout.setVerticalGroup(mainVerticalGroup);
	}
	
	/**
	 * Creates and sets basic layout with label on top of it.
	 * 
	 * @param topLabelText String which will be used as label.
	 */
	private void initGroups(String topLabelText)
	{
		initGroups();
		createLabel(topLabelText);
	}
	
	public GroupLayout.Group getHorizontalGroup()
	{
		return mainHorizontalGroup;
	}
	
	public GroupLayout.Group getVerticalGroup()
	{
		return mainVerticalGroup;
	}
	
	public void makeVerticalGap(int size)
	{
		makeVerticalGap(size,size,size);
	}
	
	public void makeVerticalGap(int size,int maxSize)
	{
		makeVerticalGap(size,size,maxSize);
	}
	
	public void makeVerticalGap(int minSize,int size,int maxSize)
	{
		mainVerticalGroup.addGap(minSize,size,maxSize);
	}
	
	/**
	 * Adds given element directly to the JPanel.
	 * 
	 * @param el Element to add.
	 */
	public void addElement(JComponent el)
	{
		addElement(el,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE);
	}
	
	/**
	 * Adds given element directly to the JPanel.
	 * 
	 * @param el Element to add.
	 * @param horizontalAlignment Horizontal aligment used on this element.
	 */
	public void addElement(JComponent el,GroupLayout.Alignment horizontalAlignment)
	{
		addElement(el,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE,horizontalAlignment);
	}
	
	/**
	 * Adds given element directly to the JPanel with static width and height.
	 * 
	 * @param el Element to add.
	 * @param width Width which will be set for the added element.
	 * @param height Height which will be set for the added element.
	 */
	public void addElement(JComponent el,int width,int height)
	{
		addElement(el,width,height,width,height,GroupLayout.Alignment.LEADING);
	}
	
	/**
	 * Adds given element directly to the JPanel with static width and height.
	 * 
	 * @param el Element to add.
	 * @param width Width which will be set for the added element.
	 * @param height Height which will be set for the added element.
	 * @param horizontalAlignment Horizontal aligment used on this element.
	 */
	public void addElement(JComponent el,int width,int height, GroupLayout.Alignment horizontalAlignment)
	{
		addElement(el,width,height,width,height,horizontalAlignment);
	}
	
	/**
	 * Adds given element directly to the JPanel with resizable width and height.
	 * 
	 * @param el Element to add.
	 * @param width Preffered element width.
	 * @param height Preffered element height.
	 * @param maxWidth Max resize width.
	 * @param maxHeight Max resize height.
	 * @param horizontalAlignment Horizontal aligment used on this element.
	 */
	public void addElement(JComponent el,int width,int height,int maxWidth, int maxHeight,GroupLayout.Alignment horizontalAlignment)
	{
		mainHorizontalGroup.addComponent(el,horizontalAlignment,width,width,maxWidth);
		mainVerticalGroup.addComponent(el,height,height,maxHeight);
				makeVerticalGap(BaseGUI.DEFAULT_VERTICAL_GAP);
	}
	
	/**
	 * Adds given groups to the JPanel, using groups allows more flexibility.
	 * 
	 * @param horizontalGroup The horizontal group containing element/s to add.
	 * @param verticalGroup The vertical group containing element/s to add.
	 */
	public void addElement(GroupLayout.Group horizontalGroup,GroupLayout.Group verticalGroup)
	{
		addElement(horizontalGroup,verticalGroup,GroupLayout.Alignment.LEADING);
	}
	
	/**
	 * Adds given groups to the JPanel, using groups allows more flexibility.
	 * 
	 * @param horizontalGroup The horizontal group containing element/s to add.
	 * @param verticalGroup The vertical group containing element/s to add.
	 * @param horizontalAlignment Horizontal aligment used on this element.
	 */
	public void addElement(GroupLayout.Group horizontalGroup,GroupLayout.Group verticalGroup,GroupLayout.Alignment horizontalAlignment)
	{
		mainHorizontalGroup.addGroup(horizontalAlignment,horizontalGroup);
		mainVerticalGroup.addGroup(verticalGroup);
	}
}
