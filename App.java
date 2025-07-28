package main;

import javax.swing.SwingUtilities;


public class App {
    public static void main(String[] args) throws ClassNotFoundException {
        SwingUtilities.invokeLater(() -> {
            try {
				new InventarioFrame().setVisible(true);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
    }
}
