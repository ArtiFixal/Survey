package survey;

/**
 * Describes user locality size.
 * 
 * @author ArtiFixal
 */
public enum Settlement {
    VILLAGE(0),
    TOWN(1),
    CITY(2),
    METROPOLIS(3);
    
    final byte settlementID;
    
    private Settlement(int settlementID) {
        this.settlementID=(byte)settlementID;
    }
    
    public byte getSettlementID()
    {
        return settlementID;
    }
    
    @Override
    public String toString()
    {
        return switch (settlementID) {
            case 0 -> "village";
            case 1 -> "town";
            case 2 -> "city";
            case 3 -> "metropolis";
            default -> throw new IllegalArgumentException("Illegal settlement of ID: "+settlementID);
        };
    }
	
	public static Settlement interpretFromNumber(int n)
	{
		return switch(n) {
			case 1 -> Settlement.VILLAGE;
			case 2 -> Settlement.TOWN;
			case 4 -> Settlement.CITY;
			case 8 -> Settlement.METROPOLIS;
			default -> throw new IllegalArgumentException("Settlement can't be interpreted from number: "+n);
		};
	}
}
