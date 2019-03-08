import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

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
	    
	    // on fait le panneau des fichiers
	    JPanel foldersPanel = new JPanel();
	    foldersPanel.setLayout(new BoxLayout(foldersPanel, BoxLayout.PAGE_AXIS));
	    foldersPanel.add(folderPanel1);
	    foldersPanel.add(folderPanel2);
	    foldersPanel.setMaximumSize(new Dimension(600, 700));
	    foldersPanel.setMinimumSize(new Dimension(600, 700));

	    // on fait le panneau central
	    JPanel centralPanel = new JPanel();
	    centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.LINE_AXIS));
	    centralPanel.add(foldersPanel);
	    centralPanel.add(resultsPanel);
	    
	    // Le bouton de comparaison commence désactivé
		cmdPanel.button.setEnabled(false);
		cmdPanel.setMaximumSize(new Dimension(1600, 100));
	    
	    // on ajoute nos panneaux à notre fenêtre
	    this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
	    this.getContentPane().add(centralPanel);
	    this.getContentPane().add(cmdPanel);
	    
	    //Et enfin, on rends la fenêtre visible        
	    this.setVisible(true);
	}
	
	public void onChildReady(JPanel child, boolean isChildReady) {
		
		if(child == folderPanel1)
			ready1 = isChildReady;
		
		if(child == folderPanel2)
			ready2 = isChildReady;
		
		cmdPanel.button.setEnabled(ready1 && ready2);	
		
		if(ready1 && ready2) {
			resultsPanel.checkFileLists(folderPanel1.getFileList(), folderPanel2.getFileList());
		}
	}
	
	public void compareFiles() {
		
		//first, we clean up
		resultsPanel.clearFileDifferences();
		
		// open the files to be compared
		String path1 = folderPanel1.getFolderPath();
		String path2 = folderPanel2.getFolderPath();
		ArrayList<String> files1 = new ArrayList<> (resultsPanel.commonFiles1);
		ArrayList<String> files2 = new ArrayList<> (resultsPanel.commonFiles2);
		
		for (int i = 0; i < files1.size(); i++) {
			compareXmlFile(files1.get(i), path1, files2.get(i), path2);
		} 
	}
	
	void compareXmlFile(String file1, String path1, String file2, String path2) {
		try (
				// reading two xml file to compare in Java program 
				FileInputStream fis1 = new FileInputStream(path1 + "/" + file1); 
				FileInputStream fis2 = new FileInputStream(path2 + "/" + file2);
				
				// using BufferedReader for improved performance 
				BufferedReader source = new BufferedReader(new InputStreamReader(fis1)); 
				BufferedReader target = new BufferedReader(new InputStreamReader(fis2));
			) {
			
			// configuring WMLUnit to ignore white spaces
			XMLUnit.setIgnoreWhitespace(true);
			
			// comparing two XML using XMLUnit
			List<Difference> differences = compareXML(source, target);
			
			// showing differences found in two xml files
			resultsPanel.printDifferences(differences, file1);
			
		} catch (SAXException | IOException e) {
			e.printStackTrace();
			resultsPanel.addDifference(file1, file2);
		}
	}
	
	public List<Difference> compareXML(Reader source, Reader target) throws SAXException, IOException{ 
		
		//creating Diff instance to compare two XML files 
		Diff xmlDiff = new Diff(source, target); 
		
		//for getting detailed differences between two xml files 
		DetailedDiff detailXmlDiff = new DetailedDiff(xmlDiff); 
		
		//obtain the raw list of differences
		List<Difference> differences =  detailXmlDiff.getAllDifferences();
		
		List<String> ignoredPaths = readIgnoredPaths();
		
		//remove ignored differences
		ignoreFalseDifferences(differences, ignoredPaths);
		
		return differences;
	}
	
	private void ignoreFalseDifferences(List<Difference> differences, List<String> ignoredPaths) {		
		List<Difference> toRemove = new ArrayList<>();
		
		//for every difference found
		for(Difference difference : differences) {
			
			//get the difference path
			String balise;
			if(difference.getControlNodeDetail().getXpathLocation() != null){
				balise = difference.getControlNodeDetail().getXpathLocation();
			} else {
				balise = difference.getTestNodeDetail().getXpathLocation();
			}
			
			//if the difference path belongs to the ignored paths
			if(ignoredPaths.contains(balise)) {
				//we mark to difference for removal
				toRemove.add(difference);
			}
		}
		
		// remove all the marked differences
		for (Difference removedDif : toRemove) {
			differences.remove(removedDif);
		}
	}
	
	public List<String> readIgnoredPaths() {
		List<String> ignoredPaths = new ArrayList<>();
		String fileName = "IgnoredPaths.txt";
		File file = new File(fileName);
		
		try (
				// reading two xml file to compare in Java program 
				FileInputStream fis = new FileInputStream(file);
				
				// using BufferedReader for improved performance 
				BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			) {
			
			// add each line of the file as a new string to the list
			String newLine = reader.readLine();
			while (newLine != null) {
				ignoredPaths.add(newLine);
				newLine = reader.readLine();
			}
			
		} catch (FileNotFoundException e) {
			
			// if the file hasn't been found we create it
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// return the list
		return ignoredPaths;
	}
}
