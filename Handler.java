package ch.lespagesweb.buildagame;

import java.awt.Graphics;
import java.util.LinkedList;

public class Handler 
{
	//contains the index of the Player object in the list of game objects
	Player player;
	
	FocusAdjuster focusAdjuster;
	
	LinkedList<GameObject> objects = new LinkedList<GameObject>();

	LinkedList<Platform> platforms = new LinkedList<Platform>();
	
	public Handler()
	{
		
	}
	
	public void setFocusAdjuster(FocusAdjuster focus) 
	{
		this.focusAdjuster = focus;
	}
	
	public FocusAdjuster getFocusAdjuster()
	{
		if (null == this.focusAdjuster) {
			this.focusAdjuster = new FocusAdjuster(this);
		}
		return this.focusAdjuster;
	}

	public void tick() 
	{
		for (int i=0; i < objects.size(); i++) {
			GameObject tempObject = objects.get(i);
			tempObject.tick();
		}
	}

	public void render(Graphics g) 
	{
		this.getFocusAdjuster().addScreenVelIfNeeded();
		for (int i=0; i < objects.size(); i++) {
			GameObject tempObject = objects.get(i);
			tempObject.render(g);
		}
		this.getFocusAdjuster().removeScreenVelIfNeeded();
	}

	public void addObject(GameObject object) 
	{
		if (object instanceof Player) {
			this.player = (Player) object;
		} else if (object instanceof Platform) {
            this.platforms.add((Platform) object);
		}
		object.setHandler(this);
		this.objects.add(object);
	}
	
	public LinkedList<Platform> getPlatforms()
	{
		return this.platforms;
	}
	
	public Player getPlayer()
	{
		return this.player;
	}
	
	public void removeObject(GameObject object) 
	{
		if (object instanceof Player) {
			this.player = null;
		} else if (object instanceof Platform) {
            this.platforms.remove((Platform) object);
		}
		this.objects.remove(object);
	}
}
