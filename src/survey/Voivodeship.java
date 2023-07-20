package survey;

/**
 * Describes user geographical location.
 * 
 * @author ArtiFixal
 */
public enum Voivodeship {
	ZACHODNIO_POMORSKIE(0),
	POMORSKIE(1),
	WARMIŃSKO_MAZURSKIE(2),
	PODLASKIE(3),
	LUBUSKIE(4),
	WIELKOPOLSKIE(5),
	KUJAWSKO_POMORSKIE(6),
	ŁÓDZKIE(7),
	MAZOWIECKIE(8),
	LUBELSKIE(9),
	DOLNOŚLĄSKIE(10),
	OPOLSKIE(11),
	ŚLĄSKIE(12),
	ŚWIĘTOKRZYSKIE(13),
	MAŁOPOLSKIE(14),
	PODKARPACKIE(15);
	
	private final byte voivodeshipID;

	private Voivodeship(int voivodeshipID) {
		this.voivodeshipID=(byte)voivodeshipID;
	}

	public byte getVoivodeshipID()
	{
		return voivodeshipID;
	}
	
	@Override
	public String toString() {
		return switch(voivodeshipID) {
			case 0 -> "zachodnio-pomorskie";
			case 1 -> "pomorskie";
			case 2 -> "warmińsko-mazurskie";
			case 3 -> "podlaskie";
			case 4 -> "lubuskie";
			case 5 -> "wielkopolskie";
			case 6 -> "kujawsko-pomorskie";
			case 7 -> "łódzkie";
			case 8 -> "mazowieckie";
			case 9 -> "lubelskie";
			case 10 -> "dolnośląskie";
			case 11 -> "opolskie";
			case 12 -> "śląskie";
			case 13 -> "świętokrzyskie";
			case 14 -> "małopolskie";
			case 15 -> "podkarpackie";
			default -> throw new IllegalArgumentException("Illegal ID of voivodeship: "+voivodeshipID);
		};
	}
	
	public static Voivodeship interpretFromNumber(int n)
	{
		return switch(n) {
			case 1 -> Voivodeship.ZACHODNIO_POMORSKIE;
			case 2 -> Voivodeship.POMORSKIE;
			case 4 -> Voivodeship.WARMIŃSKO_MAZURSKIE;
			case 8 -> Voivodeship.PODLASKIE;
			case 16 -> Voivodeship.LUBUSKIE;
			case 32 -> Voivodeship.WIELKOPOLSKIE;
			case 64 -> Voivodeship.KUJAWSKO_POMORSKIE;
			case 128 -> Voivodeship.ŁÓDZKIE;
			case 256 -> Voivodeship.MAZOWIECKIE;
			case 512 -> Voivodeship.LUBELSKIE;
			case 1024 -> Voivodeship.DOLNOŚLĄSKIE;
			case 2048 -> Voivodeship.OPOLSKIE;
			case 4096 -> Voivodeship.ŚLĄSKIE;
			case 8192 -> Voivodeship.ŚWIĘTOKRZYSKIE;
			case 16384 -> Voivodeship.MAŁOPOLSKIE;
			case 32768 -> Voivodeship.PODKARPACKIE;
			default -> throw new IllegalArgumentException("Voivodeship can't be interpreted from number: "+n);
		};
	}
}
