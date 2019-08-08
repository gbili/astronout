package ch.lespagesweb.buildagame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

/**
 * Everything in the game is going to be a GameObject: enemies, clouds etc
 * 
 * @author g
 *
 */
public abstract class GameObject 
{
	protected int width, height;
	protected int x, y;
	protected ID id;
	protected int velX, velY;
	protected Image img;
	
	protected Handler handler;
	
	protected int screenVelX, screenVelY;
	
	protected Game game;
	
	public GameObject(int width, int height, int x, int y, ID id, boolean center, Game game, String pathToImage) 
	{
		this.width = width;
		this.height = height;
		this.x = (center)?x-this.height/2:x;
		this.y = (center)?y-this.width/2:y;
		this.id = id;
		this.game = game;
		this.img = new ImageIcon(pathToImage).getImage();
	}
	
	public void setHandler(Handler handler)
	{
		this.handler = handler;
	}

	public Handler getHandler()
	{
		return this.handler;
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle(x,y,this.getWidth(), this.getHeight());
	}
	
	protected abstract void innerTick();
	public abstract void render(Graphics g);
	
	public void tick()
	{
		this.innerTick();

		this.x += this.getHandler().getFocusAdjuster().getScreenVelX();
		this.y += this.getHandler().getFocusAdjuster().getScreenVelY();
	}
	
	public void setX(int x) 
	{
		this.x = x;
	}

	public int getX() 
	{
		return this.x;
	}

	public void setY(int y) 
	{
		this.y = y;
	}
	
	public int getY() 
	{
		return this.y;
	}

	public void setWidth(int width) 
	{
		this.width = width;
	}
	
	public int getWidth() 
	{
		return this.width;
	}

	public void setHeight(int height) 
	{
		this.height = height;
	}
	
	public int getHeight() 
	{
		return this.height;
	}
	
	public void setId(ID id) 
	{
		this.id = id;
	}
	
	public ID getId() 
	{
		return this.id;
	}

	public void addVelX(int velX) 
	{
		this.velX += velX;
	}

	public void addVelY(int velY) 
	{
		this.velY += velY;
	}
	
	public void setVelX(int velX) 
	{
        this.velX = velX;
	}

	public void setVelY(int velY) 
	{
		this.velY = velY;
	}

	public int getVelX() 
	{
		return this.velX;
	}

	public int getVelY() 
	{
		return this.velY;
	}
}