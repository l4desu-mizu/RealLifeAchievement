package visual;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.font.TextLayout;

import javax.swing.Timer;

public class Toast extends Window implements ActionListener{

	private static final long serialVersionUID = -507728427454148991L;
	private Main owner;
	private Timer poptimer;
	private boolean repositioning;
	
	private Point clickedPoint;
	
	Toast(Window o) throws HeadlessException {
		super(o);
		owner = (Main) o;
		this.setMinimumSize(new java.awt.Dimension(290,90));//setting default min size, to make sure that there is a minimum good looking state
		this.setBounds((int) owner.getSettings().getSetPosition().getX(),
				(int) owner.getSettings().getSetPosition().getY(),
				(int) owner.getSettings().getSetSize().getWidth(),
				(int) owner.getSettings().getSetSize().getHeight());
		System.out.println(owner.getSettings().getSetPosition());
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == 1){
					if(!repositioning)
						roast();
					clickedPoint=e.getPoint();
				}
			}
			/*public void mouseReleased(MouseEvent e){
				repositioning=false;
				pop();
			}*/
		});
		this.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent e){
				Point mousePosition=e.getLocationOnScreen();
				mousePosition.translate(-clickedPoint.x,-clickedPoint.y);
				if(repositioning)
					setLocation(mousePosition);
			}
		});
		poptimer=new Timer((int)owner.getSettings().getSetPopuptime(),this);
		poptimer.setActionCommand("timesUp");
		repositioning=false;
		repaint();
		pop();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2=(Graphics2D)g;
		java.awt.Paint tmpPaint = g2.getPaint();
		java.awt.Stroke tmpStroke = g2.getStroke();

		// drawBackground gradient
		g2.setPaint(new GradientPaint(0, 2 * this.getHeight(), new Color(
				0xffffff), this.getWidth() / 2, -this.getHeight(), new Color(
				0x000000)));
		g2.fillRect(0, 0, this.getWidth() + 1, this.getHeight() + 1);

		// draw Border
		g2.setPaint(tmpPaint);
		g2.setColor(Color.GRAY);
		g2.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_ROUND));
		g2.drawRect(1, 1, this.getWidth() - 3, this.getHeight() - 3);
		
		// draw Text
		g2.setStroke(tmpStroke);
		g2.setColor(Color.white);
		//setting some rendering hints for prettier text
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		
		//pretty much why I hate gui foo
		//setting fonts
		Font fname=new Font("Futura",Font.BOLD,16),
				fverb=fname.deriveFont(Font.PLAIN, 12f),
				fachiev=fname.deriveFont(25f),
				fdescr=fname.deriveFont(Font.PLAIN, 13f);
		String name=owner.getGetter().getName(),
				verb="achieved:",
				achievment=owner.getGetter().getAchievment(),
				description=owner.getGetter().getDescription();
		
		//setting context, textlayout, so it's more or less only to be drawn with specific bounds
		TextLayout nameLayout=new TextLayout(name,fname,g2.getFontRenderContext()),
			verbLayout=new TextLayout(verb,fverb,g2.getFontRenderContext()),
			achievLayout=new TextLayout(achievment,fachiev,g2.getFontRenderContext()),
			descrLayout=new TextLayout(description,fdescr,g2.getFontRenderContext());
		
		nameLayout.draw(g2, 10, 20);
		verbLayout.draw(g2, (int)(15+nameLayout.getBounds().getMaxX()), 20);
		achievLayout.draw(g2, 15, 20+23);
		
		//magic linefeed foo -.- "Java: warum einfach, wenns auch kompliziert geht?"
		if(descrLayout.getBounds().intersectsLine(this.getX()+this.getWidth(),this.getY(), //loift die description rechts ueber
				this.getX()+this.getWidth(),this.getY()+this.getHeight())){
			int breakingIndex=description.substring(0, description.length()/2).lastIndexOf(' ');//damits nicht mitten im wort ist
			String[] descriptionParts={description.substring(0, breakingIndex),
					description.substring(breakingIndex+1)};
			descrLayout=new TextLayout(descriptionParts[0],fdescr,g2.getFontRenderContext());
			descrLayout.draw(g2, 10, 20+15+25);
			descrLayout=new TextLayout(descriptionParts[1],fdescr,g2.getFontRenderContext());
			descrLayout.draw(g2, 10, 20+15+15+25);
		}
		else
			descrLayout.draw(g2, 10, 20+15+25);
	}

	public void pop() {
		// without animation
		poptimer.restart();
		poptimer.start();
		this.setVisible(true);
		repaint();
		owner.toFront();
		this.toFront();
	}

	public void roast() {
		if(!repositioning)
			this.setVisible(false);
		poptimer.stop();
	}
	
	public void setRepositioning(boolean r){
		this.repositioning=r;
	}
	public boolean getRepositioning(){
		return this.repositioning;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("timesUp")){
			this.roast();
		}
		
	}

	public void reloadSettings() {
		poptimer.stop();
		poptimer.setDelay((int)owner.getSettings().getSetPopuptime());
		this.setSize(owner.getSettings().getSetSize());//unneccessary
		this.setLocation(owner.getSettings().getSetPosition());//unneccessary
	}
}
