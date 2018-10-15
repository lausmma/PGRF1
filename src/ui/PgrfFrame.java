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

public class PgrfFrame extends JFrame implements MouseMotionListener{

    private BufferedImage img;
    static int width = 800;
    static int height = 600;
    private JPanel panel;
    static int FPS = 1000/60;

    private utils.Renderer renderer;

    private int coorX;
    private int coorY;
    private int clickX;
    private int clickY;

    private int count = 5;

    List<Point> list = new ArrayList<>();
    private int draggedX;
    private int draggedY;
    private boolean isDragged = false;

    private List<Drawable> drawables;
    private boolean isFirstClick = true;
    private DrawableType type = DrawableType.LINE;

    public static void main(String... args){

        PgrfFrame pgrfFrame = new PgrfFrame();
        pgrfFrame.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        pgrfFrame.init(width, height);
    }

    private void init(int width, int height){
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
                if(type == DrawableType.LINE) {
                    if (isFirstClick) {
                        clickX = e.getX();
                        clickY = e.getY();
                        //isFirstClick = false;
                    } else {
                        drawables.add(new Line(clickX, clickY, e.getX(), e.getY()));
                    }
                    isFirstClick = !isFirstClick;
                }
                if(type == DrawableType.N_OBJECT){
                    //TODO N-úhelník
                }

                super.mouseClicked(e);
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_UP){
                    // + šipka nahoru
                    count++;
                }
                if(e.getKeyCode() == KeyEvent.VK_DOWN){
                    // - šipa dolů
                    count--;
                    if(count < 3){
                        count = 3;
                    }
                }
                super.keyReleased(e);
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

    private void draw(){
        img.getGraphics().fillRect(0, 0, img.getWidth(), img.getHeight());

        //drawMPolygon();

        for (Drawable drawable : drawables) {
            drawable.draw(renderer);
        }

        getGraphics().drawImage(img, 0, 0,null);
        panel.paintComponents(getGraphics());
    }

    private void mouseEvents() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                list.add(new Point(e.getX(), e.getY()));
                isDragged = false;
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
    }

    public void drawMPolygon(){
        if(list.size() == 1){
            renderer.drawPixel((int)list.get(0).getX(), (int)list.get(0).getY());
        }
        if(list.size() > 1){
            int lastX = (int)list.get(0).getX();
            int lastY = (int)list.get(0).getY();
            for(int i = 0; i < list.size(); i++){
                renderer.lineDDA(lastX, lastY, (int)list.get(i).getX(), (int)list.get(i).getY());
                lastX = (int)list.get(i).getX();
                lastY = (int)list.get(i).getY();
            }
        }

        if(list.size() > 2){
            renderer.lineDDA((int)list.get(0).getX(), (int)list.get(0).getY(), (int)list.get(list.size()-1).getX(), (int)list.get(list.size()-1).getY());
        }

        if(isDragged && list.size() > 1){
            renderer.color = Color.green.getRGB();
            renderer.lineDDA((int)list.get(0).getX(), (int)list.get(0).getY(), draggedX, draggedY);
            renderer.lineDDA((int)list.get(list.size()-1).getX(), (int)list.get(list.size()-1).getY(), draggedX, draggedY);
        }
        renderer.color = Color.RED.getRGB();
    }
}