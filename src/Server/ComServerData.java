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
	 * Flaga otwartego po��czenia z modu�em GUI
	 */
	public boolean isGuiConnected = false;

	/*
	 * Flaga otwartego po��czenia z modu�em sterowania ruchem
	 */
	public boolean isCarConnected = false;

	/*
	 * Flaga otwartego po��czenia z modu�em sterowania �wiat�ami
	 */
	public boolean isLightConnected = false;

	/*
	 * Aktualne wsp�rz�dne wszystkich pojazd�w
	 */
	private String cars;

	/*
	 * Aktualny stan �wiate�
	 */
	private String lights;

	/*
	 * Ustawienia symulacji
	 */
	private String settings;
	
	public ComServerData(){
		/*
		 * wprowadzenie danych domy�lnych
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
			// TODO sprawdzanie czy jest po��czenie z modu�em �wiate� (je�li nie generowanie co okre�lony czas danych)
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
