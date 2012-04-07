package server;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Server Data Class- przechowuje wszystkie dane symulacji.
 */
class ComServerData
{
	private final ReentrantReadWriteLock fLock = new ReentrantReadWriteLock();
	private final Lock fReadLock = fLock.readLock();
	private final Lock fWriteLock = fLock.writeLock();

	/*
	 * Flaga otwartego po³¹czenia z modu³em GUI
	 */
	public boolean isGuiConnected = false;

	/*
	 * Flaga otwartego po³¹czenia z modu³em sterowania ruchem
	 */
	public boolean isCarConnected = false;

	/*
	 * Flaga otwartego po³¹czenia z modu³em sterowania œwiat³ami
	 */
	public boolean isLightConnected = false;

	/*
	 * Aktualne wspó³rzêdne wszystkich pojazdów
	 */
	private String cars;

	/*
	 * Aktualny stan œwiate³
	 */
	private String lights;

	/*
	 * Ustawienia symulacji
	 */
	private String settings;
	
	public ComServerData(){
		/*
		 * wprowadzenie danych domyœlnych
		 */
		
	}

	public String getCars() {
		String result = "";
		fReadLock.lock();
		try {
			result = this.cars;
		}
		finally {
			fReadLock.unlock();
		}
		return result;
	}

	public void setCars(String cars) {
		fWriteLock.lock();
		try {
			this.cars = cars;
		}
		finally {
			fWriteLock.unlock();
		}
	}

	public String getLights() {
		String result = "";
		fReadLock.lock();
		try {
			// TODO sprawdzanie czy jest po³¹czenie z modu³em œwiate³ (jeœli nie generowanie co okreœlony czas danych)
			result = this.lights;
		}
		finally {
			fReadLock.unlock();
		}
		return result;
	}

	public void setLights(String lights) {
		fWriteLock.lock();
		try {
			this.lights = lights;
		}
		finally {
			fWriteLock.unlock();
		}
	}

	public String getSettings() {
		String result = "";
		fReadLock.lock();
		try {
			result = this.settings;
		}
		finally {
			fReadLock.unlock();
		}
		return result;
	}

	public void setSettings(String settings) {
		fWriteLock.lock();
		try {
			this.settings = settings;
		}
		finally {
			fWriteLock.unlock();
		}
	}
	
}
