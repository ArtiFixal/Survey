package survey.GUI;

import javax.swing.JPanel;

/**
 * Interface used as callback. Provides methods allowing to interact with 
 * {@code JTabbedPane} from descendant elements.
 * 
 * @author ArtiFixal
 */
public interface Tabbable {
	public void addNewTab(JPanel p,String name);
	public void renameTab(String tabName);
	public void closeTab();
}
