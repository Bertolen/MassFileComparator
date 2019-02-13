import java.awt.Color;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class ResultsPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel folderLabel = new JLabel("Folders matching");
	JTextArea prompt = new JTextArea();
	JScrollPane tablePane;
	Vector<String> commonFiles = new Vector<String>();
	
	private DefaultTableModel tableData = new DefaultTableModel(new String[]{"Property","Value"}, 3);

	public ResultsPanel() {
		// on initialise le tableau
		tableData.setValueAt("List 1 size", 0, 0);
		tableData.setValueAt("List 2 size", 1, 0);
		tableData.setValueAt("Matches", 2, 0);
		JTable table = new JTable(tableData);
		tablePane = new JScrollPane(table);
		
		// on remplis le panneau
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(folderLabel);
		this.add(tablePane);
		
		// on finalise le panneau
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setSize(400, 750);
	}

	public void CheckFileLists(String[] list1, String[] list2) {
		
		//checks how many matches we have
		commonFiles.clear();
		for(int i = 0 ; i < list1.length; i++) {
			
			for(int j = 0 ; j < list2.length; j++) {
				
				if(list1[i].equals(list2[j])) {
					commonFiles.add(list1[i]);
				}
			}
		}

		// update the files content
		tableData.setValueAt(String.valueOf(list1.length), 0, 1);
		tableData.setValueAt(String.valueOf(list2.length), 1, 1);
		tableData.setValueAt(String.valueOf(commonFiles.size()), 2, 1);
	}
	
}
