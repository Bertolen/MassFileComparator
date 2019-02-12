import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame{
	private FolderPanel folderPanel1 = new FolderPanel();
	private FolderPanel folderPanel2 = new FolderPanel();
	private ResultsPanel resultsPanel = new ResultsPanel();
	private CommandsPanel cmdPanel = new CommandsPanel();

	private boolean ready1 = false;
	private boolean ready2 = false;
	
	public Window() {
		
		//D�finit un titre pour notre fen�tre
	    this.setTitle("Comparateur de fichiers de masse");
	    //D�finit sa taille : 1600 pixels de large et 800 pixels de haut
	    this.setSize(1600, 800);
	    //Nous demandons maintenant � notre objet de se positionner au centre
	    this.setLocationRelativeTo(null);
	    //Termine le processus lorsqu'on clique sur la croix rouge
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    // on fait l'ent�te de notre fen�tre
	    JPanel northPan = new JPanel();
	    northPan.setLayout(new BoxLayout(northPan, BoxLayout.LINE_AXIS));
	    northPan.add(folderPanel1);
	    northPan.add(folderPanel2);
	    northPan.add(resultsPanel);
	    
	    // Le bouton de comparaison commence d�sactiv�
		cmdPanel.button.setEnabled(false);
	    
	    // on ajoute nos panneaux � notre fen�tre
	    this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
	    this.getContentPane().add(northPan);
	    this.getContentPane().add(cmdPanel);
	    
	    //Et enfin, on rends la fen�tre visible        
	    this.setVisible(true);
	}
	
	public void OnChildReady(JPanel child, boolean isChildReady) {
		
		if(child == folderPanel1)
			ready1 = isChildReady;
		
		if(child == folderPanel2)
			ready2 = isChildReady;
		
		cmdPanel.button.setEnabled(ready1 && ready2);	
		
		if(ready1 && ready2) {
			resultsPanel.CheckFileLists(folderPanel1.getFileList(), folderPanel2.getFileList());
		}
	}
	
	public void CompareFiles() {
		System.out.println("compare files");	
		
	}
}
