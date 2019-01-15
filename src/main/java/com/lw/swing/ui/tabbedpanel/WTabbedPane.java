package com.lw.swing.ui.tabbedpanel;


import org.jdesktop.fuse.InjectedResource;
import org.jdesktop.fuse.ResourceInjector;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class WTabbedPane extends JTabbedPane {

    private int preferredUnselectedTabWidth = 80;
    private int preferredTabHeight = 50;
    @InjectedResource
    private Color selectedBackground = new Color(.38f, .38f, .38f, .7f);
    @InjectedResource
    private Color background = new Color(.38f, .38f, .38f, .34f);
    private boolean fillBackground = false;

    public WTabbedPane() {
        this(TOP);
    }

    public WTabbedPane(int tabPlacement) {
        super(tabPlacement);
        init();
    }

    private void init() {
        ResourceInjector.get().inject(this);
        this.setFocusable(false);
        this.setBackground(background);
        this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        this.setOpaque(false);
        this.setUI(new WTabbedPaneUI(this));
        this.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                //inform each tab component that select changed.
                updateTabComponents();
            }
        });
        this.putClientProperty("TabbedPane.tabInsets", new Insets(0, 0, 0, 0));
    }

    @Override
    public void addTab(String title, Component component) {
        addTab(null, title, component, true);
    }

    public void addTab(String iconText, String title, Component component) {
        addTab(iconText, title, component, true);
    }

    public void addTab(String iconText, String title, Component component, boolean isShwoCloseBut) {
        super.addTab(title, component);
        int index = this.getTabCount() - 1;
        WTabComponent tabComponent = new WTabComponent(this, iconText, title, isShwoCloseBut);
        this.setTabComponentAt(index, tabComponent);
        this.setToolTipTextAt(index, title);
        updateTabComponents();
    }

    public int getPreferredTabHeight() {
        return preferredTabHeight;
    }

    public void setPreferredTabHeight(int preferredTabHeight) {
        this.preferredTabHeight = preferredTabHeight;
    }

    private void updateTabComponents() {
        int selectedIndex = this.getSelectedIndex();
        for (int i = 0; i < this.getTabCount(); i++) {
            Component c = this.getTabComponentAt(i);
            if (c instanceof WTabComponent) {
                WTabComponent component = (WTabComponent) c;
                if (!component.isFixed()) {
                    boolean selected = selectedIndex == i;
                    component.updateSelection(selected);
                }
            }
        }
    }


    public int getPreferredUnselectedTabWidth() {
        return preferredUnselectedTabWidth;
    }


    public Color getSelectedBackground() {
        return selectedBackground;
    }

    public void setSelectedBackground(Color selectedBackground) {
        this.selectedBackground = selectedBackground;
    }

    public boolean isFillBackground() {
        return fillBackground;
    }

    public void setFillBackground(boolean fillBackground) {
        this.fillBackground = fillBackground;
    }
}
