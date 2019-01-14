package com.lw.swing.ui;

import com.lw.swing.demo.ButtonPanel;
import com.lw.swing.demo.NotificationPanel;
import com.lw.swing.ui.panel.WBlurEffectPanel;
import com.lw.swing.ui.slider.SideBar;
import com.lw.swing.ui.slider.SidebarSection;
import org.jdesktop.fuse.ResourceInjector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @description:
 * @className: NavigationPanel
 * @author: liwen
 * @date: 2018/12/14 15:13
 */
public class NavigationPanel extends WBlurEffectPanel {

    private SideBar sideBar;

    public NavigationPanel() {
        super();
        sideBar = new SideBar(SideBar.SideBarMode.TOP_LEVEL, true, 350, true);
        sideBar.addSection(crateSidebarSection(sideBar, "按钮", null, "fa-home"));
        sideBar.addSection(crateSidebarSection(sideBar, "弹出消息", null, "fa-home"));
        sideBar.addSection(crateSidebarSection(sideBar, "导航栏", null, "fa-home"));


        SideBar innerSideBar = new SideBar(SideBar.SideBarMode.INNER_LEVEL, true, -1, true);
        innerSideBar.setOpaque(false);
        innerSideBar.addSection(crateSidebarSection(innerSideBar, "单选框", null, "fa-home"));
        innerSideBar.addSection(crateSidebarSection(innerSideBar, "复选框", null, "fa-home"));
        innerSideBar.addSection(crateSidebarSection(innerSideBar, "下拉框", null, "fa-home"));
        innerSideBar.addSection(crateSidebarSection(innerSideBar, "文本框", null, "fa-home"));

        SidebarSection sss2 = crateSidebarSection(sideBar, "表单", innerSideBar, "fa-home");

        sideBar.addSection(sss2);
        sideBar.addSection(crateSidebarSection(sideBar, "选项卡", null, "fa-home"));
        sideBar.addSection(crateSidebarSection(sideBar, "面板", null, "fa-home"));
        sideBar.addSection(crateSidebarSection(sideBar, "表格", null, "fa-home"));
        sideBar.addSection(crateSidebarSection(sideBar, "对话框", null, "fa-home"));
        sideBar.addSection(crateSidebarSection(sideBar, "日期与时间选择", null, "fa-home"));

        sideBar.setOpaque(false);
        this.add(sideBar);
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
                }else if(text.equals("弹出消息")){
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
