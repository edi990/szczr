package lights;

/**
 * Klasa reprezentujaca sygnalizator, zawiera informacje o pozycji sygnalizatora na skrzyzowaniu
 * jego kolorze, ilosci samochodow czekajacych na zielone swiatlo, znacznik czy swiatlo juz wzielo udzial w cyklu
 *
 */
public class StreetLight {	
	public static final double STREET_CAPACITY	= 20.0;
	
	private Position position;
	private LightColor color;
	private Integer streetTraffic;
	private Double factor;
	private Boolean used;
	
	public StreetLight() {
		position = Position.NONE;
		color = LightColor.RED;
		streetTraffic = 0;
		factor = 0.0;
		used = false;
	}
	
	public StreetLight(Position position) {
		this.position = position;
		color = LightColor.RED;
		streetTraffic = 0;
		factor = 0.0;
		used = false;
	}
	
	public void setPosition(Position position) {
		this.position = position;
	}
	
	public Position getPosition() {
		return position;
	}
	
	public void setColor(LightColor color) {
		this.color = color;
	}
	
	public LightColor getColor() {
		return color;
	}
	
	public void setUsed(boolean value) {
		used = value;
	}
	
	public boolean isUsed() {
		return used.booleanValue();
	}
	
	public void calculateFactor() {
		factor = (double)streetTraffic / STREET_CAPACITY * 100.0;
	}
	
	public double getFactor() {
		return factor;
	}
	
	public void setData(int input) {
		streetTraffic = input;
	}
	
	public int getData() {
		if (color == LightColor.GREEN)
			return Position.parseInt(position);
		else
			return -1;
	}
}
