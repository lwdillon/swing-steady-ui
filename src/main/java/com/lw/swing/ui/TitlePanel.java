package com.lw.swing.ui;

import com.lw.swing.ui.panel.WBlurEffectPanel;
import com.lw.swing.utils.IconFont;
import com.lw.swing.utils.IconLoader;
import com.lw.swing.utils.SysFont;
import org.jdesktop.fuse.InjectedResource;
import org.jdesktop.fuse.ResourceInjector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * @description:
 * @className: TitlePanel
 * @author: liwen
 * @date: 2018/12/16 13:32
 */
public class TitlePanel extends WBlurEffectPanel implements ActionListener {

    private JLabel logoIconLabel;
    private JLabel logoLabel;
    private WIconButton menulabel;
    private WIconButton barlabel;

    @InjectedResource
    private Color borderColor=new Color(1f, 1f, 1f, .12f);
    @InjectedResource
    private Color background=new Color(200, 100, 100, 87);

    private WIconButton btnClose = createButton("", "fa-times");
    private WIconButton btnMax = createButton("", "fa-window-maximize");
    private WIconButton btnMin = createButton("", "fa-window-minimize");

    public TitlePanel() {

        ResourceInjector.get().inject(this);

        initSwing();

        initListener();
    }

    private void initListener() {

    }


    private void initSwing() {

        JPanel logoPanel = new JPanel(new BorderLayout(15,0));
        logoPanel.setOpaque(false);
        logoPanel.add(getLogoIconLabel(), BorderLayout.WEST);
        logoPanel.add(getLogoLabel(), BorderLayout.CENTER);

        JPanel leftPane = new JPanel(new BorderLayout(8, 0));
        leftPane.setOpaque(false);
        leftPane.add(getMenulabel(), BorderLayout.WEST);
        leftPane.add(logoPanel, BorderLayout.CENTER);
        leftPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPane.setOpaque(false);
        buttonPane.add(btnMin);
        buttonPane.add(btnMax);
        buttonPane.add(btnClose);

        JPanel barPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        barPane.setOpaque(false);
        barPane.add(getBarlabel());

        JPanel rightPane = new JPanel(new BorderLayout(5, 5));
        rightPane.setOpaque(false);
        rightPane.add(buttonPane, BorderLayout.NORTH);
        rightPane.add(barPane, BorderLayout.EAST);


        this.setPreferredSize(new Dimension(0, 64));
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,borderColor));
        this.setBackground(background);
        this.setLayout(new BorderLayout());
        this.add(leftPane, BorderLayout.WEST);
        this.add(rightPane, BorderLayout.EAST);

    }


    public JLabel getLogoIconLabel() {
        if (logoIconLabel == null) {
            logoIconLabel = new JLabel();
            logoIconLabel.setFont(IconFont.ICON_FONT.deriveFont(50f));
            logoIconLabel.setText(IconFont.getIcon("fa-java"));
//            logoIconLabel.setIcon(IconLoader.icon("/images/logo.png"));
        }
        return logoIconLabel;
    }

    public JLabel getLogoLabel() {
        if (logoLabel == null) {
            logoLabel = new JLabel("swing-steady-ui");
        }
        return logoLabel;
    }

    public WIconButton getMenulabel() {
        if (menulabel == null) {
            menulabel = new WIconButton("", "fa-bars");
            menulabel.setFont(SysFont.FONT_20_BOLD);
            menulabel.setFill(false);
            menulabel.setDrawBorder(false);

            menulabel.setPreferredSize(new Dimension(48, 48));

        }
        return menulabel;
    }

    public WIconButton getBarlabel() {

        if (barlabel == null) {
            barlabel = new WIconButton("", "fa-toolbox");
            barlabel.setFill(false);
            barlabel.setDrawBorder(false);
        }
        return barlabel;
    }

    private WIconButton createButton(String tip, String icon) {
        WIconButton button = new WIconButton("", icon);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setToolTipText(tip);
        button.setFont(SysFont.FONT_14_PLAIN);
        button.setDrawBorder(false);
        button.setFill(false);
        button.addActionListener(this);
        return button;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnClose) {
            close();
        } else if (e.getSource() == btnMax) {
            maximize();
        } else if (e.getSource() == btnMin) {
            iconify();
        }

    }

    /**
     * Closes the Window.
     */
    private void close() {
        Window window = SwingUtilities.getWindowAncestor(this);

        if (window != null) {
            window.dispatchEvent(new WindowEvent(
                    window, WindowEvent.WINDOW_CLOSING));
        }
    }

    /**
     * Iconifies the Frame.
     */
    private void iconify() {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.setExtendedState(Frame.ICONIFIED);
        }
    }

    /**
     * Maximizes the Frame.
     */
    private void maximize() {
        restore();
    }

    /**
     * Restores the Frame size.
     */
    private void restore() {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);

        if (frame == null) {
            return;
        }

        if (frame.getExtendedState() == Frame.MAXIMIZED_BOTH) {
            frame.setExtendedState(Frame.NORMAL);

        } else {
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);

        }
    }

}
