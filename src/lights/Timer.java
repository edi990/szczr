package lights;

/**
 * Klasa reprezentujaca timer kazde ze skrzyzowan posiada instancje tej klasy
 * @author wpw
 *
 */
public class Timer {
	public static final int SHORT	= 2;
	public static final int NORMAL	= 3;
	public static final int LONG	= 4;
	
	private Integer time;
	private Boolean expired;
	
	public Timer() {
		time = 0;
		expired = true;
	}
	
	public boolean isExpired() {
		return expired.booleanValue();
	}
	
	public void reduce() {
		time--;
		if (time <= 0)
			expired = true;
	}
	
	/**
	 * ustawia timer w zaleznosci od wartosci wspolczynnika podanego jako parametr
	 * @param factor
	 */
	public void setTimer(double factor) {
		expired = false;
		if (factor > 0.0 && factor <= 25.0 )
			time = SHORT;
		else if (factor > 25.0 && factor <= 75.0)
			time = NORMAL;
		else if (factor > 75.0 && factor <= 100.0)
			time = LONG;
	}
}
