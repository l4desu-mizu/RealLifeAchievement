package visual;

import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import IO.ChronikIO;

public class Chronik extends Frame {
	
	private ChronikIO io;
	
	Chronik(Image icon){
		super("Real Life Achievments Chronik");
		this.setVisible(false);
		this.setIconImage(icon);
		this.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width/2-this.getWidth()/2,Toolkit.getDefaultToolkit().getScreenSize().height/2-this.getHeight()/2,300,500);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				setVisible(false);
			}
		});
	}

}
