import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NameEntry extends JFrame {

	private JPanel contentPane;
	JLabel lblTheBestDucking;
	public int diff = 50;
	public static boolean start = false;
	public boolean mario = false;
	public JTextField t;
	public static String name;
	//static JFrame menu;
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {

					Menu frame = new Menu();
					menu = frame;
					frame.setVisible(true);
			}*/


	/**
	 * Create the frame.
	 */
	public NameEntry() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 430);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(new Color(237, 197, 63));
		
		lblTheBestDucking = new JLabel(new ImageIcon("lose.gif")); //JLabel for logo at the top 
		lblTheBestDucking.setHorizontalAlignment(SwingConstants.CENTER);
		lblTheBestDucking.setFont(new Font("Comic Sans MS", Font.PLAIN, 22));
		lblTheBestDucking.setBounds(30, 27, 476, 224);
		contentPane.add(lblTheBestDucking);
		
		t = new JTextField("enter your name", 16); 
		t.setBounds(30,270,250,40);
		
		contentPane.add(t);

		JButton btnNewButton1 = new JButton("SUBMIT");
		btnNewButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				name = t.getText();
				//Main.lose.dispose();
			}
		});
		btnNewButton1.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		btnNewButton1.setBounds(290, 270, 100, 40);
		contentPane.add(btnNewButton1);
		
		
		JButton btnNewButton = new JButton("QUIT");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				Main.lose.dispose();
			}
		});
		btnNewButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		btnNewButton.setBounds(400, 270, 100, 40);
		contentPane.add(btnNewButton);
		
		
		JLabel lblNewLabel = new JLabel("<html><b>SCORE:</B><p><html>");
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblNewLabel.setBounds(30, 200, 282, 250);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel3 = new JLabel(" "+Integer.toString(Main.score));
		lblNewLabel3.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
		lblNewLabel3.setForeground(Color.white);
		lblNewLabel3.setBounds(90, 200, 282, 250);
		contentPane.add(lblNewLabel3);
		
	}
}