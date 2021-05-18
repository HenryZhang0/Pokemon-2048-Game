import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;

public class Main {
	public static Board board; //The main gameboard
    public static int tile[][] = new int[4][4]; //Array to store the tiles
    public static boolean win = false; //Boolean for if the player won
    public static Random rand = new Random(); //Random for generating the new tiles
    public static Menu frame; //The menu object
    public static EndScreen lose; //The endscreen object
    public static boolean moved = false; //Boolean for if a tile moved (to generate a new tile)
    public static int score = 0; //score counter
    public static Music mainsong = new Music(); //Main music object
    public static Endmusic endsong = new Endmusic(); //Object for ending song
    public static int sound = 0; //Counter for the alternating sound effects
    public static boolean ended = false; //Boolean for if game ended already
    /** Initializer, initializes the game
     * Generates two new tiles
     * Initializes the board
     * Plays music
     */
    public static void initialize() {
    	frame.dispose(); //closes the menu
    	board = new Board(4, 4); //initializes the board
        rng(); //generates new tiles
        rng();
    	board.displayTitle(Integer.toString(score)); //shows score
        mainsong.play(); //plays song
    }
    /** Main method
     * The main game controls live here
     */
    public static void main(String[] args) 
    {
    	frame = new Menu (); //instantiates the menu
        frame.setVisible(true);
        while (!frame.start) //prevents keypresses from starting before menu screen stops
        {
            try {
                Thread.sleep(1); //sleeps the processor
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Keylistener for the arrow keys to control the game
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                synchronized (IsKeyPressed.class) {
                    switch (ke.getID()) {
                        case KeyEvent.KEY_PRESSED:
                            if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
                                moveLeft(); //moves tiles to the left
                                refresh(); //redraws the tiles
                            }
                            else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                                moveRight(); //moves tiles to the right
                                refresh(); //redraws the tiles
                            }
                            else if (ke.getKeyCode() == KeyEvent.VK_UP) {
                                moveUp(); //Moves the tiles up
                                refresh(); //redraws the tiles
                            }
                            else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
                                moveDown(); //moves the tiles down
                                refresh(); //redraws the tiles
                            }
                            else if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                                gameover(); //ends the game if player presses escape
                            }
                            break;
                        case KeyEvent.KEY_RELEASED: //generates new tile when key is released
                            if(moved)
                        		rng(); //generates new tile if moved
                            moved = false; //resets moved variable
                            break;
                        default:
                        	System.out.println("KEYBOARD ERROR!");
                        	break;
                    }
                    return false;
                }
            }
        });
    }
    /** Moves all tiles as far left as possible
     *  Combines tiles that are the same value into
     *  a new combined tile
     */
    public static void moveLeft() {
        boolean space = true; //space means there is more the tile can move
        while (space) {
            space = false; 
            for (int i = 0; i < 4; i++) { //loops through the 2d array
                for (int j = 1; j < 4; j++) {
                    if (tile[i][j - 1] == 0 && tile[i][j] != 0) { //moves the tiles to the left
                        tile[i][j - 1] = tile[i][j]; //sets left tile to the right tile
                        tile[i][j] = 0; //removes right tile
                        moved = true; //tells the program that a piece moved
                        space = true; //tells program that there might be more space left
                    }
                    else if (tile[i][j] != 0 && tile[i][j - 1] == tile[i][j]) { //combines two of the same tiles together
                        tile[i][j - 1] = tile[i][j - 1] + tile[i][j]; //sets left tile to addition of two right tiles
                        score += tile[i][j - 1] + tile[i][j]; //adds to the score
                        tile[i][j] = 0; //removes the right tile
                        moved = true; //tells the program that a piece moved
                        sfx(sound%3); //plays sound effect
                        sound++;
                    }
                }
            }
        }
    }
    /** Moves all tiles as far right as possible
     *  Combines tiles that are the same value into
     *  a new combined tile
     */
    public static void moveRight() {
        boolean space = true;
        while (space) {
            space = false;
            for (int i = 0; i < 4; i++) {
                for (int j = 2; j >= 0; j--) {
                    if (tile[i][j + 1] == 0 && tile[i][j] != 0) {
                        tile[i][j + 1] = tile[i][j];
                        tile[i][j] = 0;
                        moved = true;
                        space = true;
                    }
                    if (tile[i][j] != 0 && tile[i][j + 1] == tile[i][j]) {
                        tile[i][j + 1] = tile[i][j + 1] + tile[i][j];
                        score += tile[i][j + 1] + tile[i][j];
                        tile[i][j] = 0;
                        moved = true;
                        sfx(sound%3);
                        sound++;
                    }
                }
            }
        }
    }
    /** Moves all tiles as far up as possible
     *  Combines tiles that are the same value into
     *  a new combined tile
     */
    public static void moveUp() {
        boolean space = true;
        while (space) {
            space = false;
            for (int j = 0; j < 4; j++) {
                for (int i = 1; i < 4; i++) {
                    if (tile[i-1][j] == 0 && tile[i][j] != 0) {
                        tile[i-1][j] = tile[i][j];
                        tile[i][j] = 0;
                        space = true;
                        moved = true;
                    }
                    if (tile[i][j] != 0 && tile[i-1][j] == tile[i][j]) {
                        tile[i-1][j] = tile[i-1][j] + tile[i][j];
                        score += tile[i-1][j] + tile[i][j];
                        tile[i][j] = 0;
                        moved = true;
                        sfx(sound%3);
                        sound++;
                    }
                }
            }
        }
    }
    /** Moves all tiles as far down as possible
     *  Combines tiles that are the same value into
     *  a new combined tile
     */
    public static void moveDown() {
        boolean space = true;
        while (space) {
            space = false;
            for (int j = 0; j < 4; j++) {
                for (int i = 2; i >= 0; i--) {
                    if (tile[i+1][j] == 0 && tile[i][j] != 0) {
                        tile[i+1][j] = tile[i][j];
                        tile[i][j] = 0;
                        space = true;
                        moved = true;
                    }
                    if (tile[i][j] != 0 && tile[i+1][j] == tile[i][j]) {
                        tile[i+1][j] = tile[i+1][j] + tile[i][j];
                        score += tile[i+1][j] + tile[i][j];
                        tile[i][j] = 0;
                        moved = true;
                        sfx(sound%3);
                        sound++;
                    }
                }
            }
        }
    }
    /** Generates a new tile on the grid
     * Puts tile in an empty spot
     */
    public static void rng() {
        boolean empty = true;
        while (empty) {
            int num = rand.nextInt(4);
            int num2 = rand.nextInt(4);
            int number = rand.nextInt(4);
            if (tile[num][num2] == 0) {
                empty = false;
                if (number == 0 || number == 1 || number == 2) {
                    tile[num][num2] = 2;
                    board.putPeg("2glow", num, num2);
                    //System.out.println("generated 2");
                } else {
                    tile[num][num2] = 4;
                    board.putPeg("4glow", num, num2);
                    //System.out.println("generated 4");
                }
            }
        }
    }
    /** Refreshes the board and
     *  Draws all the tiles
     *  Loops through the array and
     *  Puts images/icons
     */
    public static void refresh() 
    {
    	board.displayMessage(Integer.toString(score)); //Draws the score
    	boolean dead = true; //boolean for if adjacent tiles are combinable
    	boolean full = true; //boolean for if the board is full
    	//loops through tiles array
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) { 
            	
            //Checks if tiles are full and adjacent tiles can't be combined
            	if(tile[i][j]==0)
            		full = false;
            	if(i!=0 && tile[i][j] == tile[i-1][j])
            		dead = false;
            	if(i!=3 && tile[i][j] == tile[i+1][j])
            		dead = false;
            	if(j!=0 && tile[i][j] == tile[i][j-1])
            		dead = false;
            	if(j!=3 && tile[i][j] == tile[i][j+1])
            		dead = false;
            	
            //Checks if you win (if a tile is 2048) 
            	if(tile[i][j] == 2048)
            		win = true;
            	
            //drawing the tiles
                switch (tile[i][j]) {
                    case 0:
                        board.removePeg(i, j); //Removes the tile if it is zero
                        break;
                    case 2:
                        board.putPeg("number2", i, j);
                        break;
                    case 4:
                        board.putPeg("number4", i, j);
                        break;
                    case 8:
                        board.putPeg("number8", i, j);
                        break;
                    case 16:
                        board.putPeg("number16", i, j);
                        break;
                    case 32:
                        board.putPeg("number32", i, j);
                        break;
                    case 64:
                        board.putPeg("number64", i, j);
                        break;
                    case 128:
                        board.putPeg("number128", i, j);
                        break;
                    case 256:
                        board.putPeg("number256", i, j);
                        break;
                    case 512:
                        board.putPeg("number512", i, j);
                        break;
                    case 1024:
                        board.putPeg("number1024", i, j);
                        break;
                    case 2048:
                        board.putPeg("number2048", i, j);
                        break;
                    default:
                        System.out.println("Draw error " + i + ", " + j);
                }
            }
        }
    	System.out.println((dead&&full)? "dead and full" : "");
    	if(full && dead && !ended) { //Ends the game if the boared is full and no adjacent pieces can combine
    			gameover(); //Calls the endscreen
    			ended = true; //You can only end once
		}
    	if(win == true && !ended) { //Wins the game if player gets 2048
			winning(); //Calls the winscreen
    		ended = true; //You can only end once
		}
    }
    /** Method for gameover screen
     * Generates a new window and 
     * Plays music
     */
    public static void gameover()
    {
        mainsong.stop(); //stops music
        endsong.play(); //plays end music
    	System.out.println("gameover");
    	Board.f.dispose(); //closes the game
        //instantiating the endscreen
    	EndScreen lose = new EndScreen();
        lose.setVisible(true); //sets the screen to visible
    }
    /** Method for win screen
     * Generates a new window and 
     * Plays music
     */
    public static void winning()
    {
        mainsong.stop();
        endsong.play();
    	Board.f.dispose();
    	WinScreen won =  new WinScreen();
    	won.setVisible(true);
    	System.out.println("game win!");
    	
    }
    /** Plays sound effect for combining tiles
     * @param sound the number for which sound you want to play
     */
    public static void sfx(int sound) {
    	AudioInputStream audioInputStream = null;
		try {
			switch (sound) { //Switches the sound effect
			case 0:
				audioInputStream = AudioSystem.getAudioInputStream(new File("sound1.wav").getAbsoluteFile());
				break;
			case 1:
				audioInputStream = AudioSystem.getAudioInputStream(new File("sound2.wav").getAbsoluteFile());
				break;
			case 2:
				audioInputStream = AudioSystem.getAudioInputStream(new File("sound3.wav").getAbsoluteFile());
				break;
			}
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        Clip clip = null;
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}
        try {
			clip.open(audioInputStream);
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        clip.start(); //plays the sound
    }
}
/** Class for the main music
 */
class Music {
    static Clip clip = null;
	Music() 
	{
    	AudioInputStream audioInputStream = null;
    	try {
			audioInputStream = AudioSystem.getAudioInputStream(new File("uprize.wav").getAbsoluteFile()); //fetches the song file
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}
        try {
			clip.open(audioInputStream);
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	/** Plays the music
	 * pre: the music is already playing
	 * post: the music is stopped
	 */
	public static void play() {
    	clip.start();
    	clip.loop(100); //loops the song 100 times
	}
	/** Stops the music
	 * pre: the music is already playing
	 * post: the music is stopped
	 */
	public static void stop() {
		clip.stop();
	}
}
/** Class for the endscreen music
 */
class Endmusic {
    static Clip clip = null;
	Endmusic() 
	{
    	AudioInputStream audioInputStream = null;
    	try {
			audioInputStream = AudioSystem.getAudioInputStream(new File("orangejuice.wav").getAbsoluteFile()); //fetches the song file
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}
        try {
			clip.open(audioInputStream);
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	/** Plays the music
	 * pre: the music is already playing
	 * post: the music is stopped
	 */
	public static void play() {
    	clip.start();	    	
	}
	/** Stops the music
	 * pre: the music is already playing
	 * post: the music is stopped
	 */
	public static void stop() {
		clip.stop();
	}
}