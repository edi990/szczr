package server;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Przechowuje aktualne dane ca�ej symulacji:
 * <ul>
 * 		<li>po�o�enia samochod�w</li>
 * 		<li>stan sygnalizator�w</li>
 * 		<li>obci��enie na skrzy�owaniach</li>
 * 		<li>ustawienia symulacji</li>
 * 		<li>stan po��cze� z modu�ami</li>
 * 		<li>stan symulacji (w��czona/wy��czona)</li>
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
	 * Flaga otwartego po��czenia z modu�em GUI
	 */
	public boolean isGuiConnected = false;

	/**
	 * Flaga otwartego po��czenia z modu�em sterowania ruchem
	 */
	public boolean isCarConnected = false;

	/**
	 * Flaga otwartego po��czenia z modu�em sterowania �wiat�ami
	 */
	public boolean isLightConnected = false;

	/**
	 * Czy symulacja jest uruchomiona - sterowanie z GUI
	 */
	public boolean simulationRunning = false;	

	/**
	 * Aktualne wsp�rz�dne wszystkich pojazd�w
	 */
	private LinkedList<Integer> cars = null;

	/**
	 * Nowe samochody dodane w GUI
	 */
	private LinkedList<Integer> newCars = null;

	/**
	 * Aktualne dane obci��enia skrzy�owa�
	 */
	private LinkedList<Integer> crossroadsCars = null;

	/**
	 * Aktualny stan �wiate�
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
		 * wprowadzenie danych domy�lnych
		 */
		LinkedList<Integer> list = new LinkedList<Integer>();
		list.add(10);
		list.add(20);
		setSettings(new LinkedList<Integer>(list));
	}

	/**
	 * Zwraca aktualne po�o�enia samochod�w (id, x, y).
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
	 * Zapisuje aktualne po�o�enia samochod�w.
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
	 * Zwraca obci��enia skrzy�owa� (ilo�� samochod�w z ka�dej strony)
	 * @return dane o obci��eniu skrzy�owa�
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
	 * Wprowadza dane o obci��eniu skrzy�owa�.
	 * @param crossroadsCars dane o obci��eniu
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
	 * Zwraca sta� sygnalizacji.
	 * @return stan sygnalizator�w
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
	 * Wprowadza stan sygnalizator�w.
	 * @param lights stan sygnalizator�w
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
	 * Wprowadza ustawienia symulacji (pr�dko��, odst�py mi�dzy nowymi samochodami).
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
	 * Zwraca i czy�ci kolejk� nowych samochod�w do dodania.
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
	 * @param newCars lista wjazd�w na kt�rych maj� zosta� dodane
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
