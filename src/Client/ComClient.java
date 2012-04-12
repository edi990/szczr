package client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import protocol.ComData;

/**
 * Klasa obs�uguj�ca po��czenie z serwerem po stronie klienta.
 *
 */
public class ComClient
{
	/**
	 * uchwyt po��czenia z serwerem
	 */
	private Socket				comSocket;
	
	/**
	 * strumie� wyj�ciowy
	 */
	private ObjectOutputStream	oOut;
	
	/**
	 * strumie� wej�ciowy
	 */
	private ObjectInputStream	oIn;
	
	/**
	 * czy po��czenie jest nawi�zane
	 */
	private boolean				IsItOpen = false;
	
	/**
	 * czy mo�na wys�a� zapytanie
	 */
	private boolean				requesting = true;

	/**
	 * Tworzy po��czenie z serwerem.
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
	 * Wysy�a obiekt z danymi zapytania do serwera i odbiera odpowied�.
	 * @param data obiekt z danymi przeznaczonymi do wys�ania
	 * @return obiekt z odpowiedzi�
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
	 * Otwiera po��czenie z serwerem.
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
			System.err.println("(openCom:) Nie rozpoznaj� adresu: " + sServerName);
			IsItOpen = false;
			throw(e);
		} catch (java.io.IOException e){
			System.err.println("(openCom:) Nie mo�na po��czy� z: " + sServerName);
			IsItOpen = false;
			throw(e);
		}
	}

	/**
	 * Sprawdza czy po��czenie zosta�o nawi�zane.
	 * @return true- nawi�zano po��czenie
	 */
	public boolean isItOpen()
	{
		return IsItOpen;
	}

	/**
	 * Odbiera odpowied� od serwera.
	 * @param tServData referencja do obiektu w kt�rym zostanie zapisana odpowied�
	 * @throws IOException
	 */
	public void getServerData(ComData tServData) throws IOException
	{
		tServData.comData = null;
		try {
			tServData.copy((ComData)oIn.readObject());
		} catch (ClassNotFoundException e){
			System.out.println("B��dne dane...");
		} 
		
		System.out.println("Odpowied� serwera: " + tServData.message);
		if (tServData.message.equals("EXIT.")){
			tServData.bExit = true;
		}
		return;
	}

	/**
	 * Wysy�a dane do serwera.
	 * @param tServData dane do wys�ania
	 * @throws IOException
	 */
	public void sendDataToServer(ComData tServData) throws IOException
	{
		System.out.println("Wysy�am: " + tServData.message);
		oOut.writeObject(tServData);
		return;
	}

	/**
	 * Zamyka po��czenie.
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
	 * Ko�czy wymian� danych z serwerem.
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
