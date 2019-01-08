package com.lw.swing.ui;

import com.lw.swing.ui.panel.WBlurEffectPanel;
import com.lw.swing.ui.tabbedpanel.WTabbedPane;
import com.lw.swing.utils.GraphicsUtil;
import com.lw.swing.utils.IconLoader;
import org.jdesktop.animation.transitions.ScreenTransition;
import org.jdesktop.animation.transitions.TransitionTarget;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.core.animation.timing.interpolators.AccelerationInterpolator;
import org.jdesktop.fuse.InjectedResource;
import org.jdesktop.fuse.ResourceInjector;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.jdesktop.swingx.image.GaussianBlurFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @description:
 * @className: ContentPanel
 * @author: liwen
 * @date: 2018/12/17 09:03
 */
public class ContentPanel extends JXPanel implements TransitionTarget {
    private final TitlePanel titlePanel;
    private final JPanel footerPanel;
    private final JPanel navPanel;
    private final JPanel barPanel;
    private final JPanel contentPanel;
    private final ScreenTransition screenTransition;
    private BufferedImage backgroundImage;
    @InjectedResource
    private Color borderColor = new Color(1f, 1f, 1f, .12f);

    private enum ScreenType {NAV, BAR}

    private ScreenType currentScreen;


    public ContentPanel() {
        this(new BorderLayout());
    }

    public ContentPanel(LayoutManager layout) {


        ResourceInjector.get().inject(this);
        setLayout(layout);
        titlePanel = new TitlePanel();
        footerPanel = new JXPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, borderColor));
        footerPanel.setPreferredSize(new Dimension(0, 35));
        navPanel = new JXPanel(new BorderLayout());
        navPanel.setOpaque(false);
        navPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, borderColor));
        navPanel.setPreferredSize(new Dimension(256, 0));
        barPanel = new JXPanel(new BorderLayout());
        barPanel.setOpaque(false);
        barPanel.setPreferredSize(new Dimension(55, 0));
        barPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, borderColor));
        contentPanel = new JXPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        this.add(titlePanel, BorderLayout.NORTH);
        this.add(footerPanel, BorderLayout.SOUTH);
        this.add(navPanel, BorderLayout.WEST);
        this.add(barPanel, BorderLayout.EAST);
        this.add(contentPanel, BorderLayout.CENTER);

        currentScreen = ScreenType.NAV;
        screenTransition = new ScreenTransition(this, this, 400);

        titlePanel.getMenulabel().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentScreen = ScreenType.NAV;
                screenTransition.start();
            }
        });
        titlePanel.getBarlabel().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentScreen = ScreenType.BAR;
                screenTransition.start();
            }
        });
    }

    @Override
    public void setupNextScreen() {


        switch (currentScreen) {
            case NAV:
                remove(navPanel);
                int navPanelW = (navPanel.getWidth() == 256 ? 62 : 256);
                navPanel.setPreferredSize(new Dimension(navPanelW, 0));
                add(navPanel, BorderLayout.WEST);
                break;
            case BAR:
                remove(barPanel);
                int barPanelW = (barPanel.getWidth() == 0 ? 55 : 0);
                barPanel.setPreferredSize(new Dimension(barPanelW, 0));
                add(barPanel, BorderLayout.EAST);
                break;
            default:
                assert false;
                break;
        }

    }

    public Component addNavPanel(Component component) {
        return navPanel.add(component);
    }

    public Component addFooterPanel(Component component) {
        return footerPanel.add(component);
    }

    public Component addBarPanel(Component component) {
        return barPanel.add(component);
    }

    public Component addContenPanel(Component component) {
        return contentPanel.add(component);
    }

    public TitlePanel getTitlePanel() {
        return titlePanel;
    }

    public JPanel getFooterPanel() {
        return footerPanel;
    }

    public JPanel getNavPanel() {
        return navPanel;
    }

    public JPanel getBarPanel() {
        return barPanel;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag && getAlpha() < 1f) {
            startFadeIn();
        }
    }

    private void startFadeIn() {

        Animator timer = new Animator.Builder().setDuration(1200, MILLISECONDS).setInterpolator(new AccelerationInterpolator(.7, .3)).build();
        timer.addTarget(PropertySetter.getTarget(this, "alpha", 0f, 1.0f));
        timer.start();

    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();
        g2.drawImage(getBackgroundImage(), 0, 0, getWidth(), getHeight(), null);
        g2.dispose();
    }


    public BufferedImage getBackgroundImage() {
        if (backgroundImage == null) {
            backgroundImage = IconLoader.image("/images/login/backg.jpg");
            backgroundImage = GraphicsUtilities.createThumbnailFast(
                    backgroundImage, getWidth() / 2);
            backgroundImage = new GaussianBlurFilter(15).filter(backgroundImage, null);

        }
        return backgroundImage;
    }
}
