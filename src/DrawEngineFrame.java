import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;


public class DrawEngineFrame extends JFrame 
{
	public static int TILE_SIZE = 16;

	private JPanel contentPane;
    private MapPanel mapPanel;
	
	
	//private LevelGenerator levelGenerator;
	PathFinder pathFinder;
	private Tile[][] map;
	private int mapWidth, mapHeight;
	
	/* For testing path finding */
	private Position initialPos = new Position(2,2);
	private Position finalPos = new Position(6,6);
	
	
	/**
	 * Create the frame.
	 * get date from Level
	 * create buttons with ActionListeners :
		* ClickListener on panel ( for map editor )
		* ActionListeners on buttons
	 * @param map initialised LevelGenerator
	 */
	public DrawEngineFrame(Tile[][] map)
	{
		this.map = map;

		pathFinder = new PathFinder();


		initWindow();

		/*Init Map Panel */
		mapPanel = new MapPanel();
		
		mapPanel.addMouseListener(new MouseAdapter() 
		{
			/**
			 * On LeftClick : Set InitialPosition ( testing pathfinding )
			 * On RightClick : Set finalPosition ( testing pathfinding )
			*/
			@Override
			public void mouseClicked(MouseEvent event) 
			{
				int i = event.getY()/TILE_SIZE;
				int j = event.getX()/TILE_SIZE;
				System.out.println("(" + i + "," + j +") = " + map[i][j]);
				
				if(event.getButton() == MouseEvent.BUTTON1)
				{
					initialPos.setPosX(j);
					initialPos.setPosY(i);
					mapPanel.clear();
				}
				else if(event.getButton() == MouseEvent.BUTTON2)
				{
					finalPos.setPosX(j);
					finalPos.setPosY(i);
					mapPanel.clear();
				}
			}
		});
		
		contentPane.add(mapPanel, BorderLayout.CENTER);

		/*Init Button Panels */
		JPanel btnPanel = new JPanel();
		contentPane.add(btnPanel, BorderLayout.SOUTH);
		
		JButton btnDrawShortest = new JButton("Draw shortest");
		btnDrawShortest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				//pathFinder = new PathFinder();
				mapPanel.drawPath(pathFinder.getShortestPath(map, initialPos, finalPos));
			}
		});
		btnPanel.add(btnDrawShortest);
		
		JComboBox<String> mapsCmBox = new JComboBox<>();

		mapsCmBox.addItem("Level Map");
		/*Add Mouses' maps*/
		mapsCmBox.addItem("Mouse Map");
		/*Add Cats' maps */
		mapsCmBox.addItem("Cat Map");

		btnPanel.add(mapsCmBox);

		JButton switchMapButton = new JButton("Switch map");
		btnPanel.add(switchMapButton);
	}

	private void switchToLevel()
	{

	}

	private void switchToCatMap(int i)
	{

	}

	private void switchToMouseMap(int i)
	{

	}

	private void initWindow()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 70,
                TILE_SIZE * LevelGenerator.MAP_WIDTH + TILE_SIZE ,
                TILE_SIZE * LevelGenerator.MAP_HEIGHT + 100
		);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}
	
	class MapPanel extends JPanel 
    {
		private static final long serialVersionUID = 1L;

		private BufferedImage bufferedMap;
		private Graphics2D graphic;

		private BufferedImage catSprite;

		public MapPanel()
		{
			bufferedMap = new BufferedImage(LevelGenerator.MAP_WIDTH*TILE_SIZE,
					LevelGenerator.MAP_HEIGHT*TILE_SIZE,
					BufferedImage.TYPE_BYTE_INDEXED);
			graphic = bufferedMap.createGraphics();

			// Load Cat
			try
			{
				catSprite = ImageIO.read(new File("res/cat_Sprite.png"));
			}
			catch (IOException e)
			{
				Graphics2D g2d = catSprite.createGraphics();

				g2d.setColor(Color.red);
				g2d.fillRect(0,0,TILE_SIZE, TILE_SIZE);
				g2d.dispose();
			}
			drawMap();
		}
		
		@Override
        public void paintComponent(Graphics g) 
        {
            super.paintComponent(g);
            
            /*Draw buffered map*/
			g.drawImage(bufferedMap, 0, 0, null);
			g.drawImage(catSprite, 6*TILE_SIZE,6*TILE_SIZE, null);

        }
	

        /*Par defaut draw levelGenerator.map*/
        public void drawMap() {
            drawMap(map);
        }
        
        /*Takes a map, buffer it, and repaint it*/
        public void drawMap(Tile[][] Map)
		{
            bufferMap(map);
            bufferObjects();
            repaint();   
        }
        
        public void drawPoint(int x, int y, Color color)
        {
        	graphic.setColor(color);
			graphic.fillRect(x*TILE_SIZE, y*TILE_SIZE,
					TILE_SIZE, TILE_SIZE);
        	repaint();
        }
        
        public void clear()
        {
        	bufferMap(map);
        	bufferObjects();
        	repaint();
        }
        
        public void drawPath(ArrayList<Position> path)
        {
        	bufferPath(path);
        	
        	repaint();
        }
        
        /*Buffer functions*/
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
    		/* Draw Characters */
            // get mouse pos 
            // draw mouse 
            // get cats pos 
            // draw cats 
			drawPoint(initialPos.getPosX(), initialPos.getPosY(), Color.BLUE);
			drawPoint(finalPos.getPosX(), finalPos.getPosY(), Color.GREEN);
        }
        
        private void bufferMap(Tile[][] map)
        {
        	// create graphics ( to draw )

        	/* Draw Map */
            for (int i = 0; i < LevelGenerator.MAP_HEIGHT; i++) {
                for (int j = 0; j < LevelGenerator.MAP_WIDTH; j++)
                {
                    // Choose tile color 
                    Color tileColor = Color.WHITE;
                    switch (map[i][j]) {
                        case WALL:
                            tileColor = Color.BLACK;
                            break;
                        case EMPTY:
                            tileColor = Color.WHITE;
                            break;
                        case MINE:
                            tileColor = Color.RED;
                            break;
                        case CHEESE:
                            tileColor = Color.YELLOW;
                            break;
                    }
                    
                    // Draw Tile :  i for y (line) , j for x (column)

                    graphic.setColor(tileColor);
                    graphic.fillRect(j*TILE_SIZE, i*TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
			// ended
        }
        

    }

}
