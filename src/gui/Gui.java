package gui;

import java.io.IOException;
import java.util.LinkedList;

import protocol.ComData;
import client.ComClient;

/**
 * Obs³uga interfejsu graficznego.<br/>
 * Wyœwietla aktualny stan symulacji i umo¿liwia zmianê jej ustawieñ.
 * 
 */
public class Gui {

	/**
	 * przerwa pomiêdzy kolejnymi krokami
	 */
	static int sleepTime = 10;
	
	/**
	 * iloœæ iteracji pomiêdzy kolejnymi samochodami
	 */
	static int carSpawn = 20;

	/**
	 * stan symulacji (w³¹czona/wy³¹czona)
	 */
	static boolean running = false;

	/**
	 * okno z symulacj¹
	 */
	static Window window;

	/**
	 * kolejka samochodów do dodania
	 */
	private static LinkedList<Integer> newCars = null;

	/**
	 * Próbuje po³¹czyæ siê z serwerem.<br/>
	 * W razie niepowodzenia czeka i próbuje ponownie.
	 * @return obiekt ob³uguj¹cy po³¹czenie
	 * @throws InterruptedException
	 */
	static ComClient tryToConnect() throws InterruptedException{
		ComClient cli = null;
		while(true){
			try {
				cli = new ComClient("GUI");
			} catch (IOException e) {
				Thread.sleep(1000*sleepTime);
				System.out.println("Próba wznawiania po³¹czenia...");
				continue;
			}
			break;
		}
		return cli;
	}

	/**
	 * Sprawdza czy symulacja jest uruchomiona, jeœli tak pobiera ustawienia.<br/>
	 * Jeœli nie, czeka na start symulacji i wysy³a informacjê o starcie do serwera.
	 * @param cli obiekt obs³uguj¹cy po³¹czenie z serwerem
	 * @throws InterruptedException
	 */
	static void waitForStart(ComClient cli) throws InterruptedException{
		ComData data = new ComData();
		// czeka na start
		
		try {
			data = cli.send(new ComData("STATE"));
		} catch (IOException e) {
			// proba nawiazania polaczenia
			cli = tryToConnect();
		}
		if(data.message.equals("STARTED")) {
			running = true;
			window.menu.startStop.setText("Stop");
			window.menu.carSpawn.setEnabled(false);
			
			try {
				data = cli.send(new ComData("SETTINGS"));
				if(data.comData != null){
					sleepTime = data.comData.get(0);
					carSpawn = data.comData.get(1);
					window.menu.carSpawn.setText(new Integer(carSpawn*sleepTime).toString());
				}
			} catch (IOException e) {
				// proba nawiazania polaczenia
				cli = tryToConnect();
			}
		}
		
		while(!running){
			Thread.sleep(100);
		}
		
		try {
			data.message = "START";
			data.comData = new LinkedList<Integer>();
			data.comData.add(sleepTime);
			data.comData.add(carSpawn);
			data = cli.send(new ComData(data));
		} catch (IOException e) {
			// proba nawiazania polaczenia
			cli = tryToConnect();
		}

	}

	/**
	 * Wysy³a sygna³ o zatrzymaniu symulacji do serwera.
	 * @param cli obiekt obs³uguj¹cy po³¹czenie z serwerem
	 * @throws InterruptedException
	 */
	static void sendStop(ComClient cli) throws InterruptedException{
		
		// wysyla stop do serwera
		try {
			cli.send(new ComData("STOP"));
		} catch (IOException e) {
			// proba nawiazania polaczenia
			cli = tryToConnect();
		}
		
		waitForStart(cli);

	}

	/**
	 * Metoda obs³uguj¹ca przep³yw danych pomiêdzy GUI a serwerem oraz utworzenie i odœwie¿anie symulacji.
	 * @param windowWidth szerokoœæ okna symulacji
	 * @param windowHeight wysokoœæ okna
	 * @param sleepTime przerwa pomiêdzy kolejnymi zapytaniami
	 * @throws InterruptedException
	 */
	private static void go(int windowWidth, int windowHeight, int sleepTime) throws InterruptedException{
		window=new Window(windowWidth+200, windowHeight);
		
		ComData data = new ComData();
		ComClient cli = null;
		
		// proba nawiazania polaczenia
		cli = tryToConnect();
		
		waitForStart(cli);

		while(true){
			
			/*
			 * sprawdzam czy stop
			 */
			if(!running) sendStop(cli);
			
			/*
			 * prosze o samochody, wysylam nowe
			 */
			data.message = "CARS";
			data.comData = Gui.getNewCars();
			try {
				data = cli.send(new ComData(data));
			} catch (IOException e) {
				// proba nawiazania polaczenia
				cli = tryToConnect();
			}
			synchronized(DrawingArea.list){
				if(data.comData != null) DrawingArea.list = new LinkedList<Integer>(data.comData);
			}
			
			/*
			 * prosze o swiatla
			 */
			try {
				data = cli.send(new ComData("LIGHTS"));
			} catch (IOException e) {
				// proba nawiazania polaczenia
				cli = tryToConnect();
			}
			synchronized(DrawingArea.listLights){
				if(data.comData != null) 
					for(int i=2,j=0;i<27;i+=3,++j){
						DrawingArea.listLights.set(i,data.comData.get(j));
					}
			}

			window.repaint();
			Thread.sleep(sleepTime);
		}
		
	}

	/**
	 * Uruchomienie GUI
	 * @param args argumenty wywo³ania (nieistotne)
	 * @throws InterruptedException
	 */
	public static void main(String[] args){
		try {
			go(671, 693, 10);
		} catch (InterruptedException e) {
			System.err.println("Wyst¹pi³ b³¹d przerwania...");
		}
	}

	/**
	 * Pobiera nowe samochody z kolejki i czyœci j¹.
	 * @return lista samochodów (nr wjazdów)
	 */
	public static LinkedList<Integer> getNewCars() {
		if(newCars == null) return null;
		synchronized(Gui.newCars){
			LinkedList<Integer> temp = new LinkedList<Integer>(newCars);
			newCars = null;
			return new LinkedList<Integer>(temp);
		}
	}

	/**
	 * Dodaje nowy samochód (nr wjazdu).
	 * @param newCar nr wjazdu
	 */
	public static void addNewCar(int newCar) {
		if(newCars == null) newCars = new LinkedList<Integer>();
		synchronized(Gui.newCars){
			Gui.newCars.add(newCar);
		}
	}
}
