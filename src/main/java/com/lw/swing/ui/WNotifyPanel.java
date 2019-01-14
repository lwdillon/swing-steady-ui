package com.lw.swing.ui;

import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingSource;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

/**
 * 通知消息panel
 * Created by liwen on 2017/6/16.
 */
public class WNotifyPanel extends JPanel {

    private JPanel eastPanel = new JPanel(new ModifiedFlowLayout());
    private JPanel centerPanel = new JPanel(new GridBagLayout());
    private JPanel centerNotify = new JPanel(new ModifiedFlowLayout());
    private JPanel westPanel = new JPanel(new ModifiedFlowLayout());

    public WNotifyPanel() {
        initSwing();
    }

    private void initSwing() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(100, 10, 10, 10));
        eastPanel.setOpaque(false);
        centerPanel.setOpaque(false);
        westPanel.setOpaque(false);
        centerNotify.setOpaque(false);
        this.setOpaque(false);

        centerPanel.add(centerNotify, new GridBagConstraints(0, 0, 1, 1, 0D, 1D, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        this.add(westPanel, BorderLayout.WEST);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(eastPanel, BorderLayout.EAST);
    }

    public void showNotify(String message, int type, Object constraints) {
        try {
            WMessagerPanel messagerPanel = new WMessagerPanel();
            messagerPanel.showMessage(message, type);
            if (constraints.equals(BorderLayout.EAST)) {
                eastPanel.add(messagerPanel);
                eastPanel.revalidate();
            } else if (constraints.equals(BorderLayout.CENTER)) {
                centerNotify.add(messagerPanel);
                centerNotify.revalidate();
            } else {
                westPanel.add(messagerPanel);
                westPanel.revalidate();
            }

            this.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

                JButton jb = new JButton("fdsa");

                final WNotifyPanel wNotifyPanel = new WNotifyPanel();
                jb.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        wNotifyPanel.showNotify("普通消息fdsafdsafdsa", new Random().nextInt(7), BorderLayout.EAST);
                        wNotifyPanel.showNotify("普通消息fdsafdsafdsa", new Random().nextInt(7), BorderLayout.CENTER);
                        wNotifyPanel.showNotify("普通消息fdsafdsafdsa", new Random().nextInt(7), BorderLayout.WEST);
                    }
                });
                frame.add(jb, BorderLayout.NORTH);
                frame.add(wNotifyPanel);
                frame.setVisible(true);

                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowOpened(WindowEvent e) {

                    }
                });


            }
        });
    }
}
