package survey.GUI;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import survey.Survey;
import survey.SurveyIO;

/**
 * Panel repesenting {@code Survey} in GUI. Displays its name and contains 
 * the button responsible for opening the survey to solve.
 * 
 * @author ArtiFixal
 */
public class SurveyElement extends JPanel{
	public final static Color DEFAULT_BACKGROUND=new Color(70,73,75);
	public final static Color DEFAULT_FOREGROUND=new Color(187,187,187);
	public static Tabbable destination;
	protected GroupLayout layout;
	protected File pathToSurvey;
	
	public SurveyElement(File pathToSurvey,Tabbable destination) {
		if(SurveyElement.destination==null)
			SurveyElement.destination=destination;
		this.pathToSurvey=pathToSurvey;
		init();
		surveyName.setText(SurveyIO.removeFileExtension(pathToSurvey.getName()));
		solve.addActionListener(createSolveAction());
	}
	
	private void init()
	{
		setBorder(new LineBorder(new Color(102,102,102), 1));
		this.surveyName=new JTextPane();
		surveyName.setEnabled(false);
		surveyName.setBackground(DEFAULT_BACKGROUND);
		surveyName.setDisabledTextColor(DEFAULT_FOREGROUND);
		StyledDocument doc=surveyName.getStyledDocument();
		SimpleAttributeSet center=new SimpleAttributeSet();
		StyleConstants.setAlignment(center,StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0,doc.getLength(),center,false);
		solve=new JButton("Solve");
		layout=new GroupLayout(this);
		setLayout(layout);
	}
	
	public String getSurveyName()
	{
		return surveyName.getText();
	}
	
	/**
	 * Allows flexibility in placing buttons.
	 */
	protected interface ButtonPlacement{
		/**
		 * @return Buttons horizontal group.
		 */
		GroupLayout.Group getHorizontalGroup();
		
		/**
		 * @return Buttons vertical group.
		 */
		GroupLayout.Group getVerticalGroup();
	}
	
	public void initLayoutGroups()
	{
		initLayoutGroups(new ButtonPlacement() {
			@Override
			public GroupLayout.Group getHorizontalGroup() {
				GroupLayout.SequentialGroup horizontal=layout.createSequentialGroup()
						.addComponent(solve);
				return horizontal;
			}

			@Override
			public GroupLayout.Group getVerticalGroup() {
				GroupLayout.SequentialGroup vertical=layout.createSequentialGroup()
						.addComponent(solve)
						.addGap(BaseGUI.DEFAULT_VERTICAL_GAP);
				return vertical;
			}
		});
	}
	
	protected void initLayoutGroups(ButtonPlacement placement) {
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addGap(BaseGUI.DEFAULT_HORIZONTAL_GAP)
						.addComponent(surveyName,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
						.addGap(BaseGUI.DEFAULT_HORIZONTAL_GAP)
				)
				.addGroup(GroupLayout.Alignment.CENTER,placement.getHorizontalGroup())
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGap(BaseGUI.DEFAULT_VERTICAL_GAP)
				.addComponent(surveyName,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE)
				.addGap(BaseGUI.DEFAULT_VERTICAL_GAP)
				.addGroup(placement.getVerticalGroup())
		);
	}
	
	private ActionListener createSolveAction()
	{
		return (e)->{
			SurveyIO r=new SurveyIO();
			try {
				Survey s=r.read(pathToSurvey);
				SurveyRender render=new SurveyRender(s,destination);
				destination.addNewTab(render,surveyName.getText());
			}catch(Exception ex) {
				JOptionPane.showMessageDialog(getRootPane(),"An error occured during loading survey: "+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
			}	
		};
	}
	
	protected JTextPane surveyName;
	protected JButton solve;
}
