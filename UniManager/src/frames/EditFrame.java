package frames;

import javax.swing.JFrame;

public class EditFrame extends JFrame {
    public EditFrame(String title){
        this.setTitle(title);
        this.setSize(900, 500);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
