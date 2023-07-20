package survey.GUI;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import survey.GUI.Blocks.MultiElementBlock;
import survey.GUI.Blocks.ResultBlock;
import survey.ResultIO;
import survey.Survey;
import survey.SurveyIO;
import survey.SurveyResult;

/**
 * Creates GUI which displays to the pollster results of chosen {@code Survey}.
 * 
 * @author ArtiFixal
 */
public class ResultsRender extends MultiElementBlock<ResultBlock>{
	/**
	 * Survey which results will be displayed.
	 */
	public final Survey whichToRender;
	private static Tabbable closeCallback;
	
	/**
	 * Reader which will load all possible survey results.
	 */
	private final ResultIO reader;
	
	public ResultsRender(Survey whichToRender,Tabbable closeCallback) throws Exception{
		this.whichToRender=whichToRender;
		if(ResultsRender.closeCallback==null)
			ResultsRender.closeCallback=closeCallback;
		reader=new ResultIO(BaseGUI.getResultsSaveMethod());
		close=new JButton("Close");
		close.addActionListener(createCloseAction());
		createResultBlocks();
		super.addElement(close,GroupLayout.Alignment.CENTER);
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
	
	private void createResultBlocks() throws Exception
	{
		makeVerticalGap(BaseGUI.DEFAULT_VERTICAL_GAP);
			ArrayList<SurveyResult> results=reader.readResults(whichToRender);
			int taskSize=whichToRender.getQuestions().size();
			ExecutorService tp=Executors.newFixedThreadPool(SurveyIO.getEffectiveThreadNumber(taskSize));
			ArrayList<Callable<ResultBlock>> tasks=new ArrayList<>(taskSize);
			for(int i=0;i<taskSize;i++)
			{
				tasks.add(createBlockTask(results,i));
			}
			List<Future<ResultBlock>> finishedTasks=tp.invokeAll(tasks);
			int lastTaskIndex=finishedTasks.size()-1;
			for(int i=0;i<lastTaskIndex;i++)
			{
				ResultBlock result=finishedTasks.get(i).get();
				addNewElement(result,true);
			}
			addNewElement(finishedTasks.get(lastTaskIndex).get(),false);
		makeVerticalGap(BaseGUI.DEFAULT_VERTICAL_GAP);
	}
	
	private Callable<ResultBlock> createBlockTask(ArrayList<SurveyResult> results,int questionIndex)
	{
		return () -> new ResultBlock(results,whichToRender.getQuestionN(questionIndex),questionIndex);
	}
	
	private ActionListener createCloseAction()
	{
		return (e)->closeCallback.closeTab();
	}
	
	private final JButton close;
}
