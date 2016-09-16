package visual;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TrayMenu extends PopupMenu {

	private static final long serialVersionUID = 8794176609051298786L;

	private Main t;
	TrayMenu(Main target){
		t=target;
		this.add("Show");
		this.add("Hide");
		//this.add("Chronik");
		this.add("Settings");
		this.add("Exit");
		this.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(e.getActionCommand().equals("Exit"))
					System.exit(0);
				else if(e.getActionCommand().equals("Hide"))
					t.getToast().roast();
				else if(e.getActionCommand().equals("Show"))
					t.getToast().pop();
				else if(e.getActionCommand().equals("Chronik"))
					t.goToChronik();
				else if(e.getActionCommand().equals("Settings"))
					t.goToSettings();
			}
		});
	}
}
