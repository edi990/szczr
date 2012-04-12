package simulator;

import java.io.IOException;
import protocol.ComData;
import client.ComClient;

/**
 * Obs�uga symulatora.<br/>
 * Wymiana danych pomi�dzy symulatorem a serwerem.
 * 
 */
public class Main {

	/**
	 * przerwa pomi�dzy kolejnymi krokami
	 */
	static int sleepTime;

	/**
	 * ilo�� iteracji pomi�dzy kolejnymi samochodami
	 */
	static int carSpawn;

	/**
	 * @see gui.Gui#tryToConnect
	 */
	static ComClient tryToConnect() throws InterruptedException{
		ComClient cli = null;
		while(true){
			try {
				cli = new ComClient("CARS");
			} catch (IOException e) {
				Thread.sleep(1000*sleepTime);
				System.out.println("Pr�ba wznawiania po��czenia...");
				continue;
			}
			break;
		}
		return cli;
	}

	/**
	 * Sprawdza czy symulacja jest uruchomiona, je�li tak pobiera ustawienia.
	 * @param cli obiekt obs�uguj�cy po��czenie z serwerem
	 * @throws InterruptedException
	 */
	static void waitForStart(ComClient cli) throws InterruptedException{
		ComData data = new ComData();
		// czeka na start
		do{
			try {
				data = cli.send(new ComData("STATE"));
			} catch (IOException e) {
				// proba nawiazania polaczenia
				cli = tryToConnect();
			}
			Thread.sleep(300);
		}while(data.message.equals("STOPPED"));
		
		// pobiera ustawienia
		try {
			data = cli.send(new ComData("SETTINGS"));
			if(data.comData != null){
				sleepTime = data.comData.get(0);
				carSpawn = data.comData.get(1);
			}
		} catch (IOException e) {
			// proba nawiazania polaczenia
			cli = tryToConnect();
		}
		
	}

	/**
	 * Metoda obs�uguj�ca przep�yw danych pomi�dzy symulatorem a serwerem.
	 * @param windowWidth szeroko�� okna symulacji
	 * @param windowHeight wysoko�� okna
	 * @param squareRadius po�owa d�ugo�ci ulicy
	 * @param carRadius po�owa szeroko�ci samochodu
	 * @param lightCheck co ile iteracji sprawdza� �wiat�a
	 * @throws InterruptedException
	 */
	static void go(int windowWidth, int windowHeight, int squareRadius, int carRadius, int lightCheck) throws InterruptedException {
		ComData data = new ComData();
		ComClient cli = null;

		cli = tryToConnect();
		
		Simulator.initialize(squareRadius, carRadius);
		Simulator.addLights();
		
		waitForStart(cli);
		
		for(int i=0;;i++){
			
			/*
			 * wysylam samochody
			 */
			data.comData = Simulator.writeMap();
			data.message = "CARS";
			try {
				data = cli.send(new ComData(data));
			} catch (IOException e) {
				// proba nawiazania polaczenia
				cli = tryToConnect();
			}
			if(data.message.equals("STOPPED")){
				waitForStart(cli);
				--i; // nie licze tego kroku
				continue;
			}
			/*
			 * dodaje otrzymane nowe samochody
			 */
			else if(data.comData != null){
				for(Integer nr : data.comData){
					Simulator.addCar(new Car(nr));
				}
			}
			
			// krok naprzod
			Simulator.step();
			
			/*
			 * wysylam obciazenie skrzyzowan i pobieram swiatla
			 */
			if(i%lightCheck==0){
				// wysylam obciazenie
				data.comData = Simulator.writeLights();
				data.message = "CROSSROADSCARS";
				try {
					data = cli.send(new ComData(data));
				} catch (IOException e) {
					// proba nawiazania polaczenia
					cli = tryToConnect();
				}
				
				// pobieram swiatla
				try {
					data = cli.send(new ComData("LIGHTS"));
				} catch (IOException e) {
					// proba nawiazania polaczenia
					cli = tryToConnect();
				}
				if(data.message.equals("STOPPED")){
					waitForStart(cli);
					--i; // nie licze tego kroku
					continue;
				}
				if(data.comData != null){
					Simulator.readLights(data.comData);
				} else {
					if(i%100==0) for(Light light : Simulator.lights) light.switchState(); // zmien stany domyslnie
					// wyslij swiatla awaryjnie
					data.comData = Simulator.getLightStates();
					data.message = "sLIGHTS";
					try {
						data = cli.send(new ComData(data));
					} catch (IOException e) {
						// proba nawiazania polaczenia
						cli = tryToConnect();
					}
				}
			}
			
			// dodaj nowy samochod
			if(i%carSpawn==0)Simulator.addCar(new Car((int)(Math.random()*12)));
			
			Thread.sleep(sleepTime);
		}
	}

	/**
	 * Uruchomienie symulatora
	 * @param args argumenty wywo�ania (nieistotne)
	 * @throws InterruptedException
	 */
	public static void main(String[] args){
		sleepTime = 10;
		carSpawn = 20;
		try {
			go(671, 693, 75, 3, 10);
		} catch (InterruptedException e) {
			System.err.println("Wyst�pi� b��d przerwania...");
		}
	}
}
