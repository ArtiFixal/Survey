package survey;

/**
 * Class representing user data.
 * 
 * @author ArtiFixal
 */
public class User{
    /**
	 * Respondent gender:<br>
	 * true - male<br>
	 * false - female<br>
	 */
	public boolean sex;
	public Education education;
	public Voivodeship voivodeship;
	public AgeGroup age;
	public Settlement livingIn;
    
    public User(boolean sex,Education edu,Voivodeship voi,AgeGroup age,Settlement livingIn)
    {
        this.sex=sex;
		this.education=edu;
		this.voivodeship=voi;
		this.age=age;
		this.livingIn=livingIn;
    }
	
	public boolean getSex()
	{
		return sex;
	}
    
    public Education getEducation()
    {
        return education;
    }

	public Voivodeship getVoivodeship() {
		return voivodeship;
	}

	public AgeGroup getAgeGroup() {
		return age;
	}

	public Settlement getSettlement() {
		return livingIn;
	}
}
