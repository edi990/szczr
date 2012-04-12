package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

/**
 * Okno symulacji (zawiera mapê i menu)
 *
 */
@SuppressWarnings("serial")
class Window extends JFrame {

	/**
	 * plansza- mapa z symulacj¹
	 */
	DrawingArea da;

	/**
	 * menu- sterowanie symulacj¹
	 */
	Menu menu;

	/**
	 * Tworzy okno.
	 * @param height wysokoœæ okna
	 * @param width szerokoœæ okna
	 */
	Window (int height, int width){
		super("GUI");
		setLayout(new BorderLayout());
		da=new DrawingArea();
		add(da);

		menu = new Menu();
		add(menu, BorderLayout.EAST);
		
		setSize(height, width);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
}
