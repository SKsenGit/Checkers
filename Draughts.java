
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;





public class Draughts implements ActionListener {
	JPanel panelContainer;
	JRadioButton oneGamer;
	JButton startButton;
	public Draughts() {
		
		JFrame mainWindow = new JFrame("Draughts");
		mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainWindow.setSize(800,600);
		
		
		mainWindow.setLayout(new BorderLayout());
		mainWindow.setLocationRelativeTo(null);
		
		panelContainer = new JPanel();
		JPanel contentPane = new JPanel(new BorderLayout());
		JPanel choiceWindow = new JPanel(new FlowLayout());
		
		startButton = new JButton("Start the game");
		startButton.addActionListener(this);
		
		
		oneGamer = new JRadioButton("One gamer", true);
	    JRadioButton twoGamers = new JRadioButton("Two gamers");
	    //oneGamer.setMnemonic(KeyEvent.KEY_FIRST);
	    //twoGamers.setMnemonic(KeyEvent.KEY_LAST);
	    ButtonGroup group = new ButtonGroup();
	    group.add(oneGamer);
	    group.add(twoGamers);
	    
	    choiceWindow.add(oneGamer);
	    choiceWindow.add(twoGamers);
		choiceWindow.add(startButton);
		
		panelContainer.setLayout(new BorderLayout());
		panelContainer.add(choiceWindow,BorderLayout.CENTER);
		contentPane.add(panelContainer,BorderLayout.CENTER);
		
		mainWindow.setContentPane(contentPane);
		mainWindow.setVisible(true);
		System.out.println("Конец игры");
	}
	public static void main(String[] args) {
		
		new Draughts();

	}
	
	
	@Override
    public void actionPerformed(ActionEvent e) {
	 //System.out.println(oneGamer.isSelected());
	 if(e.getSource() == startButton) {
		 panelContainer.removeAll();
		 		 
	 	Board game = new Board(oneGamer.isSelected());
	 	panelContainer.setSize(0,0);
	 	panelContainer.setSize(game.getSize());
	 	panelContainer.add(game);
	 	panelContainer.revalidate();
		
	 }
	 
 }

}
