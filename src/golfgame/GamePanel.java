/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package golfgame;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import static java.awt.event.KeyEvent.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Max
 */
public class GamePanel extends JPanel implements MouseListener{
    
    private final GolfGame gameFrame; //There can be only one Game Frame.
    private ArrayList<GraphicsObject> renderList;
    private long lastRender;
    private boolean play;
    private Course course;
    private Club club;
    private int hole = 0;
    private long dt = 0;
    private final long frameLength = 16666666/2;
    private long nextHole;
    private boolean moving = false;
    private boolean rendering = false;
    public static int metio=0;
    
    public GamePanel(){
        this.gameFrame = (GolfGame)(this.getParent());
        this.setDoubleBuffered(true);
        this.addMouseListener(this);
        renderList = new ArrayList<GraphicsObject>();
        nextHole = -1;
    }
    
    public void beginGame(){
        this.setFocusable(true);
        this.requestFocusInWindow();
        play = true;
        /*this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    MenuPanel.pausar=true;
                    //play = false;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });*/
        KeyListener presiono = new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }

			@Override
			public void keyPressed(KeyEvent e) {
                            if(e.getKeyCode() == KeyEvent.VK_SPACE)
                            {
                                if(MenuPanel.pausar == false)
                                {
                                    MenuPanel.pausar = true;
                                }else if(MenuPanel.pausar == true)
                                {
                                    MenuPanel.pausar = false;
                                }
                                    
                            }
                            if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                            {
                                GolfGame._gg.endGame();
                            }
			}

			@Override
			public void keyReleased(KeyEvent e) { }
		};
		addKeyListener(presiono);
		setFocusable(true);
        lastRender = System.nanoTime();
        try{
            course = new Course(hole);
            club = new Club(course);
            renderList.add(course);
            renderList.add(club);
        }
        catch(Exception e){
            GolfGame._gg.endGame();
            return;
        }
        Thread t = new Thread(){
            private long sleepNano;
            private long sleepMilli;
            private int remainder;
            @Override
            public void run(){
                while(play){
                    if(!(moving || rendering)) repaint();
                    if(nextHole != -1){
                        if(nextHole < System.nanoTime() && !rendering){
                            nextHole = -1;
                            moving = true;
                            nextHole();
                        }
                    }
                    try {
                        sleepNano = (frameLength - dt);
                        sleepMilli = sleepNano/GolfGame.NANO_TO_MILLI;
                        if(sleepMilli > 0){
                            remainder = (int)(sleepNano / (sleepMilli * GolfGame.NANO_TO_MILLI));
                            Thread.sleep(sleepMilli,remainder);
                        }
                    } catch (InterruptedException ex) { }
                }
            }
        };
        t.start();
    }
    public void ballInHole(){
        course.ballToHole();
        metio=1; //MetioBola1
        Club.jugador=1; //Cambiamos a solo jugar con el 2
        if(Hole.BolasMetidas>=2){
            Hole.BolasMetidas=0;
            metio=0;
            nextHole = System.nanoTime() + GolfGame.NANO_TO_MILLI * 2500;
        }
    }
    public void ball2InHole(){
        course.ball2ToHole();
        metio=2; //MetioBola2
        Club.jugador=2; //Cambiamos a solo jugar con el 1
        if(Hole.BolasMetidas>=2){
            Hole.BolasMetidas=0;
            metio=0;
            nextHole = System.nanoTime() + GolfGame.NANO_TO_MILLI * 2500;
        }
    }
    public void nextHole(){
        try{
            MenuPanel.pausar=false;
            course = new Course(++hole);
        }
        catch(Exception e){
            GolfGame._gg.endGame();
            return;
        }
        club = new Club(course);
        renderList.clear();
        renderList.add(course);
        renderList.add(club);
        moving = false;
    }
    @Override
    public void paintComponent(Graphics g){
        rendering = true;
        super.paintComponent(g);
        dt = System.nanoTime() - lastRender;
        Point mouse = this.getMousePosition();
        Graphics2D g2 = (Graphics2D)g;
        
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        
        double scaleX = GolfGame._gg.getContentPane().getWidth() / 800.0;
        double scaleY = GolfGame._gg.getContentPane().getHeight() / 600.0;
        g2.scale(scaleX, scaleY);
        if(!moving)
            for(GraphicsObject obj : renderList){
                obj.draw(g2,dt);
            }
        if(nextHole != -1 && MenuPanel.jugadores==1){
            g2.setColor(Color.WHITE);
            Font tmpFnt = new Font("Verdana",Font.BOLD,64);
            g2.setFont(tmpFnt);
            String tmpstr = "Lo hiciste!";
            if(course.getStrokes() == 1 && course.getPar() != 1){
                tmpstr = "Hoyo en 1!";
            }
            else{
                int under = course.getPar() - course.getStrokes();
                switch(under){
                    case -2:
                        tmpstr = "Double Bogey :(";
                        break;
                    case -1:
                        tmpstr = "Ouch, Bogey!";
                        break;
                    case 0:
                        tmpstr = "Par!";
                        break;
                    case 1:
                        tmpstr = "Nice Birdie!";
                        break;
                    case 2:
                        tmpstr = "Nice Eagle!";
                        break;
                    case 3:
                        g2.setFont(new Font("Verdana", Font.BOLD,48));
                        tmpstr = "Maldita sea, un albatros!";
                        break;
                }
                if(under < -2){
                    g2.setFont(new Font("Verdana",Font.BOLD,48));
                    tmpstr = "Probablemente el golf no sea para ti...";
                }
            }
            g2.drawString(tmpstr, 400 - g2.getFontMetrics().stringWidth(tmpstr)/2, 350);
        }
        if(mouse != null){
            club.setXY((int)(mouse.x / scaleX),(int)(mouse.y / scaleY));
        }
        lastRender = System.nanoTime();
        rendering = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        club.handleClick();
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
}
