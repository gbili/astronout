package ch.lespagesweb.buildagame;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Place a Heads Up Display in the position specified by 
 * the x, y coords. The dimensions of the life bar are 
 * defined by the width and border: width - border*2
 * The separation between bar length and number of credits
 * allows to graduate the speed at which the bar changes size 
 * The bigger the credits to updateCreditsBy ratio, the longer
 * will the bar last.
 * 
 * @author g
 */
public class HeadsUpDisplay 
{
    private int x;
    private int y;
    private int width;
    private int height;
    private int border;
	private int length;
	private int maxCredits;
	private int credits;
	private int greenLevel = 255;
	private int redLevel = 0;

	public HeadsUpDisplay(int x, int y, int width, int height, int border, int maxCredits)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.border = border;
		this.maxCredits = maxCredits;
		this.setCredits(maxCredits);
	}
	
	public void setCredits(int credits)
	{
		this.credits = (int) Game.clamp(credits, 0, this.maxCredits);
		this.setLength(this.creditsToLength(this.credits));
		System.out.println("Length: " + length);
	}
	
	public int creditsToLength(int credits)
	{
		float ratio = (float)this.credits/(float)this.maxCredits;
		int length = (int) ((float)this.getMaxLength()*ratio);
		return length;
	}
	
	public int getCredits()
	{
		return this.credits;
	}
	
	public void updateCreditsBy(int c)
	{
		this.setCredits(this.getCredits() + c);
	}

	public void tick()
	{
	}
	
	public void render(Graphics g)
	{
		//render outer bar
		g.setColor(Color.WHITE);
		g.fillRect(this.x, this.y, this.width, this.height);
		g.setColor(Color.GRAY);
		g.fillRect(this.x+this.border, this.y+this.border, this.length, this.height-2*this.border);
		g.setColor(new Color(this.redLevel, this.greenLevel, 0));
		g.fillRect(this.x+this.border, this.y+this.border, this.length, this.height-2*this.border);
	}
	
	public int getMaxLength()
	{
		return this.width - this.border*2;
	}
	
	public void setLength(int l)
	{
		int max = this.getMaxLength();
		this.length = (int) Game.clamp(l, 0, max);
		this.greenLevel = (this.length/max)*255;

		int bar1 = 40;
		int bar2 = 10;
		float threshold = 0f;
		float aboveBar = 0f;
		float ratio = 0f;

		if (this.length > bar1) {
			threshold = max - bar1;
			aboveBar = this.length - bar1;
			ratio = aboveBar/threshold;
			this.greenLevel = 255;
			this.redLevel = (int) (255 - 255f*ratio);
		} else if (this.length > bar2) {
			threshold = bar1 - bar2;
			aboveBar = this.length - bar2;
			ratio = aboveBar/threshold;
			this.greenLevel = (int) (255f*ratio);
			this.redLevel = 255;
		} else {
			this.greenLevel = 0;
			this.redLevel = 255;
		}
	}

	public void updateLengthBy(int u)
	{
		this.setLength(this.getLength() + u);
	}
	
	public int getLength()
	{
		return this.length;
	}
	
}
