package com.lw.swing.ui.tabbedpanel;


import com.lw.swing.utils.IconFont;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.ConsoleHandler;

public class WTabComponent extends JPanel {


    private JLabel btnClose = new JLabel("", JLabel.CENTER);
    private boolean fixed = false;
    private boolean showCloseBut = true;
    private JLabel lbTitle = new JLabel();
    private JLabel lbIcon = new JLabel();

    private WTabbedPane tab = null;
    private Border border = BorderFactory.createEmptyBorder(0, 15, 0, 15);

    public WTabComponent(WTabbedPane tab) {
        this(tab, null, null, true);
    }

    public WTabComponent(WTabbedPane tab, String title) {
        this(tab, null, title, true);
    }

    public WTabComponent(WTabbedPane tab, String iconFont, String title, Boolean isShowCloseBut) {
        this.tab = tab;
        lbIcon.setFont(IconFont.ICON_FONT.deriveFont(18f));
        lbIcon.setText(IconFont.getIcon(iconFont));
        lbIcon.setForeground(tab.getForeground());
        btnClose.setToolTipText("Close this tab");
        btnClose.setFont(IconFont.ICON_FONT.deriveFont(14f));
        btnClose.setText(IconFont.getIcon("fa-times-circle-o"));
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.setForeground(tab.getForeground());
        btnClose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnClose.setText(IconFont.getIcon("fa-times-circle"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnClose.setText(IconFont.getIcon("fa-times-circle-o"));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                closeTab();
            }
        });

        lbTitle.setText(title);
        lbTitle.setForeground(tab.getForeground());
        this.setBorder(border);
        this.setLayout(new BorderLayout(10,0));
        if (StringUtils.isNotBlank(iconFont)) {
            this.add(lbIcon, BorderLayout.WEST);
        }
        btnClose.setVisible(isShowCloseBut);
        this.add(btnClose, BorderLayout.EAST);
        this.add(this.lbTitle, BorderLayout.CENTER);
        this.setOpaque(false);
    }

    public WTabComponent(WTabbedPane tab, Boolean isShowCloseBut) {
        this(tab);
        showCloseBut = isShowCloseBut;
        btnClose.setVisible(showCloseBut);
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        if (isTabSelected()) {
            g2d.setColor(tab.getSelectedBackground());
            g2d.fillRect(0, 0, getWidth() - 3, getHeight());
        } else {
            g2d.setColor(tab.getBackground());
            g2d.fillRect(0, 0, getWidth() - 3, getHeight());
        }
        g2d.dispose();

    }

    @Override
    public Dimension getPreferredSize() {
        int width = super.getPreferredSize().width;
//        if (!this.isTabSelected()) {
//            width = Math.min(width, tab.getPreferredUnselectedTabWidth());
//        }
        int height = tab.getPreferredTabHeight();
        return new Dimension(width, height);
    }

    public boolean isTabSelected() {
        int index = tab.indexOfTabComponent(this);
        int selectedIndex = tab.getSelectedIndex();
        return selectedIndex == index;
    }


    public void updateSelection(boolean selected) {

    }

    private void closeTab() {
        int index = tab.indexOfTabComponent(this);
        String title = tab.getTitleAt(index);
        Component component = tab.getComponentAt(index);
        String className = component.getClass().getName();

        tab.removeTabAt(index);
    }
}
