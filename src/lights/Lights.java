package lights;

import java.io.IOException;
import java.util.LinkedList;
import protocol.ComData;
import client.ComClient;

/**
 * Obs³uga sygnalizacji.<br/>
 * Wymiana danych pomiêdzy symulatorem a serwerem.
 * 
 */
public class Lights {

	/**
	 * obiekt klasy zarz¹dzaj¹cej sygnalizacj¹
	 */
	private static SystemLights system = new SystemLights();

	/**
	 * przerwa pomiêdzy kolejnymi zapytaniami
	 */
	public static int sleepTime;

	/**
	 * @see gui.Gui#tryToConnect
	 */
	static ComClient tryToConnect() throws InterruptedException{
		ComClient cli = null;
		while(true){
			try {
				cli = new ComClient("LIGHTS");
			} catch (IOException e) {
				Thread.sleep(10000);
				System.out.println("Próba wznawiania po³¹czenia...");
				continue;
			}
			break;
		}
		return cli;
	}
	
	/**
	 * @see simulator.Main#waitForStart
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
			Thread.sleep(200);
		}while(data.message.equals("STOPPED"));
		
		// pobiera ustawienia
		try {
			data = cli.send(new ComData("SETTINGS"));
			if(data.comData != null){
				sleepTime = data.comData.get(0);
			}
		} catch (IOException e) {
			// proba nawiazania polaczenia
			cli = tryToConnect();
		}
		
	}

	/**
	 * Metoda obs³uguj¹ca przep³yw danych pomiêdzy symulatorem a serwerem.
	 * @throws InterruptedException
	 */
	private static void go() throws InterruptedException{
		
		ComData data = new ComData();
		ComClient cli = null;
		
		// proba nawiazania polaczenia
		cli = tryToConnect();
		
		waitForStart(cli);
		
		while(true){
			/*
			 * prosze o obciazenie skrzyzowan
			 */
			try {
				data = cli.send(new ComData("CROSSROADSCARS"));
			} catch (IOException e) {
				// proba nawiazania polaczenia
				cli = tryToConnect();
			}
			if(data.message.equals("STOPPED")){
				waitForStart(cli);
				continue;
			}
			
			/*
			 * wysylam swiatla
			 */
			data.comData = (data.comData != null) ? makeLights(new LinkedList<Integer>(data.comData)) : null;
			data.message = "LIGHTS";
			try {
				data = cli.send(new ComData(data));
			} catch (IOException e) {
				// proba nawiazania polaczenia
				cli = tryToConnect();
			}
			
			Thread.sleep(100*sleepTime);
		}
		
	}

	/**
	 * Pobiera nowe stany sygnalizacji.
	 * @param cars obci¹¿enie skrzy¿owañ
	 * @return lista stanów sygnalizatorów
	 */
	private static LinkedList<Integer> makeLights(LinkedList<Integer> cars){
		system.setData(cars);
		system.update();
		return new LinkedList<Integer>(system.getData());
	}

	/**
	 * Uruchomienie symulatora
	 * @param args argumenty wywo³ania (nieistotne)
	 */
	public static void main(String[] args) {
		sleepTime = 10;
		try {
			go();
		} catch (InterruptedException e) {
			System.err.println("Wyst¹pi³ b³¹d przerwania...");
		}
	}
}
