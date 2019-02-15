import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class FolderPanel extends JPanel 
implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// header components
	JButton button = new JButton("Ouvrir un dossier");
	JLabel folderPath = new JLabel("Pas de dossier choisi");
	JFileChooser chooser = new JFileChooser();
	
	// list components
	JScrollPane listPane;
	DefaultListModel<String> listData = new DefaultListModel<String>();
	boolean isReady = false;
	
	public FolderPanel() {
		
		// On remplis l'entête du panneau
		JPanel header = new JPanel();
		header.setLayout(new BoxLayout(header, BoxLayout.LINE_AXIS));
		header.add(folderPath);
		header.add(button);
		
		// On branche le bouton à une action
		button.addActionListener(this);
		
		// on initialise le menu de selection du dossier
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Veuillez selectionner un dossier");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		
		// on initialise la liste
		JList<String> list = new JList<String>(listData);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		listPane = new JScrollPane(list);
//		listPane.setPreferredSize(new Dimension(600, 350));
		
		// On remplis notre panneau
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(header);
		this.add(listPane);
	}
	
	// Mise à jour de la liste de fichiers à partir du chemin de dossier obtenu
	public void UpdateList() {
		listData.clear();
		isReady = false;
		
		String[] fileList = this.getFileList();
		if(fileList != null) {
			for(int i = 0; i < fileList.length; i++) {
				listData.addElement(fileList[i]);
				isReady = true;
			}
		}
		
		this.OnChildReady(this, isReady);
	}
	
	// On écoute les actions du panneau (ici il n'y a que le bouton donc on a pas besoin de vérifier l'origine)
	public void actionPerformed(ActionEvent e) {
		if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			folderPath.setText(chooser.getSelectedFile().toString());
			this.UpdateList();
		}
	}

	// Accesseur pour transmettre la liste des fichiers dans le dossier selectionné
	public String[] getFileList() {
		File f = new File(folderPath.getText());		
		return f.list();
	}

	// Accesseur pour transmettre le chemin du dossier selectionné
	public String getFolderPath() {	
		return folderPath.getText();
	}
	
	public void OnChildReady(JPanel child, boolean isChildReady) {		
		Window window = (Window) SwingUtilities.windowForComponent(this);
		window.OnChildReady(this, isChildReady);
	}

}
