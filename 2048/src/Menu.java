import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JSlider;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
//Music player class
class Seagull {
    static Clip clip = null;
    Seagull() 
	{
    	AudioInputStream audioInputStream = null;
    	try {
			audioInputStream = AudioSystem.getAudioInputStream(new File("orangecoffee.wav").getAbsoluteFile());
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
        clip.loop(10);	    	
	}
	/** Stops the music
	 * pre: the music is already playing
	 * post: the music is stopped
	 */
	public static void stop() {
		clip.stop();
	}
}
public class Menu extends JFrame {
    public static Seagull seagul = new Seagull(); //Creates music object
	private JPanel contentPane; //Creates new content pane
	JLabel picture; //Picture
	public static boolean start = false; //Boolean for starting game
	
	public Menu() {
		seagul.play(); //Plays music
		
		//Create the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setBounds(100, 100, 550, 430);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(new Color(237, 197, 63));
		
		//Creates Logo image at the top
		picture = new JLabel(new ImageIcon("logo.jpg")); //JLabel for logo at the top 
		picture.setHorizontalAlignment(SwingConstants.CENTER);
		picture.setFont(new Font("Comic Sans MS", Font.PLAIN, 22));
		picture.setBounds(30, 27, 476, 224);
		contentPane.add(picture);
		
		//Creates button to start the game
		JButton btnNewButton = new JButton("START GAME");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				start = true;
				seagul.stop(); //Stops the music
				Main.initialize(); //runs the initialize method in Main class
			}
		});
		btnNewButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		btnNewButton.setBounds(316, 276, 190, 94);
		contentPane.add(btnNewButton);
		
		//Creates label for the instructions
		JLabel lblNewLabel = new JLabel("<html><b>HOW TO PLAY:</B><p>Use your arrow keys to move the tiles.<p>When two tiles with the same number touch, they merge into one!<p>Join the numbers and get to the 2048 tile!<html>");
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblNewLabel.setBounds(30, 192, 282, 250);
		contentPane.add(lblNewLabel);
	}
}