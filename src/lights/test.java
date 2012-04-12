package lights;

import java.util.LinkedList;
/**
 * Klasa do testowania modulu
 * @author wpw
 *
 */
public abstract class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SystemLights sysl = new SystemLights();
		LinkedList<Integer> data = new LinkedList<Integer>();
		LinkedList<Integer> res;
		
		while(true) {
			data.add(0);
			data.add(1);
			data.add(1);
			data.add(1);
			
			data.add(2);
			data.add(3);
			data.add(2);
			data.add(1);
			
			data.add(2);
			data.add(3);
			data.add(2);
			data.add(1);
			
			data.add(0);
			data.add(0);
			data.add(0);
			data.add(0);
			
			data.add(20);
			data.add(3);
			data.add(2);
			data.add(1);
			
			data.add(2);
			data.add(3);
			data.add(2);
			data.add(1);
			
			data.add(2);
			data.add(3);
			data.add(2);
			data.add(1);
			
			data.add(2);
			data.add(3);
			data.add(2);
			data.add(1);
			
			data.add(2);
			data.add(3);
			data.add(2);
			data.add(1);
			
			sysl.setData(data);
			sysl.update();
			res = sysl.getData();
			
			System.out.println(res.pollFirst().toString()+", "+ res.pollFirst().toString()+", "+
					res.pollFirst().toString()+", "+ res.pollFirst().toString()+", "+
							res.pollFirst().toString()+", "+ res.pollFirst().toString()+", "+
									res.pollFirst().toString()+", "+ res.pollFirst().toString()+", "+
											res.pollFirst().toString());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
