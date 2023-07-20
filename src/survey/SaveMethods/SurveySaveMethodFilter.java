package survey.SaveMethods;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import survey.GUI.BaseGUI;
import survey.SurveyIO;

/**
 * Allows filtering of files containing {@code Survey}.
 * 
 * @author ArtiFixal
 */
public class SurveySaveMethodFilter extends FileFilter{
	/**
	 * Strategy for which extension filter files.
	 */
	private SurveySaveMethod method; 
	
	public SurveySaveMethodFilter(SurveySaveMethod method)
	{
		this.method=method;
	}

	@Override
	public boolean accept(File f) {
		return method.isFileExtensionSuported(SurveyIO.getFileExtension(f));
	}

	@Override
	public String getDescription() {
		return BaseGUI.uppercaseFirstLetter(method.getFileExtension().substring(1))+" file";
	}
	
	public SurveySaveMethod getRelatedSaveMethod()
	{
		return method;
	}
	
}
