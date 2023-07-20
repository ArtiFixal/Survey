package survey.GUI;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import survey.ConfigIO;
import survey.ResultIO;
import survey.SaveMethods.AsZipFile;
import survey.SaveMethods.ResultSaveMethod;
import survey.SurveyIO;

/**
 * Connects program logic with user GUI.
 * 
 * @author ArtiFixal
 */
public class BaseGUI extends JFrame implements Tabbable,Updateable{
	public static final int INPUT_DEFAULT_WIDTH=270;
    public static final int DEFAULT_VERTICAL_GAP=8;
	public static final int DEFAULT_HORIZONTAL_GAP=8;
	
	/**
	 * Border used as outline in {@code CompoundBorder} indicating
	 * that an error occurred.
	 * 
	 * @see CompoundBorder
	 */
	public final static LineBorder ERROR_BORDER=new LineBorder(Color.RED, 1, true);
	
	/**
	 * Container storing surveys possible to open.
	 */
	protected final WrappingContainer<SurveyElement> wrappingContainer;
	
	/**
	 * Method in which survey results will be saved.
	 */
	protected static ResultSaveMethod resultsSaveMethod;
	
    /**
     * Creates new BaseGUI frame
	 * 
	 * @param title
     */
    public BaseGUI(String title) {
		super(title);
        initComponents();
        setLocationRelativeTo(null);
		wrappingContainer = new WrappingContainer<>(this,140,8,8);
		loadAvailableSurveys();
		wrappingContainer.initLayout();
		container.setLayout(new BorderLayout());
		JScrollPane containerScrollPane=new JScrollPane(wrappingContainer,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		containerScrollPane.setBorder(new MatteBorder(1,0,0,0,new Color(80,82,84)));
		container.add(containerScrollPane,BorderLayout.CENTER);
		ConfigIO config=new ConfigIO();
		try{
			String option=config.loadConfigOption(ConfigIO.RESULT_SAVE_METHOD_OPTION_NAME);
			resultsSaveMethod=ResultIO.interpertResultSaveMethodFromNumber(Integer.parseInt(option));
		}catch(Exception e){
			System.err.println("An error occured during config read: '"+e+"' - Fallbacking to Zip save method.");
			resultsSaveMethod=new AsZipFile();
		}
	}
	
	@Override
	public void addNewTab(JPanel p,String name) {
		JScrollPane enclosedPanel=new JScrollPane(p,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		tabs.addTab(name,enclosedPanel);
		tabs.setSelectedComponent(enclosedPanel);
	}
	
	@Override
	public void renameTab(String tabName) {
		tabs.setTitleAt(tabs.getSelectedIndex(),tabName);
	}

	@Override
	public void closeTab() {
		tabs.remove(tabs.getSelectedIndex());
	}

	@Override
	public void addComponent(JComponent element) {
		if(element instanceof SurveyElement se)
		{
			wrappingContainer.addElement(se);
		}
		else
			throw new IllegalArgumentException("Only SurveyElement objects can be added");
	}
	
	@Override
	public void removeComponent(JComponent element) {
		wrappingContainer.removeElement((SurveyElement)element);
	}
	
	public static ResultSaveMethod getResultsSaveMethod()
	{
		return resultsSaveMethod;
	}
	
	/**
	 * Changes component border to {@code CompoundBorder} builded from
	 * two borders, indicating that error have been encountered.<br>
	 * Internal - previous one <br>
	 * External - red outline
	 * 
	 * @param c Component on which to apply border
	 * 
	 * @see CompoundBorder
	 * @see #ERROR_BORDER
	 */
	public static void setErrorOutline(JComponent c)
	{
		if(!(c.getBorder() instanceof CompoundBorder))
			c.setBorder(new CompoundBorder(ERROR_BORDER,c.getBorder()));
	}
	
	/**
	 * Restores previous internal border from {@code CompoundBorder}.
	 * 
	 * @param c Component on which restore border
	 * @see CompoundBorder
	 */
	public static void restorePreviousBorder(JComponent c)
	{
		if(c.getBorder() instanceof CompoundBorder b)
		{
			c.setBorder(b.getInsideBorder());
		}
	}
	
	public static String uppercaseFirstLetter(String s)
	{
		return s.substring(0,1).toUpperCase()+s.substring(1);
	}
	
	/**
	 * Displays yes/no dialog with given text with what user will interact.
	 * 
	 * @param dialogParrent Element on top of which dialog will be displayed.
	 * @param message Dialog message.
	 * @param dialogTitle Title displayed on dialog window.
	 * 
	 * @return True if user chosen yes, false otherwise.
	 */
	public static boolean askYesNo(Component dialogParrent,String message,String dialogTitle)
	{
		return JOptionPane.showConfirmDialog(dialogParrent,message,dialogTitle,JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION;
	}
	
	/**
	 * Displays plain message to the user.
	 * 
	 * @param dialogParrent Element on top of which dialog will be displayed.
	 * @param message Dialog message.
	 * @param dialogTitle Title displayed on dialog window.
	 */
	public static void showMessage(Component dialogParrent,String message,String dialogTitle)
	{
		JOptionPane.showMessageDialog(dialogParrent,message,dialogTitle,JOptionPane.PLAIN_MESSAGE);
	}
	
	/**
	 * Displays message indicating error.
	 * 
	 * @param dialogParrent Element on top of which dialog will be displayed.
	 * @param message Dialog message.
	 * @param dialogTitle Title displayed on dialog window.
	 */
	public static void showErrorMessage(Component dialogParrent,String message,String dialogTitle)
	{
		JOptionPane.showMessageDialog(dialogParrent,message,dialogTitle,JOptionPane.ERROR_MESSAGE);
	}
	
	private void loadAvailableSurveys()
	{
		if(SurveyIO.DEFAULT_SAVE_DIR.exists())
		{
			File[] files=SurveyIO.DEFAULT_SAVE_DIR.listFiles();
			SurveyIO reader=new SurveyIO();
			ExecutorService tp=Executors.newFixedThreadPool(files.length);
			for(int i=0;i<files.length;i++)
				tp.submit(createTask(reader,files[i]));
			tp.shutdown();
			try {
				while(!tp.isTerminated())
				{
					tp.awaitTermination(50, TimeUnit.MILLISECONDS);
				}
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}
	}
	
	protected SurveyElement createNewSurveyElement(File f)
	{
		return new SurveyElement(f,this);
	}
	
	private Runnable createTask(SurveyIO reader,File f)
	{
		return ()->{
			if(reader.testSurveyFile(f))
			{
				SurveyElement el=createNewSurveyElement(f);
				el.initLayoutGroups();
				synchronized (wrappingContainer) {
					wrappingContainer.addElement(el);
					wrappingContainer.revalidate();
					wrappingContainer.repaint();
				}
			}
		};
	}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabs = new javax.swing.JTabbedPane();
        searchTab = new javax.swing.JPanel();
        searchButton = new javax.swing.JButton();
        surveySearchField = new JPlaceholderTextField("Enter survey name...");
        container = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        miReloadSurveys = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(404, 300));
        setPreferredSize(new java.awt.Dimension(404, 300));

        tabs.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout containerLayout = new javax.swing.GroupLayout(container);
        container.setLayout(containerLayout);
        containerLayout.setHorizontalGroup(
            containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        containerLayout.setVerticalGroup(
            containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 178, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout searchTabLayout = new javax.swing.GroupLayout(searchTab);
        searchTab.setLayout(searchTabLayout);
        searchTabLayout.setHorizontalGroup(
            searchTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchTabLayout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addComponent(surveySearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchButton)
                .addContainerGap(28, Short.MAX_VALUE))
            .addComponent(container, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        searchTabLayout.setVerticalGroup(
            searchTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchTabLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(searchTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchButton)
                    .addComponent(surveySearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addComponent(container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabs.addTab("Search", searchTab);

        fileMenu.setText("File");

        miReloadSurveys.setText("Reload surveys");
        miReloadSurveys.setToolTipText("");
        miReloadSurveys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miReloadSurveysActionPerformed(evt);
            }
        });
        fileMenu.add(miReloadSurveys);

        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void reloadWrappingContainer()
	{
		wrappingContainer.clearContainer();
		loadAvailableSurveys();
		wrappingContainer.update();
	}
	
    private void miReloadSurveysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miReloadSurveysActionPerformed
        reloadWrappingContainer();
    }//GEN-LAST:event_miReloadSurveysActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
		if(surveySearchField.isTextPlaceholder())
		{
			reloadWrappingContainer();
		}
		else
		{
			if(wrappingContainer.isEmpty())
				reloadWrappingContainer();
			for(int i=0;i<wrappingContainer.getElements().size();i++)
			{
				if(!wrappingContainer.getElements().get(i).getSurveyName().contains(surveySearchField.getText()))
				{
					wrappingContainer.getElements().remove(i);
				}
			}
			wrappingContainer.update();
			if(wrappingContainer.isEmpty())
				showMessage(this,"Surveys containing: "+surveySearchField.getText()+" not found.","Search results");
		}
    }//GEN-LAST:event_searchButtonActionPerformed

	/**
	 * Setups DarkLaf L&F.
	 */
	protected final static void setupDarkLaf()
	{
		FlatDarkLaf.setup();
         try{
            javax.swing.UIManager.setLookAndFeel(new FlatDarkLaf());
        }catch(javax.swing.UnsupportedLookAndFeelException e){
             System.out.println("Setting up FlatLaf L&F failed: "+e.getMessage());
        }
	}
	
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
		setupDarkLaf();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
            public void run() {
                new BaseGUI("Surveys").setVisible(true);
            }
        });
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel container;
    protected javax.swing.JMenu fileMenu;
    protected javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem miReloadSurveys;
    private javax.swing.JButton searchButton;
    private javax.swing.JPanel searchTab;
    private survey.GUI.JPlaceholderTextField surveySearchField;
    protected javax.swing.JTabbedPane tabs;
    // End of variables declaration//GEN-END:variables
}
