package com.lw.swing.ui;

import javax.swing.*;
import java.awt.*;

/**
 * @description:
 * @className: MainFrame
 * @author: liwen
 * @date: 2018/12/14 15:09
 */
public class MainFrame extends WFrame {

    private Component originalOverlay;
    private LoginOverlay loginOverlay;
    private ContentPanel panel;

    public MainFrame(final ContentPanel transPanel) {

        this.add(transPanel, BorderLayout.CENTER);
        this.setSize(1080, 714);

        this.originalOverlay = getGlassPane();
        this.panel = transPanel;
    }


    void showIntroduction() {
        setGlassPane(new IntroductionPanel((JComponent) getContentPane()));
        getGlassPane().setVisible(true);
        this.panel.setVisible(false);
    }

    void showTransitionPanel() {
        this.panel.setVisible(true);
    }

    void killOverlay() {
        setGlassPane(originalOverlay);
    }

    void hideLoginOverlay() {
        getGlassPane().setVisible(false);
    }


    void showLoginOverlay(boolean visible) {
        loginOverlay = new LoginOverlay((JComponent) getContentPane(), visible);
        setGlassPane(loginOverlay);
        getGlassPane().setSize(getSize());
        getGlassPane().validate();
        if (visible) {
            getGlassPane().setVisible(true);
        }
    }
}
