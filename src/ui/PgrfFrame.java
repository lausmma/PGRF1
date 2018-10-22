package ui;

import drawables.Drawable;
import drawables.DrawableType;
import drawables.Line;
import utils.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PgrfFrame extends JFrame implements MouseMotionListener {

    private BufferedImage img;
    static int width = 800;
    static int height = 600;
    private JPanel panel;
    static int FPS = 1000 / 60;

    private utils.Renderer renderer;

    private int coorX;
    private int coorY;
    private int clickX;
    private int clickY;
    private int coorX1;
    private int coorY1;
    private Color color;

    private int count = 3;

    List<Point> list = new ArrayList<>();
    private int draggedX;
    private int draggedY;
    private boolean isDragged = false;

    private List<Drawable> drawables;
    private boolean isFirstClick = true;
    private DrawableType type = DrawableType.LINE;

    private int lap;
    private int ax;
    private int ay;
    private int radiusX;
    private int radiusY;
    private int centerX;
    private int centerY;

    private JLabel label;

    public static void main(String... args) {

        PgrfFrame pgrfFrame = new PgrfFrame();
        pgrfFrame.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        pgrfFrame.init(width, height);
    }

    private void init(int width, int height) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(width, height);
        setTitle("Počítačová grafika");
        setLocationRelativeTo(null);

        drawables = new ArrayList<>();

        panel = new JPanel();
        add(panel);

        panel.addMouseMotionListener(this);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //Zadávání úsečky
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (type == DrawableType.LINE) {
                        if (isFirstClick) {
                            clickX = e.getX();
                            clickY = e.getY();
                        } else {
                            drawables.add(new Line(clickX, clickY, e.getX(), e.getY()));
                        }
                        isFirstClick = !isFirstClick;
                    }
                }
                /*if(type == DrawableType.N_OBJECT){
                    //TODO
                }*/
                super.mouseClicked(e);
            }
        });

        renderer = new Renderer(img);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                draw();
            }
        }, 100, FPS);

        mouseEvents();
    }

    private void draw() {
        img.getGraphics().fillRect(0, 0, img.getWidth(), img.getHeight());

        drawNPolygon();
        renderer.drawPolygon(centerX, centerY, radiusX, radiusY, ax, ay);
        if (!isFirstClick) {
            renderer.lineDDA(coorX1, coorY1, coorX, coorY);
        }
        for (Drawable drawable : drawables) {
            drawable.draw(renderer);
        }

        getGraphics().drawImage(img, 0, 0, null);
        panel.paintComponents(getGraphics());
    }

    private void mouseEvents() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(SwingUtilities.isMiddleMouseButton(e)) {
                    list.add(new Point(e.getX(), e.getY()));
                }
                coorX1 = e.getX();
                coorY1 = e.getY();
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (lap == 0) {
                        ax = 0;
                        ay = 0;
                        radiusX = 0;
                        radiusY = 0;
                        lap++;
                    }
                    switch (lap) {
                        case 1:
                            centerX = e.getX();
                            centerY = e.getY();
                            radiusX = centerX;
                            radiusY = centerY;
                            lap++;
                            break;
                        case 2:
                            radiusX = e.getX();
                            radiusY = e.getY();
                            lap++;
                            break;
                        case 3:
                            ax = e.getX();
                            ay = e.getY();
                            lap = 0;
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        isDragged = true;
        draggedX = e.getX();
        draggedY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        coorX = e.getX();
        coorY = e.getY();
        switch (lap) {
            case 1:
                centerX = e.getX();
                centerY = e.getY();
                radiusX = centerX;
                radiusY = centerY;
                break;
            case 2:
                radiusX = e.getX();
                radiusY = e.getY();
                break;
            case 3:
                ax = e.getX();
                ay = e.getY();
                break;
        }
    }

    public void drawNPolygon() {
        if (list.size() == 1) {
            renderer.drawPixel((int) list.get(0).getX(), (int) list.get(0).getY(), color.RED);
        }
        if (list.size() > 1) {
            int lastX = (int) list.get(0).getX();
            int lastY = (int) list.get(0).getY();
            for (int i = 0; i < list.size(); i++) {
                renderer.lineDDA(lastX, lastY, (int) list.get(i).getX(), (int) list.get(i).getY());
                lastX = (int) list.get(i).getX();
                lastY = (int) list.get(i).getY();
            }
        }

        if (list.size() > 2) {
            renderer.lineDDA((int) list.get(0).getX(), (int) list.get(0).getY(), (int) list.get(list.size() - 1).getX(), (int) list.get(list.size() - 1).getY());
        }

        if (isDragged && list.size() > 1) {
            renderer.lineDDA((int) list.get(0).getX(), (int) list.get(0).getY(), draggedX, draggedY);
            renderer.lineDDA((int) list.get(list.size() - 1).getX(), (int) list.get(list.size() - 1).getY(), draggedX, draggedY);
        }
    }
}