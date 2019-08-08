package ch.lespagesweb.buildagame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.LinkedList;

public class Player extends GameObject
{
	float currentForceX;
	float currentForceY;

    /* if jump vel is -20 and gravity is 10, the object feels light
     * if jump vel is -10 and gravity is 10, the object feels heavy
     */  
	int gravityVel = 10; 
	int maxVel = -50;
	boolean potential = false;
	
	public Player(int x, int y, ID id, boolean center, Game game) 
	{
		super(30, 43, x, y, id, center, game, "./little-astronaut.jpg");
	}
	
	public boolean isGroundedOn(GameObject object)
	{
		int minimumXHoldOnPlatform = 2;
		return this.y + this.height == object.getY()
			&& this.x + this.width - minimumXHoldOnPlatform > object.getX()
			&& this.x + minimumXHoldOnPlatform < object.getX() + object.getWidth();
	}
	
	public boolean isGrounded()
	{
		LinkedList<Platform> platforms = this.game.getHandler().getPlatforms();
		for (int i=0; i < platforms.size(); i++) {
			GameObject tempObject = platforms.get(i);
			this.avoidOverlap(tempObject);
			if (this.isGroundedOn(tempObject)) return true;
		}
		return false;
	}
	
	public void applyForceOnY(float force)
	{
		if (this.isGrounded() && force < 0 ) {
			this.currentForceY += force;
			this.potential = true;
		}
	}
	
	public int computePosition()
	{
		boolean isGrounded = this.isGrounded();
		//no user velocity to add
		if (!this.potential) {
            //clear force if the potential has been freed
			this.currentForceY = 0;
            // even if potential or not, if not grounded apply gravity velocity //fall
            if (!isGrounded) {
                //at each time step, 
                this.velY = (int) Game.clamp(this.velY+1, this.maxVel, this.gravityVel);
            // no potential and grounded clear velocity
            } else {
            	this.velY = 0;
            }
        // user velocity to add
		} else {
			// cannot add user velocity if not grounded
            if (!isGrounded) {
                //at each time step, 
                this.velY = (int) Game.clamp(this.velY+1, this.maxVel, this.gravityVel);
            //if it is grounded, and has potential force to be released, add the potential velocity
            } else {
                float mass = 1;
                this.velY += ( this.currentForceY / mass );
                this.potential = false;
            }
		}

		// now that we figured out the velocity lets position the player
		int yError = 25;
		return (int) Game.clamp(this.y+this.velY, 0, Game.HEIGHT - this.getHeight()-yError);
	}

	public boolean isColliding(GameObject object)
	{
		return 0 < getOverlapOnYFromAbove(object) 
			&& 0 < getOverlapOnYFromBelow(object)
			&& 0 < getOverlapOnXFromLeft(object)
			&& 0 < getOverlapOnXFromRight(object);
	}

	public float getOverlapOnXFromLeft(GameObject object)
	{ 
		 return this.x + this.width - object.getX(); 
	}

	public float getOverlapOnXFromRight(GameObject object)
	{ 
		 return object.getX() + object.getWidth() - this.x;
	}
	
	public float getOverlapOnYFromAbove(GameObject object)
	{ 
		 return this.y + this.height - object.getY(); 
	}

	public float getOverlapOnYFromBelow(GameObject object)
	{ 
		 return object.getY() + object.getHeight() - this.y;
	}

	private void fixOverlapOnX(GameObject object)
	{
        if ((float)object.getX() + 0.5f*(float)object.getWidth() > (float)this.x + 0.5f*(float)this.width) {
            this.x = object.getX() - this.width;
        } else {
            this.x = object.getX() + object.getWidth();
        }
        this.velX = -this.velX/2; // bounce the opposite way
	}
	
	private void fixOverlapOnY(GameObject object)
	{
        if ((float)object.getY() + 0.5f*(float)object.getHeight() > (float)this.y + 0.5f*(float)this.height) {
            this.y = object.getY() - this.height;
            this.velY = 0; //grounded
        } else {
            this.y = object.getY() + object.getHeight();
            this.velY = 0; // touched roof, go the opposite way
        }
	}
	
	/**
	 * Always correct the lowest overlap first 
	 * @param object
	 */
	private void avoidOverlap(GameObject object)
	{
		float ovA, ovB, ovL, ovR;
        ovA = getOverlapOnYFromAbove(object);
        ovB = getOverlapOnYFromBelow(object);
        ovL = getOverlapOnXFromLeft(object);
        ovR = getOverlapOnXFromRight(object);

        if (!this.isColliding(object)) return;

        if ((ovL < ovA && ovL < ovB) || (ovR < ovA && ovR < ovB)) {
            this.fixOverlapOnX(object);
            if (this.isColliding(object)) {
                this.fixOverlapOnY(object);
            }
        } else if ((ovA < ovL && ovA < ovR) || (ovB < ovL && ovB < ovR)) {
            this.fixOverlapOnY(object);
            if (this.isColliding(object)) {
                this.fixOverlapOnX(object);
            }
        }
	}
	
	protected void innerTick() 
	{
		int xError = 0;
		this.x = (int) Game.clamp(this.x+this.velX, 0, Game.WIDTH - this.getWidth()-xError);
		this.y = this.computePosition();
	}
	
	public void render(Graphics g) 
	{
		Graphics2D g2d = (Graphics2D) g;

		g.setColor(Color.BLACK);
		g2d.draw(this.getBounds());
		g.drawImage((Image)this.img, x, y, null);
	}
}
