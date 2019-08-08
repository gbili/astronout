package ch.lespagesweb.buildagame;

public class FocusAdjuster 
{
	int moveUp = -1;
	int moveDown = 1;
    int screenVelY = 0;
    int screenVelX = 0;

	float firstQuartileX;
	float fourthQuartileX;
	float firstQuartileY;
	float fourthQuartileY;
    
    Handler handler;

	public FocusAdjuster(Handler handler) 
	{
		this.handler = handler;
		this.firstQuartileX = Game.WIDTH/4;
		Player player = handler.getPlayer();
		int w = player.getWidth();
		this.fourthQuartileX = this.firstQuartileX*3-w;
		this.firstQuartileY = Game.HEIGHT/4;
		this.fourthQuartileY = this.firstQuartileY*3-this.handler.getPlayer().getHeight();
	}

	/**
	 * Moving the screen to follow the player position.
	 * We could do it in many ways, but two are:
	 * 1. Move the screen continuously such that the player
	 * is always in the center
	 * 2. Move the screen only when the player has gotten out
	 * of some boundary, and re-center him.
	 * We will be using the n°2. 
	 * 
	 * As long as the player is not in firsQuartile || this.fourthQuartile,
	 * we do not move the screen.
	 * Once the player crosses the boundaries, we start the screen 
	 * update process.
	 * We will move the screen up or down if he is in the this.firstQuartile 
	 * or this.fourthQuartile respectively. We will move it as long as we 
	 * have not managed to put him in some middle region around the 
	 * median.
	 * Once we manage to put him there, we stop the screen moving
	 * process.
	 * 	-> This means that if the player keeps moving we will have
	 * to follow him all the way, resulting in option 1.
	 * 
	 * 
	 * -------------------
	 *  this.firstQuartile      <--
	 * -------------------    | When player enters these areas
	 *                        | refocus screen, such that the
	 * -------------------    | player is in the second and third
	 *                        | quantile.
	 * -------------------    | When the process of moving starts,
	 *  this.fourthQuartile     <--  this.movingScreenUp = true
	 * -------------------
	 * 
	 * @param g
	 */
	public void addScreenVelIfNeeded() 
	{
		int moveDirectionX = this.isPlayerXAboveBelowBetween(this.firstQuartileX, this.fourthQuartileX);
		int moveDirectionY = this.isPlayerYAboveBelowBetween(this.firstQuartileY, this.fourthQuartileY);

		boolean isOutOfBoundsX = 0 != moveDirectionX;
		boolean isOutOfBoundsY = 0 != moveDirectionY;
		
		boolean needToAddVelX = (!this.isAlreadyMovingScreenX() && isOutOfBoundsX);
		boolean needToAddVelY = (!this.isAlreadyMovingScreenY() && isOutOfBoundsY);

		// if last render we weren't already moving the screen
		if (needToAddVelX) {
            this.screenVelX = moveDirectionX * 5; 
		}
		if (needToAddVelY) {
            this.screenVelY = moveDirectionY * 5; 
		}
	}
	
	public void removeScreenVelIfNeeded() 
	{
		boolean needToRemoveVelX = this.isAlreadyMovingScreenX() && 0 == this.isPlayerXAboveBelowBetween(this.firstQuartileX*2-this.handler.getPlayer().getWidth(), this.firstQuartileX*2+this.handler.getPlayer().getWidth());
		boolean needToRemoveVelY = this.isAlreadyMovingScreenY() && 0 == this.isPlayerYAboveBelowBetween(this.firstQuartileY*2-this.handler.getPlayer().getHeight(), this.firstQuartileY*2+this.handler.getPlayer().getHeight());

        if (needToRemoveVelX) {
            this.screenVelX = 0;
        }
        if (needToRemoveVelY) {
            this.screenVelY = 0;
        }
	}
	
	protected boolean isAlreadyMovingScreenY()
	{
		return this.screenVelY != 0;
	}

	protected boolean isAlreadyMovingScreenX()
	{
		return this.screenVelX != 0;
	}
	
	protected int isPlayerYAboveBelowBetween(float uB, float lB)
	{
		if (this.handler.getPlayer().getY() < uB) {
			return 1;
		} else if (this.handler.getPlayer().getY() > lB) {
			return -1;
		} else {
			return 0;
		}
	}

	protected int isPlayerXAboveBelowBetween(float uB, float lB)
	{
		if (this.handler.getPlayer().getX() < uB) {
			return 1;
		} else if (this.handler.getPlayer().getX() > lB) {
			return -1;
		} else {
			return 0;
		}
	}
	
	public int getScreenVelX()
	{
		return this.screenVelX;
	}

	public int getScreenVelY()
	{
		return this.screenVelY;
	}
}
