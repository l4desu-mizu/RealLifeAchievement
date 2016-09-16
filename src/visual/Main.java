package visual;
import java.awt.Frame;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import network.Getter;

public class Main extends Frame implements ActionListener{

	private static final long serialVersionUID = -8838085106538177053L;

	private SystemTray sysTray;
	private TrayMenu menu;
	private TrayIcon trayContext;
	private Image trayImage;
	private String iconName;
	private Timer gettingTimer;

	private Settings settings;
	private Getter get;
	private Toast toast;
	private Chronik chronik;

	Main(){
		super();
		//don't show this frame at all
		this.setBounds(-10, -10, 1, 1);
		this.setVisible(false);
		iconName="RLA.png";

		//load image
		//trayImage=Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("files/"+iconName));
		trayImage=new ImageIcon(this.getClass().getClassLoader().getResource("files/"+iconName)).getImage();

		settings=new Settings(this,trayImage);
		get=new Getter(settings.getSetURL());
		toast=new Toast(this);
		chronik=new Chronik(trayImage);
		menu=new TrayMenu(this);
		gettingTimer=new Timer((int)settings.getSetInterval(),this);
		gettingTimer.setActionCommand("getContent");
		gettingTimer.start();

		//init systemtray,needs menu
		if(SystemTray.isSupported())
		{
			trayContext=new TrayIcon(trayImage,"Real Life Achievments",menu);
			trayContext.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e){
					if(e.getButton()==1){
						if(toast.isVisible())
							toast.roast();
						else
							toast.pop();
					}
				}
			});
			try{
				sysTray=SystemTray.getSystemTray();
				sysTray.add(trayContext);
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

	public void reloadSettings() {
		gettingTimer.stop();
		gettingTimer.setDelay((int)this.settings.getSetInterval());
		gettingTimer.start();

		toast.reloadSettings();

		try {
			this.get.setURL(new java.net.URL(settings.getSetURL()));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
