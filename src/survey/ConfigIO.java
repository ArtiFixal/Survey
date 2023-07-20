package survey;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import survey.SaveMethods.NotAFileException;

/**
 * Class responsible for all kind of read/write operations related to config of
 * this app.
 * 
 * @author ArtiFixal
 */
public class ConfigIO {
	/**
	 * Contains config options.
	 */
	public static final File CONFIG_FILE=new File("Config.dat");
	
	/**
	 * Contains config name of strategy used to save survey results.
	 */
	public static final String RESULT_SAVE_METHOD_OPTION_NAME="ResultSaveMethod";

	public ConfigIO() {
		if(CONFIG_FILE.exists())
		{
			if(CONFIG_FILE.isDirectory())
				throw new NotAFileException(CONFIG_FILE);
		}
		else
			createDefaultConfig();
	}
	
	/**
	 * Reads given config option from {@link #CONFIG_FILE}.
	 * 
	 * @param option Option to be read.
	 * 
	 * @return Read option value.
	 * 
	 * @throws IOException Any IO error that occured during read.
	 * @throws ConfigOptionNotFoundException If reached EOF and doesn't found 
	 * given option.
	 */
	public String loadConfigOption(String option)throws IOException,ConfigOptionNotFoundException
	{
		String loadedOption=null;
		try(FileReader r=new FileReader(CONFIG_FILE);BufferedReader br=new BufferedReader(r)){
			boolean found=false;
			while(!found)
			{
				String line=br.readLine();
				int optionValueSeparatorPos=line.indexOf('=');
				if(line.substring(0,optionValueSeparatorPos).equals(option))
				{
					loadedOption=line.substring(optionValueSeparatorPos+1,optionValueSeparatorPos+2);
					found=true;
				}
			}
		}catch(NullPointerException e){
			throw new ConfigOptionNotFoundException(option);
		}
		return loadedOption;
	}
	
	/**
	 * Replaces value of given option with a new one.
	 * 
	 * @param option Option which value will be replaced
	 * @param newValue Value to be changed to
	 * 
	 * @throws IOException Any error that occured during reading and writing.
	 * @throws ConfigOptionNotFoundException If config option wasn't found.
	 */
	public void replaceConfigOption(String option,String newValue) throws IOException, ConfigOptionNotFoundException 
	{
		final File tmp=new File(CONFIG_FILE+".tmp");
		boolean found=false;
		try(FileReader r=new FileReader(CONFIG_FILE);BufferedReader br=new BufferedReader(r);FileWriter w=new FileWriter(tmp);BufferedWriter bw=new BufferedWriter(w)){
			String line;
			while((line=br.readLine())!=null)
			{
				int optionValueSeparatorPos=line.indexOf('=');
				if(line.substring(0,optionValueSeparatorPos).equals(option))
				{
					found=true;
					bw.write(option);
					bw.write('=');
					bw.write(newValue);
					bw.write('\n');
				}
				else
				{
					bw.write(line);
					bw.write('\n');
				}
			}
		}catch(Exception e){
			tmp.delete();
			throw e;
		}
		if(!found)
		{
			tmp.delete();
			throw new ConfigOptionNotFoundException(option);
		}
		else
		{
			CONFIG_FILE.delete();
			tmp.renameTo(CONFIG_FILE);
		}
	}
	
	private void createDefaultConfig()
	{
		try(FileWriter w=new FileWriter(CONFIG_FILE);BufferedWriter bw=new BufferedWriter(w))
		{
			bw.write(RESULT_SAVE_METHOD_OPTION_NAME);
			bw.write("=0\n");
		}catch(Exception e){
			System.err.println("An error occured during creating default config: "+e);
		}
	}
}
