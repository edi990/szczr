package simulator;

/**
 * Reprezentacja ruchu samochodu po prostej (jazda ulic�).
 *
 */
class Move {

	/**
	 * zmiana wsp�rz�dnej x
	 */
	private int x;

	/**
	 * zmiana wsp�rz�dnej y
	 */
	private int y;

	/**
	 * ilo�� ruch�w do wykonania do skr�tu
	 */
	private int distance;

	/**
	 * Pobierz x
	 * @return x
	 */
	int getX(){
		return x;
	}

	/**
	 * Pobierz y
	 * @return y
	 */
	int getY(){
		return y;
	}

	/**
	 * Zmniejsz dystans do pokonania (po ruchu).
	 * @return nowy dystans
	 */
	int decrementDistance(){
		return --distance;
	}

	/**
	 * Zmie� x
	 * @param x x
	 */
	void setX(int x){
		this.x=x;
	}

	/**
	 * Zmie� y
	 * @param y
	 */
	void setY(int y){
		this.y=y;
	}

	/**
	 * Inicjalizacja ruchu.
	 * @param x zmiana wsp�rz�dnej x
	 * @param y zmiana wsp�rz�dnej y
	 * @param distance dystans do pokonania
	 */
	Move(int x, int y, int distance){
		this.x=x;
		this.y=y;
		this.distance=distance;
	}
}
