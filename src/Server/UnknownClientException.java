package server;

/**
 * Klasa wyj¹tku nierozpoznanego klienta.
 * Wyst¹pi w przypadku próby po³¹czenia klienta o nieznanym identyfikatorze.
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
	 * Tworzy treœæ wyj¹tku.
	 * @return komunikat o b³êdzie
	 */
	public String toString() {
		return "Niedozwolona nazwa klienta: " + client;
	}
}
