package helper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

import javafx.scene.text.Font;

public class ControlHelper {
    public static void addHoverEffect(Component component,
        Color normalBGColor,
        Color hoverBGColor,
        Color normalForeColor,
        Color hoverForeColor
    ){
        component.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                component.setBackground(hoverBGColor);
                component.setForeground(hoverForeColor);
            }

            public void mouseExited(MouseEvent e) {
                component.setBackground(normalBGColor);
                component.setForeground(normalForeColor);
            }
        });
    }

    public static JLabel generateLabel(String lblText, int fontSize, Color foreColor){
        JLabel lbl = new JLabel(lblText);
        lbl.setFont(new Font("Serif", Font.PLAIN, fontSize));
        lbl.setForeground(foreColor);
        return lbl;
    }

    public static JButton generateButton(
            String btnText,
            Dimension btnSize,
            int btnFontSize,
            Color btnNormalColor,
            Color btnForeColor,
            Color btnBorderColor
        ){
        JButton btn = new JButton(btnText);
        btn.setPreferredSize(btnSize);
        btn.setFont(new Font("Serif", Font.PLAIN, btnFontSize));
        btn.setBackground(btnNormalColor);
        btn.setForeground(btnForeColor);
        btn.setBorder(BorderFactory.createLineBorder(btnBorderColor));

        return btn;
    }

    public static void editButtonStyle(
        JButton btn,
        Dimension btnSize,
        int btnFontSize,
        Color btnNormalColor,
        Color btnForeColor,
        Color btnBorderColor 
        ){
            btn.setPreferredSize(btnSize);
            btn.setFont(new Font("Serif", Font.PLAIN, btnFontSize));
            btn.setBackground(btnNormalColor);
            btn.setForeground(btnForeColor);
            btn.setBorder(BorderFactory.createLineBorder(btnBorderColor));
        }
}
