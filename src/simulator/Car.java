package simulator;

import java.util.LinkedList;

/**
 * Klasa reprezentuj¹ca samochód w symulatorze.
 *
 */
class Car {

	/**
	 * aktualna wspó³rzêdna x
	 */
	private int x;

	/**
	 * aktualna wspó³rzêdna y
	 */
	private int y;

	/**
	 * aleja
	 */
	int meshX;

	/**
	 * ulica
	 */
	int meshY;

	/**
	 * id samochodu
	 */
	private int index;

	/**
	 * trasa
	 */
	private LinkedList<Move> path;

	/**
	 * licznik samochodów
	 */
	private static int counter;

	/**
	 * Zwraca x
	 * @return x
	 */
	int getX(){
		return x;
	}

	/**
	 * Zwraca y
	 * @return y
	 */
	int getY(){
		return y;
	}

	/**
	 * Zwraca id
	 * @return id
	 */
	int getIndex(){
		return index;
	}

	/**
	 * Dodaje ruch do trasy.
	 * @param move ruch
	 */
	void addMove(Move move){
		path.add(move);
	}

	/**
	 * Wykonuje kolejne ruchy z wyznaczonej trasy.
	 * @return true- jeœli ruch zosta³ wykonany
	 */
	boolean move(){
		if(path.isEmpty())return false;
		if(!Simulator.isMoveLegal(x+path.getFirst().getX(), y+path.getFirst().getY(), index))return true;
		x+=path.getFirst().getX();
		y+=path.getFirst().getY();
		if(path.getFirst().decrementDistance()<=0)path.remove(0);
		return true;
	}

	/**
	 * Tworzy trasê.
	 * @param move ruch
	 */
	void createPath(int move){
		int lastMove=move;
		while(meshX>=0&&meshX<=2&&meshY>=0&&meshY<=2){
			switch((int)(Math.random()*4)){
				case 0:
					moveUp(lastMove);
					lastMove=0;
					meshY--;
					break;
				case 1:
					moveRight(lastMove);
					lastMove=1;
					meshX++;
					break;
				case 2:
					moveDown(lastMove);
					lastMove=2;
					meshY++;
					break;
				case 3:
					moveLeft(lastMove);
					lastMove=3;
					meshX--;
					break;
			}
		}
	}

	/**
	 * Ruch w górê.
	 * @param i poprzedni kierunek
	 */
	void moveUp(int i){
		switch(i){
			case 2:
				addMove(new Move(0, 1, 2*Simulator.getCarRadius()+2));
			case 1:
				addMove(new Move(1, 0, 2*Simulator.getCarRadius()+2));
			case 0:
				addMove(new Move(0, -1, 2*Simulator.getCarRadius()+2));
			case 3:
				addMove(new Move(0, -1, 2*Simulator.getSquareRadius()+2*Simulator.getCarRadius()+4));
				break;
		}
	}

	/**
	 * Ruch w prawo.
	 * @param i poprzedni kierunek
	 */
	void moveRight(int i){
		switch(i){
			case 3:
				addMove(new Move(-1, 0, 2*Simulator.getCarRadius()+2));
			case 2:
				addMove(new Move(0, 1, 2*Simulator.getCarRadius()+2));
			case 1:
				addMove(new Move(1, 0, 2*Simulator.getCarRadius()+2));
			case 0:
				addMove(new Move(1, 0, 2*Simulator.getSquareRadius()+2*Simulator.getCarRadius()+4));
				break;
		}
	}

	/**
	 * Ruch w dó³.
	 * @param i poprzedni kierunek
	 */
	void moveDown(int i){
		switch(i){
			case 0:
				addMove(new Move(0, -1, 2*Simulator.getCarRadius()+2));
			case 3:
				addMove(new Move(-1, 0, 2*Simulator.getCarRadius()+2));
			case 2:
				addMove(new Move(0, 1, 2*Simulator.getCarRadius()+2));
			case 1:
				addMove(new Move(0, 1, 2*Simulator.getSquareRadius()+2*Simulator.getCarRadius()+4));
				break;
		}
	}

	/**
	 * Ruch w lewo.
	 * @param i poprzedni kierunek
	 */
	void moveLeft(int i){
		switch(i){
			case 1:
				addMove(new Move(1, 0, 2*Simulator.getCarRadius()+2));
			case 0:
				addMove(new Move(0, -1, 2*Simulator.getCarRadius()+2));
			case 3:
				addMove(new Move(-1, 0, 2*Simulator.getCarRadius()+2));
			case 2:
				addMove(new Move(-1, 0, 2*Simulator.getSquareRadius()+2*Simulator.getCarRadius()+4));
				break;
		}
	}

	/**
	 * Tworzy samochód przy wjeŸdzie nr i.
	 * @param i nr wjazdu
	 */
	Car(int i){
		int move=0;
		meshX=0;
		meshY=0;
		path=new LinkedList<Move>();
		index=counter++;
		switch(i){
			case 0:
				x=2*Simulator.getSquareRadius()+Simulator.getCarRadius()+2;
				y=-1;
				addMove(new Move(0, 1, 2*Simulator.getSquareRadius()+Simulator.getCarRadius()+3));
				meshX=0;
				meshY=0;
				move=2;
				break;
			case 1:
				x=4*Simulator.getSquareRadius()+5*Simulator.getCarRadius()+8;
				y=-1;
				addMove(new Move(0, 1, 2*Simulator.getSquareRadius()+Simulator.getCarRadius()+3));
				meshX=1;
				meshY=0;
				move=2;
				break;
			case 2:
				x=6*Simulator.getSquareRadius()+9*Simulator.getCarRadius()+14;
				y=-1;
				addMove(new Move(0, 1, 2*Simulator.getSquareRadius()+Simulator.getCarRadius()+3));
				meshX=2;
				meshY=0;
				move=2;
				break;
			case 3:
				x=8*Simulator.getSquareRadius()+12*Simulator.getCarRadius()+19;
				y=2*Simulator.getSquareRadius()+Simulator.getCarRadius()+2;
				addMove(new Move(-1, 0, 2*Simulator.getSquareRadius()+Simulator.getCarRadius()+3));
				meshX=2;
				meshY=0;
				move=3;
				break;
			case 4:
				x=8*Simulator.getSquareRadius()+12*Simulator.getCarRadius()+19;
				y=4*Simulator.getSquareRadius()+5*Simulator.getCarRadius()+8;
				addMove(new Move(-1, 0, 2*Simulator.getSquareRadius()+Simulator.getCarRadius()+3));
				meshX=2;
				meshY=1;
				move=3;
				break;
			case 5:
				x=8*Simulator.getSquareRadius()+12*Simulator.getCarRadius()+19;
				y=6*Simulator.getSquareRadius()+9*Simulator.getCarRadius()+14;
				addMove(new Move(-1, 0, 2*Simulator.getSquareRadius()+Simulator.getCarRadius()+3));
				meshX=2;
				meshY=2;
				move=3;
				break;
			case 6:
				x=6*Simulator.getSquareRadius()+11*Simulator.getCarRadius()+16;
				y=8*Simulator.getSquareRadius()+12*Simulator.getCarRadius()+19;
				addMove(new Move(0, -1, 2*Simulator.getSquareRadius()+Simulator.getCarRadius()+3));
				meshX=2;
				meshY=2;
				move=0;
				break;
			case 7:
				x=4*Simulator.getSquareRadius()+7*Simulator.getCarRadius()+10;
				y=8*Simulator.getSquareRadius()+12*Simulator.getCarRadius()+19;
				addMove(new Move(0, -1, 2*Simulator.getSquareRadius()+Simulator.getCarRadius()+3));
				meshX=1;
				meshY=2;
				move=0;
				break;
			case 8:
				x=2*Simulator.getSquareRadius()+3*Simulator.getCarRadius()+4;
				y=8*Simulator.getSquareRadius()+12*Simulator.getCarRadius()+19;
				addMove(new Move(0, -1, 2*Simulator.getSquareRadius()+Simulator.getCarRadius()+3));
				meshX=0;
				meshY=2;
				move=0;
				break;
			case 9:
				x=-1;
				y=6*Simulator.getSquareRadius()+11*Simulator.getCarRadius()+16;
				addMove(new Move(1, 0, 2*Simulator.getSquareRadius()+Simulator.getCarRadius()+3));
				meshX=0;
				meshY=2;
				move=1;
				break;
			case 10:
				x=-1;
				y=4*Simulator.getSquareRadius()+7*Simulator.getCarRadius()+10;
				addMove(new Move(1, 0, 2*Simulator.getSquareRadius()+Simulator.getCarRadius()+3));
				meshX=0;
				meshY=1;
				move=1;
				break;
			case 11:
				x=-1;
				y=2*Simulator.getSquareRadius()+3*Simulator.getCarRadius()+4;
				addMove(new Move(1, 0, 2*Simulator.getSquareRadius()+Simulator.getCarRadius()+3));
				meshX=0;
				meshY=0;
				move=1;
				break;
		}
		createPath(move);
	};

	/**
	 * Tworzy samochód w punkcie (x,y)
	 * @param x wspó³rzêdna x
	 * @param y wspó³rzêdna y
	 */
	Car(int x, int y){
		this.x=x;
		this.y=y;
		path=new LinkedList<Move>();
		index=++counter;
	};
}