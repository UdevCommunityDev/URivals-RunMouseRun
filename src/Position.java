/**
 * A Class to represent 2D(x,y) coordinates 
 * @author Oussama
 * @version 1.0
 * @date 30/06/2017
*/
public class Position {
	
	private int x;	// stores the x coordinate of the position 
	private int y;	// stores the y coordinate of the position 
	
	/**
	 * Constructor, intialize the coordinates from parametres 
	 * @param x : the x ccordinate
	 * @param y : the y coordinate
	*/
	public Position(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Getter for the x coordinate
	 * @return the x coordinate (int)
	*/
	public int getPosX()
	{
		return x;
	}
	
	/**
	 * Getter for the y coordinate
	 * @return the y coordinate (int)
	*/
	public int getPosY()
	{
		return y;
	}
	
	/**
	 * Setter for the x coordinate
	 * @param x (int) : the x coordinate (int)
	*/
	public void setPosX(int x)
	{
		this.x = x;
	}
	
	/**
	 * Setter for the y coordinate
	 * @param y (int) : the y coordinate (int)
	*/
	public void setPosY(int y)
	{
		this.y = y;
	}

	/**
	 * Compares with given position 
	 * @param pos : the position to compare
	 * @return true if both coordinates match   
	*/
	public boolean equals(Position pos) {
		return (x == pos.x && y == pos.y);
	}


    public Position copy()
	{
		return new Position(x,y);
    }
}
