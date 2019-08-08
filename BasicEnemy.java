package ch.lespagesweb.buildagame;

import java.awt.Color;
import java.awt.Graphics;

public class BasicEnemy extends GameObject
{

	public BasicEnemy(int x, int y, ID id, boolean center, Game game) 
	{
		super(16, 16, x, y, id, center, game, "./basic-ennemy.jpg");

		this.velX = 5;
		this.velY = 5;
	}

	protected void innerTick() 
	{
		if ((this.x + this.velX <= 0) 
				|| (this.x + this.velX >= (Game.WIDTH - this.getWidth()))) 
			this.velX *= -1;

		int yError = 18;
		if ((this.y + this.velY <= 0) 
				|| (this.y + this.velY >= (Game.HEIGHT - this.getHeight() - yError))) 
			this.velY *= -1;
		
		this.x += this.velX;
		this.y += this.velY;

		this.collision();
	}
	
	public void collision()
	{
        if (this.getBounds().intersects(this.game.getHandler().getPlayer().getBounds())) {
            this.game.getHeadsUpDisplay().updateCreditsBy(-2);
        }
	}

	public void render(Graphics g) 
	{
		g.setColor(Color.RED);
		g.fillRect(x, y, this.getWidth(), this.getHeight());
	}
}
