package neiklot.visio.live;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import neiklot.visio.environment.Food;

public class Inteligence extends Ant {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int terrainWidth = 500, terrainHeight = 500;

	public Inteligence(int id, int idF, int idM, int x, int y, Color color) {
		super(id, idF, idM, x, y, color);
	}

	public boolean scape(ArrayList<AntsPredator> spiders, ArrayList<Food> foods) {
		Iterator<AntsPredator> spidersIterator = spiders.iterator();
		boolean advanceX, advanceY;
		int velocityY = 1, velocityX = 1;
		if (spiders.size() > 0) {
			while (spidersIterator.hasNext()) {
				AntsPredator spider = spidersIterator.next();
				if (distance(this.getX(), spider.getX_position(), this.getY(),
						spider.getY_position()) < 200) {
					if (spider.getX_position() > this.getX()) {
						advanceX = false;
						velocityX = 3;
					} else if (spider.getX_position() < this.getX()) {
						advanceX = true;
						velocityX = 3;
					} else {
						advanceX = true;
						velocityX = 0;
					}
					if (spider.getY_position() > this.getY()) {
						advanceY = false;
						velocityY = 3;
					} else if (spider.getY_position() < this.getY()) {
						advanceY = true;
						velocityY = 3;
					} else {
						advanceY = true;
						velocityY = 0;
					}
					this.moveAnt(advanceX, advanceY, velocityX, velocityY);
				} else {
					this.moveAnt();
				}
			}
		}else  if(foods.size() > 0) {
			this.goToEat(foods);
		} else {
			this.moveAnt();
		}

		return false;
	}

	public boolean goToEat(ArrayList<Food> foods) {
		Iterator<Food> foodIterator = foods.iterator();
		boolean advanceX, advanceY;
		int velocityY = 1, velocityX = 1;
		while (foodIterator.hasNext()) {
			Food food = foodIterator.next();
			if (distance(this.getX(), food.getX(), this.getY(), food.getY()) < 500) {
				if (food.getX() > this.getX()) {
					advanceX = true;
					velocityX = 1;
				} else if (food.getX() < this.getX()) {
					advanceX = false;
					velocityX = 1;
				} else {
					advanceX = false;
					velocityX = 0;
				}
				if (food.getY() > this.getY()) {
					advanceY = true;
					velocityY = 1;
				} else if (food.getY() < this.getY()) {
					advanceY = false;
					velocityY = 1;
				} else {
					advanceY = false;
					velocityY = 0;
				}
				this.moveAnt(advanceX, advanceY, velocityX, velocityY);
			} else {
				this.moveAnt();
			}
		}
		return false;
	}

	public static double distance(int x, int x2, int y, int y2) {
		return Math.sqrt(Math.pow((x2 - x), 2) + Math.pow((y2 - y), 2));
	}

	public void moveAnt(boolean advancingOnX, boolean advancingOnY,
			int velocityX, int velocityY) {
		this.setAge(this.getAge() + 1);
		if (advancingOnX) {
			if (!this.collisionX_MAX(terrainWidth, this.getX())) {
				this.advanceX(velocityX);
			} else {
				advancingOnX = false;
			}
		} else {
			if (!this.collisionX_MIN(0, this.getX())) {
				this.advanceX(-velocityX);
			} else {
				advancingOnX = true;
			}
		}
		if (advancingOnY) {
			if (!this.collisionY_MAX(terrainHeight, this.getY())) {
				this.advanceY(velocityY);
			} else {
				advancingOnY = false;
			}
		} else {
			if (!this.collisionY_MIN(0, this.getY())) {
				this.advanceY(-velocityY);
			} else {
				advancingOnY = true;
			}
		}
		movement++;
	}

}
