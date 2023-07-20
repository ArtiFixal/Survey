package survey.SaveMethods;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import survey.AgeGroup;
import survey.Education;
import survey.Question;
import survey.QuestionAnwserType;
import survey.ResultIO;
import survey.Settlement;
import survey.Survey;
import survey.SurveyIO;
import survey.SurveyResult;
import survey.Voivodeship;

/**
 * Strategy responsible for dealing with data located in txt files.
 * 
 * Part of strategy design pattern. 
 * 
 * @author ArtiFixal
 */
public class AsTxtFile implements SurveySaveMethod,ResultSaveMethod{
	/**
	 * Extension used by this save method.
	 */
	public static final String FILE_EXTENSION=".txt";
	
	/**
	 * Size of buffered survey IO operations.
	 */
	private static final int BUFFER_SIZE=16384;
	
	/**
	 * Writes given string to the file. First the string length is written to 
	 * the file, then String itself. This will help in reading string back from
	 * file.
	 * 
	 * @param w Writer in which we will write.
	 * @param s Text which will be written.
	 * 
	 * @throws IOException Any error that occured during writing.
	 */
	private void writeString(BufferedWriter w,String s) throws IOException
	{
		w.write(s.length());
		w.write(s);
	}
	
	/**
	 * Reads string from current position in {@code BufferedReader} file.
	 * 
	 * @param r Reader from where we will be reading.
	 * @return Read string.
	 * 
	 * @throws IOException 
	 */
	private String readString(BufferedReader r) throws IOException
	{
		int stringLength=r.read();
		char[] buffer=new char[stringLength];
		r.read(buffer,0,stringLength);
		return new String(buffer);
	}
	
	/**
	 * Skips string in a file.
	 * 
	 * @param r In which reader to skip.
	 * 
	 * @return True if string was skipped entirely, false if reached EOF.
	 * @throws IOException Any error that occured during skiping.
	 */
	private boolean skipString(BufferedReader r) throws IOException
	{
		int stringLength=r.read();
		return r.skip(stringLength)==stringLength;
	}
	
	@Override
	public void write(Survey s) throws Exception {
		final File savePath=new File(SurveyIO.DEFAULT_SAVE_DIR+"/"+s.getName()+FILE_EXTENSION);
		try(FileWriter w=new FileWriter(savePath,StandardCharsets.UTF_8);BufferedWriter bw=new BufferedWriter(w,BUFFER_SIZE)){
			bw.write(s.getQuestions().size());
			for(Question q:s.getQuestions()){
				bw.write(q.getQuestionType().getQuestionTypeID());
				writeString(bw,q.getQuestionContent());
				// Write question quantity which will be used as an indicator 
				// how many answers currently saved question has.
				bw.write(q.getAnswersQuantity());
				for(int i=0;i<q.getAnswersQuantity();i++)
				{
					writeString(bw,q.getAnswerN(i));
				}
			}
		}
	}

	@Override
	public Survey read(File pathToSurvey) throws Exception {
		if(pathToSurvey.isDirectory())
			throw new NotAFileException(pathToSurvey);
		String surveyName=pathToSurvey.getName();
		if(surveyName.contains(FILE_EXTENSION))
		{
			surveyName=SurveyIO.removeFileExtension(surveyName);
		}
		Survey s=new Survey(surveyName);
		try(FileReader r=new FileReader(pathToSurvey,StandardCharsets.UTF_8);BufferedReader br=new BufferedReader(r,BUFFER_SIZE)){
			int questionQuantity=br.read();
			if(questionQuantity<0)
				throw new NotASurveyFileException(pathToSurvey);
			for(int i=0;i<questionQuantity;i++)
			{
				QuestionAnwserType type=QuestionAnwserType.createEnumElement(br.read());
				String questionContent=readString(br);
				Question q=new Question(questionContent,type);
				int answerQuantity=br.read();
				if(answerQuantity<0)
					throw new NotASurveyFileException(pathToSurvey);
				for(int j=0;j<answerQuantity;j++)
				{
					q.addAnswer(readString(br));
				}
				s.addQuestion(q);
			}
		}
		return s;
	}

	@Override
	public boolean testSurveyFile(File pathToSurvey) {
		if(pathToSurvey.isDirectory())
			throw new NotAFileException(pathToSurvey);
		try(FileReader r=new FileReader(pathToSurvey,StandardCharsets.UTF_8);BufferedReader br=new BufferedReader(r,BUFFER_SIZE)) {
			int questionQuantity=br.read();
			if(questionQuantity<0)
				throw new NotASurveyFileException(pathToSurvey);
			for(int i=0;i<questionQuantity;i++)
			{
				QuestionAnwserType.createEnumElement(br.read());
				// Question content
				if(!skipString(br))
					throw new NotASurveyFileException(pathToSurvey);
				int answerQuantity=br.read();
				if(answerQuantity<0)
					throw new NotASurveyFileException(pathToSurvey);
				for(int j=0;j<answerQuantity;j++)
				{
					if(!skipString(br))
						throw new NotASurveyFileException(pathToSurvey);
				}
			}
		}catch(Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public String getFileExtension() {
		return FILE_EXTENSION;
	}

	@Override
	public boolean isFileExtensionSuported(String fileExtension) {
		return fileExtension.equalsIgnoreCase(FILE_EXTENSION);
	}

	@Override
	public void writeResult(SurveyResult r,Survey s) throws Exception {
		final File surveyResultDir=new File(ResultIO.DEFAULT_RESULTS_DIR+"/"+s.getName());
		if(surveyResultDir.exists())
			surveyResultDir.mkdir();
		int filesCount=surveyResultDir.listFiles().length;
		final File resultFile=new File(surveyResultDir+"/result"+(filesCount+1)+FILE_EXTENSION);
		try(FileWriter w=new FileWriter(resultFile,StandardCharsets.UTF_8);BufferedWriter bw=new BufferedWriter(w)){
			// User data
			bw.write((r.getRespondent().getSex())?1:0);
			bw.write(r.getRespondent().getAgeGroup().getAgeGroupID());
			bw.write(r.getRespondent().getEducation().getEducationID());
			bw.write(r.getRespondent().getVoivodeship().getVoivodeshipID());
			bw.write(r.getRespondent().getSettlement().getSettlementID());
			// Survey results
			for(int i=0;i<r.getAnswers().size();i++)
			{
				Object answer=r.getAnswers().get(i);
				// Mark what kind of data is stored in the file then write answer
				if(answer instanceof Long asLong)
				{
					bw.write(0);
					writeString(bw,Long.toUnsignedString(asLong));
				}
				else if(answer instanceof String asString)
				{
					bw.write(1);
					writeString(bw,asString);
				}
			}
		}
	}

	@Override
	public SurveyResult readResult(File pathToResult) throws Exception {
		SurveyResult result;
		if(pathToResult.isDirectory())
			throw new NotAFileException(pathToResult);
		try(FileReader r=new FileReader(pathToResult,StandardCharsets.UTF_8);BufferedReader br=new BufferedReader(r)){
			// User data
			boolean sex=br.read()==1;
			AgeGroup age=AgeGroup.interpretFromNumber(1<<br.read());
			Education education=Education.interpretFromNumber(1<<br.read());
			Voivodeship voivodeship=Voivodeship.interpretFromNumber(1<<br.read());
			Settlement settlement=Settlement.interpretFromNumber(1<<br.read());
			result=new SurveyResult(sex,education,voivodeship,age,settlement);
			// Survey results
			int answerType;
			while((answerType=br.read())!=-1)
			{
				if(answerType==1)
					result.addAnswer(Long.parseUnsignedLong(readString(br)));
				else
					result.addAnswer(readString(br));
			}
		}
		return result;
	}

	@Override
	public boolean testResultFile(File pathToResult) {
		if(pathToResult.isDirectory())
			throw new NotAFileException(pathToResult);
		try(FileReader r=new FileReader(pathToResult,StandardCharsets.UTF_8);BufferedReader br=new BufferedReader(r)){
			// User data
			br.skip(1);
			AgeGroup.interpretFromNumber(1<<br.read());
			Education.interpretFromNumber(1<<br.read());
			Voivodeship.interpretFromNumber(1<<br.read());
			Settlement.interpretFromNumber(1<<br.read());
			// Survey results
			boolean hasAnswers=false;
			while(br.read()!=-1)
			{
				int toSkip=br.read();
				br.skip(toSkip);
				hasAnswers=true;
			}
			// Required at least one answer
			if(!hasAnswers)
				throw new NotASurveyResultFileException(pathToResult);
		}catch(Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "As .txt file";
	}
}
