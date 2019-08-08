package ch.lespagesweb.buildagame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter 
{
	
	private Game game;
	
	public KeyInput(Game game) 
	{
		this.game = game;
	}

	public void keyPressed(KeyEvent e) 
	{
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
		
		Player player = this.game.getHandler().getPlayer();
        if (key == KeyEvent.VK_K) player.applyForceOnY(-15);
        if (key == KeyEvent.VK_J) player.applyForceOnY(5);
        if (key == KeyEvent.VK_H) player.setVelX(-5);
        if (key == KeyEvent.VK_L) player.setVelX(5);
	}

	public void keyReleased(KeyEvent e) 
	{
		int key = e.getKeyCode();

        GameObject player = this.game.getHandler().getPlayer();
        if (key == KeyEvent.VK_K) player.setVelY(0);
        if (key == KeyEvent.VK_J) player.setVelY(0);
        if (key == KeyEvent.VK_H) player.setVelX(0);
        if (key == KeyEvent.VK_L) player.setVelX(0);
	}
}