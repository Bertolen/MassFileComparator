import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class FolderContentPanel extends JPanel 
	implements ActionListener{
	
	JButton button = new JButton("Update list");
	JScrollPane listPane;
	DefaultListModel listData = new DefaultListModel();
	
	public FolderContentPanel() {
		
		// On remplis notre panneau
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		// init du bouton
		button.addActionListener(this);
		this.add(button);
		
		// init the list;
		JList list = new JList(listData);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		listPane = new JScrollPane(list);
		listPane.setPreferredSize(new Dimension(250,80));
		this.add(listPane);
		
		this.setBackground(Color.GREEN);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	// On écoute les actions du panneau (ici il n'y a que le bouton donc on a pas besoin de vérifier l'origine)
	public void actionPerformed(ActionEvent e) {
				
		listData.clear();
		
		String[] fileList = ((FolderPanel) this.getParent()).getFileList();
		for(int i = 0; i < fileList.length; i++) {
			listData.addElement(fileList[i]);			
		}
	}	

}
