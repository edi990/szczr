package lights;

import java.util.LinkedList;

/**
 * Klasa reprezentujaca siec skrzyzowan
 */
public class SystemLights {
	public static final int CROSSROADS_NUMBER = 9;
	
	private LinkedList<CrossroadLights> systemLights;
	
	public SystemLights() {
		systemLights = new LinkedList<CrossroadLights>();

		while(systemLights.size() < CROSSROADS_NUMBER) 
			systemLights.add(new CrossroadLights());
	}
	
	/**
	 * akualizuje swiatla na skrzyzowaniach
	 */
	public void update() {
		for(CrossroadLights cl: systemLights) { 
			cl.reduceTimer();
			if (cl.isTimerExpired())
				cl.switchLights();
		}
	}

	/**
	 * ustawia dane o ilosci samochodow oczekujacych na kazdym z sygnalizatorow
	 * @param input
	 */
	public void setData(LinkedList<Integer> input) {
		LinkedList<Integer> crossroadData = new LinkedList<Integer>();;
		
		for (CrossroadLights cl: systemLights) {
			crossroadData.clear();
			while (crossroadData.size() < 4) 
				crossroadData.add(input.pollFirst());
			cl.setData(crossroadData);
		}
	}
	
	/**
	 * pobiera z kazdego skrzyzowania numer sygnalizatora na ktorym swieci sie zielone swiatlo
	 * @return zielone swiatla
	 */
	public LinkedList<Integer> getData() {
		LinkedList<Integer> output = new LinkedList<Integer>();
		
		for(CrossroadLights cl: systemLights)
			output.add(cl.getData());
		
		return output;
	}
}
