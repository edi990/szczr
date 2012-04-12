package server;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Przechowuje aktualne dane ca³ej symulacji:
 * <ul>
 * 		<li>po³o¿enia samochodów</li>
 * 		<li>stan sygnalizatorów</li>
 * 		<li>obci¹¿enie na skrzy¿owaniach</li>
 * 		<li>ustawienia symulacji</li>
 * 		<li>stan po³¹czeñ z modu³ami</li>
 * 		<li>stan symulacji (w³¹czona/wy³¹czona)</li>
 * </ul>
 */
class ComServerData
{
	/**
	 * synchronizacja odczytu zapisu- blokada
	 */
	private final ReentrantReadWriteLock fLock = new ReentrantReadWriteLock();

	/**
	 * blokada przy odczycie
	 */
	private final Lock fReadLock = fLock.readLock();

	/**
	 * blokada przy zapisie
	 */
	private final Lock fWriteLock = fLock.writeLock();

	/**
	 * Flaga otwartego po³¹czenia z modu³em GUI
	 */
	public boolean isGuiConnected = false;

	/**
	 * Flaga otwartego po³¹czenia z modu³em sterowania ruchem
	 */
	public boolean isCarConnected = false;

	/**
	 * Flaga otwartego po³¹czenia z modu³em sterowania œwiat³ami
	 */
	public boolean isLightConnected = false;

	/**
	 * Czy symulacja jest uruchomiona - sterowanie z GUI
	 */
	public boolean simulationRunning = false;	

	/**
	 * Aktualne wspó³rzêdne wszystkich pojazdów
	 */
	private LinkedList<Integer> cars = null;

	/**
	 * Nowe samochody dodane w GUI
	 */
	private LinkedList<Integer> newCars = null;

	/**
	 * Aktualne dane obci¹¿enia skrzy¿owañ
	 */
	private LinkedList<Integer> crossroadsCars = null;

	/**
	 * Aktualny stan œwiate³
	 */
	private LinkedList<Integer> lights = null;

	/**
	 * Ustawienia symulacji
	 */
	private LinkedList<Integer> settings = null;

	/**
	 * Inicjalizacja bazy
	 */
	public ComServerData(){
		/*
		 * wprowadzenie danych domyœlnych
		 */
		LinkedList<Integer> list = new LinkedList<Integer>();
		list.add(10);
		list.add(20);
		setSettings(new LinkedList<Integer>(list));
	}

	/**
	 * Zwraca aktualne po³o¿enia samochodów (id, x, y).
	 * @return samochody
	 */
	public LinkedList<Integer> getCars() {
		LinkedList<Integer> result = null;
		fReadLock.lock();
		try {
			result = this.cars;
		}
		finally {
			fReadLock.unlock();
		}
		return result;
	}

	/**
	 * Zapisuje aktualne po³o¿enia samochodów.
	 * @param cars samochody
	 */
	public void setCars(LinkedList<Integer> cars) {
		fWriteLock.lock();
		try {
			this.cars = cars;
		}
		finally {
			fWriteLock.unlock();
		}
	}

	/**
	 * Zwraca obci¹¿enia skrzy¿owañ (iloœæ samochodów z ka¿dej strony)
	 * @return dane o obci¹¿eniu skrzy¿owañ
	 */
	public LinkedList<Integer> getCrossroadsCars() {
		LinkedList<Integer> result = null;
		fReadLock.lock();
		try {
			result = this.crossroadsCars;
		}
		finally {
			fReadLock.unlock();
		}
		return result;
	}

	/**
	 * Wprowadza dane o obci¹¿eniu skrzy¿owañ.
	 * @param crossroadsCars dane o obci¹¿eniu
	 */
	public void setCrossroadsCars(LinkedList<Integer> crossroadsCars) {
		fWriteLock.lock();
		try {
			this.crossroadsCars = crossroadsCars;
		}
		finally {
			fWriteLock.unlock();
		}
	}

	/**
	 * Zwraca stañ sygnalizacji.
	 * @return stan sygnalizatorów
	 */
	public LinkedList<Integer> getLights() {
		LinkedList<Integer> result = null;
		fReadLock.lock();
		try {
			result = this.lights;
		}
		finally {
			fReadLock.unlock();
		}
		return result;
	}

	/**
	 * Wprowadza stan sygnalizatorów.
	 * @param lights stan sygnalizatorów
	 */
	public void setLights(LinkedList<Integer> lights) {
		fWriteLock.lock();
		try {
			this.lights = lights;
		}
		finally {
			fWriteLock.unlock();
		}
	}

	/**
	 * Zwraca ustawienia symulacji.
	 * @return ustawienia
	 */
	public LinkedList<Integer> getSettings() {
		LinkedList<Integer> result = null;
		fReadLock.lock();
		try {
			result = this.settings;
		}
		finally {
			fReadLock.unlock();
		}
		return result;
	}

	/**
	 * Wprowadza ustawienia symulacji (prêdkoœæ, odstêpy miêdzy nowymi samochodami).
	 * @param settings ustawienia
	 */
	public void setSettings(LinkedList<Integer> settings) {
		fWriteLock.lock();
		try {
			this.settings = settings;
		}
		finally {
			fWriteLock.unlock();
		}
	}

	/**
	 * Zwraca i czyœci kolejkê nowych samochodów do dodania.
	 * @return nowe samochody
	 */
	public LinkedList<Integer> getNewCars() {
		if(this.newCars == null) return null;
		
		LinkedList<Integer> result = null;
		fReadLock.lock();
		try {
			result = new LinkedList<Integer>(this.newCars);
			this.newCars = null;
		}
		finally {
			fReadLock.unlock();
		}
		return new LinkedList<Integer>(result);
	}

	/**
	 * Dodaje do kolejki nowe samochody.
	 * @param newCars lista wjazdów na których maj¹ zostaæ dodane
	 */
	public void addNewCars(LinkedList<Integer> newCars) {
		if(newCars == null) return;
		fWriteLock.lock();
		try {
			if(this.newCars == null)
				this.newCars = newCars;
			else this.newCars.addAll(newCars);
		}
		finally {
			fWriteLock.unlock();
		}
	}
	
}
