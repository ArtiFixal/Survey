package survey.GUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import survey.ConfigIO;
import survey.ConfigOptionNotFoundException;
import survey.SurveyIO;

/**
 * Connects program logic with pollster GUI.
 * 
 * @author ArtiFixal
 */
public class PollsterApp extends BaseGUI{

	public PollsterApp() {
		super("Pollster panel");
		setMinimumSize(new Dimension(450,300));
		setPreferredSize(new Dimension(450,300));
		wrappingContainer.setSingleElementWidth(204);
		wrappingContainer.resize();
		miNewSurvey=new JMenuItem("New survey");
		miChooseDefaultResultSaveMethod=new JMenuItem("Choose default result save method");
		fileMenu.insert(miNewSurvey,0);
		fileMenu.add(miChooseDefaultResultSaveMethod);
		miNewSurvey.addActionListener(createSurveyBuilderTabAction());
		miChooseDefaultResultSaveMethod.addActionListener(createChooseDefaultResultSaveMethod());
		wrappingContainer.update();
		setVisible(true);
	}
	
	/**
     * @param args the command line arguments
     */
	public static void main(String args[]) {
		setupDarkLaf();
        java.awt.EventQueue.invokeLater(PollsterApp::new);
	}

	@Override
	protected SurveyElement createNewSurveyElement(File f) {
		return new SurveyElementAdmin(f,this,this);
	}
	
	/**
	 * Asks user what save method should be used.
	 * 
	 * @param dialogParrent Element on top of which dialog will be displayed.
	 * @param message Dialog message.
	 * @param dialogTitle Title displayed on dialog window.
	 * 
	 */
	public void chooseSaveMethod(Component dialogParrent,String message,String dialogTitle)
	{
		final Object[] methods=SurveyIO.getSupportedSaveMethods();
		Object chosen=JOptionPane.showInputDialog(dialogParrent,message,dialogTitle,JOptionPane.PLAIN_MESSAGE,null,methods,methods[0]);
		if(chosen!=null)
		{
			int index=0;
			for(int i=0;i<methods.length;i++)
			{
				if(chosen==methods[i])
				{
					index=i;
					break;
				}
			}
			ConfigIO c=new ConfigIO();
			try{
				String newOptionValue=Integer.toString(index);
				c.replaceConfigOption(ConfigIO.RESULT_SAVE_METHOD_OPTION_NAME,newOptionValue);
			}catch(IOException e){
				BaseGUI.showErrorMessage(this,"An error occuerd during reading config file: "+e.getMessage(),"Error");
			}catch(ConfigOptionNotFoundException e){
				BaseGUI.showErrorMessage(this,"Given config option: "+e.getMessage(),"Error");
			}
		}
	}

	private ActionListener createSurveyBuilderTabAction()
	{
		return (e)->{
			SurveyBuilder builder=new SurveyBuilder(this,this);
			JScrollPane scroll=new JScrollPane(builder.getTab(),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scroll.setBorder(null);
			tabs.addTab("New survey",scroll);
			tabs.setSelectedComponent(scroll);
		};
	}
	
	private ActionListener createChooseDefaultResultSaveMethod()
	{
		return (e)->{
			chooseSaveMethod(rootPane,"Default save method: ","Choose default results save method");
		};
	}
	
	private JMenuItem miNewSurvey;
	private JMenuItem miChooseDefaultResultSaveMethod;
}
