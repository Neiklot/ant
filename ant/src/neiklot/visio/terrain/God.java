package neiklot.visio.terrain;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import neiklot.visio.live.Ant;

public class God {

	public static boolean comprovingCollision(ArrayList<Ant> ants) {
		for (int a = 0; a < ants.size(); a++) {
			for (int b = 0; b < ants.size(); b++) {
				if (a != b) {
					if (distance(ants.get(a).getX(), ants.get(b).getX(), ants
							.get(a).getY(), ants.get(b).getY()) < 50
							&& reproduction(ants.get(a), ants.get(b))
							&& ants.get(a).getAge() > 200
							&& ants.get(b).getAge() > 200) {
						ants.get(b).setX(0);
						ants.get(b).setY(199);
						newBorn(ants, ants.get(a), ants.get(b));
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static int getMaxId(ArrayList<Ant> ants){
		int max=0;
		for(Ant ant:ants)
		{
			if(ant.getId()>0){
				max=ant.getId();
			}
		}
		return max;
	}

	public static double distance(int x, int x2, int y, int y2) {
		return Math.sqrt(Math.pow((x2 - x), 2) + Math.pow((y2 - y), 2));
	}

	public static void newBorn(ArrayList<Ant> ants, Ant father, Ant mather) {
		ants.add(new Ant(getMaxId(ants)+1, father.getId(), mather.getId(),
				father.getX() + 50, father.getY() + 50, Color.MAGENTA));
	}

	public static boolean reproduction(Ant father, Ant mather) {
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
		return (a.getId() == b.getIdF()||b.getId()==a.getIdF());
	}

	public static boolean isTheMather(Ant a, Ant b) {
		return (a.getId() == b.getIdM() || b.getId()==a.getIdM());
	}

	public static int comprovingDieds(ArrayList<Ant> ants, int dieds) {
		Iterator<Ant> antsIterator = ants.iterator();
		while (antsIterator.hasNext()) {
			Ant ant=antsIterator.next();
			if (ant.getAge() > ant.getAgeToDie()) {
				ant=null;
				antsIterator.remove();
				dieds++;
			}
		}
		return dieds;
	}

	public static int countBrothers(ArrayList<Ant> ants) {
		int brothers = 0;
		for (int a = 0; a < ants.size(); a++) {
			boolean brothersOk=false;
			for (int b = 0; b < ants.size(); b++) {
				if (a != b) {
					if (!isTheBrother(ants.get(a),ants.get(b))) {
						brothersOk=true;
					}
				}
			}
			if(brothersOk){
				brothers++;
			}
		}
		return brothers;
	}
}