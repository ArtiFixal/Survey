package survey.GUI;

import java.util.Arrays;

/**
 * A uppercase letter marker used in ordered lists.
 * 
 * @author ArtiFixal
 */
public class LetterListMarker{
	/**
	 * Array which stores current marker.
	 */
	private char[] currentMarker;

	public LetterListMarker() {
		currentMarker=new char[1];
		currentMarker[0]='A';
	}
	
	public char[] getMarker()
	{
		return currentMarker;
	}
	
	/**
	 * Fills up entire {@code currentMarker} array with initial marker letters.
	 * 
	 * @see #currentMarker
	 */
	private void fillMarkerArray()
	{
		for(int i=0;i<currentMarker.length;i++)
			currentMarker[i]='A';
	}
	
	/**
	 * Increases marker and if needed resizes it with new letters.
	 */
	public void increaseMarker()
	{
		int i=currentMarker.length-1;
		if(currentMarker[i]=='Z')
		{
			if(isEveryMarkerLetterAlreadyMaxed(i))
			{
				currentMarker=new char[currentMarker.length+1];
				fillMarkerArray();
			}
			else
			{
				currentMarker[i-1]++;
				currentMarker[i]='A';
			}
		}
		else
			currentMarker[i]++;
	}
	
	/**
	 * Checks if we run out of letters in marker.
	 * 
	 * @param index Index from we will begin to check backwards.
	 * @return Are we run out of letters in marker?
	 */
	private boolean isEveryMarkerLetterAlreadyMaxed(int index)
	{
		for(int i=index-1;i>=0;i--)
		{
			if(currentMarker[i]!='Z')
				return false;
		}
		return true;
	}
	
	@Override
	public String toString()
	{
		StringBuilder s=new StringBuilder();
		for(int i=0;i<currentMarker.length;i++)
			s.append(currentMarker[i]);
		return s.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof LetterListMarker marker)
		{
			return Arrays.equals(currentMarker,marker.getMarker());
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash=3;
		hash=97*hash+Arrays.hashCode(this.currentMarker);
		return hash;
	}
}
