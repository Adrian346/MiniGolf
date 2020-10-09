/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package golfgame;

import static golfgame.MenuPanel.GolpesJugador1;
import static golfgame.MenuPanel.GolpesJugador2;
import static golfgame.MenuPanel.jugadores;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 *
 * @author Primazo
 */
public class Course implements GraphicsObject {
    private Font font;
    private static Polygon movible;
    public static  Polygon movible2;
    private Polygon courseShape;
    private Polygon subida, bajada;
    private Ball ball;
    private Ball2 ball2;
    private Hole hole;
    private int par;
    private int strokes = 0;
    private int strokes2 = 0;
    public static int num;
    private static int veces=0;
    private static boolean iveces=true;
    private static int lento=0;
    
    int  [] xmovible = new int[4]; //<--
    int  [] ymovible = new int[4];
    int  [] xmovible2 = new int[4]; //<--
    int  [] ymovible2 = new int[4];
    public Course(int coursenum) throws IOException{
        num = coursenum;
        font = new Font("Verdana",Font.BOLD,16);
        courseShape = new Polygon();
        movible = new Polygon();
        movible2 = new Polygon();
        subida = new Polygon();
        bajada = new Polygon();
            InputStream in = getClass().getResourceAsStream("course"+coursenum+".txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            LineNumberReader  lnr = new LineNumberReader(new InputStreamReader(getClass().getResourceAsStream("course"+coursenum+".txt")));
            int lines = 0;
            while (lnr.readLine() != null){
                lines++;
    	    }
            int vertices=0;
            if(num+1== 9 ||num+1==1 || num+1==8  || num+1==6 || num+1==7 || num+1==10 || num+1==11 || num+1==3 || num+1==5){
                vertices = lines -7;
                
            }else if( num+1==4){
                vertices = lines -11;
            }else{
                vertices = lines -3;
            }
            int[] xpoints = new int[vertices];
            int[] ypoints = new int[vertices];
            String line = br.readLine();
            par = Integer.parseInt(line);
            line = br.readLine();
            String[] ints = line.split(" ");
            ball = new Ball(Integer.parseInt(ints[0]),Integer.parseInt(ints[1])-10,this);
            if(jugadores==2)
                ball2 = new Ball2(Integer.parseInt(ints[0]), Integer.parseInt(ints[1])+10, this);
            line = br.readLine();
            ints = line.split(" ");
            hole = new Hole(Integer.parseInt(ints[0]),Integer.parseInt(ints[1]),this);
           if((num+1)==1 || (num+1)==8 || num+1==4 || num+1==6 || num+1==9 || num+1==7 || num+1==10 || num+1==11){
               for(int i=0; i<4; i++){
                    line = br.readLine();
                    ints = line.split(" ");
                    xmovible[i] = Integer.parseInt(ints[0]);
                    ymovible[i] = Integer.parseInt(ints[1]);
                } 
               movible = new Polygon(xmovible, ymovible, 4);
            }
           if( num+1==4){
              for(int i=0; i<4; i++){
                    line = br.readLine();
                    ints = line.split(" ");
                    xmovible2[i] = Integer.parseInt(ints[0]);
                    ymovible2[i] = Integer.parseInt(ints[1]);
                } 
               movible2 = new Polygon(xmovible2, ymovible2, 4); 
           }
            
            int  [] xsubida = new int[4]; 
            int  [] ysubida = new int[4];
            if( num+1==5){
            for(int i=0; i<4; i++){
                line = br.readLine();
                ints = line.split(" ");
                xsubida[i] = Integer.parseInt(ints[0]);
                ysubida[i] = Integer.parseInt(ints[1]);
            }
            subida = new Polygon(xsubida, ysubida, 4);
            }    
            if( num+1==3){            
            for(int i=0; i<4; i++){
                line = br.readLine();
                ints = line.split(" ");
                xsubida[i] = Integer.parseInt(ints[0]);
                ysubida[i] = Integer.parseInt(ints[1]);
            }
            bajada = new Polygon(xsubida, ysubida, 4);
            }
            
            for(int cur = 0; (line = br.readLine()) != null; cur++){
                ints = line.split(" ");
                xpoints[cur] = Integer.parseInt(ints[0]);
                ypoints[cur] = Integer.parseInt(ints[1]);
            }
           
            br.close();
            lnr.close();
            courseShape = new Polygon(xpoints,ypoints,vertices);
    }
    public Polygon moveX(Polygon movible){
        if(lento==7){
          if(iveces){
            for(int i=0;i<movible.npoints;i++){
                xmovible[i]+=1;
                //ysubida[i]+=10;
            }
            veces++;
            if(veces==50){
               iveces=false; 
            }
        }else{
            for(int i=0;i<movible.npoints;i++){
                xmovible[i]-=1;
                //ysubida[i]-=10;
            }
            veces--;
            if(veces==0){
               iveces=true; 
            }
        }  
          lento=0;
       }else{
           lento++; 
        }
        
        return new Polygon(xmovible,ymovible,4);
    }
     public Polygon moveY(Polygon movible){
        if(lento==7){
          if(iveces){
            for(int i=0;i<4;i++){
                
                ymovible[i]+=1;
            }
            veces++;
            if(veces==50){
               iveces=false; 
            }
         }else{
            for(int i=0;i<4;i++){
               
                ymovible[i]-=1;
            }
            veces--;
            if(veces==0){
               iveces=true; 
            }
        }
         lento=0;
          
       }else{
            lento++;
            
        }
        return new Polygon(xmovible,ymovible,4);
    }
    public Polygon getPolygonShape(){
        return this.courseShape;
    }
    public Polygon getPolygonMovible(){
        return this.movible;
    }
    public Polygon getPolygonMovible2(){
        return this.movible2;
    }
    public Polygon getPolygonSubida(){
        return this.subida;
    }
    public Polygon getPolygonBajada(){
        return this.bajada;
    }
    public int getBallX(){
        return ball.getX();
    }
    public int getBallY(){
        return ball.getY();
    }
    public int getBall2X(){
        return ball2.getX();
    }
    public int getBall2Y(){
        return ball2.getY();
    }
    public void hitBall(double x, double y){
        ball.setXVelocity(x*2);
        ball.setYVelocity(y*2);
        strokes++;
        if(jugadores==2)
            GolpesJugador1++;
    }
    public void hitBall2(double x, double y){
        ball2.setXVelocity(x*2);
        ball2.setYVelocity(y*2);
        strokes2++;
        if(jugadores==2)
            GolpesJugador2++;
    }
    public void ballToHole(){
        ball.setXVelocity(0);
        ball.setYVelocity(0);
        ball.moveTo(hole.getX(),hole.getY());
    }
    public void ball2ToHole(){
        ball2.setXVelocity(0);
        ball2.setYVelocity(0);
        ball2.moveTo(hole.getX(),hole.getY());
    }
    public int getPar(){
        return par;
    }
    public int getStrokes(){
        return strokes;
    }
    public int getStrokes2(){
        return strokes2;
    }
    @Override
    public void draw(Graphics2D g, long interval) {
        g.setColor(new Color(142,241,229));
        g.fillRect(0, 0, 800, 600);
        g.setColor(new Color(0,216,0));
        g.fillPolygon(courseShape);
        if((num+1)==1 || num+1==10){
            g.setColor(new Color(162, 160, 118));
            if(!MenuPanel.pausar)
                movible= moveY(movible);
            g.fillPolygon(movible);
        }
        if(num+1==6){
            g.setColor(new Color(162, 160, 118));
            if(!MenuPanel.pausar)
                movible= moveX(movible);
            g.fillPolygon(movible); 
        }
        if(num+1== 9 ||num+1==8 || num+1==4 || num+1==9 || num+1==7 || num+1==11){
            g.setColor(new Color(128,128,128));
            g.fillPolygon(movible);
        }
        if( num+1==4){
            g.setColor(new Color(128,128,128));
            g.fillPolygon(movible2);
        }
        if(num+1==5){
            g.setColor(new Color(0,255,0));
            g.fillPolygon(subida);
        }
        if(num+1==3){
        g.setColor(new Color(0,150,0));
        g.fillPolygon(bajada);
        }
        hole.draw(g,interval);
        ball.draw(g,interval);
        if(jugadores==2)
            ball2.draw(g,interval);
        g.setFont(font);
        if(jugadores==1)
            g.drawString("Hoyo: "+(num+1)+" Par: "+par+" Golpes: "+strokes, 3, 13);
        if(jugadores==2)
            g.drawString("Hoyo: "+(num+1)+"  Par: "+par+"  Golpes->1: "+strokes+"  Golpes->2: "+strokes2+"          TotalGolpes->1: "+GolpesJugador1+"  TotalGolpes->2: "+GolpesJugador2, 3, 13);
        if(jugadores==3)
            g.drawString("Hoyo: "+(num+1)+"   Cronometro: "+MenuPanel.tiempo, 3, 13);
    }
    
}
