package hw.ada;

import os.ada.*;

public class StrettoRzv implements Runnable {
	private String name;
	private ADAThread tc;
    // il thread server
    
  /**
    * @param name  rif torre di controllo
    */
  public StrettoRzv(String name) {
    tc = new ADAThread(name);
    this.name=name;
  }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	
	
}
