package survey.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import survey.Question;
import survey.QuestionAnwserType;
import survey.ElementBuilder;

/**
 * Allows building user defined {@code Survey} questions.
 * 
 * @author ArtiFixal
 */
public class QuestionBuilder implements ElementBuilder<Question>{
	/**
	 * Default vertical gap between elements.
	 */
	public static final Dimension DEFAULT_VERTICAL_GAP=new Dimension(0,BaseGUI.DEFAULT_VERTICAL_GAP);
	
	/**
	 * Stores question content text.
	 */
    public JTextField questionContent;
    
	/**
	 * Defines horizontal main panel layout.
	 */
    protected GroupLayout.SequentialGroup mainHorizontalGroup;
	
	/**
	 * Defines vertical main panel layout.
	 */
    protected GroupLayout.SequentialGroup mainVerticalGroup;
	
	/**
	 * Defines horizontal answer layout. Lays inside {@code mainHorizontalGroup}.
	 * 
	 * @see #mainHorizontalGroup
	 */
    protected GroupLayout.ParallelGroup answerHorizontal;
	
	/**
	 * Defines vertical answer layout. Lays inside {@code mainVerticalGroup}.
	 * @see #mainVerticalGroup
	 */
    protected GroupLayout.SequentialGroup answerVertical;
    
	/**
	 * Contains answers to question.
	 */
    protected ArrayList<JTextField> answers;
    
	/**
	 * We are adding elements here. 
	 */
    final private JPanel contentPanel;
	
	/**
	 * Used to create groups.
	 */
    final private GroupLayout layout;
	
	/**
	 * Button responsible for adding new answers.
	 */
    final private JButton addAnswer;
	
	private void initVariables()
	{
		contentPanel.setLayout(layout);
        questionContent=new JTextField("Enter question...");
        questionType=new JComboBox<>(QuestionAnwserType.getStringValues());
        answers=new ArrayList<>(6);
        deleteAnswers=new ArrayList<>(6);
		separators=new ArrayList<>(6);
		addAnswer.addActionListener(createAnswerAddAction());
	}
	
	/**
	 * Creates default {@code QuestionBuilder} without ability to 
	 * delete question.
	 * 
	 * @param questionNumber Ordinal number of builded question.
	 */
    public QuestionBuilder(int questionNumber) {
		contentPanel=new JPanel();
		layout=new GroupLayout(contentPanel);
		answersType=new JLabel("Answers type:");
		questionLabel=new JLabel("Question "+questionNumber+":");
		addAnswer=new JButton("Add answer");
		initVariables();
		initLayoutGroups(new LayoutInitializer() {
			@Override
			public GroupLayout.Group createQuestionContentRowHorizontal() {
				return layout.createSequentialGroup().addComponent(questionContent,GroupLayout.DEFAULT_SIZE,BaseGUI.INPUT_DEFAULT_WIDTH,GroupLayout.PREFERRED_SIZE);
			}

			@Override
			public GroupLayout.Group createQuestionContentRowVertical() {
				return layout.createSequentialGroup().addComponent(questionContent,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE);
			}
		});
    }
	
	/**
	 * Creates {@code QuestionBuilder} with ability to delete question.
	 * 
	 * @param deleteQuestion Button which will delete question.
	 * @param questionNumber Ordinal number of builded question.
	 */
	public QuestionBuilder(JButton deleteQuestion,int questionNumber)
	{
		contentPanel=new JPanel();
		layout=new GroupLayout(contentPanel);
		answersType=new JLabel("Answers type:");
		questionLabel=new JLabel("Question "+questionNumber+":");
		addAnswer=new JButton("Add answer");
		initVariables();
		initLayoutGroups(deleteQuestion);
	}
	
	public JPanel getContentPanel()
	{
		return contentPanel;
	}
    
    public GroupLayout.SequentialGroup getHorizontalGroup()
    {
        return mainHorizontalGroup;
    }
    
    public GroupLayout.SequentialGroup getVerticalGroup()
    {
        return mainVerticalGroup;
    }
    
	/**
	 * Creates and appends new elements to the GUI responsible for the new answer.<br>
	 * Fails if trying to add more than {@code MAX_ANSWERS}.
	 * 
	 * @return Success of adding.
	 * @see Question#MAX_ANSWERS
	 */
    public boolean addNewAnswer()
    {
        if(answers.size()<Question.MAX_ANSWERS)
        {
            final JTextField f=new JTextField("Answer "+(answers.size()+1));
            final JButton b=new JButton("Delete");
			final Box.Filler s=new Box.Filler(DEFAULT_VERTICAL_GAP,DEFAULT_VERTICAL_GAP,DEFAULT_VERTICAL_GAP);
            answers.add(f);
            deleteAnswers.add(b);
			separators.add(s);
            b.addActionListener(createAnswerDeleteAction(answers.size()-1));
            answerHorizontal.addComponent(s)
					.addGroup(layout.createSequentialGroup()
                    .addComponent(f,GroupLayout.DEFAULT_SIZE,BaseGUI.INPUT_DEFAULT_WIDTH,GroupLayout.PREFERRED_SIZE)
                    .addGap(BaseGUI.DEFAULT_HORIZONTAL_GAP)
                    .addComponent(b)
            );
			answerVertical.addComponent(s)
                    .addGroup(layout.createParallelGroup()
                            .addComponent(f,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                            .addComponent(b)
                    );
            contentPanel.revalidate();
            contentPanel.repaint();
            return true;
        }
        return false;
    }
	
	/**
	 * Validates user intput in {@link #questionContent} field.
	 * 
	 * @return True if user entered question content, false otherwise.
	 */
	public boolean validate()
	{
		boolean ok=true;
		if(Validator.isEmptyOrDefault(questionContent, "Enter question..."))
		{
			BaseGUI.setErrorOutline(questionContent);
		}
		else
		{
			BaseGUI.restorePreviousBorder(questionContent);
		}
		return ok;
	}
    
    @Override
    public Question build()
	{
		final ArrayList<String> answersText=new ArrayList<>(answers.size());
		for(int i=0;i<answers.size();i++)
		{
			answersText.add(answers.get(i).getText());
		}
        return new Question(questionContent.getText(),QuestionAnwserType.createEnumElement(questionType.getSelectedIndex()),answersText);
    }

	@Override
	public boolean readFrom(Question element) {
		if(element.getAnswers().isEmpty()&&element.getQuestionContent().isBlank())
			return false;
		questionContent.setText(element.getQuestionContent());
		answers.get(0).setText(element.getAnswerN(0));
		questionType.setSelectedIndex(element.getQuestionType().getQuestionTypeID());
		for(int i=1;i<element.getAnswers().size();i++)
		{
			addNewAnswer();
			answers.get(i).setText(element.getAnswerN(i));
		}
		return true;
	}
	
    private ActionListener createAnswerAddAction(){
        return (ActionEvent e) -> {
			addNewAnswer();
		};
    }
    
    private ActionListener createAnswerDeleteAction(int answerIndex)
    {
        return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				contentPanel.remove(answers.get(answerIndex));
				contentPanel.remove(deleteAnswers.get(answerIndex-1));
				contentPanel.remove(separators.get(answerIndex-1));
				deleteAnswers.get(answerIndex-1).removeActionListener(deleteAnswers.get(answerIndex-1).getActionListeners()[0]);
				// reorder listener indexes
				for(int i=answerIndex;i<deleteAnswers.size();i++)
				{
					deleteAnswers.get(i).removeActionListener(deleteAnswers.get(i).getActionListeners()[0]);
					deleteAnswers.get(i).addActionListener(createAnswerDeleteAction(i));
				}
				answers.remove(answerIndex);
				deleteAnswers.remove(answerIndex-1);
				separators.remove(answerIndex-1);
				contentPanel.revalidate();
				contentPanel.repaint();
			}
		};
    }
	
	/**
	 * Interface used in initialization of layout groups
	 * 
	 * @see #initLayoutGroups(survey.QuestionBuilder.LayoutInitializer) 
	 */
	private interface LayoutInitializer{
		public GroupLayout.Group createQuestionContentRowHorizontal();
		public GroupLayout.Group createQuestionContentRowVertical();
	}
	
    private void initLayoutGroups(LayoutInitializer questionContentRow)
    {
        answers.add(new JTextField("Answer 1..."));
        answerHorizontal=layout.createParallelGroup()
                .addComponent(answers.get(0),GroupLayout.DEFAULT_SIZE,BaseGUI.INPUT_DEFAULT_WIDTH,GroupLayout.PREFERRED_SIZE);
        answerVertical=layout.createSequentialGroup()
                .addComponent(answers.get(0),GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE);
        mainHorizontalGroup=layout.createSequentialGroup()
                .addGap(28)
                .addGroup(layout.createParallelGroup()
                // Question label H
                        .addComponent(questionLabel)
						// Question content row
						.addGroup(questionContentRow.createQuestionContentRowHorizontal())
                        // Anwser type
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(answersType)
                                .addGap(BaseGUI.DEFAULT_HORIZONTAL_GAP)
                                .addComponent(questionType,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                        )
                        // Answers
                        .addGroup(answerHorizontal)
                        // Add new anwser button
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(addAnswer)
                        )
                );
                // Question label V
        mainVerticalGroup=layout.createSequentialGroup()
                .addGap(4)
                .addComponent(questionLabel)
                .addGap(4)
				// Question content row
				.addGroup(questionContentRow.createQuestionContentRowVertical())
                .addGap(BaseGUI.DEFAULT_VERTICAL_GAP)
				// Answer type
                .addGroup(layout.createParallelGroup()
                        .addComponent(answersType)
                        .addComponent(questionType,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                )
                .addGap(BaseGUI.DEFAULT_VERTICAL_GAP)
				// Answers
                .addGroup(answerVertical)
                .addGap(BaseGUI.DEFAULT_VERTICAL_GAP)
				// Add new answer button
                .addComponent(addAnswer);
		layout.setHorizontalGroup(mainHorizontalGroup);
		layout.setVerticalGroup(mainVerticalGroup);
    }

	private void initLayoutGroups(final JButton deleteQuestion)
    {
		initLayoutGroups(new LayoutInitializer() {
			@Override
			public GroupLayout.Group createQuestionContentRowHorizontal() {
				return layout.createSequentialGroup()
								.addComponent(questionContent,GroupLayout.DEFAULT_SIZE,BaseGUI.INPUT_DEFAULT_WIDTH,GroupLayout.PREFERRED_SIZE)
								.addGap(BaseGUI.DEFAULT_HORIZONTAL_GAP)
								.addComponent(deleteQuestion);
			}

			@Override
			public GroupLayout.Group createQuestionContentRowVertical() {
				return layout.createParallelGroup()
						.addComponent(questionContent,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
						.addComponent(deleteQuestion);
			}
		});
    }
	
	/**
	 * Label indicating start of question and containing its ordinal number.
	 */
    protected JLabel questionLabel;
	
	/**
	 * Describes what kind of question
	 */
    private JComboBox<String> questionType;
	
	/**
	 * Contains separators which separate rows.
	 */
	private ArrayList<Box.Filler> separators;
	
	/**
	 * Contains buttons deleting entire answer row.<br>
	 * 
	 * <b>Warning:</b><br>
	 * First answer can't be deleted, that means index 0 reffers to 1st answer 
	 * row and so on ((n-1)th button to nth row).
	 */
    private ArrayList<JButton> deleteAnswers;
	
    private final JLabel answersType;
}
