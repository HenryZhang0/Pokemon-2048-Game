import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class WinScreen extends JFrame { //class for highscore window

	public JPanel contentPane = contentPane = new JPanel();;
	public JLabel lblTheBestDucking;
	public int diff = 50;
	public JTextField t;
	public static String name;
	public static int score = Main.score;
	public static WinScreen newpage;
	ArrayList<Pair> scores = new ArrayList<Pair>(); //list of all name-score pairs

	class Pair implements Comparable<Pair> //Pair class being used to connect names with scores in order to sort 
    {
        String name;
        int score;
        Pair (String name, int score)
        {
            this.score = score;
            this.name = name;
        }
        public int compareTo(Pair other) //method for sorting 
        {
            return this.score - other.score;
        }
    }
	public WinScreen() {
		//setting up window and contentPane
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 830);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(new Color(237, 197, 63));
		
		lblTheBestDucking = new JLabel(new ImageIcon("win.gif")); //JLabel for logo at the top 
		lblTheBestDucking.setHorizontalAlignment(SwingConstants.CENTER);
		lblTheBestDucking.setFont(new Font("Comic Sans MS", Font.PLAIN, 22));
		lblTheBestDucking.setBounds(30, 27, 476, 224);
		contentPane.add(lblTheBestDucking);
		

		t = new JTextField("enter your name", 16); 
		t.setBounds(30,670,250,40);
		
		contentPane.add(t);

		JButton btnNewButton1 = new JButton("SUBMIT");
		btnNewButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				name = t.getText();
				writeScores();
				newpage = new WinScreen();
				newpage.setVisible(true);
				dispose();
			}
		});
		btnNewButton1.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		btnNewButton1.setBounds(290, 670, 100, 40);
		contentPane.add(btnNewButton1);
		
		
		JButton btnNewButton = new JButton("QUIT");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				dispose();
			}
		});
		btnNewButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		btnNewButton.setBounds(400, 670, 100, 40);
		contentPane.add(btnNewButton);
		
		
		JLabel lblNewLabel = new JLabel("<html><b>SCORE:</B><p><html>");
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblNewLabel.setBounds(30, 192, 282, 250);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel3 = new JLabel(" "+Integer.toString(Main.score));
		lblNewLabel3.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
		lblNewLabel3.setForeground(Color.white);
		lblNewLabel3.setBounds(90, 192, 282, 250);
		contentPane.add(lblNewLabel3);
		
		JLabel lblNewLabel2 = new JLabel("<html><b>HIGHSCORES:</B><p><html>");
		lblNewLabel2.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblNewLabel2.setBounds(30, 252, 282, 250);
		contentPane.add(lblNewLabel2);
		
		
		
		 //reading from the .txt file using FileReader to display high scores
		 
        try {
			 FileReader fileReader = new FileReader("highscores.txt"); //creating filereader object with our txt file

	         BufferedReader br = new BufferedReader(fileReader); //bufferedreader will read from filereader
	         
            while(true) 
            {
            	String line = br.readLine(); //looping through file line by line
            	if (line == null) //stopping loop when end is reached
            	{
            		break;
            	}
                if (line.equals("!diff")) //checking for start of entry
                {
                	if (Integer.parseInt(br.readLine()) == diff) //when entry is found matching player's difficulty
                	{
            			String currentName = br.readLine();
            			int currentScore = Integer.parseInt(br.readLine());
            			scores.add(new Pair(currentName, currentScore)); //creating a name-score pair and adding it to the list of pairs
                	}
                }
            }   
            br.close();         
        }
        catch(FileNotFoundException e) {
        	e.printStackTrace();               
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        Collections.sort(scores); //sorting all pairs in the list
	    int place = 1;
        for (int i = scores.size()-1; i >= Math.max(scores.size()-7, 0); i--) //only displays the top 5 scores for that difficulty
		{
			JLabel lblNewLabel1 = new JLabel(place + ". " +scores.get(i).name + " - " + scores.get(i).score);
			lblNewLabel1.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
			lblNewLabel1.setHorizontalAlignment(SwingConstants.LEFT);
			lblNewLabel1.setBounds(30, 400 + (scores.size() - 1 - i) * 35, 200, 29);
			contentPane.add(lblNewLabel1);
			place++;
		}
	}
	public void writeScores() {
		//writing to the .txt file using FileWriter when score is submitted
		
		 try {      
	            FileWriter fileWriter = new FileWriter("highscores.txt", true); //the boolean tells the class that we want to append to the existing text
	            BufferedWriter bw = new BufferedWriter(fileWriter); //using buffered writer for easy writing to file
	            //score entry 
	            bw.write("!diff"); //first line contains a string used to separate entries from one another, was also helpful in debugging
	            bw.newLine();
	            bw.write(Integer.toString(diff)); //second line is the difficulty 
	            bw.newLine();
	            bw.write(name); //third line is the name submitted
	            bw.newLine();
	            bw.write(Integer.toString(score)); //fourth line is the score they got
	            bw.newLine();
	            bw.close();
	        }
	        catch(IOException e) {
	        	e.printStackTrace();
	        }
	}
}
