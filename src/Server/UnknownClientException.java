package server;

/**
 * Klasa wyj�tku nierozpoznanego klienta.
 * Wyst�pi w przypadku pr�by po��czenia klienta o nieznanym identyfikatorze.
 *
 */
@SuppressWarnings("serial")
class UnknownClientException extends Exception {
	
	/**
	 * identyfikator klienta
	 */
	private String client;

	/**
	 * Konstruktor
	 * @param client id klienta
	 */
	public UnknownClientException(String client){
		this.client = client;
	}

	/**
	 * Tworzy tre�� wyj�tku.
	 * @return komunikat o b��dzie
	 */
	public String toString() {
		return "Niedozwolona nazwa klienta: " + client;
	}
}
