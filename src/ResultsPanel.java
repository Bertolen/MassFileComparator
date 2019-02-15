import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import org.custommonkey.xmlunit.Difference;

import org.apache.commons.lang3.StringUtils;

public class ResultsPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel folderLabel = new JLabel("Résumé");
	JLabel filesLabel = new JLabel("Différences");
	JTextArea prompt = new JTextArea();
	JScrollPane filesTableScroll;
	JPanel filesTablePane;
	Vector<String> commonFiles1 = new Vector<>();
	Vector<String> commonFiles2 = new Vector<>();
	
	
	private int identicalFiles = 0;
	private int differentFiles = 0;
	private int errorFiles = 0;
	private String[] folderTableHeaders = new String[]{"Propriété", "Valeur"};
	private String[] fileTableHeaders = new String[]{"Balise", "Valeur 1", "Valeur 2"};
	private DefaultTableModel folderTableData = new DefaultTableModel(folderTableHeaders, 6);

	public ResultsPanel() {
		
		// on initialise le tableau des dossiers
		folderTableData.setValueAt("Nombre de fichiers dans le dossier 1", 0, 0);
		folderTableData.setValueAt("Nombre de fichiers dans le dossier 2", 1, 0);
		folderTableData.setValueAt("Nombre de fichiers rapprochés", 2, 0);
		folderTableData.setValueAt("Nombre de fichiers identiques", 3, 0);
		folderTableData.setValueAt("Nombre de fichiers différents", 4, 0);
		folderTableData.setValueAt("Nombre de fichiers en erreur", 5, 0);
		JTable folderTable = new JTable(folderTableData);
		folderTable.setMaximumSize(new Dimension(1500, 150));
	
		// on initialise le panneau contenant les différences de fichiers
		filesTablePane = new JPanel();
		filesTablePane.setLayout(new BoxLayout(filesTablePane, BoxLayout.PAGE_AXIS));
		filesTableScroll = new JScrollPane(filesTablePane);
		
		// on remplis le panneau
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(folderLabel);
		this.add(folderTable);
		this.add(filesLabel);
		this.add(filesTableScroll);
		
		// on finalise le panneau
		this.setBorder(BorderFactory.createLineBorder(Color.black));
	}

	public void CheckFileLists(String[] list1, String[] list2) {
		
		//checks how many matches we have
		commonFiles1.clear();
		commonFiles2.clear();
		for(int i = 0 ; i < list1.length; i++) {
			
			for(int j = 0 ; j < list2.length; j++) {
				
//				if(list1[i].substring(1).equals(list2[j].substring(1))) {
					if(RootName(list1[i]).equals(RootName(list2[j]))) {
					commonFiles1.add(list1[i]);
					commonFiles2.add(list2[j]);
				}
			}
		}

		// update the files content
		folderTableData.setValueAt(String.valueOf(list1.length), 0, 1);
		folderTableData.setValueAt(String.valueOf(list2.length), 1, 1);
		folderTableData.setValueAt(String.valueOf(commonFiles1.size()), 2, 1);
		identicalFiles = 0;
		differentFiles = 0;
		errorFiles = 0;
		folderTableData.setValueAt(identicalFiles, 3, 1);
		folderTableData.setValueAt(differentFiles, 4, 1);
		folderTableData.setValueAt(errorFiles, 5, 1);
	}
	
	private String RootName(String name) {
		String rootName = new String();
		String[] nameComponents = name.split("_");
		boolean firstNumber = true;
		boolean concat = false;
		
		for (String component : nameComponents) {
			
			// by default we add everything to our output
			concat = true;
			
			// when we get a number 
			/*if(StringUtils.isNumeric(component)) {
				
				// if it is the first number we append it to our string
				if(firstNumber){
					firstNumber = false;					
				} else { // if not we don't append it to our string
					concat = false;
				}
			}*/
			
			// if the component to XPG or WPG we won't add it
			if(component.equals("XPG") || component.equals("WPG")) {
				concat = false;
			}
			
			// if the component is still set to be added, it's here that we do it
			if(concat) {
				rootName = rootName.concat(component);
				rootName = rootName.concat("_");
			}
		}
		
		System.out.println("raw name = " + name);
		System.out.println("root name = " + rootName);
		
		return rootName;
	}
	
	public void printDifferences(List<Difference> differences, String fileName){ 

		if(!differences.isEmpty()) {

			// Update the summary
			differentFiles++;
			folderTableData.setValueAt(differentFiles, 4, 1);
			
			// Prepare the new table to be displayed
			DefaultTableModel filesTableData = new DefaultTableModel(fileTableHeaders, 0);
			filesTablePane.add(new JLabel(fileName));
			filesTablePane.add(new JTable(filesTableData));
			
			// fills the table
			for(Difference difference : differences){ 
				Vector<String> row = new Vector<String>();
				
				String balise;
				if(difference.getControlNodeDetail().getXpathLocation() == null){
					balise = difference.getTestNodeDetail().getXpathLocation();
				} else {
					balise = difference.getControlNodeDetail().getXpathLocation();
				}
				
				row.add(balise);
				row.add(difference.getControlNodeDetail().getValue());
				row.add(difference.getTestNodeDetail().getValue());
				filesTableData.addRow(row);
			}
		} else {
			// update the summary
			identicalFiles++;
			folderTableData.setValueAt(identicalFiles, 3, 1);
		}
	}
	
	public void clearFileDifferences() {
		filesTablePane.removeAll();
		filesTablePane.validate();
		
		identicalFiles = 0;
		differentFiles = 0;
		errorFiles = 0;
		folderTableData.setValueAt(identicalFiles, 3, 1);
		folderTableData.setValueAt(differentFiles, 4, 1);
		folderTableData.setValueAt(errorFiles, 5, 1);
	}
	
	public void addDifference(String file1, String file2) {

		// Update the summary
		errorFiles++;
		folderTableData.setValueAt(errorFiles, 5, 1);
		
		DefaultTableModel filesTableData = new DefaultTableModel(new String[]{"fichier1","fichier2"}, 0);
		filesTablePane.add(new JLabel("XML Error"));
		filesTablePane.add(new JTable(filesTableData));
		Vector<String> row = new Vector<>();
		row.add(file1);
		row.add(file2);
		filesTableData.addRow(row);
	}
	
}
