package com.lw.swing.ui;

import com.lw.swing.ui.panel.WBlurEffectPanel;
import com.lw.swing.ui.tabbedpanel.WTabbedPane;
import com.lw.swing.utils.LinearGradientTypeLoader;
import org.jdesktop.fuse.ResourceInjector;
import org.jdesktop.fuse.TypeLoaderFactory;
import org.jdesktop.fuse.swing.SwingModule;

import javax.swing.*;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @description:
 * @className: TransitionManager
 * @author: liwen
 * @date: 2018/12/14 15:08
 */
public class TransitionManager {


    private static MainFrame mainFrame;
    private static NavigationPanel navPanel;
    private static FooterPanel footerPanel;
    private static JPanel barPanel;
    private static WTabbedPane tabbedPane;
    private static ContentPanel contentPanel;

    /**
     * Creates the main application frame.  Should be called from the
     * EDT only.
     */
    public static MainFrame createMainFrame() {

        ResourceInjector.addModule(new SwingModule());
        ResourceInjector.get().load(MainFrame.class, "/style.properties");
        TypeLoaderFactory.addTypeLoader(new LinearGradientTypeLoader());

        navPanel = new NavigationPanel();
        footerPanel = new FooterPanel();
        barPanel = new WBlurEffectPanel();
        tabbedPane = new WTabbedPane();
        contentPanel = new ContentPanel();
        contentPanel.addNavPanel(navPanel);
        contentPanel.addFooterPanel(footerPanel);
        contentPanel.addBarPanel(barPanel);
        contentPanel.addContenPanel(tabbedPane);

        mainFrame = new MainFrame(contentPanel);

        installRepaintManager();

        showIntroduction();

        return mainFrame;
    }


    static void showIntroduction() {
        mainFrame.showIntroduction();
    }

    static void showTransitionPanel() {
        mainFrame.showTransitionPanel();
    }

    static void showLoginOverlay() {
        showLoginOverlay(false);
    }

    static void killOverlay() {
        mainFrame.killOverlay();
    }

    static void showLoginOverlay(boolean visible) {
        mainFrame.showLoginOverlay(visible);
    }

    static void hideLoginOverlay() {
        mainFrame.hideLoginOverlay();
    }

    static MainFrame getMainFrame() {
        return mainFrame;
    }


    public static NavigationPanel getNavPanel() {
        return navPanel;
    }

    public static FooterPanel getFooterPanel() {
        return footerPanel;
    }

    public static JPanel getBarPanel() {
        return barPanel;
    }

    public static ContentPanel getContentPanel() {
        return contentPanel;
    }

    public static WTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    static void installRepaintManager() {
        ReflectionRepaintManager manager = new ReflectionRepaintManager();
        RepaintManager.setCurrentManager(manager);
    }

}
