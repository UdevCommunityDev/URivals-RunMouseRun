import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DrawEngineFrame extends JFrame 
{
	public static final int TILE_SIZE = 20;

	private JPanel contentPane;
    private MapPanel mapPanel;
	
	
	private LevelGenerator levelGenerator;
	PathFinder pathFinder;
	private Tile[][] map;
	
	/* For testing path finding */
	private Position initialPos = new Position(2,2);
	private Position finalPos = new Position(6,6);
	
	
	/**
	 * Create the frame.
	 * get date from Level
	 * create buttons with ActionListeners :
		* ClickListener on panel ( for map editor )
		* ActionListeners on buttons
	 * @param levelGenerator initialised LevelGenerator
	 */
	public DrawEngineFrame(LevelGenerator levelGenerator) 
	{
		this.levelGenerator = levelGenerator;
		map = levelGenerator.getMap();

		pathFinder = new PathFinder();


		initWindow();

		/*Init Map Panel */
		mapPanel = new MapPanel();
		
		mapPanel.addMouseListener(new MouseAdapter() 
		{
			/**
			 * On LeftClick : change the tile to next 
			 * On RightClick : Set InitialPosition ( testing pathfinding )
			 * On MiddleClick : Set finalPosition ( testing pathfinding ) 
			*/
			@Override
			public void mouseClicked(MouseEvent event) 
			{
				int i = event.getY()/TILE_SIZE;
				int j = event.getX()/TILE_SIZE;
				System.out.println("(" + i + "," + j +") = " + map[i][j]);
				
				if(event.getButton() == MouseEvent.BUTTON1)
				{	
					levelGenerator.switchTile(j, i);
					mapPanel.drawMap();
				}
				else if(event.getButton() == MouseEvent.BUTTON2)
				{
					initialPos.setPosX(j);
					initialPos.setPosY(i);
					mapPanel.clear();
				}
				else 
				{
					finalPos.setPosX(j);
					finalPos.setPosY(i);
					mapPanel.clear();				}
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
		
		JButton levelButton = new JButton("Level Map");
		btnPanel.add(levelButton);
		
		JButton mouseButton = new JButton("Mouse Map");
		btnPanel.add(mouseButton);
		
		JButton catButton = new JButton("Cat 1 Button");
		btnPanel.add(catButton);
	}

	private void initWindow()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 70,
                TILE_SIZE * levelGenerator.MAP_WIDTH + TILE_SIZE ,
                TILE_SIZE * levelGenerator.MAP_HEIGHT + 100
		);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}
	
	class MapPanel extends JPanel 
    {
		private static final long serialVersionUID = 1L;

		private Color bufferedMap[][];
		
		public MapPanel()
		{
			if(map != null)
				bufferedMap = new Color[map.length][map[0].length];
			drawMap();
		}
		
		@Override
        public void paintComponent(Graphics g) 
        {
            super.paintComponent(g);
            
            /*Draw buffered map*/
            for (int i = 0; i < levelGenerator.MAP_HEIGHT; i++) {
                for (int j = 0; j < levelGenerator.MAP_WIDTH; j++)
                {
                	g.setColor(bufferedMap[i][j]);
                	
                	g.fillRect(j*TILE_SIZE, i*TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
                
            
        }
	

        /*Par defaut draw levelGenerator.map*/
        public void drawMap() {
            drawMap(levelGenerator.getMap());
        }
        
        /*Takes a map, buffer it, and repaint it*/
        public void drawMap(Tile[][] Map) {
            map = levelGenerator.getMap();
            bufferMap();
            bufferObjects();
            repaint();   
        }
        
        public void drawPoint(int x, int y, Color color)
        {
        	bufferedMap[y][x] = color;
        	repaint();
        }
        
        public void clear()
        {
        	bufferMap();
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
        	
        	for(Position t : path)
        	{
        		bufferedMap[t.getPosY()][t.getPosX()] = Color.BLUE;
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
        
        private void bufferMap()
        {
        	/* Draw Map */
            for (int i = 0; i < levelGenerator.MAP_HEIGHT; i++) {
                for (int j = 0; j < levelGenerator.MAP_WIDTH; j++)
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
//                    g.setColor(tileColor);
//                    g.fillRect(j*TILESIZE, i*TILESIZE, TILESIZE, TILESIZE);
                    
                    bufferedMap[i][j] = tileColor;
                }
            }
        }
        

    }

}
