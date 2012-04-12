package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.JPanel;

/**
 * Plansza symulacji- mapa z samochodami
 *
 */
@SuppressWarnings("serial")
class DrawingArea extends JPanel{

	/**
	 * po³owa d³ugoœci przecznicy
	 */
	public static int squareRadius = 75;

	/**
	 * po³owa szerokoœci samochodu
	 */
	public static int carRadius = 3;

	/**
	 * lista wspó³rzêdnych samochodów
	 */
	public static LinkedList<Integer> list = new LinkedList<Integer>();

	/**
	 * lista stanów sygnalizatorów
	 */
	public static LinkedList<Integer> listLights = new LinkedList<Integer>();

	/**
	 * paleta kolorów
	 */
	Color[] colors=new Color[100];

	/**
	 * Odmalowuje mapê.
	 * @param g komponent
	 */
	protected void paintComponent(Graphics g){
		LinkedList<Integer> listCopy;
		LinkedList<Integer> listLightsCopy;
		synchronized(list){
			listCopy = new LinkedList<Integer>(list);
		}
		synchronized(listLights){
			listLightsCopy = new LinkedList<Integer>(listLights);
		}
		ListIterator<Integer> iterator = listCopy.listIterator();
		if(iterator.hasNext()){
			squareRadius=iterator.next();
			carRadius=iterator.next();
			super.paintComponent(g);
			setBackground(Color.LIGHT_GRAY);
			paintMap(squareRadius, carRadius, g);
			paintCars(squareRadius, carRadius, iterator, g);
			
			ListIterator<Integer> iteratorLights = listLightsCopy.listIterator();
			if(iteratorLights.hasNext()) 
				paintLights(squareRadius, carRadius, iteratorLights, g);
		}
		
	}

	/**
	 * Maluje kontury ulic.
	 * @param squareRadius po³owa d³ugoœci przecznicy
	 * @param carRadius po³owa szerokoœci samochodu
	 * @param g komponent
	 */
	void paintMap(int squareRadius, int carRadius, Graphics g){
		g.setColor(Color.BLACK);
		for(int i=0; i<4; ++i){
			for(int j=0; j<4; ++j)g.drawRect(centerPoint(i, squareRadius, carRadius)-squareRadius, centerPoint(j, squareRadius, carRadius)-squareRadius, 2*squareRadius, 2*squareRadius);
		}
	}

	/**
	 * Rysuje sygnalizatory.
	 * @param squareRadius po³owa d³ugoœci przecznicy
	 * @param carRadius po³owa szerokoœci samochodu
	 * @param iterator iterator na pierwszym sygnalizatorze
	 * @param g komponent
	 */
	void paintLights(int squareRadius, int carRadius, ListIterator<Integer> iterator, Graphics g){
		int x;
		int y;
		int state;
		for(int i=0; i<9; i++){
			x=iterator.next();
			y=iterator.next();
			state=iterator.next();
			if(state==0) g.setColor(Color.GREEN);
			else g.setColor(Color.RED);
			g.drawLine(x-2*carRadius-2, y-2*carRadius-3, x-1, y-2*carRadius-3);
			if(state==1) g.setColor(Color.GREEN);
			else g.setColor(Color.RED);
			g.drawLine(x+2*carRadius+3, y-2*carRadius-2, x+2*carRadius+3, y-1);
			if(state==2) g.setColor(Color.GREEN);
			else g.setColor(Color.RED);
			g.drawLine(x+1, y+2*carRadius+3, x+2*carRadius+2, y+2*carRadius+3);
			if(state==3) g.setColor(Color.GREEN);
			else g.setColor(Color.RED);
			g.drawLine(x-2*carRadius-3, y+1, x-2*carRadius-3, y+2*carRadius+2);
		}
	}

	/**
	 * Rysuje samochody.
	 * @param squareRadius po³owa d³ugoœci przecznicy
	 * @param carRadius po³owa szerokoœci samochodu
	 * @param iterator iterator na pierwszym sygnalizatorze
	 * @param g komponent
	 */
	void paintCars(int squareRadius, int carRadius, ListIterator<Integer> iterator, Graphics g){
		int x;
		int y;
		int index;
		while(iterator.hasNext()){
			x=iterator.next();
			y=iterator.next();
			index=iterator.next();
			g.setColor(colors[index%colors.length]);
			g.fillRect(x-carRadius, y-carRadius, 2*carRadius+1, 2*carRadius+1);
		}

	}

	/**
	 * Oblicza róg skrzy¿owania.
	 * @param index nr
	 * @param squareRadius po³owa d³ugoœci przecznicy
	 * @param carRadius po³owa szerokoœci samochodu
	 * @return wspó³rzêdna
	 */
	int centerPoint(int index, int squareRadius, int carRadius){
		return ((2*index+1)*squareRadius+4*index*carRadius+6*index);
	}

	/**
	 * Losuje kolory.
	 */
	void mixColors(){
		for(int i=0; i<colors.length; i++)colors[i]=new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
	}

	/**
	 * Oblicza wspó³rzêdne sygnalizatorów.
	 */
	DrawingArea(){
		mixColors();
		for(int i=2*squareRadius+2*carRadius+3;i<=6*squareRadius+10*carRadius+15;i+=2*squareRadius+4*carRadius+6){
			for(int j=2*squareRadius+2*carRadius+3;j<=6*squareRadius+10*carRadius+15;j+=2*squareRadius+4*carRadius+6){
				listLights.add(i);
				listLights.add(j);
				listLights.add(0);
			}
		}
	}
}
