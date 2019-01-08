package com.lw.swing.utils;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingSource;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import javax.swing.*;
import java.awt.*;

/**
 * @description:
 * @className: IconFont
 * @author: liwen
 * @date: 2018/11/8 18:20
 */
public class IconFont {

    static final int FONT_SIZE = 16;


    public static final Font ICON_FONT;

    static {
        ICON_FONT = FontLoader.loadFont("font/iconfont/Font Awesome 5 Free.ttf");
    }


    public static String getIcon(String icon) {
        return IconEnum.getIcon(icon);
    }


    public static void main(String[] args) {
        TimingSource ts = new SwingTimerTimingSource();
        Animator.setDefaultTimingSource(ts);
        ts.init();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Test");
                frame.setSize(1000, 400);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JPanel centerPanel = new JPanel(new FlowLayout());
                centerPanel.setPreferredSize(new Dimension(800,0));
                centerPanel.setBackground(Color.BLACK);
                int i = 0;

                System.err.println(ICON_FONT.getName());




                for (IconEnum temp : IconEnum.values()) {
                    JPanel panel = new JPanel(new BorderLayout(7, 0));
                    panel.setPreferredSize(new Dimension(200, 40));
                    panel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
                    JLabel icon = new JLabel(String.valueOf(temp.getCharacter()), JLabel.CENTER);
                    icon.setFont(ICON_FONT.deriveFont(25f));
                    JLabel text = new JLabel(temp.getName());
                    panel.add(icon, BorderLayout.WEST);
                    panel.add(text, BorderLayout.CENTER);
                    panel.setBackground(Color.BLUE);
                    centerPanel.add(panel);
                }

                frame.add(new JScrollPane(centerPanel));
                frame.setVisible(true);


            }
        });

    }

}
