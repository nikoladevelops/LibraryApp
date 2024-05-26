package helper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class ControlHelper {
    public static void addHoverEffect(Component component,
        Color normalBGColor,
        Color hoverBGColor,
        Color normalForeColor,
        Color hoverForeColor
    ){
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                component.setBackground(hoverBGColor);
                component.setForeground(hoverForeColor);
            }

            @Override
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
    
    
    public static JPanel generateInputPanel(InputTextPaneInfo[] textPaneInfos, int panelWidth){
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(textPaneInfos.length, 1));

        // Set up Input Label and TextPane per each InputBtnInfo and add them to the mainPanel
        for (InputTextPaneInfo info : textPaneInfos) {
            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(2,1));

            JLabel lbl = new JLabel(info.labelText);
            JTextPane textPane = info.textPane;

            lbl.setFont(new Font("Serif", Font.PLAIN, 15));
            textPane.setFont(new Font("Serif", Font.PLAIN, 16));
    
            Dimension textPaneSize = new Dimension(panelWidth, 25);
            textPane.setMinimumSize(textPaneSize);
            textPane.setMaximumSize(textPaneSize);
            textPane.setPreferredSize(textPaneSize);

            inputPanel.add(lbl);
            inputPanel.add(textPane);

            mainPanel.add(inputPanel);
        }

        return mainPanel;
    }
}
