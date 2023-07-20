package survey.GUI.Blocks;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import survey.AgeGroup;
import survey.Education;
import survey.GUI.LetterListMarker;
import survey.Question;
import survey.Settlement;
import survey.SurveyResult;
import survey.Voivodeship;

/**
 * Displays data about survey answers to specific question.
 * 
 * @author ArtiFixal
 */
public class ResultBlock extends SingleElementBlock<JComboBox<String>>{
	/**
	 * ComboBox available filters.
	 */
	public final static String[] FILTER_OPTIONS=new String[]{"Sex","Age group","Education","Voivodeship","Settlement"};
	
	/**
	 * ComboBox filter by sex value.
	 */
	public static final int SEX_FILTER=0;
	
	/**
	 * ComboBox filter by age group value.
	 */
	public static final int AGE_FILTER=1;
	
	/**
	 * ComboBox filter by education level value.
	 */
	public static final int EDUCATION_FILTER=2;
	
	/**
	 * ComboBox filter by voivodeship value.
	 */
	public static final int VOIVODESHIP_FILTER=3;
	
	/**
	 * ComboBox filter by settlement value.
	 */
	public static final int SETTLEMENT_FILTER=4;
	
	/**
	 * Table in which results are displayed.
	 */
	public JTable resultsTable;
	
	public JScrollPane tablePanel;
	
	private final ArrayList<SurveyResult> resultsData;
	
	/**
	 * Index of question results represented by this block.
	 */
	private final int questionIndex;
	
	/**
	 * Marker used in displaying answers to this question.
	 */
	private final LetterListMarker lastAnswerMarker;

	public ResultBlock(ArrayList<SurveyResult> resultsData,Question q,int questionIndex) {
		super(Integer.toString(questionIndex+1)+". "+q.getQuestionContent()+":");
		this.resultsData=resultsData;
		this.questionIndex=questionIndex;
		lastAnswerMarker=new LetterListMarker();
		displayAnswers(q);
		element=new JComboBox<>(FILTER_OPTIONS);
		addElement(element);
		element.addActionListener(createChangeFilterAction());
		createResultsTable();
		tablePanel=createTablePanel();
		addElement(tablePanel);
	}
	
	private JScrollPane createTablePanel()
	{
		JPanel tablePanel=new JPanel();
		tablePanel.setLayout(new BoxLayout(tablePanel,BoxLayout.Y_AXIS));
		tablePanel.add(resultsTable.getTableHeader());
		tablePanel.add(resultsTable);
		JScrollPane scroll=new JScrollPane(tablePanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		return scroll;
	}
	
	/**
	 * Creates and displays answers to question related to this block.
	 * @param q Question related to this block.
	 */
	private void displayAnswers(Question q)
	{
		for(String answer: q.getAnswers())
		{
			createLabel(lastAnswerMarker+") "+answer);
			lastAnswerMarker.increaseMarker();
		}
	}
	
	private ActionListener createChangeFilterAction()
	{
		return (e)->{
			remove(tablePanel);
			createResultsTable();
			tablePanel=createTablePanel();
			addElement(tablePanel);
			revalidate();
			repaint();
		};
	}
	
	private void createResultsTable()
	{
		if(!resultsData.get(0).isNAnswerString(questionIndex))
		{
			String[] headers=createHeaders();
			switch(element.getSelectedIndex()) {
				case SEX_FILTER -> {
					Object[][] data=new Object[2][headers.length];
					data[0][0]="Male";
					data[1][0]="Female";
					for(int i=1;i<headers.length;i++)
					{
						int[] selectedAnswers=new int[]{0,0};
						for(SurveyResult r:resultsData)
						{
							if(r.isAnswerSelected(r.getNAnswer(questionIndex),i-1))
							{
								if(r.getRespondent().getSex())
									selectedAnswers[0]++;
								else
									selectedAnswers[1]++;
							}
						}
						data[0][i]=selectedAnswers[0];
						data[1][i]=selectedAnswers[1];	
					}
					DefaultTableModel tm=new DefaultTableModel(data,headers);
					resultsTable=new JTable(tm);
				}
				case AGE_FILTER ->
					resultsTable=createFilteredTable(AgeGroup.values(),headers,(fromWhere) -> fromWhere.getRespondent().getAgeGroup());
				case EDUCATION_FILTER ->
					resultsTable=createFilteredTable(Education.values(),headers,(fromWhere) -> fromWhere.getRespondent().getEducation());
				case VOIVODESHIP_FILTER ->
					resultsTable=createFilteredTable(Voivodeship.values(),headers,(fromWhere) -> fromWhere.getRespondent().getVoivodeship());
				case SETTLEMENT_FILTER ->
					resultsTable=createFilteredTable(Settlement.values(),headers,(fromWhere) -> fromWhere.getRespondent().getSettlement());
				default -> throw new IllegalArgumentException("There is no filter of index: "+element.getSelectedIndex());
			}
		}
		else
		{
			String[] headers=new String[]{"Label","Answer"};
			Object[][] data=new Object[2][resultsData.size()];
			int i=0;
			for(SurveyResult r:resultsData)
			{
				switch(element.getSelectedIndex()) {
					case SEX_FILTER->
						data[0][i]=r.getRespondent().getSex()?"Male":"Female";
					case AGE_FILTER->
						data[0][i]=r.getRespondent().getAgeGroup();
					case EDUCATION_FILTER->
						data[0][i]=r.getRespondent().getEducation();
					case VOIVODESHIP_FILTER->
						data[0][i]=r.getRespondent().getVoivodeship();
					case SETTLEMENT_FILTER->
						data[0][i]=r.getRespondent().getSettlement();
					default -> throw new IllegalArgumentException("There is no filter of index: "+element.getSelectedIndex());
				}
				data[1][i]=r.getNAnswer(questionIndex);
				i++;
			}
			DefaultTableModel tm=new DefaultTableModel(data,headers);
			resultsTable=new JTable(tm);
			resultsTable.setDragEnabled(false);
		}
	}
	
	private String[] createHeaders()
	{
		final ArrayList<String> headers=new ArrayList<>(6);
		headers.add("Label");
		LetterListMarker answersHeaders=new LetterListMarker();
		while(!answersHeaders.equals(lastAnswerMarker))
		{
			headers.add(answersHeaders.toString());
			answersHeaders.increaseMarker();
		}
		return headers.toArray(String[]::new);
	}
	
	/**
	 * Interface used in creating filtered table. <br>
	 * Shortens code a bit, makes it more flexible and allows easy filtering.
	 */
	private interface ComparationData{
		/**
		 * Retrives data that will be used by filter.
		 * 
		 * @param fromWhere Object from where data will be obtained.
		 * @return Data that will be compared in filter.
		 */
		public Object getData(SurveyResult fromWhere);
	}
	
	private JTable createFilteredTable(Object[] labelRowValues,Object[] tableHeaders,ComparationData dataToCompare)
	{
		final Object[][] data=new Object[labelRowValues.length][tableHeaders.length];
		for(int i=0;i<labelRowValues.length;i++)
		{
			data[i][0]=labelRowValues[i];
			for(int j=1;j<tableHeaders.length;j++)
			{
				int selectedAnswers=0;
				for(SurveyResult r:resultsData)
				{
					if(r.isAnswerSelected(r.getNAnswer(questionIndex),j-1)
							&&dataToCompare.getData(r).equals(labelRowValues[i]))
						selectedAnswers++;
				}
				data[i][j]=selectedAnswers;
			}
		}
		DefaultTableModel tm=new DefaultTableModel(data,tableHeaders);
		return new JTable(tm);
	}
}
