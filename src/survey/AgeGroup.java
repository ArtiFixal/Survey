package survey;

/**
 * Describes user affiliation to the age group.
 * 
 * @author ArtiFixal
 */
public enum AgeGroup {
	AGE_13_17(0),
	AGE_18_24(1),
	AGE_25_32(2),
	AGE_33_49(3),
	AGE_50_64(4),
	AGE_65_PLUS(5);
	
	private final byte ageGroupID;

	private AgeGroup(int ageGroupID) {
		this.ageGroupID=(byte)ageGroupID;
	}

	public byte getAgeGroupID() {
		return ageGroupID;
	}

	@Override
	public String toString()
    {
        return switch (ageGroupID) {
            case 0 -> "13-17";
            case 1 -> "18-24";
            case 2 -> "25-32";
            case 3 -> "33-49";
			case 4 -> "50-64";
			case 5 -> "65+";
            default -> throw new IllegalArgumentException("Illegal ID of age group: "+ageGroupID);
        };
    }
	
	public static AgeGroup interpretFromNumber(int n)
	{
		return switch(n) {
			case 1 -> AgeGroup.AGE_13_17;
			case 2 -> AgeGroup.AGE_18_24;
			case 4 -> AgeGroup.AGE_25_32;
			case 8 -> AgeGroup.AGE_33_49;
			case 16 -> AgeGroup.AGE_50_64;
			case 32 -> AgeGroup.AGE_65_PLUS;
			default -> throw new IllegalArgumentException("Age group can't be interpreted from number: "+n);
		};
	}
}
