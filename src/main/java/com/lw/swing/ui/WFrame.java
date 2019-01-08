package com.lw.swing.ui;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

/**
 * @description:
 * @className: WFrame
 * @author: liwen
 * @date: 2018/12/16 09:54
 */
public class WFrame extends JFrame {
    private final static Rectangle WINDOW_MIN = new Rectangle(250, 40);
    /**
     * Region from edges that dragging is active from.
     */
    private static final int BORDER_DRAG_THICKNESS = 5;

    /**
     * The amount of space (in pixels) that the cursor is changed on.
     */
    private static final int CORNER_DRAG_WIDTH = 16;

    private Cursor lastCursor =
            Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    /**
     * Maps from positions to cursor type. Refer to calculateCorner and
     * calculatePosition for details of this.
     */
    private static final int[] cursorMapping = new int[]
            {Cursor.NW_RESIZE_CURSOR, Cursor.NW_RESIZE_CURSOR, Cursor.N_RESIZE_CURSOR,
                    Cursor.NE_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR,
                    Cursor.NW_RESIZE_CURSOR, 0, 0, 0, Cursor.NE_RESIZE_CURSOR,
                    Cursor.W_RESIZE_CURSOR, 0, 0, 0, Cursor.E_RESIZE_CURSOR,
                    Cursor.SW_RESIZE_CURSOR, 0, 0, 0, Cursor.SE_RESIZE_CURSOR,
                    Cursor.SW_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR, Cursor.S_RESIZE_CURSOR,
                    Cursor.SE_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR
            };




    public WFrame() throws HeadlessException {

        initSwing();

        initListener();
    }

    private void initSwing() {
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getRootPane().setBorder(null);

    }

    private void initListener() {
        MouseInputHandler handler = new MouseInputHandler();
        this.addMouseListener(handler);
        this.addMouseMotionListener(handler);
    }


    /**
     * MouseInputHandler is responsible for handling resize/moving of
     * the Window. It sets the cursor directly on the Window when then
     * mouse moves over a hot spot.
     */
    private class MouseInputHandler implements MouseInputListener {
        /**
         * Set to true if the drag operation is moving the window.
         */
        private boolean isMovingWindow;

        /**
         * Used to determine the corner the resize is occuring from.
         */
        private int dragCursor;

        /**
         * X location the mouse went down on for a drag operation.
         */
        private int dragOffsetX;

        /**
         * Y location the mouse went down on for a drag operation.
         */
        private int dragOffsetY;

        /**
         * Width of the window when the drag started.
         */
        private int dragWidth;

        /**
         * Height of the window when the drag started.
         */
        private int dragHeight;

        @Override
        public void mousePressed(MouseEvent ev) {

            Point dragWindowOffset = ev.getPoint();
            Window w = (Window) ev.getSource();
            if (w != null) {
                w.toFront();
            }
            Point convertedDragWindowOffset = SwingUtilities.convertPoint(
                    w, dragWindowOffset, WFrame.this);

            Frame f = null;
            Dialog d = null;

            if (w instanceof Frame) {
                f = (Frame) w;
            } else if (w instanceof Dialog) {
                d = (Dialog) w;
            }

            int frameState = (f != null) ? f.getExtendedState() : 0;

            Rectangle titlePaneR = new Rectangle();
            titlePaneR.x = BORDER_DRAG_THICKNESS;
            titlePaneR.y = BORDER_DRAG_THICKNESS;
            titlePaneR.width = getWidth() - BORDER_DRAG_THICKNESS * 2;
            titlePaneR.height = getHeight() - BORDER_DRAG_THICKNESS * 2;
            if (titlePaneR.contains(convertedDragWindowOffset)) {
                if ((f != null && ((frameState & Frame.MAXIMIZED_BOTH) == 0)
                        || (d != null))) {
                    isMovingWindow = true;
                    dragOffsetX = dragWindowOffset.x;
                    dragOffsetY = dragWindowOffset.y;
                }
            } else if (f != null && f.isResizable()
                    && ((frameState & Frame.MAXIMIZED_BOTH) == 0)
                    || (d != null && d.isResizable())) {
                dragOffsetX = dragWindowOffset.x;
                dragOffsetY = dragWindowOffset.y;
                dragWidth = w.getWidth();
                dragHeight = w.getHeight();
                dragCursor = getCursor(calculateCorner(
                        w, dragWindowOffset.x, dragWindowOffset.y));
            }
        }

        @Override
        public void mouseReleased(MouseEvent ev) {
            if (dragCursor != 0 && WFrame.this != null && !WFrame.this.isValid()) {
                // Some Window systems validate as you resize, others won't,
                // thus the check for validity before repainting.
                WFrame.this.validate();
                getRootPane().repaint();
            }
            isMovingWindow = false;
            dragCursor = 0;
        }

        @Override
        public void mouseMoved(MouseEvent ev) {

            Window w = (Window) ev.getSource();

            Frame f = null;
            Dialog d = null;

            if (w instanceof Frame) {
                f = (Frame) w;
            } else if (w instanceof Dialog) {
                d = (Dialog) w;
            }

            // Update the cursor
            int cursor = getCursor(calculateCorner(w, ev.getX(), ev.getY()));

            if (cursor != 0 && ((f != null && (f.isResizable() &&
                    (f.getExtendedState() & Frame.MAXIMIZED_BOTH) == 0))
                    || (d != null && d.isResizable()))) {
                w.setCursor(Cursor.getPredefinedCursor(cursor));
            } else {
                w.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            }
        }

        private void adjust(Rectangle bounds, Dimension min, int deltaX,
                            int deltaY, int deltaWidth, int deltaHeight) {
            bounds.x += deltaX;
            bounds.y += deltaY;
            bounds.width += deltaWidth;
            bounds.height += deltaHeight;
            if (min != null) {
                if (bounds.width < min.width) {
                    int correction = min.width - bounds.width;
                    if (deltaX != 0) {
                        bounds.x -= correction;
                    }
                    bounds.width = min.width;
                }
                if (bounds.height < min.height) {
                    int correction = min.height - bounds.height;
                    if (deltaY != 0) {
                        bounds.y -= correction;
                    }
                    bounds.height = min.height;
                }
            }
        }

        public void mouseDragged(MouseEvent ev) {
            Window w = (Window) ev.getSource();
            Point pt = ev.getPoint();

            if (isMovingWindow) {
                Point eventLocationOnScreen = ev.getLocationOnScreen();
                w.setLocation(eventLocationOnScreen.x - dragOffsetX,
                        eventLocationOnScreen.y - dragOffsetY);
            } else if (dragCursor != 0) {
                Rectangle r = w.getBounds();
                Rectangle startBounds = new Rectangle(r);
                Dimension min = new Dimension(150, 64);

                switch (dragCursor) {
                    case Cursor.E_RESIZE_CURSOR:
                        adjust(r, min, 0, 0, pt.x + (dragWidth - dragOffsetX) -
                                r.width, 0);
                        break;
                    case Cursor.S_RESIZE_CURSOR:
                        adjust(r, min, 0, 0, 0, pt.y + (dragHeight - dragOffsetY) -
                                r.height);
                        break;
                    case Cursor.N_RESIZE_CURSOR:
                        adjust(r, min, 0, pt.y - dragOffsetY, 0,
                                -(pt.y - dragOffsetY));
                        break;
                    case Cursor.W_RESIZE_CURSOR:
                        adjust(r, min, pt.x - dragOffsetX, 0,
                                -(pt.x - dragOffsetX), 0);
                        break;
                    case Cursor.NE_RESIZE_CURSOR:
                        adjust(r, min, 0, pt.y - dragOffsetY,
                                pt.x + (dragWidth - dragOffsetX) - r.width,
                                -(pt.y - dragOffsetY));
                        break;
                    case Cursor.SE_RESIZE_CURSOR:
                        adjust(r, min, 0, 0,
                                pt.x + (dragWidth - dragOffsetX) - r.width,
                                pt.y + (dragHeight - dragOffsetY) -
                                        r.height);
                        break;
                    case Cursor.NW_RESIZE_CURSOR:
                        adjust(r, min, pt.x - dragOffsetX,
                                pt.y - dragOffsetY,
                                -(pt.x - dragOffsetX),
                                -(pt.y - dragOffsetY));
                        break;
                    case Cursor.SW_RESIZE_CURSOR:
                        adjust(r, min, pt.x - dragOffsetX, 0,
                                -(pt.x - dragOffsetX),
                                pt.y + (dragHeight - dragOffsetY) - r.height);
                        break;
                    default:
                        break;
                }
                if (!r.equals(startBounds)) {
                    w.setBounds(r);
                    // Defer repaint/validate on mouseReleased unless dynamic
                    // layout is active.
                    if (Toolkit.getDefaultToolkit().isDynamicLayoutActive()) {
                        w.validate();
                        getRootPane().repaint();
                    }
                }
            }
        }

        public void mouseEntered(MouseEvent ev) {
            Window w = (Window) ev.getSource();
            lastCursor = w.getCursor();
            mouseMoved(ev);
        }

        public void mouseExited(MouseEvent ev) {
            Window w = (Window) ev.getSource();
            w.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        public void mouseClicked(MouseEvent ev) {
            Window w = (Window) ev.getSource();
            Frame f = null;

            if (w instanceof Frame) {
                f = (Frame) w;
            } else {
                return;
            }


            int state = f.getExtendedState();
            if ((ev.getClickCount() % 2) == 0 &&
                    ((ev.getModifiers() & InputEvent.BUTTON1_MASK) != 0)) {
                if (f.isResizable()) {
                    if ((state & Frame.MAXIMIZED_BOTH) != 0) {
                        f.setExtendedState(state & ~Frame.MAXIMIZED_BOTH);
                    } else {
                        f.setExtendedState(state | Frame.MAXIMIZED_BOTH);
                    }
                    return;
                }
            }
        }

        /**
         * Returns the corner that contains the point <code>x</code>,
         * <code>y</code>, or -1 if the position doesn't match a corner.
         */
        private int calculateCorner(Window w, int x, int y) {
            Insets insets = w.getInsets();
            int xPosition = calculatePosition(x - insets.left,
                    w.getWidth() - insets.left - insets.right);
            int yPosition = calculatePosition(y - insets.top,
                    w.getHeight() - insets.top - insets.bottom);

            if (xPosition == -1 || yPosition == -1) {
                return -1;
            }
            return yPosition * 5 + xPosition;
        }

        /**
         * Returns the Cursor to render for the specified corner. This returns
         * 0 if the corner doesn't map to a valid Cursor
         */
        private int getCursor(int corner) {
            if (corner == -1) {
                return 0;
            }
            return cursorMapping[corner];
        }

        /**
         * Returns an integer indicating the position of <code>spot</code>
         * in <code>width</code>. The return value will be:
         * 0 if < BORDER_DRAG_THICKNESS
         * 1 if < CORNER_DRAG_WIDTH
         * 2 if >= CORNER_DRAG_WIDTH && < width - BORDER_DRAG_THICKNESS
         * 3 if >= width - CORNER_DRAG_WIDTH
         * 4 if >= width - BORDER_DRAG_THICKNESS
         * 5 otherwise
         */
        private int calculatePosition(int spot, int width) {
            if (spot < BORDER_DRAG_THICKNESS) {
                return 0;
            }
            if (spot < CORNER_DRAG_WIDTH) {
                return 1;
            }
            if (spot >= (width - BORDER_DRAG_THICKNESS)) {
                return 4;
            }
            if (spot >= (width - CORNER_DRAG_WIDTH)) {
                return 3;
            }
            return 2;
        }
    }


}
