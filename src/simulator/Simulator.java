package simulator;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Klasa symulacji, tworzy samochody, trasy, zarz�dza ich ruchem.
 *
 */
class Simulator{

	/**
	 * po�owa d�ugo�ci przecznicy (powinna by� du�o wi�ksza ni� carRadius)
	 */
	private static int squareRadius;

	/**
	 * po�owa szeroko�ci samochodu
	 */
	private static int carRadius;

	/**
	 * wsp�rz�dne samochod�w
	 */
	public static LinkedList<Car> cars;

	/**
	 * stany sygnalizacji
	 */
	public static LinkedList<Light> lights;

	/**
	 * Pobierz szroko��
	 * @return szeroko��
	 */
	static int getSquareRadius(){
		return squareRadius;
	}

	/**
	 * Pobierz szroko��
	 * @return szeroko��
	 */
	static int getCarRadius(){
		return carRadius;
	}

	/**
	 * Dodaj nowy samoch�d.
	 * @param someCar samoch�d
	 */
	static void addCar(Car someCar){
		if(!isMoveLegal(someCar.getX(), someCar.getY(), someCar.getIndex()))return;
		cars.add(someCar);
	}

	/**
	 * Utw�rz list� z sygnalizatorami.
	 */
	static void addLights(){
		for(int i=2*squareRadius+2*carRadius+3;i<=6*squareRadius+10*carRadius+15;i+=2*squareRadius+4*carRadius+6){
			for(int j=2*squareRadius+2*carRadius+3;j<=6*squareRadius+10*carRadius+15;j+=2*squareRadius+4*carRadius+6)
				lights.add(new Light(i, j, 0));
		}
	}

	/**
	 * Sprawdza czy mo�e si� ruszy� samochodem
	 * @param x x
	 * @param y x
	 * @param index id samochodu
	 * @return true- mo�na wykona� ruch
	 */
	static boolean isMoveLegal(int x, int y, int index){
		for(Car car : cars)if(index!=car.getIndex()&&Math.abs(x-car.getX())<=2*carRadius+1&&Math.abs(y-car.getY())<=2*carRadius+1)return false;
		for(Light light : lights)if(!light.greenLight(x, y))return false;
		return true;
	}

	/**
	 * Kolejny krok symulacji.
	 */
	static void step(){
		Car car;
		for(Iterator<Car> i = cars.iterator(); i.hasNext();) {
	       car=i.next();
	       if(!car.move()){
	    	   i.remove();
	    	   continue;
	       }
		}
	}

	/**
	 * Inicjalizacja symulacji.
	 * @param squareRadius d�ugo�� ulicy
	 * @param carRadius szeroko�� samochodu
	 */
	static void initialize(int squareRadius, int carRadius){
		Simulator.squareRadius=squareRadius;
		Simulator.carRadius=carRadius;
		Simulator.cars=new LinkedList<Car>();
		Simulator.lights=new LinkedList<Light>();
	}

	/**
	 * Pobierz stany sygnalizator�w.
	 * @return lista stan�w
	 */
	static LinkedList<Integer> getLightStates(){
		LinkedList<Integer> temporaryList = new LinkedList<Integer>();
		for(Light light : lights)
			temporaryList.add(light.getState());
		return temporaryList;
	}

	/**
	 * Pobierz map� (wsp�rz�dne samochod�w)
	 * @return samochody
	 */
	static LinkedList<Integer> writeMap(){
		LinkedList<Integer> temporaryList = new LinkedList<Integer>();
		temporaryList.add(squareRadius);
		temporaryList.add(carRadius);
		for(Car car : cars){
			temporaryList.add(car.getX());
			temporaryList.add(car.getY());
			temporaryList.add(car.getIndex());
		}
		return new LinkedList<Integer>(temporaryList);
	}

	/**
	 * Pobierz dane o obci��eniu ulic.
	 * @return obci��enie
	 */
	static LinkedList<Integer> writeLights(){
		LinkedList<Integer> waitingCars = new LinkedList<Integer>();
		for(int i=0;i<36;++i) waitingCars.add(0);
		int i=0;
		for(Light light : lights){
			for(Car car : cars){
				if(car.getX()==(light.getX()-carRadius-1)&&car.getY()<(light.getY()-2*carRadius)&&car.getY()>(light.getY()-2*carRadius-2*squareRadius)) waitingCars.set(4*i, waitingCars.get(4*i)+1);
				if(car.getY()==(light.getY()-carRadius-1)&&car.getX()>(light.getX()+2*carRadius)&&car.getX()<(light.getX()+2*carRadius+2*squareRadius)) waitingCars.set(4*i+1, waitingCars.get(4*i+1)+1);
				if(car.getX()==(light.getX()+carRadius+1)&&car.getY()>(light.getY()+2*carRadius)&&car.getY()<(light.getY()+2*carRadius+2*squareRadius)) waitingCars.set(4*i+2, waitingCars.get(4*i+2)+1);
				if(car.getY()==(light.getY()+carRadius+1)&&car.getX()<(light.getX()-2*carRadius)&&car.getX()>(light.getX()-2*carRadius-2*squareRadius)) waitingCars.set(4*i+3, waitingCars.get(4*i+3)+1);
			}
			i++;
		}
		return waitingCars;
	}

	/**
	 * Zmiana stanu sygnalizator�w.
	 * @param states nowe stany
	 */
	static void readLights(LinkedList<Integer> states){
		int i=0;
		for(Light light : lights){
			light.setState(states.get(i++));
		}
	}
	
}
