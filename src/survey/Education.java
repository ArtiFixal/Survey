package survey;

/**
 * Describes user education level.
 * 
 * @author ArtiFixal
 */
public enum Education {
    PRIMARY_SCHOOL(0),
	
    SECONDARY_SCHOOL(1),
	
    HIGH_SCHOOL(2),
    
    HIGHER_EDUCATION(3);
	
	final byte educationID;
    
    private Education(int educationID)
    {
        this.educationID=(byte)educationID;
    }
    
    public byte getEducationID()
    {
        return educationID;
    }
    
    @Override
    public String toString()
    {
        return switch (educationID) {
            case 0 -> "primary school";
            case 1 -> "secondary school";
            case 2 -> "high school";
            case 3 -> "higher education";
            default -> throw new IllegalArgumentException("Illegal ID of education: "+educationID);
        };
    }
	
	public static Education interpretFromNumber(int n)
	{
		return switch(n) {
			case 1 -> Education.PRIMARY_SCHOOL;
			case 2 -> Education.SECONDARY_SCHOOL;
			case 4 -> Education.HIGH_SCHOOL;
			case 8 -> Education.HIGHER_EDUCATION;
			default -> throw new IllegalArgumentException("Education can't be interpreted from number: "+n);
		};
	}
}
