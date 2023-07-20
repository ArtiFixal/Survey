package survey.GUI;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import survey.Question;
import survey.Survey;
import survey.SurveyIO;
import survey.ElementBuilder;
import survey.SaveMethods.SurveySaveMethod;
import survey.SaveMethods.SurveySaveMethodFilter;

/**
 * Allows building user defined surveys.
 * 
 * @author ArtiFixal
 * @see survey.Survey
 */
public class SurveyBuilder implements ElementBuilder<Survey>{
    
	protected ArrayList<QuestionBuilder> questions;
    protected JTextField surveyName;
	protected GroupLayout layout;
	protected GroupLayout.ParallelGroup questionHorizontalGroup;
	protected GroupLayout.SequentialGroup questionVerticalGroup;
	protected static Tabbable callbacks;
	protected static Updateable surveyCreatrionCallback;
    protected JPanel tab;
    
    public SurveyBuilder(Tabbable callbacks,Updateable surveyCreatrionCallback)
    {
		tab=new JPanel();
		layout = new GroupLayout(tab);
		tab.setLayout(layout);
        surveyName=new JTextField("Enter name...");
        setSurveyName=new JButton("Set name");
		saveSurvey=new JButton("Save");
		close=new JButton("Close");
		if(SurveyBuilder.callbacks==null)
			SurveyBuilder.callbacks=callbacks;
		if(SurveyBuilder.surveyCreatrionCallback==null)
			SurveyBuilder.surveyCreatrionCallback=surveyCreatrionCallback;
		setSurveyName.addActionListener(createSetNameAction());
		saveSurvey.addActionListener(createSaveAction());
		close.addActionListener(createCloseAction());
        addQuestion=new JButton("Add new question");
        addQuestion.addActionListener(createQuestionAddAction());
		questions=new ArrayList<>();
		separators=new ArrayList<>();
		verticalJSeparators=new ArrayList<>();
		deleteQuestions=new ArrayList<>();
        initNewTab();
    }
    
    public JPanel getTab()
    {
        return tab;
    }
	
	public void addNewQuestion()
	{
		final JSeparator s=new JSeparator();
		final Box.Filler g=new Box.Filler(new Dimension(0,BaseGUI.DEFAULT_VERTICAL_GAP),new Dimension(0,BaseGUI.DEFAULT_VERTICAL_GAP),new Dimension(0,BaseGUI.DEFAULT_VERTICAL_GAP));
		final JButton b=new JButton("Delete question");
		b.addActionListener(createDeleteQuestionAction(questions.size()));
		final QuestionBuilder q=new QuestionBuilder(b,questions.size()+1);
		questionHorizontalGroup.addComponent(g).addComponent(s).addGroup(layout.createSequentialGroup()
				.addComponent(q.getContentPanel())
		);
		questionVerticalGroup.addComponent(g).addComponent(s,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(q.getContentPanel())
				);
		separators.add(g);
		verticalJSeparators.add(s);
		questions.add(q);
		deleteQuestions.add(b);
		tab.revalidate();
		tab.repaint();
	}
	
    private void initNewTab() {
		final JSeparator s=new JSeparator();
		verticalJSeparators.add(s);
		final QuestionBuilder q=new QuestionBuilder(questions.size()+1);
		questions.add(q);
		questionHorizontalGroup=layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(q.getContentPanel());
		questionVerticalGroup=layout.createSequentialGroup()
				.addComponent(q.getContentPanel());
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)//(GroupLayout.Alignment.LEADING)
				// Survey name
				.addGroup(layout.createSequentialGroup()
						.addContainerGap(28, Short.MAX_VALUE)
						.addComponent(surveyName, GroupLayout.DEFAULT_SIZE, BaseGUI.INPUT_DEFAULT_WIDTH, Short.MAX_VALUE)
						.addGap(BaseGUI.DEFAULT_HORIZONTAL_GAP)
						.addComponent(setSurveyName)
						.addContainerGap(28, Short.MAX_VALUE)
				)
				// Survey questions
				.addComponent(s)
				.addGroup(questionHorizontalGroup)
				// Add question button
				.addComponent(addQuestion,GroupLayout.Alignment.CENTER)
				.addGroup(GroupLayout.Alignment.CENTER,layout.createSequentialGroup()
						.addComponent(saveSurvey)
						.addGap(BaseGUI.DEFAULT_HORIZONTAL_GAP)
						.addComponent(close)
				)
		);
		// Survey name
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGap(12)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(surveyName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(setSurveyName)
				)
				.addGap(12)
				.addComponent(s,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
				// Survey questions
				.addGroup(questionVerticalGroup)
				// Add question button
				.addGap(12)
				.addComponent(addQuestion)
				.addGap(12)
				.addGroup(layout.createParallelGroup()
						.addComponent(saveSurvey)
						.addComponent(close)
				)
				.addGap(12)
		);
	}
	
	private ActionListener createQuestionAddAction()
	{
		return (e) -> {
			addNewQuestion();
		};
	}
	
	private ActionListener createDeleteQuestionAction(int index)
	{
		return (e) ->{
			tab.remove(separators.get(index-1));
			tab.remove(verticalJSeparators.get(index));
			tab.remove(questions.get(index).getContentPanel());
			tab.remove(deleteQuestions.get(index-1));
			deleteQuestions.get(index-1).removeActionListener(deleteQuestions.get(index-1).getActionListeners()[0]);
			for(int i=index;i<deleteQuestions.size();i++)
			{
				deleteQuestions.get(i).removeActionListener(deleteQuestions.get(i).getActionListeners()[0]);
				deleteQuestions.get(i).addActionListener(createDeleteQuestionAction(i));
			}
			separators.remove(index-1);
			verticalJSeparators.remove(index);
			questions.remove(index);
			deleteQuestions.remove(index-1);
			tab.revalidate();
			tab.repaint();
		};
	}
	
	private ActionListener createSaveAction(){
		return (e) ->{
			if(validate())
			{
				final File suggestedFileName=new File(SurveyIO.DEFAULT_SAVE_DIR+"/"+surveyName.getText());
				JFileChooser savePath=new JFileChooser(SurveyIO.DEFAULT_SAVE_DIR);
				savePath.setSelectedFile(suggestedFileName);
				final SurveySaveMethod[] availableFilters=SurveyIO.getSupportedSaveMethods();
				for(int i=0;i<availableFilters.length;i++)
				{
					savePath.addChoosableFileFilter(new SurveySaveMethodFilter(availableFilters[i]));
				}
				savePath.setAcceptAllFileFilterUsed(false);
				if(savePath.showSaveDialog(tab)==JFileChooser.APPROVE_OPTION)
				{
					Survey toSave=build();
					SurveyIO w;
					if(savePath.getFileFilter() instanceof SurveySaveMethodFilter method)
						w=new SurveyIO(method.getRelatedSaveMethod());
					else
						w=new SurveyIO();
					try {
						w.write(toSave);
						SurveyElementAdmin newGuiElement=new SurveyElementAdmin(new File(SurveyIO.DEFAULT_SAVE_DIR+"/"+toSave.getName()+w.getSurveySaveMethod().getFileExtension()),callbacks,surveyCreatrionCallback);
						callbacks.closeTab();
						surveyCreatrionCallback.addComponent(newGuiElement);
					}catch(Exception ex){
						JOptionPane.showMessageDialog(tab, "An error: "+ex.toString()+" occured during saving survey.","Error: "+toSave.getName(),JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		};
	}
	
	private ActionListener createSetNameAction()
	{
		return (e)->{
			callbacks.renameTab(surveyName.getText());
		};
	}
	
	private ActionListener createCloseAction()
	{
		return (e)->{
			callbacks.closeTab();
		};
	}
	
	public boolean validate()
	{
		boolean ok=true;
		if(Validator.isEmptyOrDefault(surveyName,"Enter name..."))
		{
			BaseGUI.setErrorOutline(surveyName);
			ok=false;
		}
		else
		{
			BaseGUI.restorePreviousBorder(surveyName);
		}
		for(int i=0;i<questions.size();i++)
		{
			if(!questions.get(i).validate())
				ok=false;
		}
		return ok;
	}
	
	@Override
    public Survey build() {
        ArrayList<Question> q=new ArrayList<>(questions.size());
		for(int i=0;i<questions.size();i++)
		{
			q.add(questions.get(i).build());
		}
		return new Survey(surveyName.getText(),q);
    }

	@Override
	public boolean readFrom(Survey element) {
		surveyName.setText(element.getName());
		questions.get(0).readFrom(element.getQuestionN(0));
		for(int i=1;i<element.getQuestions().size();i++)
		{
			addNewQuestion();
			questions.get(i).readFrom(element.getQuestionN(i));
		}
		return true;
	}
	
	final private JButton setSurveyName;
	final private JButton saveSurvey;
	final private JButton close;
    protected JButton addQuestion;
	final private ArrayList<JButton> deleteQuestions;
	final private ArrayList<Box.Filler> separators;
	final private ArrayList<JSeparator> verticalJSeparators;
}
