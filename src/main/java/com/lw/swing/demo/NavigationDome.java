package com.lw.swing.demo;

import com.lw.swing.ui.TransitionManager;
import com.lw.swing.ui.slider.SideBar;
import com.lw.swing.ui.slider.SidebarSection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @description:
 * @className: NavigationDome
 * @author: liwen
 * @date: 2019/1/14 16:50
 */
public class NavigationDome extends JPanel {


    public NavigationDome() {


        SideBar sideBar = new SideBar(SideBar.SideBarMode.TOP_LEVEL, true, 350, true);
        sideBar.addSection(crateSidebarSection(sideBar, "导航栏", null, "fa-home"));
        sideBar.addSection(crateSidebarSection(sideBar, "导航栏", new JTree(), "fa-home"));
        sideBar.addSection(crateSidebarSection(sideBar, "导航栏", new JTable(), "fa-home"));


        SideBar innerSideBar = new SideBar(SideBar.SideBarMode.INNER_LEVEL, true, -1, true);
        innerSideBar.setOpaque(false);
        innerSideBar.addSection(crateSidebarSection(innerSideBar, "单选框", null, "fa-home"));
        innerSideBar.addSection(crateSidebarSection(innerSideBar, "复选框", null, "fa-home"));
        innerSideBar.addSection(crateSidebarSection(innerSideBar, "下拉框", null, "fa-home"));
        innerSideBar.addSection(crateSidebarSection(innerSideBar, "文本框", null, "fa-home"));

        SidebarSection sss2 = crateSidebarSection(sideBar, "导航栏", innerSideBar, "fa-home");

        sideBar.addSection(sss2);
        sideBar.addSection(crateSidebarSection(sideBar, "导航栏", null, "fa-home"));
        sideBar.setBackground(new Color(127,127,127,124));
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.add(sideBar, BorderLayout.WEST);
    }

    private SidebarSection crateSidebarSection(SideBar sideBar, final String text, final JComponent component, final String icontfont) {
        SidebarSection sidebarSection = new SidebarSection(sideBar, text, icontfont, component);
        sidebarSection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (component instanceof SideBar) {
                    return;
                }
                Component c = new JLabel("cc");
                if (text.equals("按钮")) {
                    c = new ButtonPanel();
                } else if (text.equals("弹出消息")) {
                    c = new NotificationPanel();
                }
                if (TransitionManager.getTabbedPane().indexOfTab(text) == -1) {
                    TransitionManager.getTabbedPane().addTab(icontfont, text, c);
                } else {
                    TransitionManager.getTabbedPane().setSelectedIndex(TransitionManager.getTabbedPane().indexOfTab(text));
                }

            }
        });
        return sidebarSection;
    }
}
