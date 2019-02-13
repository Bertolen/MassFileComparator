import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.xml.sax.SAXException;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;

public class Window extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FolderPanel folderPanel1 = new FolderPanel();
	private FolderPanel folderPanel2 = new FolderPanel();
	private ResultsPanel resultsPanel = new ResultsPanel();
	private CommandsPanel cmdPanel = new CommandsPanel();

	private boolean ready1 = false;
	private boolean ready2 = false;
	
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
	    
	    // Le bouton de comparaison commence désactivé
		cmdPanel.button.setEnabled(false);
	    
	    // on ajoute nos panneaux à notre fenêtre
	    this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
	    this.getContentPane().add(northPan);
	    this.getContentPane().add(cmdPanel);
	    
	    //Et enfin, on rends la fenêtre visible        
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
		
		String path1 = folderPanel1.getFolderPath();
		String path2 = folderPanel2.getFolderPath();
		Vector<String> files = (Vector<String>) resultsPanel.commonFiles.clone();
		
		for (String file : files) {
			CompareXmlFile(file, path1, path2);
		} 
	}
	
	void CompareXmlFile(String file, String path1, String path2) {
		try {
			// reading two xml file to compare in Java program 
			System.out.println("fichier 1 : " + path1 + "/" + file);
			System.out.println("fichier 2 : " + path2 + "/" + file);
			FileInputStream fis1 = new FileInputStream(path1 + "/" + file); 
			FileInputStream fis2 = new FileInputStream(path2 + "/" + file);
			
			// using BufferedReader for improved performance 
			BufferedReader source = new BufferedReader(new InputStreamReader(fis1)); 
			BufferedReader target = new BufferedReader(new InputStreamReader(fis2));
			
			// configuring WMLUnit to ignore white spaces
			XMLUnit.setIgnoreWhitespace(true);
			
			// comparing two XML using XMLUnit
			List<Difference> differences = compareXML(source, target);
			
			// showing differences found in two xml files
			printDifferences(differences);
			
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static List<Difference> compareXML(Reader source, Reader target) throws SAXException, IOException{ 
		
		//creating Diff instance to compare two XML files 
		Diff xmlDiff = new Diff(source, target); 
		
		//for getting detailed differences between two xml files 
		DetailedDiff detailXmlDiff = new DetailedDiff(xmlDiff); 
		
		return detailXmlDiff.getAllDifferences(); 
	} 
	
	public static void printDifferences(List<Difference> differences){ 
		
		int totalDifferences = differences.size(); 
		System.out.println("==============================="); 
		System.out.println("Total differences : " + totalDifferences); 
		System.out.println("================================"); 
		
		for(Difference difference : differences){ 
			System.out.println(difference); 
			System.out.println(difference.getControlNodeDetail().getValue());
			System.out.println(difference.getControlNodeDetail().getXpathLocation()); 
		} 
	}
}
