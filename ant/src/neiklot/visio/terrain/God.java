package neiklot.visio.terrain;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import neiklot.visio.environment.Food;
import neiklot.visio.live.Ant;
import neiklot.visio.live.AntsPredator;
import neiklot.visio.live.Inteligence;

public class God {
	static int terrainWidth = 500;
	static int terrainHeight = 500;
	static Random rand = new Random();

	public synchronized static boolean comprovingCollision(List<Ant> ants) {
		for (int a = 0; a < ants.size(); a++) {
			for (int b = 0; b < ants.size(); b++) {
				if (a != b) {
					if (distance(ants.get(a).getX(), ants.get(b).getX(), ants
							.get(a).getY(), ants.get(b).getY()) < 50
							&& reproduction(ants.get(a), ants.get(b))
							&& ants.get(a).getAge() > 200
							&& ants.get(b).getAge() > 200) {

						newBorn(ants, ants.get(a), ants.get(b));
						return true;
					}
				}
			}
		}
		return false;
	}

	public synchronized static void raiseFood(ArrayList<Food> foods) {
		int randomNumX = rand.nextInt(terrainWidth);
		int randomNumY = rand.nextInt(terrainHeight);
		foods.add(new Food(randomNumX, randomNumY));
	}

	public synchronized static boolean launching(List<Ant> ants,
			ArrayList<Food> foods) {
		for (int a = 0; a < ants.size(); a++) {
			Iterator<Food> foodIterator = foods.iterator();
			while (foodIterator.hasNext()) {
				Food food = foodIterator.next();
				if (distance(ants.get(a).getX(), food.getX(), ants.get(a)
						.getY(), food.getY()) < 50) {
					foodIterator.remove();
					ants.get(a).setEnergy(1000);
				}
			}
		}
		return false;
	}

	public static int hunter(List ants, ArrayList<AntsPredator> spiders,
			int dieds) {
		int died = dieds;
		for (int a = 0; a < spiders.size(); a++) {
			Iterator<Ant> antIterator = ants.iterator();
			while (antIterator.hasNext()) {
				Ant ant = antIterator.next();
				if (distance(spiders.get(a).getX_position(), ant.getX(),
						spiders.get(a).getY_position(), ant.getY()) < 10) {
					died++;
					antIterator.remove();
				}
			}
		}
		return died;
	}

	public static int getMaxId(List<Ant> ants) {
		int max = 0;
		for (Ant ant : ants) {
			if (ant.getId() > 0) {
				max = ant.getId();
			}
		}
		return max;
	}

	public synchronized static double distance(int x, int x2, int y, int y2) {
		return Math.sqrt(Math.pow((x2 - x), 2) + Math.pow((y2 - y), 2));
	}

	public synchronized static void newBorn(List<Ant> ants, Ant father,
			Ant mather) {
		int randomNumX = rand.nextInt(terrainWidth);
		int randomNumY = rand.nextInt(terrainHeight);
		if (father.getInteligence() && mather.getInteligence()) {
			Inteligence ant = new Inteligence(getMaxId(ants) + 1,
					father.getId(), mather.getId(), randomNumX, randomNumY,
					Color.RED);
			ant.setInteligence(true);
			ants.add(ant);
		} else if (father.getInteligence()) {
			Inteligence ant = new Inteligence(getMaxId(ants) + 1,
					father.getId(), mather.getId(), randomNumX, randomNumY,
					Color.RED);
			ant.setInteligence(true);
			ants.add(ant);
		}else if (mather.getInteligence()) {
			Inteligence ant = new Inteligence(getMaxId(ants) + 1,
					father.getId(), mather.getId(), randomNumX, randomNumY,
					Color.BLACK);
			if (getRandomBoolean()) {
				ant.setColor(Color.RED);
				ant.setInteligence(true);
			}
			ants.add(ant);
		} else {
			Ant ant = new Ant(getMaxId(ants) + 1, father.getId(),
					mather.getId(), randomNumX, randomNumY, Color.BLACK);
			ants.add(ant);
		}

	}

	public synchronized static boolean getRandomBoolean() {
		return Math.random() < 0.5;
	}

	public synchronized static boolean reproduction(Ant father, Ant mather) {
		if (!isTheSame(father, mather) && !isTheBrother(father, mather)
				&& !isTheFather(father, mather) && !isTheMather(father, mather)) {
			return true;
		}
		return false;
	}

	public static boolean isTheSame(Ant a, Ant b) {
		return (a.getId() == b.getId());
	}

	public static boolean isTheBrother(Ant a, Ant b) {
		return (a.getIdF() == b.getIdF() && a.getIdM() == b.getIdM());
	}

	public static boolean isTheFather(Ant a, Ant b) {
		return (a.getId() == b.getIdF() || b.getId() == a.getIdF());
	}

	public static boolean isTheMather(Ant a, Ant b) {
		return (a.getId() == b.getIdM() || b.getId() == a.getIdM());
	}

	public synchronized static int comprovingDieds(List ants, int dieds) {
		Iterator<Ant> antsIterator = ants.iterator();
		while (antsIterator.hasNext()) {
			Ant ant = antsIterator.next();
			if (ant.getAge() > ant.getAgeToDie() || ant.getEnergy() <= 0) {
				antsIterator.remove();
				dieds++;
			}
		}
		return dieds;
	}

	public synchronized static int countBrothers(ArrayList<Ant> ants) {
		int brothers = 0;
		for (int a = 0; a < ants.size(); a++) {
			boolean brothersOk = false;
			for (int b = 0; b < ants.size(); b++) {
				if (a != b) {
					if (!isTheBrother(ants.get(a), ants.get(b))) {
						brothersOk = true;
					}
				}
			}
			if (brothersOk) {
				brothers++;
			}
		}
		return brothers;
	}
}
