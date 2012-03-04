package visual;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.Timer;

public class Toast extends Window implements ActionListener{

	private static final long serialVersionUID = -507728427454148991L;
	private Main owner;
	private Timer poptimer;

	Toast(Window o) throws HeadlessException {
		super(o);
		owner = (Main) o;
		this.setBounds((int) owner.getSettings().getSetPosition().getX(),
				(int) owner.getSettings().getSetPosition().getX(),
				(int) owner.getSettings().getSetSize().getWidth(),
				(int) owner.getSettings().getSetSize().getHeight());
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == 1)
					roast();
				/*
				 * if(e.getButton()==3)
				 * owner.getMenu().show(e.getComponent(),e.getX(),e.getY());
				 */
			}
		});
		poptimer=new Timer((int)owner.getSettings().getSetPopuptime(),this);
		poptimer.setActionCommand("timesUp");
		pop();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		java.awt.Paint old = g2.getPaint();

		// drawBackground gradient
		g2.setPaint(new GradientPaint(0, 2 * this.getHeight(), new Color(
				0xffffff), this.getWidth() / 2, -this.getHeight(), new Color(
				0x000000)));
		g2.fillRect(0, 0, this.getWidth() + 1, this.getHeight() + 1);

		g2.setPaint(old);
		g2.setColor(Color.GRAY);
		g2.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_ROUND));
		g2.drawRect(1, 1, this.getWidth() - 3, this.getHeight() - 3);
	}

	public void pop() {
		// without animation
		repaint();
		this.setVisible(true);
		owner.toFront();
		this.toFront();
		poptimer.start();
	}

	public void roast() {
		this.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("timesUp")){
			this.roast();
			poptimer.stop();
		}
		
	}
}
