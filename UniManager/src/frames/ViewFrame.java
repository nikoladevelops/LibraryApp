package frames;

import db.utility.DbInfo;
import java.awt.FlowLayout;
import javafx.scene.paint.Color;

import javax.swing.*;

public class ViewFrame extends JFrame {
    protected DbInfo dbInfo;
    public ViewFrame(String frameTitle, DbInfo dbInfo){
       
        this.setTitle(frameTitle);
        this.setSize(1000, 500);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setLayout(new FlowLayout());
        this.dbInfo = dbInfo;
    }
}
