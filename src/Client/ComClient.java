package client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import protocol.ComData;

/**
 * Klasa obs³uguj¹ca po³¹czenie z serwerem po stronie klienta.
 *
 */
public class ComClient
{
	/**
	 * uchwyt po³¹czenia z serwerem
	 */
	private Socket				comSocket;
	
	/**
	 * strumieñ wyjœciowy
	 */
	private ObjectOutputStream	oOut;
	
	/**
	 * strumieñ wejœciowy
	 */
	private ObjectInputStream	oIn;
	
	/**
	 * czy po³¹czenie jest nawi¹zane
	 */
	private boolean				IsItOpen = false;
	
	/**
	 * czy mo¿na wys³aæ zapytanie
	 */
	private boolean				requesting = true;

	/**
	 * Tworzy po³¹czenie z serwerem.
	 * @param who identyfikator klienta- GUI, CARS, LIGHTS (inne niedozwolone)
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public ComClient(final String who) throws UnknownHostException, IOException{
		openCom("localhost", 6666);
		if(isItOpen()){
			ComData data = new ComData();
			getServerData(data);
			if(data.message.equals("Ready:")){
				sendDataToServer(new ComData(who));
				getServerData(data);
			}
		}
	}

	/**
	 * Wysy³a obiekt z danymi zapytania do serwera i odbiera odpowiedŸ.
	 * @param data obiekt z danymi przeznaczonymi do wys³ania
	 * @return obiekt z odpowiedzi¹
	 * @see protocol.ComData
	 * @throws IOException 
	 */
	public ComData send(ComData data) throws IOException{
		if(requesting){
			sendDataToServer(data);
			getServerData(data);
			return data;
		}
		return null;
	}

	/**
	 * Otwiera po³¹czenie z serwerem.
	 * @param sServerName adres serwera
	 * @param iPortNumber nr portu
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void openCom(String sServerName, int iPortNumber) throws UnknownHostException, IOException  
	{
		try {
			comSocket = new Socket(sServerName, iPortNumber);     
			oOut = new ObjectOutputStream(comSocket.getOutputStream());     
			oIn = new ObjectInputStream(comSocket.getInputStream());
			IsItOpen = true;
		} catch (java.net.UnknownHostException e){
			System.err.println("(openCom:) Nie rozpoznajê adresu: " + sServerName);
			IsItOpen = false;
			throw(e);
		} catch (java.io.IOException e){
			System.err.println("(openCom:) Nie mo¿na po³¹czyæ z: " + sServerName);
			IsItOpen = false;
			throw(e);
		}
	}

	/**
	 * Sprawdza czy po³¹czenie zosta³o nawi¹zane.
	 * @return true- nawi¹zano po³¹czenie
	 */
	public boolean isItOpen()
	{
		return IsItOpen;
	}

	/**
	 * Odbiera odpowiedŸ od serwera.
	 * @param tServData referencja do obiektu w którym zostanie zapisana odpowiedŸ
	 * @throws IOException
	 */
	public void getServerData(ComData tServData) throws IOException
	{
		tServData.comData = null;
		try {
			tServData.copy((ComData)oIn.readObject());
		} catch (ClassNotFoundException e){
			System.out.println("B³êdne dane...");
		} 
		
		System.out.println("OdpowiedŸ serwera: " + tServData.message);
		if (tServData.message.equals("EXIT.")){
			tServData.bExit = true;
		}
		return;
	}

	/**
	 * Wysy³a dane do serwera.
	 * @param tServData dane do wys³ania
	 * @throws IOException
	 */
	public void sendDataToServer(ComData tServData) throws IOException
	{
		System.out.println("Wysy³am: " + tServData.message);
		oOut.writeObject(tServData);
		return;
	}

	/**
	 * Zamyka po³¹czenie.
	 * @throws IOException
	 */
	public void closeCom() throws IOException
	{
		oOut.close();
		oIn.close();
		comSocket.close();     
		IsItOpen = false;
	}

	/**
	 * Koñczy wymianê danych z serwerem.
	 * @throws IOException
	 */
	public void finalize() throws IOException{
		ComData data = null;
		try {
			sendDataToServer(new ComData("!EXIT.@"));
			getServerData(data);
			closeCom();
		} catch (IOException e) {
			throw e;
		}
	}
}
