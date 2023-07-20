package survey.GUI;

import survey.Survey;

/**
 * Callback used to update {@code WrappingContainer} by new {@code Survey} in 
 * pollsetr GUI.
 * 
 * @author ArtiFixal
 */
public interface NewSurveyCreated {
	public void updateSurveyContainer(Survey s);
}
