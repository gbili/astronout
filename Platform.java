package ch.lespagesweb.buildagame;

import java.awt.Color;
import java.awt.Graphics;

public class Platform extends GameObject 
{
	public Platform(int width, int height, int x, int y, ID id, boolean center, Game game) 
	{
		super(width, height, x, y, id, center, game, "./cloud.jpg");
	}

	protected void innerTick() 
	{
		this.x += this.velX;
		this.y += this.velY;
	}

	public void render(Graphics g) 
	{
		g.setColor(Color.white);
		g.fillRect(this.x, this.y, this.getWidth(), this.getHeight());
	}
}