package com.lw.swing.demo;

import com.lw.swing.ui.TransitionManager;
import com.lw.swing.ui.WIconButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * @description:
 * @className: NotificationPanel
 * @author: liwen
 * @date: 2019/1/14 15:36
 */
public class NotificationPanel extends JComponent {


    public NotificationPanel() {

        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        JComponent p1 = new JPanel(new FlowLayout());
        p1.setOpaque(false);
        p1.add(createButton("消息提示", 0));
        p1.add(createButton("消息提示", 1));
        p1.add(createButton("消息提示", 2));
        p1.add(createButton("消息提示", 3));
        p1.add(createButton("消息提示", 4));
        p1.add(createButton("消息提示", 5));
        p1.add(createButton("消息提示", 6));

        JComponent p2 = new JPanel(new FlowLayout());
        p2.setOpaque(false);
        p2.add(createButton("消息通知(左)", BorderLayout.WEST));
        p2.add(createButton("消息通知(中)", BorderLayout.CENTER));
        p2.add(createButton("消息通知(右)", BorderLayout.EAST));

        this.add(p1);
        this.add(p2);

    }

    private WIconButton createButton(final String text, final int type) {
        WIconButton iconButton = new WIconButton(text);
        iconButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TransitionManager.showMessage(text, type);
            }
        });
        return iconButton;
    }

    private WIconButton createButton(final String text, final Object constraints) {
        WIconButton iconButton = new WIconButton(text);
        iconButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TransitionManager.showNotify(text,  new Random().nextInt(7), constraints);
            }
        });
        return iconButton;
    }
}
