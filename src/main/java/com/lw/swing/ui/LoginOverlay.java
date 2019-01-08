package com.lw.swing.ui;

import com.lw.swing.utils.IconFont;
import com.lw.swing.utils.IconLoader;
import com.lw.swing.utils.SysFont;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;
import org.jdesktop.core.animation.timing.interpolators.AccelerationInterpolator;
import org.jdesktop.fuse.InjectedResource;
import org.jdesktop.fuse.ResourceInjector;
import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class LoginOverlay extends JComponent implements ActionListener {

    private final JComponent clip;
    private final boolean makeVisible;
    private float blackAlpha = 1.0f;
    private float alpha = 1.0f;
    private float loding = 1.0f;

    private JButton loginBut;
    private JLabel logoLb;
    private JLabel titleLb;
    private Color textColor = new Color(0xFFFFFF);
    private WTextField userTextField;
    private WPasswordTextField passwordTextField;
    private JLabel infoLable;
    private JXPanel panel;
    private JPanel lodingPanel;
    private Animator backImgAnimator;
    private Timer timer;

    @InjectedResource
    private BufferedImage bufferedImage;
    @InjectedResource
    private String backImageList;

    @InjectedResource
    private String copyrightNotice="©Swing•人生";

    @InjectedResource
    private String title="登录";

    private List<BufferedImage> imageList = null;
    private BufferedImage firstImage = null;
    private BufferedImage secondImage = null;
    private int currentImageIndex = 0;

    private Animator loadingTimer;


    public LoginOverlay(JComponent clip, boolean makeVisible) {
        ResourceInjector.get().inject(this);
        this.clip = clip;
        this.makeVisible = makeVisible;

        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 7, 0));
        topPanel.add(getLogoLb(), BorderLayout.CENTER);
        topPanel.add(getTitleLb(), BorderLayout.SOUTH);


        JLabel label = new JLabel("", JLabel.CENTER);
        label.setText(copyrightNotice);
        label.setForeground(Color.WHITE);

        userTextField = new WTextField("fa-user", "", "用户名", "用户名（汉字、字母、数字的组合）");
        userTextField.setTextFiledBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(1f, 1f, 1f, .3f)));
        userTextField.setTextFont(new Font("宋体", Font.PLAIN, 20));
        userTextField.setRegex("[^\\s]{2,20}");

        passwordTextField = new WPasswordTextField("fa-lock", "", "密码", "密码（6-16位数字和字母的组合)");
        passwordTextField.setTextFiledBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(1f, 1f, 1f, .3f)));
        passwordTextField.setTextFont(new Font("宋体", Font.PLAIN, 20));
        passwordTextField.setRegex("[^\\s]{5,20}");

        lodingPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.setColor(Color.WHITE);

                g2.translate(-100, 0);
                int w = (int) (loding * 400);
                if (loding > .5f) {
                    w = (int) (400 - loding * 400);
                }
                g2.fillRoundRect((int) (loding * (getWidth() + 200)), 0, w, 5, 10, 10);
                g2.dispose();


            }
        };
        lodingPanel.setBackground(new Color(1f, 1f, 1f, .24f));
        lodingPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        lodingPanel.add(userTextField, new GridBagConstraints(0, 0, 1, 1, 1D, 1D, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 35));
        lodingPanel.add(passwordTextField, new GridBagConstraints(0, 1, 1, 1, 1D, 1D, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 35));
        lodingPanel.add(getLoginBut(), new GridBagConstraints(0, 2, 1, 1, 1D, 1D, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 35));
        lodingPanel.add(label, new GridBagConstraints(0, 3, 1, 1, 1D, 1D, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 10));


        panel = new JXPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(lodingPanel, BorderLayout.CENTER);
        setLayout(new GridBagLayout());

        add(new TitlePanel(), new GridBagConstraints(0, 0, 1, 1, 1D, 1D, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        add(panel, new GridBagConstraints(0, 0, 1, 1, 1D, 1D, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 150, 74));

        initBackImages();

    }


    public Timer getTimer() {
        if (timer == null) {
            timer = new Timer(5000, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    int size = imageList.size() - 1;

                    if (currentImageIndex + 1 <= size) {
                        firstImage = imageList.get(currentImageIndex);
                        secondImage = imageList.get(currentImageIndex + 1);
                        currentImageIndex += 1;

                    } else {
                        firstImage = imageList.get(currentImageIndex);
                        secondImage = imageList.get(0);
                        currentImageIndex = 0;
                    }

                    getBackImgAnimator().restart();

                }
            });
        }
        return timer;
    }


    private void initBackImages() {
        new SwingWorker<List<BufferedImage>, String>() {
            @Override
            protected List<BufferedImage> doInBackground() throws Exception {

                List<BufferedImage> list = new ArrayList<BufferedImage>();

                String[] images = backImageList.split(",");

                for (String img : images) {
                    list.add(IconLoader.image(img));
                }

                return list;
            }

            @Override
            protected void done() {
                try {
                    imageList = get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }


    public JLabel getLogoLb() {
        if (logoLb == null) {
            logoLb = new JLabel(IconFont.getIcon("fa-desktop"), JLabel.CENTER);
            logoLb.setFont(IconFont.ICON_FONT.deriveFont(150f));
            logoLb.setForeground(textColor);

        }
        return logoLb;
    }


    public JLabel getTitleLb() {
        if (titleLb == null) {
            titleLb = new JLabel(title, JLabel.CENTER);
            titleLb.setFont(new Font("宋体", Font.PLAIN, 28));
            titleLb.setForeground(textColor);
        }
        return titleLb;
    }


    public JButton getLoginBut() {
        if (loginBut == null) {
            loginBut = new JButton("登 录") {
                @Override
                protected void paintComponent(Graphics g) {

                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.dispose();

                }
            };
            loginBut.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    loginBut.setBackground(new Color(1f, 1f, 1f, .4f));
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    loginBut.setBackground(new Color(1f, 1f, 1f, .5f));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    loginBut.setBackground(new Color(1f, 1f, 1f, .3f));
                }
            });

            loginBut.addActionListener(this);
            loginBut.setBorder(null);
            loginBut.setForeground(textColor);
            loginBut.setBackground(new Color(1f, 1f, 1f, .4f));
            loginBut.setBorderPainted(false);


        }
        return loginBut;
    }


    @Override
    public void setVisible(boolean visible) {
        if (makeVisible) {
            blackAlpha = 0.0f;
        }
        super.setVisible(visible);
        if (visible) {
            if (makeVisible) {
                startBlackFadeIn();
            } else {
                startFadeIn();
            }

        } else {
            getTimer().stop();
            if (loadingTimer != null && loadingTimer.isRunning()) {
                loadingTimer.stop();
            }
        }
    }

    public void setBlackAlpha(float blackAlpha) {
        this.blackAlpha = blackAlpha;
        repaint();
    }

    public float getBlackAlpha() {
        return blackAlpha;
    }

    private void startBlackFadeIn() {

        Animator timer = new Animator.Builder().setDuration(800, MILLISECONDS).setInterpolator(new AccelerationInterpolator(.7, .3)).build();
        timer.addTarget(PropertySetter.getTarget(this, "blackAlpha", 0.0f, 1.0f));
        timer.addTarget(new TimingTargetAdapter() {
            @Override
            public void end(Animator source) {
                startFadeIn();
            }

        });
        timer.start();
    }

    public Animator getBackImgAnimator() {

        if (backImgAnimator == null) {
            backImgAnimator = new Animator.Builder().setDuration(2000, MILLISECONDS).setInterpolator(new AccelerationInterpolator(.7, .3)).build();
            backImgAnimator.addTarget(PropertySetter.getTarget(this, "alpha", 0.0f, 1.0f));
        }
        return backImgAnimator;
    }


    private void startFadeIn() {

        Animator timer = new Animator.Builder().setDuration(800, MILLISECONDS).setInterpolator(new AccelerationInterpolator(.7, .3)).build();
        timer.addTarget(PropertySetter.getTarget(panel, "alpha", 0.001f, 1.0f));

        timer.addTarget(new TimingTargetAdapter() {
            @Override
            public void end(Animator source) {
                userTextField.getTextField().requestFocusInWindow();
                getTimer().start();
            }

        });
        timer.start();


    }

    private void startFadeOut() {

        Animator timer = new Animator.Builder().setDuration(1200, MILLISECONDS).setInterpolator(new AccelerationInterpolator(.7, .3)).build();
        timer.addTarget(PropertySetter.getTarget(panel, "alpha", 1.0f, 0.0f));
        timer.addTarget(PropertySetter.getTarget(this, "blackAlpha", 1.0f, 0.0f));
        timer.addTarget(new TimingTargetAdapter() {
            @Override
            public void end(Animator source) {
                TransitionManager.hideLoginOverlay();
            }

        });
        timer.start();

    }

    private void startWait() {

        if (loadingTimer == null) {
            loadingTimer = new Animator.Builder().setRepeatCount(Animator.INFINITE).setRepeatBehavior(Animator.RepeatBehavior.LOOP).setDuration(1200, MILLISECONDS).setInterpolator(new AccelerationInterpolator(.5, .5)).build();
            loadingTimer.addTarget(PropertySetter.getTarget(this, "loding", 0f, .5f, 1f));
        }

        loadingTimer.start();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        paintBackground(g2);
        g2.dispose();
    }


    private Rectangle getClipBounds() {
        Point point = clip.getLocation();
        point = SwingUtilities.convertPoint(clip, point, this);
        return new Rectangle(point.x, point.y, clip.getWidth(), clip.getHeight());
    }

    private void paintBackground(Graphics2D g2) {
        Composite composite = g2.getComposite();

        if (firstImage != null) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1 - alpha));
            g2.drawImage(firstImage, 0, 0, getWidth(), getHeight(), null);
        } else {
//            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, blackAlpha));
            g2.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), null);
        }
        if (secondImage != null) {

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.drawImage(secondImage, 0, 0, getWidth(), getHeight(), null);

        }

        g2.setComposite(composite);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getLoginBut().setEnabled(false);
        TransitionManager.getMainFrame().getRootPane().setDefaultButton(null);
        startWait();
        new LodingLoader(userTextField.getText(), passwordTextField.getText()).execute();
    }


    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;

        repaint();

    }

    public float getLoding() {
        return loding;
    }

    public void setLoding(float loding) {
        this.loding = loding;
        lodingPanel.repaint();
    }

    private class LodingLoader extends SwingWorker<Object, Object> {
        private String userid;
        private String pwd;

        public LodingLoader(String userId, String pwd) {
            this.userid = userId;
            this.pwd = pwd;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Object doInBackground() throws InterruptedException {

            Thread.sleep(5 * 1000);
            return userid;
        }

        @Override
        protected void done() {
            try {
                startFadeOut();
                TransitionManager.showTransitionPanel();
            } catch (Exception ignore) {
                ignore.printStackTrace();
            } finally {
                getLoginBut().setEnabled(true);
            }
        }
    }


    /**
     * @description:
     * @className: TitlePanel
     * @author: liwen
     * @date: 2018/12/16 13:32
     */
    class TitlePanel extends JPanel implements ActionListener {

        private WIconButton btnClose = createButton("", "fa-times");
        private WIconButton btnMax = createButton("", "fa-window-maximize");
        private WIconButton btnMin = createButton("", "fa-window-minimize");

        public TitlePanel() {

            initSwing();

        }

        private void initSwing() {

            JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            buttonPane.setOpaque(false);
            buttonPane.add(btnMin);
            buttonPane.add(btnMax);
            buttonPane.add(btnClose);
            btnClose.setBackground(Color.RED);
            this.setLayout(new BorderLayout());
            this.setOpaque(false);
            this.add(buttonPane, BorderLayout.EAST);

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
}
