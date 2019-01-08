package com.lw.swing.ui.slider;


import com.lw.swing.utils.IconFont;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.core.animation.timing.interpolators.AccelerationInterpolator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Panel that contains both the title/header part and the content part.
 *
 * @author oliver
 */

public class SidebarSection extends JComponent {

    private static final long serialVersionUID = 1L;

    public int minComponentHeight = 40;
    public int minComponentWidth = 350;
    public int minTitleWidth = 62;

    public TitlePanel titlePanel;
    private JComponent titleComponent;
    private SideBar sideBarOwner;
    private Object userObject;
    private String text;

    public JComponent contentPane; //sidebar section's content

    private ArrowLabel arrowPanel;
    private JLabel iconLabel;

    private int calculatedHeight;

    public SidebarSection(SideBar owner, String text, Icon icon) {
        this(owner, new JLabel(text), icon, null, null, null);
    }

    public SidebarSection(SideBar owner, String text, String iconFont) {
        this(owner, new JLabel(text), null, iconFont, null, null);
    }

    public SidebarSection(SideBar owner, String text, Icon icon, JComponent component) {
        this(owner, new JLabel(text), icon, null, component, null);
    }

    public SidebarSection(SideBar owner, String text, String iconFont, JComponent component) {
        this(owner, new JLabel(text), null, iconFont, component, null);
    }


    /**
     * Construct a new sidebar section with the specified owner and model.
     *
     * @param owner - SideBar
     * @param model
     */
    public SidebarSection(SideBar owner, JComponent titleComponent, Icon icon, String iconFont, JComponent component, Object userObject) {


        this.contentPane = component;
        this.sideBarOwner = owner;
        this.userObject = userObject;
        this.titleComponent = titleComponent;
        this.iconLabel = new JLabel("", JLabel.CENTER);
        titleComponent.setForeground(Color.WHITE);
        if (icon != null) {
            iconLabel.setIcon(icon);
        }
        if (StringUtils.isNotEmpty(iconFont)) {
            iconLabel.setFont(IconFont.ICON_FONT);
            iconLabel.setText(IconFont.getIcon(iconFont));
            iconLabel.setForeground(Color.WHITE);
        }

        this.titlePanel = new TitlePanel();
        titlePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {

                if (contentPane == null) {
                    return;
                }

                if (SidebarSection.this != sideBarOwner.getCurrentSection()) {
                    if (sideBarOwner.getCurrentSection() != null) {
                        sideBarOwner.getCurrentSection().collapse(true);

                    }

                    expand(); //expand this!
                } else {
                    collapse(true);
                }
            }
        });


        if (owner.thisMode == SideBar.SideBarMode.INNER_LEVEL) {
            minComponentHeight = 30;
            titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        } else {
            minComponentHeight = 40;
            titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        }

        titlePanel.setLayout(new BorderLayout());
        titlePanel.setPreferredSize(new Dimension(this.getPreferredSize().width, minComponentHeight));
        arrowPanel = new ArrowLabel();
        arrowPanel.setPreferredSize(new Dimension(30, 0));
        arrowPanel.setForeground(Color.WHITE);
        iconLabel.setPreferredSize(new Dimension(minTitleWidth, minTitleWidth));
        titlePanel.add(iconLabel, BorderLayout.WEST);
        titlePanel.add(titleComponent, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        if (component != null) {
            if (sideBarOwner.showArrow) {
                //在标签面板中添加箭头和标签
                titlePanel.add(arrowPanel, BorderLayout.EAST);
            }
            add(component, BorderLayout.CENTER);

        }
        revalidate();
    }


    public void expand() {
        sideBarOwner.setCurrentSection(this);


        calculatedHeight = -1;
        calculatedHeight = sideBarOwner.getSize().height;

        if (this.sideBarOwner.animate) {
            contentPane.setVisible(true);
            Animator anim = new Animator.Builder().setDuration(300, MILLISECONDS).setInterpolator(new AccelerationInterpolator(.7, .3)).build();
            anim.addTarget(PropertySetter.getTarget(this, "calculatedHeight", minComponentHeight, calculatedHeight));
            anim.addTarget(PropertySetter.getTarget(arrowPanel, "angle", 0D, 90D));
            anim.start();
        } else {
            arrowPanel.setAngle(90d);
            if (sideBarOwner.thisMode == SideBar.SideBarMode.INNER_LEVEL) {
                calculatedHeight = 1000;
                Dimension d = new Dimension(Integer.MAX_VALUE, calculatedHeight);
                setMaximumSize(d);
                sideBarOwner.setPreferredSize(d);
                if (contentPane != null) {
                    contentPane.setVisible(true);
                }
                revalidate();
            } else {
                setMaximumSize(new Dimension(Integer.MAX_VALUE, calculatedHeight));
                if (contentPane != null) {
                    contentPane.setVisible(true);
                }
                revalidate();
            }
        }
    }


    public void collapse(boolean animate) {
        // remove reference
        if (sideBarOwner.getCurrentSection() == SidebarSection.this) {
            sideBarOwner.setCurrentSection(null);
        }


        if (animate && this.sideBarOwner.animate) {
            contentPane.setVisible(true);
            Animator anim = new Animator.Builder().setDuration(300, MILLISECONDS).setInterpolator(new AccelerationInterpolator(.3, .7)).build();
            anim.addTarget(PropertySetter.getTarget(this, "calculatedHeight", calculatedHeight, minComponentHeight));
            anim.addTarget(PropertySetter.getTarget(arrowPanel, "angle", 90, 0));
            anim.start();
        } else {
            arrowPanel.setAngle(0d);
            if (sideBarOwner.thisMode == SideBar.SideBarMode.INNER_LEVEL) {
                setMaximumSize(new Dimension(Integer.MAX_VALUE, titlePanel.getPreferredSize().height));
                if (contentPane != null) {
                    contentPane.setVisible(false);
                }
                revalidate();
            } else {
                setMaximumSize(new Dimension(Integer.MAX_VALUE, titlePanel.getPreferredSize().height));
                if (contentPane != null) {
                    contentPane.setVisible(false);
                }
                revalidate();
            }
        }
    }

    public String getText() {
        return text;
    }

    public TitlePanel getTitlePanel() {
        return titlePanel;
    }

    public Object getUserObject() {
        return userObject;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(minComponentWidth, minComponentHeight);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(minComponentWidth, minComponentHeight);
    }

    public int getCalculatedHeight() {
        return calculatedHeight;
    }


    @Override
    public void setBounds(int x, int y, int width, int height) {

        if (minTitleWidth >= width) {
            titleComponent.setVisible(false);
//            arrowPanel.setVisible(false);
        } else {
            titleComponent.setVisible(true);
//            arrowPanel.setVisible(true);
        }
        super.setBounds(x, y, width, height);
    }

    /**
     * @Description:
     * @param:
     * @return: void
     * @auther: liwen
     * @date: 2018/12/25 9:31 AM
     */
    public void setSidebarIcon(boolean b) {

    }

    public void setCalculatedHeight(int calculatedHeight) {
        this.calculatedHeight = calculatedHeight;
        setMaximumSize(new Dimension(Integer.MAX_VALUE, calculatedHeight));
        revalidate();

    }

    public void addActionListener(ActionListener l) {
        getTitlePanel().addActionListener(l);
    }

    class TitlePanel extends JButton {

        private boolean mouseEntered = false;

        public TitlePanel() {


            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    mouseEntered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    mouseEntered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            if (mouseEntered) {

                g2.setColor(sideBarOwner.getSelectionBackground());
                g2.setComposite(AlphaComposite.SrcOver.derive(.5f));
                g2.fillRoundRect(-getHeight() / 2, 0, getWidth() + getHeight(), getHeight(), getHeight() / 2, getHeight() / 2);
            }
            g2.dispose();
        }


    }


}