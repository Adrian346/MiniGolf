/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package golfgame;

import static golfgame.Course.num;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Transparency;

/**
 *
 * @author Max
 */
public class Ball implements GraphicsObject {
    private final int BALL_RADIUS = 4;
    private double x;
    private double y;
    public static double vx;
    public static double vy;
    private double ax;
    private double ay;
    private double frix;
    private int lastI;
    private Course course;
    private double apothem;
    
    public static int cambiaDireccion=0;
    public static int saliendo=0;
    public static int subio=0;
    public static int bajo=0;
    public static int subiendo=0;
    
    public Ball(int x, int y,Course course){
        this.x = x;
        this.y = y;
        vx = 0;
        vy = 0;
        ax = 0;
        ay = 0;
        frix = 0.8;
        lastI = -1;
        this.course = course;
    }
    public void setXVelocity(double vx){
        this.vx = vx;
        this.lastI = -1;
    }
    public void setYVelocity(double vy){
        this.vy = vy;
    }
    public int getX(){
        return (int)this.x;
    }
    public int getY(){
        return (int)this.y;
    }
    private void checkCollision(long interval, Graphics2D g){
        int[] xcoords = course.getPolygonShape().xpoints;
        int[] ycoords = course.getPolygonShape().ypoints;
        if(num+1==1 || num+1==8 || num+1==4 || num+1==6 || num+1==9 || num+1==7 || num+1==10 || num+1==11){
            int[] xmovible = course.getPolygonMovible().xpoints;
            int[] ymovible = course.getPolygonMovible().ypoints;
            comprobarColision(interval, g, xmovible, ymovible);
       }
        if( num+1==4){
            int[] xmovible2 = course.getPolygonMovible2().xpoints;
            int[] ymovible2 = course.getPolygonMovible2().ypoints;
            comprobarColision(interval, g, xmovible2, ymovible2);
       }
        if(num+1==5){
            int[] xsubida = course.getPolygonSubida().xpoints;
            int[] ysubida = course.getPolygonSubida().ypoints;
            comprobarColisionSubidaBajada(interval, g, xsubida, ysubida, 1);//1 = subida   
        }
        if(num+1==3){
            int[] xbajada = course.getPolygonBajada().xpoints;
            int[] ybajada = course.getPolygonBajada().ypoints;
            comprobarColisionSubidaBajada(interval, g, xbajada, ybajada, 0); //0 = bajada  
        }
        comprobarColision(interval, g, xcoords, ycoords);
    }
     public void comprobarColision(long interval, Graphics2D g, int []coordsx, int []coordsy){   
         double x1,y1,x2,y2;
        double vmag = Math.sqrt(vy*vy + vx*vx);
        double vdir = Math.atan2(vy,vx);
        for(int i = 0; i < coordsx.length; i++){
            
            x1 = coordsx[i]; y1 = coordsy[i];
            if(i+1 < coordsx.length){
                x2 = coordsx[i+1]; y2 = coordsy[i+1];
            }
            else{
                x2 = coordsx[0]; y2 = coordsy[0];
            }
            double area = Math.abs((x2 - x1)*(y - y1) - (x - x1)*(y2-y1));
            double lineln = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
            apothem = area/lineln;
            double linedir = Math.atan2(y2-y1, x2-x1);
            double wallNormal = linedir + Math.PI/2;
            double reflection = Math.PI + (vdir + (2.0 * (Math.PI * 2.0 - (vdir - wallNormal))));
            // This took a lot of trial and error. But it works. Finally
            
            if(i != lastI){
                if(apothem < BALL_RADIUS && 
                        (
                            (
                                ((x1 < x && x < x2) || (x2 < x && x < x1)) && 
                                ((y1 < y && y < y2) || (y2 < y && y < y1))
                            )
                            ||
                            ((x1 < x && x < x2) || (x2 < x && x < x1))
                            ||
                            ((y1 < y && y < y2) || (y2 < y && y < y1))
                        )
                ){ //collision
                    vx = vmag*Math.cos(reflection);
                    vy = vmag*Math.sin(reflection);
                    lastI = i;
                    subio=0;
                    bajo=0;
                    saliendo=0;
                    cambiaDireccion=0;
                    break;
                }
            }
        }
    }
     public void comprobarColisionSubidaBajada(long interval, Graphics2D g, int []coordsx, int []coordsy, int cuesta){//cuesta =0 bajada, cuesta==1 subida   
        double x1,y1,x2,y2;
        double vmag = Math.sqrt(vy*vy + vx*vx);
        double vdir = Math.atan2(vy,vx);
        if(bajo==0 && subio==0 && saliendo==0 && cambiaDireccion==0)
            lastI = -1;
        for(int i = 0; i < coordsx.length; i++){
            
            x1 = coordsx[i]; y1 = coordsy[i];
            if(i+1 < coordsx.length){
                x2 = coordsx[i+1]; y2 = coordsy[i+1];
            }
            else{
                x2 = coordsx[0]; y2 = coordsy[0];
            }
            double area = Math.abs((x2 - x1)*(y - y1) - (x - x1)*(y2-y1));
            double lineln = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
            apothem = area/lineln;
            double linedir = Math.atan2(y2-y1, x2-x1);
            double wallNormal = linedir + Math.PI/2;
            double reflection = Math.PI + (vdir + (2.0 * (Math.PI * 2.0 - (vdir - wallNormal))));
            // This took a lot of trial and error. But it works. Finally
            if(i != lastI){
                if(apothem < BALL_RADIUS){
                    if(x1 < x && x < x2){//Arriba
                        vx = vmag*Math.cos(reflection);
                        vy = vmag*Math.sin(reflection);
                        lastI = i;
                        break;
                    }if(x2 < x && x < x1){//Abajo
                        vx = vmag*Math.cos(reflection);
                        vy = vmag*Math.sin(reflection);
                        lastI = i;
                        break;
                    }if(y1 < y && y < y2){//Derecha
                        if(cuesta==1){
                            if(subio==1){
                                subio=0;
                                saliendo=1;
                            }else if(subio==0 && saliendo==0){
                                bajo=1;
                            }
                        }else if(cuesta==0){
                            if(bajo==1)
                            {
                                bajo=0;
                                saliendo=1;
                            }else if(bajo==0 && saliendo==0){
                                subio=1;
                                subiendo=0; //0=Sube Por Derecha
                            }
                            if(vx>0 && saliendo==1)
                            {
                                vx *= 0.9 + (frix * (interval / 1000000000.0));
                                vy=0;
                            }
                            if(vx>0 && saliendo==0)
                            {
                                vx=80; 
                                vy=0;
                            }
                        }
                        break;
                    }if(y2 < y && y < y1){//Izquierda 
                        if(cuesta==1){//izq=subio //der=bajo
                            if(bajo==1)
                            {
                                bajo=0;
                                saliendo=1;
                            }else if(bajo==0 && saliendo==0){
                                subio=1;
                                subiendo=1; //1=Sube por la Izquierda
                            }
                            if(vx<0 && saliendo==1)
                            {
                                vx *= 0.9 - (frix * (interval / 1000000000.0));
                                vy=0;
                            }
                            if(vx<0 && saliendo==0)
                            {
                                vx=-100; 
                                vy=0;
                            }
                        }else if(cuesta==0){
                            if(subio==1){
                                subio=0;
                                saliendo=1;
                            }else if(subio==0 && saliendo==0){
                                bajo=1;
                            }
                        }
                        break;
                    }
                }
            }
        }
    }
    public void moveTo(int x, int y){
        this.x = x;
        this.y = y;
    }
    @Override
    public void draw(Graphics2D g, long interval) {
        if(!MenuPanel.pausar){
        if(vx==0 && vy==0)
        {
            subio=0;            
            cambiaDireccion=0;
            saliendo=0;
            bajo=0;
        }
        this.x += vx * (interval / 5000000000.0);
        this.y += vy * (interval / 5000000000.0);
        this.vx += ax * (interval / 5000000000.0);
        this.vy += ay * (interval / 5000000000.0);
        if(saliendo==1){
            vx *= 1 - (frix * (interval / 1500000000.0));
            vy *= 1 - (frix * (interval / 1500000000.0));
        }else if(subio==0 && bajo==0){
            vx *= 1 - (frix * (interval / 1500000000.0));
            vy *= 1 - (frix * (interval / 1500000000.0));
        }else if(subio==1 && saliendo==0){
            vx *= 1 - (frix * (interval / 1000000000.0));
            vy *= 1 - (frix * (interval / 1000000000.0));
            if((vx<=40 && subiendo==1) || (vx*(-1)<=40 && subiendo==0)){//1=Sube por la izquierda 0=Por Derecha
                if(cambiaDireccion==0){
                    vx *= (-1);
                    cambiaDireccion=1;
                }
                if(subiendo==1)
                    vx *= 10 - (frix * (interval / 5000000000.0));
                if(subiendo==0)
                    vx *= 10 + (frix * (interval / 5000000000.0));
                vy *= 1 + (frix * (interval / 1000000000.0));
                subio=0;
            }
        }
        if(bajo==1 && saliendo==0){
            vx *=1.01  - (frix * (interval / 1000000000.0));
            vy *= 1 - (frix * (interval / 1000000000.0));
        }
        if(Math.sqrt(vx*vx + vy*vy) < 20){
            vx = 0;
            vy = 0;
        }
        if(vx != 0 || vy != 0) 
            checkCollision(interval,g);
        }
        g.setColor(Color.white);
        g.fillOval((int)x - 4, (int)y - 4, 8, 8);
        g.drawString("Jugador 1", (int)x-22, (int)y-8);
        
    }
    
}
