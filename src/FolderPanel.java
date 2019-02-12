import java.awt.LayoutManager;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class FolderPanel extends JPanel {
	private FolderSelectPanel selectPane = new FolderSelectPanel();
	private FolderContentPanel contentPane = new FolderContentPanel();
	
	public FolderPanel() {
		
		// On remplis notre panneau
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(selectPane);
		this.add(contentPane);
	}

	// Accesseur pour transmettre la liste des fichiers dans le dossier selectionné
	public String[] getFileList() {
		return selectPane.getFileList();
	}

}
