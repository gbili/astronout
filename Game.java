package ch.lespagesweb.buildagame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.util.Random;

public class Game extends Canvas implements Runnable 
{
	private static final long serialVersionUID = 8015133412740305332L;
	
	public static final int WIDTH = 400, HEIGHT = 600;

	private Thread thread; // The entire game is going to run in this game
	private boolean running = false;

	private Random r;
	private Handler handler;
	
	private int seconds = 0;
	
	private HeadsUpDisplay hud;
	
	public Game() 
	{
		hud = new HeadsUpDisplay(10, 10, 104, 20, 2, 100);
	    this.addKeyListener(new KeyInput(this));
		//handler has to go before window since window will call game.start()
	    //which in turn calls thread.start() which will call game.run() 
	    //which will call game.tick(); and tick uses handler, therefore it must
	    //be initialized
	    new Window(WIDTH, HEIGHT, "Let's build a Game", this);

		this.getHandler().addObject(new Player(WIDTH/2, HEIGHT/2, ID.Player, true, this));
		for (int i=0; i < 5; i ++)
            this.getHandler().addObject(new BasicEnemy(this.getRandom().nextInt(WIDTH), this.getRandom().nextInt(HEIGHT), ID.BasicEnemy, true, this));

		new PlatformGenerator(10, 340, 30, 30, Game.WIDTH, Game.HEIGHT, 120, this);
	}
	
	public Random getRandom()
	{
		if (null == this.r) {
			this.r= new Random();
		}
		return this.r;
	}
	
	public Handler getHandler()
	{
		if (null == this.handler) {
			this.handler= new Handler();
		}
		return this.handler;
	}
	
	public HeadsUpDisplay getHeadsUpDisplay()
	{
		return this.hud;
	}
	
	public static float clamp(float num, float min, float max)
	{
		return (num < min)
				? min : (num > max)
						? max : num;
	}

	public synchronized void start() 
	{
		this.thread = new Thread(this);
		this.thread.start();
		this.running = true;
	}

	public void gameOver(Graphics g)
	{
        Graphics2D g2b = (Graphics2D) g;
        g2b.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2b.setFont(new Font("Sans-Serif", Font.PLAIN, 48));
        g2b.setColor(Color.GREEN);
        g2b.drawString("Game Over!!", 50, 400); 
        g2b.drawString("Score:" + this.seconds, 70, 450); 
	}

	public synchronized void stop() 
	{
		try {
			this.thread.join();
			this.running = false;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() 
	{
		this.requestFocus(); //nothing to do with focusAdjuster, its to get attention of OS
		long lastTime = System.nanoTime();
		double amountOfTicksPerSecond = 60.0;
		int nsPerSecond = 1000000000;
		double nsPerTick = nsPerSecond / amountOfTicksPerSecond; //nano seconds per tick
		double ticksToDo = 0;
		long timer = System.currentTimeMillis();
		//int frames = 0;
		
		while (this.running) {
			long now = System.nanoTime();
			//(now - lastTime): time passed since last loop 
			long nsSinceLastLoop = (now - lastTime);
			ticksToDo += nsSinceLastLoop / nsPerTick; // how many ticks should we have done since last time
			lastTime = now; //update the now
			while (1 <= ticksToDo) { // if we are one tick or more behind 
				this.tick(); //do the tick 
				ticksToDo--; // reduce the delta by one -> delta [0; ∞]
				if (this.running) { //if the game is still running render
                    this.render();
				}
				/*frames++; //Increase the number of frames*/ //if 1 second passed since last check, 
				if (System.currentTimeMillis() - timer > 1000) {
					timer += 1000;//increase game internal timer
					//System.out.println("FPS: " + frames);//print the FPS
					this.seconds++;
					//frames = 0;//reset frames count
				}
			}
		}
		this.stop();
	}
	
	public void tick() 
	{
		this.handler.tick();
		this.hud.tick();
	}
	
	public void render() 
	{
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// Draw Seconds (score) to screen
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(new Font("Sans-Serif", Font.PLAIN, 48));
        g2.setColor(Color.WHITE);
        g2.drawString("" + this.seconds, 300, 40); 

		this.handler.render(g);
		//below because we want it to be rendered with max zindex
		this.hud.render(g);
		
		if (this.hud.getCredits() == 0) {
			this.gameOver(g);
		}

		g.dispose();
		bs.show();

	}
	
	public static void main (String args[]) throws Exception 
	{
		new Game();
	}
}