package lights;

import java.util.LinkedList;

/**
 * 
 * Klasa reprezentujaca 4 swiatla na skrzyzowaniu oraz odmierzajaca czas do nastepnej zmiany swiatla
 *
 */
public class CrossroadLights {
	private LinkedList<StreetLight> lights;
	private Boolean blocked;
	private Timer timer;
	
	/**
	 * Domyslnie skrzyzowanie posiada 4 sygnalizatory wyzerowany timer
	 * 
	 */
	public CrossroadLights() {
		lights = new LinkedList<StreetLight>();
		blocked = true;
		timer = new Timer();
		
		lights.add(new StreetLight(Position.NORTH));
		lights.add(new StreetLight(Position.EAST));
		lights.add(new StreetLight(Position.SOUTH));
		lights.add(new StreetLight(Position.WEST));
	}
	
	/**
	 * sprawdza czy skrzyzowanie jest zablokowane
	 * @return true- zablokowany
	 */
	public boolean isBlocked() {
		return blocked.booleanValue();
	}
	
	/**
	 * blokuje/odblokowuje skrzyzowanie tzn zapala wszystkie swiatla na czerwono
	 * @param value
	 */
	public void setBlocked(boolean value) {
		if (!value)
			blocked = false;
		else {
			blocked = true;
			for(StreetLight l: lights)
				l.setColor(LightColor.RED);
		}
	}
	
	/**
	 * sprawdza stan timera jezeli 0 to zwraca true
	 * @return true- czas uplynal
	 */
	public boolean isTimerExpired() {
		return timer.isExpired();
	}
	
	/**
	 * redukuje timer
	 */
	public void reduceTimer() {
		timer.reduce();
	}
	
	/**
	 * ustawia dane o liczbie oczekujacych samochodow na kazdym z sygnalizatorow
	 * @param input
	 */
	public void setData(LinkedList<Integer> input) {
		for (StreetLight sl: lights)
			sl.setData(input.pollFirst());
	}
	
	/**
	 * wysyla informacje o tym ktory z sygnalizatorow ma zielone swiatlo
	 * @return nr sygnalizatora
	 */
	public int getData() {
		Integer output = -1;
		
		for (StreetLight sl: lights)
			if((output = sl.getData()) != -1)
				return output;

		return 4;
	}
	
	/**
	 * funkcja odpowiedzialna za przelaczanie swiatel
	 */
	public void switchLights() {
		StreetLight light;
		
		if (!blocked) {
			blocked = true;
			for(StreetLight sl: lights)
				sl.setColor(LightColor.RED);
		} else {
			calculateFactors();
			cycleControl();
			if ((light = selectStreetLight()) == null)
				return;
			timer.setTimer(light.getFactor());
			light.setColor(LightColor.GREEN);
			blocked = false;
		}
	}
	
	/* public void switchLights() {
		cycleControl();
		calculateFactors();
		timer.setTimer(selectStreetLight().getFactor());
	} */

	/**
	 * pilnuje wykonanie cyklu cykl konczy sie gdy na kazdym z sygnalizatorow bylo zielone swiatlo
	 */
	private void cycleControl() {
		for (StreetLight l: lights)
			if(!l.isUsed())
				return;
		for (StreetLight l: lights)
			l.setUsed(false);
	}
	
	/**
	 * oblicza wspolczynnik zajetosci dla kazdej z ulic
	 */
	private void calculateFactors() {
		for(StreetLight l: lights)
			l.calculateFactor();
	}
	
	/**
	 * Funkcja sygnalizator na ktorym ma zapalic sie zielone swiatlo
	 * @return sygnalizator
	 */
	private StreetLight selectStreetLight() {
		StreetLight light = new StreetLight();
		
		for(StreetLight l: lights){
			if (l.getFactor() == 0)
				l.setUsed(true);
			if ((light.getFactor() < l.getFactor()) && !l.isUsed())
				light = l;
		}
		
		light.setUsed(true);
		
		if (light.getFactor() == 0)
			return null;
		else
			return light;
	}
}
