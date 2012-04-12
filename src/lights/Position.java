package lights;

/**
 * Definicje kierunkow
 * @author wpw
 *
 */
public enum Position {
	NORTH, SOUTH, EAST, WEST, NONE;
	
	/**
	 * parsuje zadana pozycje do liczby dziesietnej
	 * @param position
	 * @return pozycja
	 */
	public static int parseInt(Position position) {
		switch(position) {
		case NORTH: return 0;
		case EAST: return 1;
		case SOUTH: return 2;
		case WEST: return 3;
		default: return -1;
		}
	}
	
	/**
	 * parsuje zadana pozycje do stringa
	 * @param position
	 * @return pozycja
	 */
	public static String toString(Position position) {
		switch(position) {
		case NORTH: return "0";
		case EAST: return "1";
		case SOUTH: return "2";
		case WEST: return "3";
		default: return "-1";
		}
	}
}
