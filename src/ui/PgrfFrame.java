package ui;

import utils.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class PgrfFrame extends JFrame implements MouseMotionListener {

    private BufferedImage img;
    static int width = 800;
    static int height = 600;
    private JPanel panel;
    static int FPS = 1000/60;
    private utils.Renderer renderer;
    private int coorX;
    private int coorY;
    private int clickX = 300;
    private int clickY = 300;
    private int count = 5; //TODO - nesmí klesnout pod 3

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

        panel = new JPanel();
        add(panel);

        panel.addMouseMotionListener(this);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickX = e.getX();
                clickY = e.getY();
                super.mouseClicked(e);
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_UP){
                    //šipka nahoru
                    count++;
                }
                if(e.getKeyCode() == KeyEvent.VK_DOWN){
                    //minus na numerické klávesnici
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

        draw();
    }

    private void draw(){
        img.getGraphics().fillRect(0, 0, img.getWidth(), img.getHeight());

        renderer.lineDDA(clickX, clickY, coorX, coorY);
        renderer.drawPolygon(clickX, clickY, coorX, coorY, count);

        getGraphics().drawImage(img, 0, 0,null);
        panel.paintComponents(getGraphics());
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        coorX = e.getX();
        coorY = e.getY();
    }
}