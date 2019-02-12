import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ResultsPanel extends JPanel {
	
	JLabel title = new JLabel("Results");
	JTextArea prompt = new JTextArea();

	public ResultsPanel() {
		// on remplis le panneau
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(title);
		this.add(prompt);
		
		// on finalise le panneau
		this.setBorder(BorderFactory.createLineBorder(Color.black));
//		this.setSize(600, 800);
	}

	public void CheckFileLists(String[] list1, String[] list2) {
		
		//checks how many matches we have
		int matches = 0;
		for(int i = 0 ; i < list1.length; i++) {
			
			for(int j = 0 ; j < list2.length; j++) {
				
				if(list1[i].equals(list2[j])) {					
					matches++;
				}
			}
		}

		System.out.println("list 1 size : " + list1.length);
		System.out.println("list 2 size : " + list2.length);
		System.out.println("matches : " + matches);
		prompt.setText("list 1 size : " + list1.length + "\nlist 2 size : " + list2.length +"\nmatches : " + matches);
	}
	
}
