package survey.GUI;

import survey.Voivodeship;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import survey.GUI.Blocks.AnswerBlocks.ExclusionAnswerBlock;
import survey.GUI.Blocks.AnswerBlocks.AnswerBlock;
import survey.GUI.Blocks.MultiElementBlock;
import survey.GUI.Blocks.Block;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import survey.AgeGroup;
import survey.Education;
import survey.GUI.Blocks.AnswerBlocks.SingleElementAnswerBlock;
import survey.Question;
import survey.ResultIO;
import survey.Settlement;
import survey.Survey;
import survey.SurveyResult;

/**
 * Creates GUI which displays {@code Survey} and allows user to solve it, by 
 * using all form of input fields.
 * 
 * @author ArtiFixal
 */
public class SurveyRender extends MultiElementBlock<Block>{
	/**
	 * Survey 
	 */
	public Survey whichToRender;
	private final Tabbable closeCallback;
	
	public SurveyRender(Survey whichToRender, Tabbable closeCallback) {
		super(4);
		this.closeCallback=closeCallback;
		GroupLayout.SequentialGroup newVertical=layout.createSequentialGroup();
		mainVerticalGroup.addGroup(layout.createSequentialGroup()
				.addGap(14)
				.addGroup(newVertical)
				.addGap(14)
		);
		mainVerticalGroup=newVertical;
		this.whichToRender=whichToRender;
		addNewElement(createGenderRadioGroup(),true);
		addNewElement(createAgeField(),true);
		addNewElement(createEducationRadioGroup(),true);
		addNewElement(createVoivodeshipComboBox(),true);
		addNewElement(createSettlementRadioGroup(),true);
		initQuestions();
		saveResults=new JButton("Save results");
		saveResults.addActionListener(createSaveResultsAction());
		close=new JButton("Close");
		close.addActionListener(createCloseAction());
		// Call base class default method, since subclass one is overrided
		GroupLayout.SequentialGroup buttonGroupHorizontal=layout.createSequentialGroup()
				.addComponent(saveResults)
				.addGap(BaseGUI.DEFAULT_HORIZONTAL_GAP)
				.addComponent(close);
		GroupLayout.ParallelGroup buttonGroupVertical=layout.createParallelGroup()
				.addComponent(saveResults)
				.addComponent(close);
		//super.addElement(saveResults,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.Alignment.CENTER);
		addElement(buttonGroupHorizontal,buttonGroupVertical,GroupLayout.Alignment.CENTER);
	}
	
	@Override
	public void addElement(JComponent el,int width,int height,int maxWidth,int maxHeight,GroupLayout.Alignment horizontalAlignment) {
		GroupLayout.ParallelGroup newHorizontal=layout.createParallelGroup();
		mainHorizontalGroup.addGroup(layout.createSequentialGroup()
				.addGap(28)
				.addGroup(newHorizontal)
				.addGap(28)
		);
		// Swap groups to make indents on both sides in rows excluding JSeparator
		GroupLayout.ParallelGroup tmp=mainHorizontalGroup;
		mainHorizontalGroup=newHorizontal;
		newHorizontal=tmp;
		super.addElement(el,width,height,maxWidth,maxHeight,horizontalAlignment);
		// Restore main group
		mainHorizontalGroup=newHorizontal;
	}
	
	/**
	 * Creates question blocks.
	 */
	private void initQuestions()
	{
		int totalSize=whichToRender.getQuestions().size()-1;
		for(int i=0;i<totalSize;i++)
		{
			addNewElement(createNewQuestionBlock(whichToRender.getQuestionN(i),i+1));
			createSeparator();
		}
		addNewElement(createNewQuestionBlock(whichToRender.getQuestionN(totalSize),totalSize+1));
	}
	
	/**
	 * @param text Checkbox text.
	 * 
	 * @return Refference to created checkbox.
	 */
	private JCheckBox createChceckBox(String text)
	{
		return new JCheckBox(BaseGUI.uppercaseFirstLetter(text));
	}
	
	/**
	 * @param text Radio button text.
	 * 
	 * @return Refference to created radio button.
	 */
	private JRadioButton createRadioButton(String text)
	{
		return new JRadioButton(BaseGUI.uppercaseFirstLetter(text));
	}
	
	/**
	 * Creates block responsible for selecton of gender by respondent.
	 * 
	 * @see survey.User#sex
	 */
	private ExclusionAnswerBlock createGenderRadioGroup()
	{
		ExclusionAnswerBlock genderChooseBlock=new ExclusionAnswerBlock("Select your sex:");
		genderChooseBlock.addNewElement(new JRadioButton("Male"));
		genderChooseBlock.addNewElement(new JRadioButton("Female"));
		return genderChooseBlock;
	}
	
	/**
	 * Creates block responsible for selection of group age by respondent.
	 * 
	 * @see AgeGroup
	 */
	private ExclusionAnswerBlock createAgeField()
	{
		ExclusionAnswerBlock ageChooseBlock=new ExclusionAnswerBlock("Select your age group:");
		ageChooseBlock.addNewElement(createRadioButton(AgeGroup.AGE_13_17.toString()));
		ageChooseBlock.addNewElement(createRadioButton(AgeGroup.AGE_18_24.toString()));
		ageChooseBlock.addNewElement(createRadioButton(AgeGroup.AGE_25_32.toString()));
		ageChooseBlock.addNewElement(createRadioButton(AgeGroup.AGE_33_49.toString()));
		ageChooseBlock.addNewElement(createRadioButton(AgeGroup.AGE_50_64.toString()));
		ageChooseBlock.addNewElement(createRadioButton(AgeGroup.AGE_65_PLUS.toString()));
		return ageChooseBlock;
	}
	
	/**
	 * Creates question block related to a given question. Question block 
	 * contains question content and appropriate input fields allowing user 
	 * to give answer to the question.
	 * 
	 * @param whichQuestion Question to which block will be related to.
	 * @param questionNumber Ordinal number of question.
	 * 
	 * @return Question block with appropriate input fields.
	 */
	private Block createNewQuestionBlock(Question whichQuestion, int questionNumber)
	{
		Block b;
		switch(whichQuestion.getQuestionType()) {
			case CHECKBOX -> {
				b=new AnswerBlock<JCheckBox>(questionNumber+". "+whichQuestion.getQuestionContent()+":",whichQuestion.getAnswersQuantity());
				addAnswersToBlock(b,whichQuestion);
			}
			case RADIO -> {
				b=new ExclusionAnswerBlock(questionNumber+". "+whichQuestion.getQuestionContent()+":",whichQuestion.getAnswersQuantity());
				addAnswersToBlock(b,whichQuestion);
			}
			case COMBO_BOX->
				b=new SingleElementAnswerBlock<JComboBox<String>>(new JComboBox<>(whichQuestion.getAnswers().toArray(new String[whichQuestion.getAnswersQuantity()])),questionNumber+". "+whichQuestion.getQuestionContent()+":");
			case INPUT-> 
				b=new SingleElementAnswerBlock<JTextField>(new JTextField(),questionNumber+". "+whichQuestion.getQuestionContent()+":");
			default -> throw new IllegalArgumentException("Unknown question type:"+whichQuestion.getQuestionType().name());
		}
		return b;
	}
	
	/**
	 * Creates block responsible for selection of education level by respondent.
	 * 
	 * @see Education
	 */
	private ExclusionAnswerBlock createEducationRadioGroup()
	{
		ExclusionAnswerBlock educationChooseBlock=new ExclusionAnswerBlock("Select your education level:",Education.values().length);
		educationChooseBlock.addNewElement(createRadioButton(Education.PRIMARY_SCHOOL.toString()));
		educationChooseBlock.addNewElement(createRadioButton(Education.SECONDARY_SCHOOL.toString()));
		educationChooseBlock.addNewElement(createRadioButton(Education.HIGH_SCHOOL.toString()));
		educationChooseBlock.addNewElement(createRadioButton(Education.HIGHER_EDUCATION.toString()));
		return educationChooseBlock;
	}
	
	/**
	 * Fills block with answers to a given question.
	 * 
	 * @param toWhichBlock Block in which answers will be added.
	 * @param whichQuestion What anwsers will be added.
	 */
	private void addAnswersToBlock(Block toWhichBlock,Question whichQuestion)
	{
		if(toWhichBlock instanceof ExclusionAnswerBlock radio)
		{
			for(int i=0;i<whichQuestion.getAnswersQuantity();i++)
			{
				radio.addNewElement(createRadioButton(whichQuestion.getAnswerN(i)));
			}
		}
		else if(toWhichBlock instanceof AnswerBlock checkbox)
		{
			for(int i=0;i<whichQuestion.getAnswersQuantity();i++)
			{
				checkbox.addNewElement(createChceckBox(whichQuestion.getAnswerN(i)));
			}
		}
	}
	
	/**
	 * Creates block responsible for selection of voivodeship by respondent.
	 * 
	 * @see Voivodeship
	 */
	private SingleElementAnswerBlock<JComboBox<Voivodeship>> createVoivodeshipComboBox()
	{
		SingleElementAnswerBlock<JComboBox<Voivodeship>> voivodeshipSelect=new SingleElementAnswerBlock<>(new JComboBox<Voivodeship>(Voivodeship.values()),"Select your voivodeship:");
		return voivodeshipSelect;
	}
	
	/**
	 * Creates block responsible for selection of settlement by respondent.
	 * 
	 * @see Settlement
	 */
	private ExclusionAnswerBlock createSettlementRadioGroup()
	{
		ExclusionAnswerBlock chooseSettlementBlock=new ExclusionAnswerBlock("Select your settlement:",Settlement.values().length);
		chooseSettlementBlock.addNewElement(createRadioButton(Settlement.VILLAGE.toString()));
		chooseSettlementBlock.addNewElement(createRadioButton(Settlement.TOWN.toString()));
		chooseSettlementBlock.addNewElement(createRadioButton(Settlement.CITY.toString()));
		chooseSettlementBlock.addNewElement(createRadioButton(Settlement.METROPOLIS.toString()));
		return chooseSettlementBlock;
	}
	
	private boolean validateRequiredAnswers(ExclusionAnswerBlock genderBlock,ExclusionAnswerBlock ageBlock,
			ExclusionAnswerBlock educationBlock,ExclusionAnswerBlock settlementBlock)
	{
		boolean valid=true;
		StringBuilder mess=new StringBuilder();
		// Validate gender
		if(!Validator.validateBlock(genderBlock))
		{
			valid=false;
			mess.append("You must select gender.\n");
		}
		// Validate age
		if(!Validator.validateBlock(ageBlock))
		{
			valid=false;
			mess.append("You must select your age group.\n");
		}
		// Validate education level
		if(!Validator.validateBlock(educationBlock))
		{
			valid=false;
			mess.append("You must select education level.\n");
		}
		// Validate settlement
		if(!Validator.validateBlock(settlementBlock))
		{
			valid=false;
			mess.append("You must select your settlement.\n");
		}
		if(!valid)
		{
			JOptionPane.showMessageDialog(this,mess.toString(),"Error",JOptionPane.ERROR_MESSAGE);
		}
		return valid;
	}
	
	/**
	 * @return Answer to the question of given block.
	 */
	private Object getAnswer(Block b)
	{
		if(b instanceof AnswerBlock longResult)
		{
			return longResult.getAnswer();
		}
		else if(b instanceof SingleElementAnswerBlock singleResult)
		{
			if(singleResult.getAnswer() instanceof JTextComponent textResult)
			{
				return textResult.getText();
			}
			else if(singleResult.getAnswer() instanceof JComboBox objectResult)
			{
				return objectResult.getSelectedIndex();
			}
		}
		throw new AssertionError("Unknown answer type");
	}
	
	/**
	 * Creates {@code SurveyResult} from respondent input. 
	 */
	private SurveyResult getSurveyResults(ExclusionAnswerBlock genderBlock,
			ExclusionAnswerBlock ageBlock,ExclusionAnswerBlock educationBlock,
			SingleElementAnswerBlock<JComboBox<Voivodeship>> voivodeBlock,
			ExclusionAnswerBlock settlementBlock)
	{
		SurveyResult r=new SurveyResult(genderBlock.getStoredNElement(0).isSelected(),
				Education.interpretFromNumber(educationBlock.getAnswer().intValue()),
				Voivodeship.interpretFromNumber(1<<(int)getAnswer(voivodeBlock)),
				AgeGroup.interpretFromNumber(ageBlock.getAnswer().intValue()),
				Settlement.interpretFromNumber(settlementBlock.getAnswer().intValue()));
		for(int i=5;i<elements.size();i++)
		{
			r.addAnswer(getAnswer(elements.get(i)));
		}
		return r;
	}
	
	private ActionListener createSaveResultsAction()
	{
		return (e)->{
			ExclusionAnswerBlock genderBlock=(ExclusionAnswerBlock)elements.get(0);
			ExclusionAnswerBlock ageBlock=(ExclusionAnswerBlock)elements.get(1);
			ExclusionAnswerBlock educationBlock=(ExclusionAnswerBlock)elements.get(2);
			SingleElementAnswerBlock<JComboBox<Voivodeship>> voivodeBlock=(SingleElementAnswerBlock<JComboBox<Voivodeship>>)elements.get(3);
			ExclusionAnswerBlock settlementBlock=(ExclusionAnswerBlock)elements.get(4);
			if(validateRequiredAnswers(genderBlock,ageBlock,educationBlock,settlementBlock))
			{
				SurveyResult r=getSurveyResults(genderBlock,ageBlock,educationBlock,voivodeBlock,settlementBlock);
				ResultIO w=new ResultIO(BaseGUI.getResultsSaveMethod());
				try {
					w.writeResult(r,whichToRender);
					closeCallback.closeTab();
				}catch(Exception ex) {
					JOptionPane.showMessageDialog(this,"An error occured during saving survey result: "+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
				}
			}
			else
			{
				BaseGUI.showErrorMessage(this,"Before saving you first need to answer requierd questions.","Unanswered required questions!");
			}	
		};
	}
	
	private ActionListener createCloseAction()
	{
		return (e)->{
			closeCallback.closeTab();
		};
	}
	
	private JButton saveResults;
	private JButton close;
}
