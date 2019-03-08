import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CommandsPanel extends JPanel 
	implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton button = new JButton("Compare files");
	
	public CommandsPanel() {
		
		// init du bouton
		button.addActionListener(this);
		this.add(button);
		
		this.setBorder(BorderFactory.createLineBorder(Color.black));
//		this.setPreferredSize(new Dimension(1600, 50));
		
	}
	
	// On écoute les actions du panneau (ici il n'y a que le bouton donc on a pas besoin de vérifier l'origine)
	public void actionPerformed(ActionEvent e) {	
		Window window = (Window) SwingUtilities.windowForComponent(this);
		window.compareFiles();
	}

}
