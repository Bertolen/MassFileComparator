import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame{
	private FolderPanel folderPanel1 = new FolderPanel();
	private FolderPanel folderPanel2 = new FolderPanel();
	private ResultsPanel resultsPanel = new ResultsPanel();
	private CommandsPanel cmdPanel = new CommandsPanel();
	
	public Window() {
		
		//Définit un titre pour notre fenêtre
	    this.setTitle("Comparateur de fichiers de masse");
	    //Définit sa taille : 1600 pixels de large et 800 pixels de haut
	    this.setSize(1600, 800);
	    //Nous demandons maintenant à notre objet de se positionner au centre
	    this.setLocationRelativeTo(null);
	    //Termine le processus lorsqu'on clique sur la croix rouge
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    // on fait l'entête de notre fenêtre
	    JPanel northPan = new JPanel();
	    northPan.setLayout(new BoxLayout(northPan, BoxLayout.LINE_AXIS));
	    northPan.add(folderPanel1);
	    northPan.add(folderPanel2);
	    northPan.add(resultsPanel);
	    
	    // on ajoute nos panneaux à notre fenêtre
	    this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
	    this.getContentPane().add(northPan);
	    this.getContentPane().add(cmdPanel);
	    
	    //Et enfin, on rends la fenêtre visible        
	    this.setVisible(true);
	}
}
