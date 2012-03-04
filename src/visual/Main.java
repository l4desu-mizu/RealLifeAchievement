package visual;
import java.awt.Frame;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import network.Getter;

public class Main extends Frame implements ActionListener{

	private static final long serialVersionUID = -8838085106538177053L;
	
	private SystemTray tray;
	private TrayMenu menu;
	private Image trayImage;
	
	private Timer gettingTimer;
	
	private Settings settings;
	private Toast toast;
	private Chronik chronik;
	private Getter get;
	
	Main(){
		super();
		//don't show this frame at all
		this.setBounds(-10, -10, 1, 1);
		this.setVisible(false);
		
		//load image
		try {
			trayImage=ImageIO.read(new File(this.getClass().getResource("RLA.png").getPath()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		settings=new Settings(trayImage);
		toast=new Toast(this);
		chronik=new Chronik(trayImage);
		get=new Getter();
		menu=new TrayMenu(this);
		gettingTimer=new Timer((int)settings.getSetIntervall(),this);
		gettingTimer.setActionCommand("getContent");
		
		//init systemtray,needs menu
		if(SystemTray.isSupported())
		{
			try{
				tray=SystemTray.getSystemTray();
				tray.add(new TrayIcon(trayImage,"Real Life Achievments",menu));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		else{
			System.exit(0);//weil wegen derzeit keine andere möglichkeit es ohne tray vernünftig zu verwenden
		}
	}
	
	public Getter getGetter(){
		return get;
	}
	
	public Settings getSettings(){
		return settings;
	}
	
	public void goToSettings(){
		settings.setVisible(true);
		settings.toFront();
	}
	
	public void goToChronik(){
		chronik.setVisible(true);
		chronik.toFront();
	}
		
	protected Toast getToast(){
		return toast;
	}
	
	public static void main(String[] args){
		new Main();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("getContent"))
			if(get.getData())
				toast.pop();
		
	}
}
