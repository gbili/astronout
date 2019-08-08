package ch.lespagesweb.buildagame;

import java.util.Random;

public class PlatformGenerator {
	
	Random r = new Random();
	int sumOfRadiuses = 0;
	int jumpRadius;
	int additionalSpace = 10;
	float difficulty = 1f;
	float deviation = 0.2f; //max deviation
	float xMean;

	public PlatformGenerator(int quantity, int platformWidth, int platformHeight, int firstPlatformStartsAt, int gameWidth, int gameHeight, int jumpRadius, Game game) {
		Platform platform;
		this.jumpRadius	= jumpRadius;
		this.sumOfRadiuses += this.jumpRadius * this.difficulty; //first radius considered mean
		int x = game.getRandom().nextInt(gameWidth - platformWidth);
		xMean = (float) x;
		int y = gameHeight - platformHeight - firstPlatformStartsAt;
        platform = new Platform(
            platformWidth, 
            platformHeight, 
            x,
            y, 
            ID.Platform, false, game
        );
//			System.out.println("xMean: " + xMean);
//        System.out.println("x: " + x);
//        System.out.println("           y: " + y);
        game.getHandler().addObject(platform);
        int radius;
		for (int i=2; i <= quantity; i ++) {
			radius = this.semiRandomRadiusAroundMean(i);
			if (xMean < (float)Game.WIDTH/2f) {
//                System.out.println(xMean + "<" + (float)Game.WIDTH/2f);
                x += platformWidth/2 + radius; 
			} else {
//                System.out.println(xMean + ">=" + (float)Game.WIDTH/2f);
                x += platformWidth/2 - radius; 
			}
			this.sumOfRadiuses += radius;
			xMean = (xMean*(i-1) + x)/i;
//			System.out.println("xMean: " + xMean);
//			System.out.println("x: " + x);
			y -= (game.getHandler().getPlayer().getHeight() + platformHeight + this.additionalSpace);
//			System.out.println("           y: " + y);
			platform = new Platform(
                platformWidth, 
                platformHeight, 
                x,
                y, 
                ID.Platform, false, game
			);
            game.getHandler().addObject(platform);
		}
	}
	
	public int semiRandomRadiusAroundMean(int i)
	{
		int rInt;
		//if we are below the difficulty
		float meanRadius = this.difficulty * this.jumpRadius;
		float diffToMean = this.deviation*meanRadius;
        rInt = r.nextInt((int)diffToMean);
        int returnRadius;
		if ((float)(this.sumOfRadiuses/i) < meanRadius) {
			returnRadius = (int) meanRadius + rInt;
		} else {
			returnRadius = (int) meanRadius - rInt;
		}
		this.sumOfRadiuses += returnRadius;
		return returnRadius;
	}
}
