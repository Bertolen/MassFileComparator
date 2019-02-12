import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

public class FolderSelectPanel extends JPanel 
	implements ActionListener {
	
	JButton button = new JButton("Choose folder");
	JLabel folderPath = new JLabel("No folder selected");
	JFileChooser chooser = new JFileChooser();
	
	public FolderSelectPanel() {
		
		// On remplis notre panneau
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(folderPath);
		this.add(button);
		
		// On branche le bouton à une action
		button.addActionListener(this);
		
		// on initialise le menu de selection du dossier
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Please select a file to compare");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		
		// finitions du panneau
		this.setSize(400, 100);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	// On écoute les actions du panneau (ici il n'y a que le bouton donc on a pas besoin de vérifier l'origine)
	public void actionPerformed(ActionEvent e) {
		
		if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			
			folderPath.setText(chooser.getSelectedFile().toString());
			
		} else {
			
			folderPath.setText("No folder selected");
			
		}
	}
	
	// Accesseur pour transmettre la liste des fichiers dans le dossier selectionné
	public String[] getFileList() {
		File f = new File(folderPath.getText());
		
		return f.list();
		
	}

}
