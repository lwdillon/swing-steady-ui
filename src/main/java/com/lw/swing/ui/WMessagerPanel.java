package com.lw.swing.ui;

import com.lw.swing.utils.IconFont;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.PropertySetter;
import org.jdesktop.core.animation.timing.TimingSource;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;
import org.jdesktop.core.animation.timing.interpolators.AccelerationInterpolator;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by liwen on 2017/6/15.
 */
public class WMessagerPanel extends JPanel {

    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_INFO = 1;
    public static final int TYPE_SUCCESS = 2;
    public static final int TYPE_WARNING = 3;
    public static final int TYPE_IMPORTANT = 4;
    public static final int TYPE_ESPECIALLY = 5;
    public static final int TYPE_DANGER = 6;

    private static Color DEFAULT_COLOR = new Color(0x353535);
    private static Color INFO_COLOR = new Color(0x03b8cf);
    private static Color SUCCESS_COLOR = new Color(0x38b03f);
    private static Color WARNING_COLOR = new Color(0xf1a325);
    private static Color IMPORTANT_COLOR = new Color(0xbd7b46);
    private static Color ESPECIALLY_COLOR = new Color(0x8666b8);
    private static Color DANGER_COLOR = new Color(0xea644a);


    private int paddingX = 15;
    private int paddingY = 10;
    private boolean showColse = true;
    private String colseIcon = IconFont.getIcon("fa-times");
    private Animator animationStart;
    private Animator animationEnd;
    private Animator animationClose;
    private double scale = 0d;
    private float alpha = 0f;
    private int preW = 0, preH = 0;
    private Shape closeShape;

    private Color background = new Color(0x353535);
    private Color foreground = new Color(0xffffff);
    private Color colseColor = background;
    private String iconfont = IconFont.getIcon("fa-bell");
    private String text = "message...";
    private int type = TYPE_DEFAULT;

    /**
     * 图标与文字间隔
     */
    private int interval = 15;

    public WMessagerPanel() {
        initSwing();
        initAnimator();
        initListener();
    }

    private void initSwing() {
        setBackground(background);
        setForeground(foreground);
        setOpaque(false);
    }

    public void start() {
        animationStart.restart();
    }

    public void stop() {
        if (!animationEnd.isRunning()) {
            animationEnd.start();
        }
    }

    @Override
    public boolean contains(int x, int y) {
        FontMetrics fontMetrics = getFontMetrics(getFont());
        int iconWidth = getIconWidth();
        int textWidth = fontMetrics.stringWidth(getText());
        int colseIconWidth = showColse ? fontMetrics.stringWidth(colseIcon) : 0;

        int iW = paddingX + iconWidth + (iconWidth == 0 ? 0 : interval);
        int tW = textWidth + (colseIconWidth == 0 ? 0 : interval);
        int cW = colseIconWidth + 24;
        int w = iW + tW + cW;
        int h = paddingY * 2 + fontMetrics.getHeight();

        int rx = (getWidth() - w) / 2;
        int ry = (getHeight() - h) / 2;
        int rw = rx + w;
        int rh = ry + h;

        if (x > rx && x < rw && y > ry && y < rh) {
            return true;
        } else {
            return false;
        }
    }

    private void initListener() {

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (closeShape == null) return;

                if (closeShape.getBounds().contains(e.getPoint())) {
                    if (!animationEnd.isRunning()) {
                        animationEnd.start();
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (closeShape == null) return;
                if (closeShape.getBounds().contains(e.getPoint())) {
                    setColseColor(getBackground().darker());
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    setColseColor(getBackground());
                    setCursor(Cursor.getDefaultCursor());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
            }
        };
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
    }

    private void initAnimator() {
        animationClose = new Animator.Builder().setStartDelay(2000, MILLISECONDS).setDuration(1, MILLISECONDS).build();
        animationClose.addTarget(new TimingTargetAdapter() {
            @Override
            public void end(Animator source) {
                if (!animationEnd.isRunning()) animationEnd.start();
            }
        });
        animationStart = new Animator.Builder().setDuration(300, MILLISECONDS).setInterpolator(new AccelerationInterpolator(.3f, .3f)).build();
        animationStart.addTarget(PropertySetter.getTarget(WMessagerPanel.this, "scale", 0.5f, 1.1f, 1f));
        animationStart.addTarget(PropertySetter.getTarget(WMessagerPanel.this, "alpha", 0f, 1f));
        animationStart.addTarget(new TimingTargetAdapter() {
            @Override
            public void end(Animator source) {
                animationClose.restart();
            }

            @Override
            public void begin(Animator source) {
                animationClose.cancel();
                setVisible(true);
            }
        });
        animationEnd = new Animator.Builder().setDuration(300, MILLISECONDS).setInterpolator(new AccelerationInterpolator(.3, .7)).build();
        animationEnd.addTarget(PropertySetter.getTarget(WMessagerPanel.this, "scale", 1f, .5f));
        animationEnd.addTarget(PropertySetter.getTarget(WMessagerPanel.this, "alpha", 1f, 0f));
        animationEnd.addTarget(new TimingTargetAdapter() {
            @Override
            public void end(Animator source) {
                setVisible(false);
                animationClose.cancel();
                if (getParent() != null && getParent() instanceof JPanel) {
                    Container container = getParent();
                    container.remove(WMessagerPanel.this);
                    if (container.getComponentCount() < 1) {
                        TransitionManager.killOverlay();
                    }
                }

            }
        });
    }

    public void showMessage(String message) {

        showMessage(message, TYPE_DEFAULT);
    }

    public void showMessage(String message, int type) {
        try {
            this.text = message;
            switch (type) {

                case TYPE_SUCCESS:
                    setIconfont("fa-check-circle");
                    setBackground(SUCCESS_COLOR);
                    break;
                case TYPE_INFO:
                    setIconfont("fa-info-circle");
                    setBackground(INFO_COLOR);
                    break;
                case TYPE_DANGER:
                    setIconfont("fa-exclamation-circle");
                    setBackground(DANGER_COLOR);
                    break;
                case TYPE_WARNING:
                    setIconfont("fa-exclamation-triangle");
                    setBackground(WARNING_COLOR);
                    break;
                case TYPE_ESPECIALLY:
                    setIconfont("");
                    setBackground(ESPECIALLY_COLOR);
                    break;
                case TYPE_IMPORTANT:
                    setIconfont("");
                    setBackground(IMPORTANT_COLOR);
                    break;
                default:
                    setIconfont("fa-comment-dots");
                    setBackground(DEFAULT_COLOR);
            }
            colseColor = getBackground();
            FontMetrics fontMetrics = getFontMetrics(getFont());
            int iconWidth = getIconWidth();
            int textWidth = fontMetrics.stringWidth(getText());
            int colseIconWidth = showColse ? fontMetrics.stringWidth(colseIcon) : 0;

            int iW = paddingX + iconWidth + (iconWidth == 0 ? 0 : interval);
            int tW = textWidth + (colseIconWidth == 0 ? 0 : interval);
            int cW = colseIconWidth + 24;
            int rh = paddingY * 2 + fontMetrics.getHeight();
            int w = iW + tW + cW;
            int h = rh;
            setCloseShape(new Rectangle((getWidth() - w) / 2 + iW + tW, (getHeight() - rh) / 2, cW, h));
            setVisible(true);
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getIconWidth(){
        FontMetrics fontMetrics = getFontMetrics(getFont());
        return StringUtils.isNotBlank(getIconfont())?fontMetrics.stringWidth(getIconfont()):0;
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fontMetrics = getFontMetrics(getFont());
        int iconWidth = getIconWidth();
        int textWidth = fontMetrics.stringWidth(getText());
        int colseIconWidth = showColse ? fontMetrics.stringWidth(colseIcon) : 0;

        int iW = paddingX + iconWidth + (iconWidth == 0 ? 0 : interval);
        int tW = textWidth + (colseIconWidth == 0 ? 0 : interval);
        int cW = colseIconWidth + 24;
        int h = paddingY * 2 + fontMetrics.getHeight();
        int w = iW + tW + cW;

        return new Dimension(w, h);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();
        FontMetrics fontMetrics = getFontMetrics(getFont());
        AffineTransform affineTransform = g2.getTransform();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int iconWidth = getIconWidth();
        int textWidth = fontMetrics.stringWidth(getText());
        int colseIconWidth = showColse ? fontMetrics.stringWidth(colseIcon) : 0;

        int iW = paddingX + iconWidth + (iconWidth == 0 ? 0 : interval);
        int tW = textWidth + (colseIconWidth == 0 ? 0 : interval);
        int cW = colseIconWidth + 24;
        int rh = paddingY * 2 + fontMetrics.getHeight();
        int w = iW + tW + cW;
        int h = rh;
        if (getWidth() != preW || getHeight() != preH) {
            setCloseShape(new Rectangle((getWidth() - w) / 2 + iW + tW, (getHeight() - rh) / 2, cW, h));
            this.preH = getHeight();
            this.preW = getWidth();
        }

        g2.translate(getWidth() / 2, getHeight() / 2);
        g2.scale(scale, scale);
        g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
        int x = -w / 2;
        int y = -h / 2;
        int textY = y + fontMetrics.getAscent() + paddingY;

        g2.setColor(getBackground());
        g2.fillRect(x, y, iW + tW, rh);
        g2.setColor(colseColor);
        g2.fillRect(x + iW + tW, y, cW, rh);

        g2.setColor(getForeground());
        g2.drawString(getText(), x + iW, textY);
        g2.setFont(IconFont.ICON_FONT);
        if (StringUtils.isNotBlank(getIconfont())) {
            g2.drawString(getIconfont(), x + paddingX, textY);

        }
        g2.drawString(colseIcon, x + iW + tW + 12, textY);
        g2.setTransform(affineTransform);

        g2.dispose();
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
        repaint();
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        repaint();
    }

    public String getIconfont() {
        return iconfont;
    }

    public void setIconfont(String iconfont) {
        this.iconfont = IconFont.getIcon(iconfont);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Shape getCloseShape() {
        return closeShape;
    }

    public void setCloseShape(Shape closeShape) {
        this.closeShape = closeShape;
    }

    public void setColseColor(Color colseColor) {
        this.colseColor = colseColor;
        repaint();
    }

    public static void main(String[] args) {

        TimingSource ts = new SwingTimerTimingSource();
        Animator.setDefaultTimingSource(ts);
        ts.init();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Test");
                frame.setSize(1000, 400);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JButton jb = new JButton("fdsa");
                final WMessagerPanel centerPanel = new WMessagerPanel();

                jb.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        centerPanel.showMessage("普通消息fdsafdsafdsa", new Random().nextInt(7));
                    }
                });
                frame.add(jb, BorderLayout.NORTH);
                frame.add(centerPanel);
                frame.setVisible(true);

                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowOpened(WindowEvent e) {

                    }
                });


            }
        });
    }
}
