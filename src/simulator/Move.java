package simulator;

/**
 * Reprezentacja ruchu samochodu po prostej (jazda ulic¹).
 *
 */
class Move {

	/**
	 * zmiana wspó³rzêdnej x
	 */
	private int x;

	/**
	 * zmiana wspó³rzêdnej y
	 */
	private int y;

	/**
	 * iloœæ ruchów do wykonania do skrêtu
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
	 * Zmieñ x
	 * @param x x
	 */
	void setX(int x){
		this.x=x;
	}

	/**
	 * Zmieñ y
	 * @param y
	 */
	void setY(int y){
		this.y=y;
	}

	/**
	 * Inicjalizacja ruchu.
	 * @param x zmiana wspó³rzêdnej x
	 * @param y zmiana wspó³rzêdnej y
	 * @param distance dystans do pokonania
	 */
	Move(int x, int y, int distance){
		this.x=x;
		this.y=y;
		this.distance=distance;
	}
}
