package neiklot.visio.terrain;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import neiklot.visio.environment.Food;
import neiklot.visio.live.Ant;

public class TerrainConstructor {

	final TerrainPanel terrain = new TerrainPanel();

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
			@Override
			public void actionPerformed(ActionEvent ae) {
				terrain.paused = false;
				System.out.println("No pause pressed");
			}
		});
	}

	private void initComponents() {
		Ant ant = new Ant(0, -1, -2, 0, 0, Color.BLUE);
		Ant ant2 = new Ant(1, -3, -4, 50, 50, Color.RED);
		Ant ant3 = new Ant(2, -5, -6, 0, 0, Color.BLUE);
		Ant ant4 = new Ant(3, -7, -8, 50, 50, Color.RED);

		JFrame frame = new JFrame("Terrain");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridBagLayout());
		terrain.addEntity(ant);
		terrain.addEntity(ant2);
		terrain.addEntity(ant3);
		terrain.addEntity(ant4);

		setGamePanelKeyBindings(terrain);
		frame.add(terrain);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		runGameLoop();
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

	class TerrainPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		ArrayList<Ant> ants = new ArrayList<>();
		ArrayList<Food> foods = new ArrayList<>();
		public boolean running = true, paused = false;
		private int frameCount = 0;
		private int fps = 0;
		private int borns = 0, dieds = 0, brothers = 0;

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
			return new Dimension(200, 200);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.green);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.BLACK);
			// g.drawString("FPS: " + fps, 5, 10);
			g.drawString("ants population: " + ants.size(), 5, 20);
			g.drawString("ants died: " + dieds, 5, 30);
			g.drawString("ants born: " + borns, 5, 40);
			g.drawString("ants No brothers: " + brothers, 5, 50);
			for (Ant ant : ants) {
				ant.draw(g);
			}
			for (Food food : foods) {
				food.draw(g);
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
						for (Ant ant : ants) {
							ant.moveAnt();
						}
						dieds = God.comprovingDieds(ants, dieds);
						// brothers = God.countBrothers(ants);
						
						if(now%100==0){
							God.raiseFood(foods);
						}
						brothers = terrain.foods.size();
						God.launching(ants, foods);
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
