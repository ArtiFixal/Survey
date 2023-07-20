package survey.SaveMethods;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import survey.AgeGroup;
import survey.Education;
import survey.GUI.CompleteStatusDialog;
import survey.Question;
import survey.QuestionAnwserType;
import survey.ResultIO;
import survey.Settlement;
import survey.Survey;
import survey.SurveyIO;
import survey.SurveyResult;
import survey.Voivodeship;

/**
 * Strategy responsible for dealing with data located in zip files.
 * 
 * Part of strategy design pattern.
 * 
 * @author ArtiFixal
 */
public class AsZipFile implements SurveySaveMethod,ResultSaveMethod{
	/**
	 * Extension used by this save method.
	 */
	public static final String FILE_EXTENSION=".zip";
	
	public AsZipFile() {
		
	}
	
	@Override
	public void write(Survey s) throws Exception {
		final File f=new File(SurveyIO.DEFAULT_SAVE_DIR+"/"+s.getName()+FILE_EXTENSION);
		final Map<String,String> zipEnv=Map.of("create","true");
		final CompleteStatusDialog dialog=new CompleteStatusDialog("Saving survey: "+s.getName(),s.getQuestions().size());
		URI zipUri=URI.create("jar:"+f.toURI());
		try(FileSystem fs=FileSystems.newFileSystem(zipUri,zipEnv)){
			for(int i=0;i<s.getQuestions().size();i++)
			{
				Path zipPath=fs.getPath("/Question"+(i+1)+".dat");
				try(BufferedWriter bw=Files.newBufferedWriter(zipPath,StandardCharsets.UTF_8,StandardOpenOption.CREATE,StandardOpenOption.WRITE)) {
					bw.write(s.getQuestionN(i).getQuestionType().getQuestionTypeID());
					bw.write(s.getQuestionN(i).getQuestionContent()+"\n");
					for(int j=0;j<s.getQuestionN(i).getAnswersQuantity();j++)
						bw.write(s.getQuestionN(i).getAnswerN(j)+"\n");
					dialog.increaseCompletness();
				}
			}
		}
		catch(Exception e){
			throw e;
		}
	}

	@Override
	public Survey read(File pathToSurvey) throws Exception{
		if(pathToSurvey.isDirectory())
			throw new NotAFileException(pathToSurvey);
		String surveyName=pathToSurvey.getName();
		if(surveyName.contains(FILE_EXTENSION))
		{
			surveyName=SurveyIO.removeFileExtension(surveyName);
		}
		Survey s=new Survey(surveyName);
		URI zipUri=URI.create("jar:"+pathToSurvey.toURI());
		final Map<String,String> zipEnv=Map.of("create","false");
		try(FileSystem fs=FileSystems.newFileSystem(zipUri,zipEnv)){
			Path root=fs.getPath(".");
			// Count files inside zip
			int files=0;
			try (DirectoryStream ds=Files.newDirectoryStream(root)) {
				for(Iterator<Path> it=ds.iterator();it.hasNext();)
				{
					files++;
					it.next();
				}
			}
			final CompleteStatusDialog dialog=new CompleteStatusDialog("Reading survey: "+surveyName, files);
			final ExecutorService tp=Executors.newFixedThreadPool(SurveyIO.getEffectiveThreadNumber(files));
			// Populate tasks
			ArrayList<Callable<Question>> tasks=new ArrayList<>();
			try (DirectoryStream ds=Files.newDirectoryStream(root)) {
				for(Iterator<Path> it=ds.iterator();it.hasNext();)
				{
					tasks.add(createReadQuestionTask(Files.newBufferedReader(it.next())));
				}
			}
			List<Future<Question>>done=tp.invokeAll(tasks);
			tp.shutdown();
			// Add questions to survey
			// zipfs iterator moves throught quesions backwards
			for(int i=done.size()-1;i>=0;i--)
			{
				s.addQuestion(done.get(i).get());
				dialog.increaseCompletness();
			}
		}catch(Exception e){
			throw e;
		}
		return s;
	}
	
	@Override
	public boolean testSurveyFile(File pathToSurvey){
		if(pathToSurvey.isDirectory())
			throw new NotAFileException(pathToSurvey);
		URI zipUri=URI.create("jar:"+pathToSurvey.toURI());
		final Map<String,String> zipEnv=Map.of("create","false");
		try(FileSystem fs=FileSystems.newFileSystem(zipUri,zipEnv)){
			Path root=fs.getPath(".");
			try (DirectoryStream ds=Files.newDirectoryStream(root)) {
				// test questions
				Iterator<Path> it=ds.iterator();
				if(!it.hasNext())
					throw new NotASurveyFileException(pathToSurvey);
				while(it.hasNext())
				{
					try(BufferedReader br=Files.newBufferedReader(it.next())){
						QuestionAnwserType.createEnumElement(br.read());
						String content=br.readLine();
						if(content==null||content.isBlank())
							throw new NotASurveyFileException(pathToSurvey);
						String line;
					if((line=br.readLine())==null||line.isBlank())
						throw new NotASurveyFileException(pathToSurvey);
					}
				}
			}
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
	
	private Callable<Question> createReadQuestionTask(BufferedReader br) throws Exception
	{
		return () -> {
			Question q=null;
			try(br){
				QuestionAnwserType type=QuestionAnwserType.createEnumElement(br.read());
				String content=br.readLine();
				ArrayList<String> answers=new ArrayList<>(6);
				String line;
				while((line=br.readLine())!=null){
					answers.add(line);
				}
				q=new Question(content,type,answers);
			}catch(Exception e){
				throw e;
			}
			return q;
		};
	}

	@Override
	public void writeResult(SurveyResult r,Survey s) throws Exception {
		final File surveyResultDir=new File(ResultIO.DEFAULT_RESULTS_DIR+"/"+s.getName());
		if(!surveyResultDir.exists())
			surveyResultDir.mkdir();
		int filesCount=surveyResultDir.listFiles().length;
		final File resultFile=new File(surveyResultDir+"/result"+(filesCount+1)+FILE_EXTENSION);
		final Map<String,String> zipEnv=Map.of("create","true");
		final CompleteStatusDialog dialog=new CompleteStatusDialog("Saving survey results: "+s.getName(),s.getQuestions().size()+1);
		URI zipUri=URI.create("jar:"+resultFile.toURI());
		try(FileSystem fs=FileSystems.newFileSystem(zipUri,zipEnv)){
			Path userData=fs.getPath("person.dat");
			try(BufferedWriter bw=Files.newBufferedWriter(userData,StandardCharsets.US_ASCII,StandardOpenOption.CREATE,StandardOpenOption.WRITE))
			{
				bw.write((r.getRespondent().getSex())?1:0);
				bw.write(r.getRespondent().getAgeGroup().getAgeGroupID());
				bw.write(r.getRespondent().getEducation().getEducationID());
				bw.write(r.getRespondent().getVoivodeship().getVoivodeshipID());
				bw.write(r.getRespondent().getSettlement().getSettlementID());
				dialog.increaseCompletness();
			}
			for(int i=0;i<r.getAnswers().size();i++)
			{
				Path answerToQuestion=fs.getPath("/question"+(i+1)+".dat");
				try(BufferedWriter bw=Files.newBufferedWriter(answerToQuestion,StandardCharsets.UTF_8,StandardOpenOption.CREATE,StandardOpenOption.WRITE))
				{
					Object answer=r.getNAnswer(i);
					// Mark what kind of data is stored in the file then write answer
					if(answer instanceof Long asLong)
					{
						bw.write(0);
						bw.write(Long.toUnsignedString(asLong));
					}
					else if(answer instanceof String asString)
					{
						bw.write(1);
						bw.write(asString);
					}
				}
				dialog.increaseCompletness();
			}
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public SurveyResult readResult(File pathToResult) throws Exception {
		SurveyResult result=null;
		URI zipUri=URI.create("jar:"+pathToResult.toURI());
		final Map<String,String> zipEnv=Map.of("create","false");
		try(FileSystem fs=FileSystems.newFileSystem(zipUri,zipEnv)){
			Path userData=fs.getPath("person.dat");
			// Read user data and create SurveyResult object from it
			try(BufferedReader br=Files.newBufferedReader(userData))
			{
				boolean sex=br.read()==1;
				AgeGroup age=AgeGroup.interpretFromNumber(1<<br.read());
				Education education=Education.interpretFromNumber(1<<br.read());
				Voivodeship voivodeship=Voivodeship.interpretFromNumber(1<<br.read());
				Settlement settlement=Settlement.interpretFromNumber(1<<br.read());
				result=new SurveyResult(sex,education,voivodeship,age,settlement);
			}
			Path root=fs.getPath(".");
			try (DirectoryStream ds=Files.newDirectoryStream(root)) {
				Iterator<Path> it=ds.iterator();
				ArrayList<Object> answers=new ArrayList<>();
				if(!it.hasNext())
					throw new NotASurveyResultFileException(pathToResult);
				while(it.hasNext())
				{
					Path currentPath=it.next();
					if(Files.isSameFile(currentPath,userData))
						continue;
					try(BufferedReader br=Files.newBufferedReader(currentPath)){
						int dataType=br.read();
						String line=br.readLine();
						// Long
						if(dataType==0)
							answers.add(Long.parseUnsignedLong(line));
						// String
						else
							answers.add(line);
						// zipfs iterator moves throught quesions backwards
					}
				}
				for(int i=answers.size()-1;i>=0;i--)
				{
					result.addAnswer(answers.get(i));
				}
			}
		}catch(Exception e){
			throw e;
		}
		return result;
	}
	
	@Override
	public boolean testResultFile(File pathToResult){
		URI zipUri=URI.create("jar:"+pathToResult.toURI());
		final Map<String,String> zipEnv=Map.of("create","false");
		try(FileSystem fs=FileSystems.newFileSystem(zipUri,zipEnv)){
			Path userData=fs.getPath("person.dat");
			try(BufferedReader br=Files.newBufferedReader(userData))
			{
				// skip sexID
				br.skip(1);
				AgeGroup.interpretFromNumber(1<<br.read());
				Education.interpretFromNumber(1<<br.read());
				Voivodeship.interpretFromNumber(1<<br.read());
				Settlement.interpretFromNumber(1<<br.read());
			}
			Path root=fs.getPath(".");
			try (DirectoryStream ds=Files.newDirectoryStream(root)) {
				Iterator<Path> it=ds.iterator();
				if(!it.hasNext())
					throw new NotASurveyResultFileException(pathToResult);
				while(it.hasNext())
				{
					Path currentPath=it.next();
					if(Files.isSameFile(currentPath,userData))
						continue;
					try(BufferedReader br=Files.newBufferedReader(currentPath)){
						// skip data type
						br.skip(1);
						String line=br.readLine();
						if(line==null||line.isBlank())
							throw new NotASurveyResultFileException(pathToResult);
					}
				}
			}
		}catch(Exception e){
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
		return FILE_EXTENSION.equalsIgnoreCase(fileExtension);
	}

	@Override
	public String toString() {
		return "As .zip file";
	}
}
