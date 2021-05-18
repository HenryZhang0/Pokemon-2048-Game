import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Board extends JPanel
{
	public static JFrame f; //creates a frame 
    private static final int X_DIM = 100;
    private static final int Y_DIM = 100; //determines the size
    private static final int X_OFFSET = 50;
    private static final int Y_OFFSET = 88; //determines the position
    private static final double MIN_SCALE = 0.25;
    private static final int GAP = 20;
    private static final int FONT_SIZE = 30;
    private static final int gap = 7;
    // Grid colours
    private static final Color GRID_COLOR_A = new Color(208, 192, 181);
    private static final Color GRID_COLOR_B = new Color(208, 192, 181);
    private static final Color GRID_COLOR_C = new Color(255, 0, 0); //set colours
    // Image to use if a match is not found
    private static final ImageIcon DEFAULT_PIECE = new ImageIcon("bishopb.gif");
    private static final Color DEFAULT_COLOUR = Color.BLACK;
    // Preset images for pieces
    private static final ImageIcon[] PIECES = new ImageIcon[13];

    // String used to indicate each colour
    private static final String[] PIECE_NAMES = {"2glow", "4glow","number2","number4","number8","number16","number32","number64","number128","number256","number512","number1024","number2048"}; //sets tile name array
    // 2D ARRAY TO STORE ALL OF THE PIECE IMAGES THAT ARE ON THE BOARD
    private ImageIcon[][] grid;
    private Coordinate lastClick;  // How the mouse handling thread communicates
    // to the board where the last click occurred
    private String message = "";
    private String title = "";
    private int numLines = 0;
    private int[][] line = new int[4][100];  // maximum number of lines is 100
    private int columns, rows;

    private boolean first = true;
    private int originalWidth;
    private int originalHeight;
    private double scale;

    /** A constructor to build a 2D board.
     */
    public Board (int rows, int cols)
    {
        super( true );
        f = new JFrame( "2048" ); //makes a new window
        
        JLabel lblNewLabel = new JLabel("<html><b>HOW TO PLAY:</B><p>Use your arrow keys to move the tiles.<p>When two tiles with the same number touch, they merge into one!<p>Join the numbers and get to the 2048 tile!<html>");
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblNewLabel.setBounds(30, 192, 282, 250); //window dimensions and contents
		f.add(lblNewLabel);
        
        this.columns = cols;
        this.rows = rows;
        originalWidth = 2*X_OFFSET+X_DIM*cols;
        originalHeight = 2*Y_OFFSET+Y_DIM*rows+GAP+FONT_SIZE;

        this.setPreferredSize( new Dimension( originalWidth, originalHeight ) );

        f.setResizable(true);

        this.grid = new ImageIcon[cols][rows];

        this.addMouseListener(
                new MouseInputAdapter()
                {
                    /** A method that is called when the mouse is clicked
                     */
                    public void mouseClicked(MouseEvent e)
                    {
                    int x = (int)e.getPoint().getX();
                    int y = (int)e.getPoint().getY();

                    // We need to by synchronized to the parent class so we can wake
                    // up any threads that might be waiting for us
                    synchronized(Board.this)
                    {
                        int curX = (int)Math.round(X_OFFSET*scale);
                        int curY = (int)Math.round(Y_OFFSET*scale);
                        int nextX = (int)Math.round((X_OFFSET+X_DIM*grid.length)*scale);
                        int nextY = (int)Math.round((Y_OFFSET+Y_DIM*grid[0].length)*scale);

                        // Subtract one from high end so clicks on the black edge
                        // don't yield a row or column outside of board because of
                        // the way the coordinate is calculated.
                        if (x >= curX && y >= curY && x < nextX && y < nextY)
                        {
                            lastClick = new Coordinate(y,x);
                            // Notify any threads that would be waiting for a mouse click
                            Board.this.notifyAll() ;
                        } /* if */
                    } /* synchronized */
                } /* mouseClicked */
            } /* anonymous MouseInputAdapater */
        );
        
        
        
        // PUT ALL OF THE IMAGES OF THE PIECES INTO AN ARRAY ===========================
        PIECES [0] = new ImageIcon("2glow.gif");
        PIECES [1] = new ImageIcon("4glow.gif");
        PIECES [2] = new ImageIcon("2.gif");
        PIECES [3] = new ImageIcon("4.gif");
        PIECES [4] = new ImageIcon("8.gif");
        PIECES [5] = new ImageIcon("16.gif");
        PIECES [6] = new ImageIcon("32.gif");
        PIECES [7] = new ImageIcon("64.gif");
        PIECES [8] = new ImageIcon("128.gif");
        PIECES [9] = new ImageIcon("256.gif");
        PIECES [10] = new ImageIcon("512.gif");
        PIECES [11] = new ImageIcon("1024.gif");
        PIECES [12] = new ImageIcon("2048.gif");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setContentPane( this );
        f.pack();
        
        f.setBackground(new Color(187,173,161)); //set the background color
        f.setVisible(true);
    }
    /** Sets the appearance of the texts
    */
    private void paintText(Graphics g)
    {
    	
    	//Title 2048 text
    	g.setColor( new Color(237, 200, 76) );
        g.fillRect(53, 10, 180, 70);
        
    	g.setFont(new Font("Comic Sans MS", Font.BOLD, (int)(Math.round(70*scale))));
        g.setColor (new Color(251,248,241));
        g.drawString("2048", 55, 72);
        
        //Highscore text
        
        
        //Score text
        g.setFont(new Font("Comic Sans MS", Font.BOLD , (int)(Math.round(24*scale))));
        g.setColor( GRID_COLOR_B );
        g.fillRect(270, 10, 180, 70);
        g.setColor( new Color(238, 228, 218) );
        g.drawString("SCORE", 315, 37);
        g.setFont(new Font("Comic Sans MS", Font.BOLD , (int)(Math.round(24*scale))));
        g.setColor( new Color(252,252,252) );
        g.drawString(message, 315, 70);
        
        //Encouragement text
        g.setFont(new Font("Comic Sans MS", Font.PLAIN , (int)(Math.round(18*scale))));
        g.setColor( GRID_COLOR_B );
        g.fillRect(53, 500, 398, 70);
        g.setColor( new Color(114, 110, 99) );
        g.drawString("Join the numbers and get to the 2048 Tile!", 65, 540);
    }
    
    /** 
    * Paints the empty tiles of the grids
    * @param g  
    */
    private void paintGrid(Graphics g) 
    {
        for (int i = 0; i < this.grid.length; i++)
        {
            for (int j = 0; j < this.grid[i].length; j++)
            {
                if ((i%2 == 0 && j%2 != 0) || (i%2 != 0 && j%2 == 0))
                    g.setColor(GRID_COLOR_A);
                else
                    g.setColor(GRID_COLOR_B);
                int curX = (int)Math.round(((X_OFFSET+X_DIM*i)+gap)*scale);
                int curY = (int)Math.round(((Y_OFFSET+Y_DIM*j)+gap)*scale);
                int nextX = (int)Math.round((X_OFFSET+X_DIM*(i+1))*scale);
                int nextY = (int)Math.round((Y_OFFSET+Y_DIM*(j+1))*scale);
                int deltaX = nextX-curX;
                int deltaY = nextY-curY;

                g.fillRect( curX, curY, deltaX, deltaY );
                // PUT THE IMAGE ON THE SQUARE IF ONE IS SUPPOSED TO BE THERE
                ImageIcon curImage = this.grid[i][j];
                if (curImage != null) // Draw pegs if they exist
                {
                    curImage.paintIcon(this, g, curX, curY);
                    //g.setColor(curColour);
                    //g.fillOval(curX+deltaX/4, curY+deltaY/4, deltaX/2, deltaY/2);
                }
            }
        }
        ((Graphics2D) g).setStroke( new BasicStroke(0.5f) );
        
        int curX = (int)Math.round(X_OFFSET*scale);
        int curY = (int)Math.round(Y_OFFSET*scale);
        int nextX = (int)Math.round((X_OFFSET+X_DIM*grid.length)*scale);
        int nextY = (int)Math.round((Y_OFFSET+Y_DIM*grid[0].length)*scale);
        g.drawRect(curX, curY, nextX-curX, nextY-curY);
    }
    /**
    * Fetches image from library
    * @param thePiece String which is the piece's name
    * @return returns the image of the piece that was called unless less none of the requirements are met, which then returns the default piece
    */
    private ImageIcon convertImageIcon( String thePiece )
    {
        for( int i=0; i<PIECE_NAMES.length; i++ ) 
        {
            if( PIECE_NAMES[i].equalsIgnoreCase( thePiece ) )
                return PIECES[i];
        }
        return DEFAULT_PIECE;
    }
    /** This method will save the value of the colour of the peg in a specific
     * spot.  theColour is restricted to
     *   "yellow", "blue", "cyan", "green", "pink", "white", "red", "orange"
     * Otherwise the colour black will be used.
     */
    public void putPeg(String theColour, int row, int col)
    {
        this.grid[col][row] = this.convertImageIcon( theColour );
        this.repaint();
    }

    public void removePeg(int row, int col)
    {
        this.grid[col][row] = null;
        repaint();
    }
    /** Sets the message to be displayed under the board
     */
    public void displayMessage(String theMessage)
    {
        message = theMessage;
        this.repaint();
    }
    
    public void displayTitle(String theMessage)
    {
        title = theMessage;
        this.repaint();
    }
    /** 
    * The method that draws everything
    * @param g Graphics it determines the graphic you want to paint
    */
    public void paintComponent( Graphics g )
    {
        this.setScale();
        this.paintGrid(g);
        this.paintText(g);
    }
    /**
    * sets the scale of the window
    */
    public void setScale()
    {
        double width = (0.0+this.getSize().width) / this.originalWidth;
        double height = (0.0+this.getSize().height) / this.originalHeight;
        this.scale = Math.max( Math.min(width,height), MIN_SCALE );
    }

     /** 
     * Waits for user to click somewhere and then returns the click.
     */
    public Coordinate getClick()
    {
        Coordinate returnedClick = null;
        synchronized(this) {
            lastClick = null;
            while (lastClick == null)
            {
                try {
                    this.wait();
                } catch(Exception e) {
                    // We'll never call Thread.interrupt(), so just consider
                    // this an error.
                    e.printStackTrace();
                    System.exit(-1) ;
                } /* try */
            }
            int col = (int)Math.floor((lastClick.getCol()-X_OFFSET*scale)/X_DIM/scale);
            int row = (int)Math.floor((lastClick.getRow()-Y_OFFSET*scale)/Y_DIM/scale);
            // Put this into a new object to avoid a possible race.
            returnedClick = new Coordinate(row,col);
        }
        return returnedClick; 
    }
}