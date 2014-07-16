package neiklot.visio.environment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Food {
	int x_positon, y_position;

	public Food(int x,int y){
		this.setX(x);
		this.setY(y);
	}
	public int getX() {
		return x_positon;
	}

	public void setX(int x) {
		this.x_positon = x;
	}

	public int getY() {
		return y_position;
	}

	public void setY(int y) {
		this.y_position = y;
	}
	
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.YELLOW);
		g2.drawRect(this.getX(), this.getY(), 2, 2);

	}

}
