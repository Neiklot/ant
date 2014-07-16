package neiklot.visio.live;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import javax.swing.JPanel;

public class Ant extends JPanel {

	boolean advancingOnX = true, advancingOnY = true, inteligence = false;
	Random rand = new Random();
	int randomNumX = 0, randomNumY = 0;
	int movement = 0;
	int id, idF, idM;
	int age;
	int ageToDie;
	int energy;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int x_position, y_position;
	private int width = 50, height = 50;
	Color color;

	public Ant(int id, int idF, int idM, int x, int y, Color color) {
		this.setAgeToDie(randomAgeToDie());
		this.setAge(0);
		this.x_position = x;
		this.y_position = y;
		this.color = color;
		this.setId(id);
		this.setIdF(idF);
		this.setIdM(idM);
		this.setEnergy(250);
		this.inteligence = false;
	}

	public int randomAgeToDie() {
		return 1000 - rand.nextInt(900);
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

	public int getId() {
		return this.id;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public boolean isInteligence() {
		return inteligence;
	}

	public void setInteligence(boolean inteligence) {
		this.inteligence = inteligence;
	}

	public int getAgeToDie() {
		return ageToDie;
	}

	public void setAgeToDie(int ageToDie) {
		this.ageToDie = ageToDie;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIdF(int idF) {
		this.idF = idF;
	}

	public void setIdM(int idM) {
		this.idM = idM;
	}

	public int getIdF() {
		return idF;
	}

	public int getIdM() {
		return idM;
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
		this.setX(this.getX() + i);
	}

	public void advanceY(int i) {
		this.setY(this.getY() + i);
	}

	public int getX() {
		return this.x_position;

	}

	public void setX(int x) {
		this.x_position = x;
	}

	public void setY(int y) {
		this.y_position = y;
	}

	public int getY() {
		return this.y_position;
	}

	public Color getColor() {
		return this.color;
	}

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(this.getColor());
		g2.drawLine(this.getX(), this.getY(), this.getX() + 1, this.getY() + 1);
	}

	@Override
	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x, y, width, height);
	}

	public boolean view() {

		return false;
	}

	public void moveAnt() {
		this.setAge(this.getAge() + 1);
		if (movement > 50) {
			movement = 0;
			advancingOnX = getRandomBoolean();
			advancingOnY = getRandomBoolean();
			randomNumX = rand.nextInt(3);
			randomNumY = rand.nextInt(3);
		}

		if (advancingOnX) {
			if (!this.collisionX_MAX(199, this.getX())) {
				this.advanceX(randomNumX);
			} else {
				advancingOnX = false;
			}
		} else {
			if (!this.collisionX_MIN(0, this.getX())) {
				this.advanceX(-randomNumX);
			} else {
				advancingOnX = true;
			}
		}
		if (advancingOnY) {
			if (!this.collisionY_MAX(199, this.getY())) {
				this.advanceY(randomNumY);
			} else {
				advancingOnY = false;
			}
		} else {
			if (!this.collisionY_MIN(0, this.getY())) {
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

	public boolean getInteligence() {
		return this.inteligence;
	}

}
