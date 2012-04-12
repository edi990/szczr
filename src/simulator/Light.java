package simulator;

/**
 * Klasa reprezentuj�ca sygnalizator w symulatorze.
 *
 */
class Light {

	/**
	 * wsp�rz�dna x
	 */
	private int x;

	/**
	 * wsp�rz�dna y
	 */
	private int y;

	/**
	 * stan sygnalizatora (z kt�rej strony zielone)
	 */
	private int state;

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
	 * Pobierz stan
	 * @return stan sygnalizatora (-1,0,1,2,3)
	 */
	int getState(){
		return state;
	}

	/**
	 * Zmie� stan sygnalizatora.
	 * @param state stan
	 */
	void setState(int state){
		this.state=state;
	}

	/**
	 * Sprawdza czy zielone.
	 * @param x wsp�rz�dna x samochodu
	 * @param y wsp�rz�dna y samochodu
	 * @return true- mo�na jecha�
	 */
	boolean greenLight(int x, int y){
		int c=Simulator.getCarRadius();
		if(this.x-c-1==x&&this.y-3*c-3==y&&state!=0)return false;
		if(this.x+3*c+3==x&&this.y-c-1==y&&state!=1)return false;
		if(this.x+c+1==x&&this.y+3*c+3==y&&state!=2)return false;
		if(this.x-3*c-3==x&&this.y+c+1==y&&state!=3)return false;
		return true;
	}

	/**
	 * Awaryjna zmiana stanu sygnalizatora.
	 */
	void switchState(){
		state=(++state)%4;
	}

	/**
	 * Inicjalizacja sygnalizatora.
	 * @param x wsp�rz�dna x
	 * @param y wsp�rz�dna y
	 * @param state stan
	 */
	Light(int x, int y, int state){
		this.x=x;
		this.y=y;
		this.state=state;
	}
}
