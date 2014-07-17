package neiklot.visio.live;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

public class AntsPredator {
	int terrainWidth=500,terrainHeight=500;
	int x_position, y_position;
	boolean advancingOnX = true, advancingOnY = true, inteligence = false;
	int movement = 0;
	int randomNumX = 0, randomNumY = 0;
	Random rand = new Random();
	
	public AntsPredator(){
		this.setX_position(terrainWidth);
		this.setY_position(terrainHeight);
	}

	public int getX_position() {
		return x_position;
	}

	public void setX_position(int x_position) {
		this.x_position = x_position;
	}

	public int getY_position() {
		return y_position;
	}

	public void setY_position(int y_position) {
		this.y_position = y_position;
	}
	
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.blue);
		g2.drawRect(this.getX_position(), this.getY_position(), 2, 2);

	}
	
	public boolean collisionX_MAX(int x_max, int x) {
		if (this.x_position > x_max) {
			return true;
		}
		return false;
	}

	public boolean collisionX_MIN(int x_min, int x) {
		if (this.x_position < x_min) {
			return true;
		}
		return false;
	}
	
	public boolean collisionY_MAX(int y_max, int y) {
		if (this.y_position > y_max) {
			return true;
		}
		return false;
	}

	public boolean collisionY_MIN(int y_min, int y) {
		if (this.y_position < y_min) {
			return true;
		}
		return false;
	}
	
	public void advanceX(int i) {
		this.setX_position(this.getX_position() + i);
	}

	public void advanceY(int i) {
		this.setY_position(this.getY_position() + i);
	}
	
	public void moveAntPredator() {
		if (movement > 50) {
			movement = 0;
			advancingOnX = getRandomBoolean();
			advancingOnY = getRandomBoolean();
			randomNumX = rand.nextInt(3);
			randomNumY = rand.nextInt(3);
		}

		if (advancingOnX) {
			if (!this.collisionX_MAX(terrainWidth, this.getX_position())) {
				this.advanceX(randomNumX);
			} else {
				advancingOnX = false;
			}
		} else {
			if (!this.collisionX_MIN(0, this.getX_position())) {
				this.advanceX(-randomNumX);
			} else {
				advancingOnX = true;
			}
		}
		if (advancingOnY) {
			if (!this.collisionY_MAX(terrainHeight, this.getY_position())) {
				this.advanceY(randomNumY);
			} else {
				advancingOnY = false;
			}
		} else {
			if (!this.collisionY_MIN(0, this.getY_position())) {
				this.advanceY(-randomNumY);
			} else {
				advancingOnY = true;
			}
		}
		movement++;
	}

	public boolean getRandomBoolean() {
		return Math.random() < 0.5;
	}

}
