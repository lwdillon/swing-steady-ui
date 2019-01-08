package com.lw.swing.ui;


import javax.swing.*;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WTree extends JTree {

    public TreePath mouseInPath;
    public boolean mouse = false;

    private Color backgroundSelectionColor =Color.RED;

    public WTree(TreeModel newModel) {
        super(newModel);
        initMotionListener();
        this.setOpaque(false);
    }

    public WTree() {
        this(getDefaultTreeModel());
    }

    private void initMotionListener() {

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                TreePath path = getClosestPathForLocation(e.getX(), e.getY());


                if (path == null) {
                    return;
                }
                Rectangle newRect = getPathBounds(path);
                newRect.x = 0;
                newRect.width = getWidth();


                Rectangle oldRect = new Rectangle();

                if (oldRect == null||newRect==null) {
                    return;
                }
                if (mouseInPath != null) {
                    oldRect = getPathBounds(mouseInPath);
                    oldRect.x = 0;
                    oldRect.width = getWidth();
                }

                boolean b = e.getY() - newRect.getY() > newRect.getHeight();

                if (!b) {
                    if (mouseInPath != null) {

                        mouseInPath = path;
                        repaint(newRect.union(oldRect));
                    } else {
                        mouseInPath = path;
                        repaint(newRect);
                    }
                } else if (mouseInPath != null) {

                    mouseInPath = null;
                    repaint(oldRect);
                }

            }


        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                TreePath path = getClosestPathForLocation(e.getX(), e.getY());

                if (path != null) {
                    setSelectionPath(path);
                }
                super.mousePressed(e);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                mouse = true;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                mouse = false;

                if (mouseInPath != null) {
                    Rectangle oldRect = getPathBounds(mouseInPath);
                    oldRect.x = 0;
                    oldRect.width = getWidth();
                    repaint(oldRect);
                }
            }

        });
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        if (getSelectionPath() != null) {
            Rectangle selRect = getPathBounds(getSelectionPath());
            if (selRect != null) {
                g2.setColor(getBackgroundSelectionColor());
                g2.fillRect(0, selRect.y, getWidth(), selRect.height);

            }
        }


        if (mouse && mouseInPath != null) {
            Rectangle bounds = getPathBounds(mouseInPath);
            g2.setComposite(AlphaComposite.SrcOver.derive(.8f));
            g2.setColor(getBackgroundSelectionColor());
            g2.fillRect(0, bounds.y, getWidth(), bounds.height);
            g2.dispose();

        }


        super.paintComponent(g);


    }

    @Override
    public void setModel(TreeModel newModel) {
        super.setModel(newModel);
        mouseInPath = null;
    }

    public Color getBackgroundSelectionColor() {
        return backgroundSelectionColor;
    }

    public void setBackgroundSelectionColor(Color backgroundSelectionColor) {
        this.backgroundSelectionColor = backgroundSelectionColor;
    }
}
