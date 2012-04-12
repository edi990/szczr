package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Menu użytkownika- sterowanie symulacją.
 *
 */
@SuppressWarnings("serial")
class Menu extends JPanel{

	/**
	 * przycisk startu / stopu
	 */
	JButton startStop;

	/**
	 * przycisk symulacji krokowej
	 */
	JButton step;

	/**
	 * pole tekstowe z czasem pomiędzy kolejnymi pojawieniami się samochodu
	 */
	JTextField carSpawn;

	/**
	 * zegar używany do zadań
	 */
	Timer timer = new Timer();

	/**
	 * Tworzy wszystkie komponenty (buttony, pola tekstowe).
	 */
	Menu(){
		setSize(200,300);
		setLayout(new GridLayout(20,2));

		add(new JLabel("Odstęp pomiędzy nowymi [ms]:  "));
		
		carSpawn = new JTextField(new Integer(Gui.carSpawn*Gui.sleepTime).toString(), 10);
		add(carSpawn);

		startStop = new JButton("Start");
		startStop.setVerticalTextPosition(AbstractButton.CENTER);
		startStop.setHorizontalTextPosition(AbstractButton.LEADING);
		startStop.setMnemonic(KeyEvent.VK_S);
		startStop.addActionListener(new ButtonClick());
		add(startStop);
		
		step = new JButton("Krok");
		step.setVerticalTextPosition(AbstractButton.CENTER);
		step.setHorizontalTextPosition(AbstractButton.LEADING);
		step.setMnemonic(KeyEvent.VK_K);
		step.addActionListener(new ButtonClick());
		add(step);
		
		add(new JLabel("Dodaj samochod:  "));
		
		JButton[] btns = new JButton[12];
		for(int i=0;i<12;++i){
			btns[i] = new JButton();
			if(i<3){
				btns[i].setText("↓");
			} else if(i<6){
				btns[i].setText("←");
			} else if(i<9){
				btns[i].setText("↑");
			} else if(i<12){
				btns[i].setText("→");
			}
			btns[i].setActionCommand(new Integer(i).toString());
			btns[i].addActionListener(new AddCarClick());
		}
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout());
		topPanel.add(btns[0]);
		topPanel.add(btns[1]);
		topPanel.add(btns[2]);
		add(topPanel);
		
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new GridLayout());
		middlePanel.add(btns[11]);
		middlePanel.add(new JLabel(" "));
		middlePanel.add(btns[3]);
		add(middlePanel);
		
		JPanel middle2Panel = new JPanel();
		middle2Panel.setLayout(new GridLayout());
		middle2Panel.add(btns[10]);
		middle2Panel.add(new JLabel(" "));
		middle2Panel.add(btns[4]);
		add(middle2Panel);
		
		JPanel middle3Panel = new JPanel();
		middle3Panel.setLayout(new GridLayout());
		middle3Panel.add(btns[9]);
		middle3Panel.add(new JLabel(" "));
		middle3Panel.add(btns[5]);
		add(middle3Panel);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout());
		bottomPanel.add(btns[8]);
		bottomPanel.add(btns[7]);
		bottomPanel.add(btns[6]);
		add(bottomPanel);
		
		
	}

	/**
	 * Listener dla przycisków sterujących
	 *
	 */
	public class ButtonClick implements ActionListener{
		public void actionPerformed(ActionEvent e){
			String text = (String)e.getActionCommand();
			
			if(text.equals("Start")){
				startStop.setText("Stop");
				carSpawn.setEnabled(false);
				try{
					int ile = (int)(Integer.parseInt(carSpawn.getText())/Gui.sleepTime);
					if(ile>0) Gui.carSpawn = ile;
				}catch(NumberFormatException ex){
					carSpawn.setText(new Integer(Gui.carSpawn*Gui.sleepTime).toString());
				}
				
				Gui.running = true;
			} else if(text.equals("Stop")) {
				startStop.setText("Start");
				carSpawn.setEnabled(true);
				Gui.running = false;
			} else if(text.equals("Krok")) {
				Gui.running = true;
				timer.schedule(new StopSimulationTask(), (long) (1500));
				step.setEnabled(false);
			}
		}
	}

	/**
	 * Listener dla przycisków dodających nowe samochody.
	 *
	 */
	public class AddCarClick implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Gui.addNewCar(Integer.parseInt(e.getActionCommand()));
		}
	}

	/**
	 * Zadanie zatrzymujące symulację (używane w trybie krokowym).
	 *
	 */
	class StopSimulationTask extends TimerTask{
		public void run(){
			Gui.running = false;
			step.setEnabled(true);
			carSpawn.setEnabled(true);
			startStop.setText("Start");
		}
	}
}
