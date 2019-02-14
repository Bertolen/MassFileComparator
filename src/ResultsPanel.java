import java.awt.Color;
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

public class ResultsPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel folderLabel = new JLabel("Folders matching");
	JLabel filesLabel = new JLabel("Files matching");
	JTextArea prompt = new JTextArea();
	JScrollPane folderTablePane;
	JScrollPane filesTablePane;
	Vector<String> commonFiles1 = new Vector<>();
	Vector<String> commonFiles2 = new Vector<>();
	
	private DefaultTableModel folderTableData = new DefaultTableModel(new String[]{"Property","Value"}, 3);
	private DefaultTableModel filesTableData = new DefaultTableModel(new String[]{"Path", "Value 1", "Value 2"}, 0);

	public ResultsPanel() {
		// on initialise le tableau des dossiers
		folderTableData.setValueAt("List 1 size", 0, 0);
		folderTableData.setValueAt("List 2 size", 1, 0);
		folderTableData.setValueAt("Matches", 2, 0);
		JTable folderTable = new JTable(folderTableData);
		folderTablePane = new JScrollPane(folderTable);
	
		// on initialise le tableau des fichiers
		JTable filesTable = new JTable(filesTableData);
		filesTablePane = new JScrollPane(filesTable);
		
		// on remplis le panneau
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(folderLabel);
		this.add(folderTablePane);
		this.add(filesLabel);
		this.add(filesTablePane);
		
		// on finalise le panneau
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setSize(1000, 750);
	}

	public void CheckFileLists(String[] list1, String[] list2) {
		
		//checks how many matches we have
		commonFiles1.clear();
		commonFiles2.clear();
		for(int i = 0 ; i < list1.length; i++) {
			
			for(int j = 0 ; j < list2.length; j++) {
				
				if(list1[i].substring(1).equals(list2[j].substring(1))) {
					commonFiles1.add(list1[i]);
					commonFiles2.add(list2[i]);
				}
			}
		}

		// update the files content
		folderTableData.setValueAt(String.valueOf(list1.length), 0, 1);
		folderTableData.setValueAt(String.valueOf(list2.length), 1, 1);
		folderTableData.setValueAt(String.valueOf(commonFiles1.size()), 2, 1);
	}
	
	public void printDifferences(List<Difference> differences, String fileName){ 
		
		if(differences.size() > 0) {
			Vector<String> row = new Vector<String>();
			row.add(fileName);
			filesTableData.addRow(row);
		}
		
		for(Difference difference : differences){ 
			Vector<String> row = new Vector<String>();
			row.add(difference.getControlNodeDetail().getXpathLocation());
			row.add(difference.getControlNodeDetail().getValue());
			row.add(difference.getTestNodeDetail().getValue());
			filesTableData.addRow(row);
		} 
	}
	
}
