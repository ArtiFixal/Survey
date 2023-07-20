package survey.GUI;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import survey.NoResultsException;
import survey.Survey;
import survey.SurveyIO;

/**
 * Pollster equivalent of panel representing {@code Survey}. It allows pollster
 * to solve/preview, edit, delete and display results of the {@code Survey}.
 * 
 * @author ArtiFixal
 */
public class SurveyElementAdmin extends SurveyElement{
	private static Updateable updateCallbacks;
	
	public SurveyElementAdmin(File pathToSurvey,Tabbable destination,Updateable updateCallbacks) {
		super(pathToSurvey,destination);
		if(SurveyElementAdmin.updateCallbacks==null)
			SurveyElementAdmin.updateCallbacks=updateCallbacks;
		init();
	}
	
	private void init()
	{
		edit=new JButton("Edit");
		edit.addActionListener(createEditAction());
		delete=new JButton("Delete");
		delete.addActionListener(createDeleteAction());
		results=new JButton("Show results");
		results.addActionListener(createResultsAction());
	}

	@Override
	public void initLayoutGroups() {
		initLayoutGroups(new ButtonPlacement() {
			@Override
			public GroupLayout.Group getHorizontalGroup() {
				GroupLayout.ParallelGroup horizontal=layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addGroup(layout.createSequentialGroup()
								.addComponent(solve)
								.addGap(BaseGUI.DEFAULT_HORIZONTAL_GAP)
								.addComponent(edit)
						)
						.addComponent(delete)
						.addComponent(results);
				return horizontal;
			}

			@Override
			public GroupLayout.Group getVerticalGroup() {
				GroupLayout.SequentialGroup vertical=layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup()
								.addComponent(solve)
								.addComponent(edit)
						)
						.addGap(BaseGUI.DEFAULT_VERTICAL_GAP)
						.addComponent(delete)
						.addGap(BaseGUI.DEFAULT_VERTICAL_GAP)
						.addComponent(results)
						.addGap(BaseGUI.DEFAULT_VERTICAL_GAP);
				return vertical;
			}
		});
	}
	
	private ActionListener createEditAction()
	{
		return (e)->{
			SurveyBuilder builder=new SurveyBuilder(destination,updateCallbacks);
			SurveyIO r=new SurveyIO();
			try {
				Survey s=r.read(pathToSurvey);
				builder.readFrom(s);
				destination.addNewTab(builder.getTab(),surveyName.getText());
			}catch(Exception ex) {
				BaseGUI.showErrorMessage(getRootPane(),"An error occured during loading survey to edit: "+ex.getMessage(),"Error");
			}			
		};
	}
	
	private ActionListener createDeleteAction()
	{
		return (e)->{
			if(BaseGUI.askYesNo(getRootPane(),"Are you sure you want to delete survey: "+pathToSurvey.getName()+"?","Survey delete"))
				try{
					Files.delete(pathToSurvey.toPath());
					updateCallbacks.removeComponent(this);
				}catch(IOException ex){
					BaseGUI.showErrorMessage(getRootPane(),"An error occurred during deletion of survey: "+ex.getMessage(),"Error");
				}
		};
	}
	
	private ActionListener createResultsAction()
	{
		return (e)->{
			try{
				SurveyIO reader=new SurveyIO();
				Survey s=reader.read(pathToSurvey);
				ResultsRender results=new ResultsRender(s,destination);
				destination.addNewTab(results,s.getName());
			}catch(NoResultsException ex){
				BaseGUI.showMessage(getRootPane(),"Survey: "+ex.getSurvey().getName()+" have no results yet.","Info");
			}
			catch(Exception ex){
				BaseGUI.showErrorMessage(getRootPane(),"An error occurred during reading survey results: "+ex.getMessage(),"Error");
			}
		};
	}
	
	private JButton edit;
	private JButton delete;
	private JButton results;
}
