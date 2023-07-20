package survey.GUI;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Element container which displays in flow direction as much as possible 
 * elements per one row based on given fixed element width.
 * 
 * @author ArtiFixal
 * @param <T> Type of stored elements
 */
public class WrappingContainer<T extends JComponent> extends JPanel{
	/**
	 * Width on which base are calculated max displayed elements per row.
	 * 
	 * @see #currentElementsPerRow
	 */
	public int singleElementWidth;
	
	/**
	 * Horizontal gap between elements.
	 */
	public int hgap;
	
	/**
	 * Vectical gap between elements.
	 */
	public int vgap;
	
	/**
	 * Number of elements currently displayed per row.
	 */
	private int currentElementsPerRow;
	
	/**
	 * Stores refference to added elements.
	 */
	protected ArrayList<T> elements;
	
	/**
	 * Refference to the main horizontal group.
	 */
	protected GroupLayout.ParallelGroup horizontal;
	
	/**
	 * Refference to the main vertical group.
	 */
	protected GroupLayout.SequentialGroup vertical;
	
	/**
	 * Parrent of this
	 */
	private final JFrame parrent;
	private GroupLayout layout;
	
	public WrappingContainer(JFrame parrent,int singleElementWidth) {
		this.parrent=parrent;
		this.singleElementWidth=singleElementWidth;
		hgap=8;
		vgap=8;
		initFields();
	}

	public WrappingContainer(JFrame parrent,int singleElementWidth,int hgap,int vgap) {
		this.parrent=parrent;
		this.singleElementWidth=singleElementWidth;
		this.hgap=hgap;
		this.vgap=vgap;
		initFields();
	}

	public ArrayList<T> getElements() {
		return elements;
	}
	
	public boolean isEmpty()
	{
		return elements.isEmpty();
	}

	public void setHgap(int hgap) {
		this.hgap=hgap;
	}

	public void setVgap(int vgap) {
		this.vgap=vgap;
	}
	
	/**
	 * Clears container without its update.
	 */
	public void clearContainer()
	{
		elements.clear();
	}

	public void setSingleElementWidth(int singleElementWidth) {
		this.singleElementWidth=singleElementWidth;
	}
	
	public void addElement(T element)
	{
		elements.add(element);
	}
	
	public void removeElement(T element)
	{
		elements.remove(element);
		remove(element);
	}
	
	private void initFields()
	{
		layout=new GroupLayout(this);
		setLayout(layout);
		elements=new ArrayList<>();
		parrent.addComponentListener(createResizeAction());
	}
	
	private int calcElementsInRow()
	{
		int elementsInRow=parrent.getWidth()/singleElementWidth;
		elementsInRow=(parrent.getWidth()-(elementsInRow*hgap))/singleElementWidth;
		return elementsInRow;
	}
	
	public void initLayout()
	{
		currentElementsPerRow=calcElementsInRow();
		GroupLayout.ParallelGroup horizontalContent=layout.createParallelGroup();
		horizontal=layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGap(hgap,hgap,Short.MAX_VALUE)
				.addGroup(horizontalContent)
				.addGap(hgap,hgap,Short.MAX_VALUE);
		vertical=layout.createSequentialGroup();
		vertical.addGap(22);
		for(int i=0;i<elements.size();i++)
		{
			GroupLayout.SequentialGroup rowHorizontal=layout.createSequentialGroup();
			GroupLayout.ParallelGroup rowVertical=layout.createParallelGroup();
			for(int j=0;i<elements.size()-1&&j<currentElementsPerRow-1;j++)
			{
				rowHorizontal.addComponent(elements.get(i),singleElementWidth,singleElementWidth,singleElementWidth)
						.addGap(hgap);
				rowVertical.addComponent(elements.get(i),GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
				i++;
			}
			rowHorizontal.addComponent(elements.get(i),singleElementWidth,singleElementWidth,singleElementWidth);
			rowVertical.addComponent(elements.get(i),GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			horizontalContent.addGroup(rowHorizontal);
			vertical.addGroup(rowVertical);
			vertical.addGap(vgap);
		}
		layout.setHorizontalGroup(horizontal);
		layout.setVerticalGroup(vertical);
	}
	
	/**
	 * Resizes container only if needed.
	 */
	public void resize()
	{
		if(calcElementsInRow()!=currentElementsPerRow)
		{
			update();
		}
	}
	
	/**
	 * Forces an container update even if no resize is needed.
	 */
	public void update()
	{
		removeAll();
		initLayout();
		revalidate();
		repaint();
	}
	
	private ComponentAdapter createResizeAction(){
		return new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				resize();
			}
		};
	}
}
