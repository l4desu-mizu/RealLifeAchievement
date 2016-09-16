package visual;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import IO.ChronikIO;

public class Chronik extends Frame {

	private static final long serialVersionUID = 7020303547570688232L;

	private ChronikIO io;

	private Dimension minSize;

	Chronik(Image icon){
		super("Real Life Achievments Chronik");
		this.setVisible(false);
		this.setIconImage(icon);
		minSize=new Dimension(300,500);
		this.setSize(minSize);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-this.getWidth()/2,Toolkit.getDefaultToolkit().getScreenSize().height/2-this.getHeight()/2);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				setVisible(false);
			}
		});
	}

}
