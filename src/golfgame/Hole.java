/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package golfgame;

import static golfgame.MenuPanel.jugadores;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Max
 */
public class Hole implements GraphicsObject {
    private double x;
    private double y;
    private Course course;
    private boolean check1 = true, check2 = true;
    public static int BolasMetidas; 
    public Hole(int x, int y,Course course){
        this.x = x;
        this.y = y;
        this.course = course;
    }
    public int getX(){
        return (int)this.x;
    }
    public int getY(){
        return (int)this.y;
    }
    @Override
    public void draw(Graphics2D g, long interval) {
        if(check1 && Math.abs(course.getBallX() - x) < 8 && Math.abs(course.getBallY()- y) < 8 && GamePanel.metio!=1){
            BolasMetidas++;
            if(jugadores==1 || jugadores==3)
                BolasMetidas=2;
            if(jugadores==3)
                MenuPanel.pausar=true;
            GolfGame._gg.gamePanel.ballInHole();
            check1 = false;
        }
        if(jugadores==2){
            if(check2 && Math.abs(course.getBall2X()-x) < 8 && Math.abs(course.getBall2Y()-y)< 8 && GamePanel.metio!=2){
                BolasMetidas++;
                GolfGame._gg.gamePanel.ball2InHole();
                check2 = false;
            }
        }
        g.setColor(new Color(30,10,0));
        g.fillOval((int)x - 10, (int)y - 10, 20, 20);
    }
    
}
