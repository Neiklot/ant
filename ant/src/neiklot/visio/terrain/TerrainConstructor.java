package neiklot.visio.terrain;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import neiklot.visio.environment.Food;
import neiklot.visio.live.Ant;
import neiklot.visio.live.AntsPredator;
import neiklot.visio.live.Inteligence;

public class TerrainConstructor {

	final TerrainPanel terrain = new TerrainPanel();
	final ResultsPanel results = new ResultsPanel();
	int terrainWidth = 500, terrainHeight = 500;

	public TerrainConstructor() {
		initComponents();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TerrainConstructor();
			}
		});
	}

	private void setGamePanelKeyBindings(final TerrainPanel terrain) {
		terrain.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke("D"), "D pressed");
		terrain.getActionMap().put("D pressed", new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent ae) {
				terrain.paused = false;
				System.out.println("No pause pressed");
			}
		});
		terrain.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke("H"), "H pressed");
		terrain.getActionMap().put("H pressed", new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent ae) {
				addHunter();
				System.out.println("Created Spider");
			}
		});
		
		terrain.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke("L"), "L pressed");
		terrain.getActionMap().put("L pressed", new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent ae) {
				addInteligentAnt();
				System.out.println("Mutation produced");
			}
		});

		terrain.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke("J"), "J pressed");
		terrain.getActionMap().put("J pressed", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				killerHunter();
				System.out.println("Killed Spider");
			}
		});
	}

	private void initComponents() {

		initScenario();
		JFrame frame = new JFrame("Terrain");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridBagLayout());

		setGamePanelKeyBindings(terrain);
		frame.add(results);
		frame.add(terrain);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		runGameLoop();
	}

	public void addHunter() {
		AntsPredator spider = new AntsPredator();
		terrain.addAntPredator(spider);
	}
	
	public void addInteligentAnt() {
		Inteligence ant = new Inteligence(God.getMaxId(terrain.ants) + 1, 0,
				0, 50, 50, Color.RED);
		ant.setInteligence(true);
		terrain.addEntity(ant);
	}

	public void killerHunter() {
		Iterator<AntsPredator> spiderIterator = terrain.spiders.iterator();
		if (spiderIterator.hasNext()) {
			spiderIterator.next();
			spiderIterator.remove();
		}
	}

	public void initScenario() {
		Inteligence ant = new Inteligence(0, -1, -2, 0, 0, Color.RED);
		ant.setInteligence(true);
		Ant ant2 = new Ant(1, -3, -4, 50, 50, Color.BLACK);
		Inteligence ant3 = new Inteligence(2, -5, -6, 0, 0, Color.RED);
		ant3.setInteligence(true);
		Ant ant4 = new Ant(3, -7, -8, 50, 50, Color.BLACK);

		terrain.addEntity(ant);
		terrain.addEntity(ant2);
		terrain.addEntity(ant3);
		terrain.addEntity(ant4);

	}

	public void runGameLoop() {
		Thread loop = new Thread(new Runnable() {
			@Override
			public void run() {
				terrain.gameLoop();
			}
		});
		loop.start();
	}

	class ResultsPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ResultsPanel() {
			setLayout(null);
		}

		@Override
		public Dimension getMinimumSize() {
			return getPreferredSize();
		}

		@Override
		public Dimension getMaximumSize() {
			return getPreferredSize();
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(200, 200);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.BLACK);
			g.drawString("ants population: " + terrain.ants.size(), 5, 20);
			g.drawString("ants died: " + terrain.dieds, 5, 30);
			g.drawString("ants born: " + terrain.borns, 5, 40);
			g.drawString("Dummy Ants: "
					+ (terrain.ants.size() - terrain.brothers), 5, 50);
			g.drawString("Inteligent Ants: " + terrain.brothers, 5, 60);
			g.drawString("Generations: " + terrain.generations, 5, 70);

		}

		private void drawResult() {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					repaint();
				}
			});
		}

	}

	class TerrainPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public boolean running = true, paused = false;
		private int frameCount = 0;
		private int fps = 0;
		private int borns = 0, dieds = 0, brothers = 0, generations = 0;
		List<Ant> ants = Collections.synchronizedList(new ArrayList<Ant>());
		ArrayList<Food> foods = new ArrayList<>();
		ArrayList<AntsPredator> spiders = new ArrayList<>();

		public TerrainPanel() {
			setLayout(null);
		}

		@Override
		public Dimension getMinimumSize() {
			return getPreferredSize();
		}

		@Override
		public Dimension getMaximumSize() {
			return getPreferredSize();
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(terrainWidth, terrainHeight);
		}

		@Override
		protected synchronized void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.green);
			g.fillRect(0, 0, getWidth(), getHeight());

			Iterator<Ant> antsIterator=ants.iterator();
			while(antsIterator.hasNext()){
				antsIterator.next().draw(g);
			}
			Iterator<Food> foodIterator=foods.iterator();
			while(foodIterator.hasNext()){
				foodIterator.next().draw(g);
			}
			Iterator<AntsPredator> spriderIterator=spiders.iterator();
			while(spriderIterator.hasNext()){
				spriderIterator.next().draw(g);
			}
			frameCount++;
		}

		public void gameLoop() {
			final double GAME_HERTZ = 30.0;
			final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
			final int MAX_UPDATES_BEFORE_RENDER = 5;
			double lastRenderTime = System.nanoTime();
			double lastUpdateTime = System.nanoTime();
			final double TARGET_FPS = 60;
			final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
			int lastSecondTime = (int) (lastUpdateTime / 1000000000);

			while (running) {
				double now = System.nanoTime();
				int updateCount = 0;
				if (!paused) {
					while (now - lastUpdateTime > TIME_BETWEEN_UPDATES
							&& updateCount < MAX_UPDATES_BEFORE_RENDER) {
						brothers = 0;

						// RESTART
						if (ants.size() <= 0) {
							dieds = 0;
							borns = 0;
							generations++;
							initScenario();
						}

						// FOOD RAISE
						if (now % 100 == 0 || now % 50 == 0) {
							God.raiseFood(foods);
						}

						// MOVING PREDATORS
						for (AntsPredator spider : spiders) {
							if (!spider.goToHunter(ants)) {
								spider.moveAntPredator();
							}
						}

						dieds = God.hunter(ants, spiders, dieds);

						// ANTS MOVEMENT & EVOLUTION
						for (Ant ant : ants) {
							if (ant.getInteligence()) {
								Inteligence antInteligence = ((Inteligence) ant);
								// antInteligence.goToEat(foods);
								antInteligence.scape(spiders, foods);
							} else {
								ant.moveAnt();
							}
						}
						dieds = God.comprovingDieds(ants, dieds);

						if (now % 20 == 0) {
							for (Ant ant : ants) {
								ant.setEnergy(ant.getEnergy() - 1);
							}
						}

						// MOVE INTELIGENT
						for (Ant ant : ants) {
							if (ant.getInteligence()) {
								brothers++;
							}
						}
						God.launching(ants, foods);

						// COPROVING COLLISIONS
						if (God.comprovingCollision(ants)) {
							borns++;
						}

						lastUpdateTime += TIME_BETWEEN_UPDATES;
						updateCount++;
					}

					if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
						lastUpdateTime = now - TIME_BETWEEN_UPDATES;
					}

					drawTerrain();
					results.drawResult();
					lastRenderTime = now;

					int thisSecond = (int) (lastUpdateTime / 1000000000);
					if (thisSecond > lastSecondTime) {
						System.out.println("NEW SECOND " + thisSecond + " "
								+ frameCount);
						fps = frameCount;
						frameCount = 0;
						lastSecondTime = thisSecond;
					}

					while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS
							&& now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
						Thread.yield();

						try {
							Thread.sleep(1);
						} catch (Exception e) {
						}

						now = System.nanoTime();
					}
				}
			}
		}

		public void addEntity(Ant ant) {
			ants.add(ant);
		}

		public void addAntPredator(AntsPredator spider) {
			spiders.add(spider);
		}

		public void addFood(Food food) {
			foods.add(food);
		}

		private void drawTerrain() {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					repaint();
				}
			});
		}
	}

}
