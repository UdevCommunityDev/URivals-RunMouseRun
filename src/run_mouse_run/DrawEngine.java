package run_mouse_run;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;


public class DrawEngine extends JFrame
{
	public static int TILE_SIZE = 24; // Must have a SpriteSheet{TileSize}.png in res/

	private JPanel contentPane;
	private JScrollPane mapContainerPanel;
	private MapPanel mapPanel;
	private JLabel mapName, timeLabel;
	private JComboBox<String> mapsCmBox;


	PathFinder pathFinder;

	private ArrayList<Map> maps;

	/* For testing path finding */
	private Position initialPos = new Position(2,2);
	private Position finalPos = new Position(6,6);


	/**
	 * Create the frame.
	 * get date from Level
	 * create buttons with ActionListeners :
	 * ClickListener on panel ( for map editor )
	 * ActionListeners on buttons
	 * @param map initialised run_mouse_run.LevelGenerator
	 */
	public DrawEngine(Map map)	/// TODO : Add Maps to parameters
	{
		maps = new ArrayList<>();
		maps.add(map); // don't use addMap() because cmBox is null /// TODO : Check if empty

		pathFinder = new PathFinder();


		initWindow(); /// TODO : Add Start Game Button

		/*=============================Text Panel ===============================*/
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BorderLayout(0,0));


		timeLabel = new JLabel("Time : 00:00");
		timeLabel.setFont(new Font("Calibri", Font.PLAIN, 18));

		mapName = new JLabel("Level Map");
		mapName.setFont(new Font("Calibri", Font.PLAIN, 18));

		textPanel.add(mapName, BorderLayout.WEST);
		textPanel.add(timeLabel, BorderLayout.EAST);


		contentPane.add(textPanel, BorderLayout.NORTH);

		/* ===========================Init run_mouse_run.Map Panel ============================*/
		mapPanel = new MapPanel(map);


		mapContainerPanel = new JScrollPane(mapPanel);

		mapPanel.addMouseListener(new MouseAdapter()/// TODO : run_mouse_run.Map editor
		{
			/*
			 * On LeftClick : Set InitialPosition ( testing pathfinding )
			 * On RightClick : Set finalPosition ( testing pathfinding )
			 */
			@Override
			public void mouseClicked(MouseEvent event)
			{
				int i = event.getY()/TILE_SIZE;
				int j = event.getX()/TILE_SIZE;

				if(event.getButton() == MouseEvent.BUTTON1)
				{
					initialPos.setPosX(j);
					initialPos.setPosY(i);
					mapPanel.clear();
				}
				else if(event.getButton() == MouseEvent.BUTTON3)
				{
					finalPos.setPosX(j);
					finalPos.setPosY(i);
					mapPanel.clear();
				}
			}
		});

		mapContainerPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mapContainerPanel.getVerticalScrollBar().setUnitIncrement(10);
		contentPane.add(mapContainerPanel, BorderLayout.CENTER);

		/*==========================Init Button Panel==================================*/
		JPanel btnPanel = new JPanel();
		contentPane.add(btnPanel, BorderLayout.SOUTH);

		// Draw Shortest Button
		JButton btnDrawShortest = new JButton("Draw shortest");
		btnDrawShortest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				//pathFinder = new run_mouse_run.PathFinder();
				mapPanel.drawPath(pathFinder.getShortestPath(map, initialPos, finalPos));
			}
		});
		btnPanel.add(btnDrawShortest);

		// Switch run_mouse_run.Map Checkbox+Button
		mapsCmBox = new JComboBox<>();

		mapsCmBox.addItem(map.name);

		mapsCmBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				String mapName = (String) mapsCmBox.getSelectedItem();

				// find map
				for(Map m : maps)
				{
					if(m.name == mapName)
					{
						switchToMap(m);
					}
				}
			}
		});
		btnPanel.add(mapsCmBox);

		// new run_mouse_run.Map button
		JButton newMapButton = new JButton("New run_mouse_run.Map");
		newMapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DrawEngine newFrame = new DrawEngine(map);

				newFrame.setVisible(true);
			}
		});
		btnPanel.add(newMapButton);
	}

	public void addMap(Map map)
	{
		maps.add(map);
		mapsCmBox.addItem(map.name);
	}

	public void removeMap(Map map)
	{
		maps.remove(map);
	}

	private void switchToMap(Map map)
	{
		mapPanel.setMap(map);
		mapName.setText(map.name);
		update();
	}

	private void initWindow()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Run Mouse Run!");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int winHeight = TILE_SIZE * LevelGenerator.MAP_HEIGHT + TILE_SIZE + 50;

        System.out.println("PANEL HEIGHT : " + winHeight);
        if(winHeight >= screenSize.getHeight() - 50)
		    winHeight = (int) screenSize.getHeight() - 50;

		setSize(new Dimension(
				TILE_SIZE * LevelGenerator.MAP_WIDTH + TILE_SIZE + 10,
                winHeight
		));

        System.out.println("FRAME HEIGHT : " + winHeight);

        setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

	public void update()
	{
		mapPanel.clear();
		mapPanel.repaint();
	}

	class MapPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;

		public Map map;

		private BufferedImage bufferedMap;
		private Graphics2D graphic;

		// SpriteSheet
		/*
		* 0 : Empty, 1 : Mouse, 2 : Cat, 3 : Cheese, 4: Mine
		*/
		private ArrayList<BufferedImage> sprites;

		public MapPanel(Map map)
		{
			this.map = map;

			setPreferredSize(new Dimension(LevelGenerator.MAP_WIDTH*TILE_SIZE,
					LevelGenerator.MAP_HEIGHT*TILE_SIZE));

			/*init buffered run_mouse_run.Map */
			bufferedMap = new BufferedImage(LevelGenerator.MAP_WIDTH*TILE_SIZE,
					LevelGenerator.MAP_HEIGHT*TILE_SIZE,
					BufferedImage.TYPE_BYTE_INDEXED);
			graphic = bufferedMap.createGraphics();

			// Load Sprites
			loadSprites();
			// draw map
			drawMap();

			// draw chars
			bufferCharacters();
		}

		public void setMap(Map map)
		{
			this.map = map;
		}

		private void loadSprites()
		{
			BufferedImage spriteSheet;	/// TODO : Sprites resizable


			try
			{
				File spriteSheetFile = new File("res/spritesheet"+TILE_SIZE+".png");
				spriteSheet = ImageIO.read(spriteSheetFile);
			}catch (IOException e)		/// TODO Error checking when loading sprites

			{
				System.err.println("Error loading Sprite sheet ");
				// generate spriteSheet with colors
				spriteSheet = new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_BYTE_INDEXED);
			}

			// load each sprite
			sprites = new ArrayList<>();

			for(int i = 0; i < spriteSheet.getHeight()/TILE_SIZE; i++)			//  TODO : Sprites Separated

			{
				sprites.add(
						spriteSheet.getSubimage(0,i*TILE_SIZE, TILE_SIZE, TILE_SIZE)
				);
			}
		}

		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);

            /*Draw buffered map*/
			g.drawImage(bufferedMap, 0, 0, null);

		}

		public void clear()
		{
			bufferMap();
			bufferObjects();
			bufferCharacters();
			repaint();
		}

        /* Draw functions, all buffer on map, bufferChars and repaint the map */

		/*Takes the DrawEngin map, buffer it, add objects and repaint it*/
		public void drawMap()
		{
			bufferMap();
			bufferObjects();
			bufferCharacters();
			repaint();
		}

		public void drawPoint(int x, int y, Color color)
		{
			graphic.setColor(color);
			graphic.fillRect(x*TILE_SIZE, y*TILE_SIZE,
					TILE_SIZE, TILE_SIZE);
			repaint();
		}



		public void drawPath(ArrayList<Position> path)
		{
			bufferPath(path);
			bufferObjects();
			bufferCharacters();
			repaint();
		}

        /*Buffer functions, don't repaint the map*/

		public void bufferCharacters()
		{
			// mouse
			graphic.drawImage(sprites.get(1),
					LevelGenerator.MOUSES_INITIAL_POS.getPosX()*TILE_SIZE,
					LevelGenerator.MOUSES_INITIAL_POS.getPosY()*TILE_SIZE,
					null);

			// cats
			graphic.drawImage(sprites.get(2),
					LevelGenerator.CATS_INITIAL_POS.getPosX()*TILE_SIZE,
					LevelGenerator.CATS_INITIAL_POS.getPosY()*TILE_SIZE,
					null);
		}

		private void bufferPath(ArrayList<Position> path)
		{
			if(path.isEmpty()) return;

			graphic.setColor(Color.BLUE);
			for(Position t : path)
			{
				graphic.fillRect(t.getPosX()*TILE_SIZE, t.getPosY()*TILE_SIZE,
						TILE_SIZE, TILE_SIZE);
			}
		}

		private void bufferObjects()
		{
    		/*For testing */
			drawPoint(initialPos.getPosX(), initialPos.getPosY(), Color.RED);
			drawPoint(finalPos.getPosX(), finalPos.getPosY(), Color.GREEN);
		}

		private void bufferMap()
		{
        	/* Draw run_mouse_run.Map */
			for (int i = 0; i < LevelGenerator.MAP_HEIGHT; i++) {
				for (int j = 0; j < LevelGenerator.MAP_WIDTH; j++)
				{
					// Choose tile /// TODO : Consider WALL & NOT_DISCOVERED

					switch (map.getTile(j, i)) {
						case WALL:
							// Draw run_mouse_run.Tile :  i for y (line) , j for x (column)
							graphic.setColor(Color.BLACK);
							graphic.fillRect(j*TILE_SIZE, i*TILE_SIZE, TILE_SIZE, TILE_SIZE);
							break;
						case EMPTY:
							graphic.drawImage(sprites.get(0),j*TILE_SIZE, i*TILE_SIZE, null);
							break;
						case MINE:
							graphic.drawImage(sprites.get(0),j*TILE_SIZE, i*TILE_SIZE, null);
							graphic.drawImage(sprites.get(4),j*TILE_SIZE, i*TILE_SIZE, null);
							break;
						case CHEESE:
							graphic.drawImage(sprites.get(0),j*TILE_SIZE, i*TILE_SIZE, null);
							graphic.drawImage(sprites.get(3),j*TILE_SIZE, i*TILE_SIZE, null);
							break;
					}
				}
			}
			// ended
		}
	} // End of MapPanel

}	// End Of run_mouse_run.DrawEngine
