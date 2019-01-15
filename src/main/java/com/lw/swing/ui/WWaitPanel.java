package com.lw.swing.ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 带加载动画的panel
 * Created by liwen on 2017/6/2.
 */
public class WWaitPanel extends JPanel {
    private LodingPanel waitPanel = new LodingPanel(this);
    private JPanel contentPanel = new JPanel(new BorderLayout(0, 1));

    private final AtomicBoolean running;

    public WWaitPanel() {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        contentPanel.setOpaque(false);
        add(contentPanel, BorderLayout.CENTER);
        add(waitPanel, BorderLayout.NORTH);
        waitPanel.setVisible(false);
        setComponentZOrder(waitPanel, 0);
        running = new AtomicBoolean(false);
    }

    public void showWaitLoding() {

        showWaitLoding("", 0f);
    }

    /**
     * 显示加载动画
     *
     * @param msg
     */
    public void showWaitLoding(final String msg, final float alpha) {
        if (running.get()) {
            return;
        }
        try {
            waitPanel.getMsgLable().setText(msg);
            waitPanel.setPreferredSize(new Dimension(0, 0));
            waitPanel.setAlpha(alpha);
            waitPanel.setVisible(true);
            running.set(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 去掉加载动画
     */
    public void hideWaitLoding() {
        try {
            running.set(false);
            waitPanel.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addComponent(Component comp, Object constraints) {
        contentPanel.add(comp, constraints);
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    class LodingPanel extends JComponent {
        private LodingComponent msgLable;
        private JComponent field;
        private float alpha = .2f;

        public LodingPanel(JComponent field) {
            this.field = field;

            setLayout(new GridBagLayout());

            add(getMsgLable(), new GridBagConstraints(0, 0, 1, 1, 0D, 0D, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 60, 10));

            setOpaque(false);
            addMouseListener(new MouseAdapter() {
            });
            addMouseMotionListener(new MouseMotionAdapter() {
            });
            addKeyListener(new KeyAdapter() {
            });
            setFocusTraversalKeysEnabled(false);
            addComponentListener(new ComponentAdapter() {
                public void componentShown(ComponentEvent evt) {
                    requestFocusInWindow();
                }
            });
        }

        public void setAlpha(float alpha) {
            this.alpha = alpha;
            repaint();
        }

        public LodingComponent getMsgLable() {
            if (msgLable == null) {
                msgLable = new LodingComponent("", BorderLayout.EAST);
                msgLable.setForeground(Color.WHITE);
            }
            return msgLable;
        }

        @Override
        public void setVisible(boolean aFlag) {

            if (aFlag) {
                msgLable.start();
            } else {
                msgLable.stop();
            }
            super.setVisible(aFlag);

        }

        @Override
        public void setBounds(int x, int y, int width, int height) {
            super.setBounds(0, 0, field.getWidth(), field.getHeight());
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.BLACK);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

}
