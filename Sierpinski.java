package com.company;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Sierpinski extends PaintedPanel implements Runnable{
    Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    int DEF_SIZE = (int) ((SCREEN_SIZE.getHeight() * 11) / 24);
    int FULL_SIZE = (int) ((SCREEN_SIZE.getHeight() * 23) / 24);
    //used to store and classify lines in fractal

    //declare attributes
    private final Graphics2D img;
    private final int itr;
    private final String type;

    //constructor
    public Sierpinski(int i, String t){
        //create JPanel
        super();
        if(t.equals("Triangle")) super.setBackground(Color.BLACK);
        else super.setBackground(Color.WHITE);
        img = super.getG2D();
        itr = i;
        type = t;
        //start thread
        Thread kochThread = new Thread(this,"Sierp Thread");
        kochThread.start();
    }//end constructor

    //methods
    @Override
    public void run(){
        int size;
        int roundingOffset;
        if(MainScreen.isFullScreen) {
            size = FULL_SIZE;
            roundingOffset = -24;
        }//end if statement
        else {
            size = DEF_SIZE;
            roundingOffset = -13;
        }//end else statement
        TimerDisplay clock = new TimerDisplay();
        clock.start();
        if(type.equals("Triangle")) {
            sierp(size/8,(size*7)/8,(size*7)/8,(size*7)/8,size/2,size/4 + roundingOffset,itr,img);
        }//end if statement
        else{
            sierpSquare(0,0,size,itr,img);
        }//end else statement
        repaint(1);
        clock.stop();
    }//end run

    @Override
    public void drawFractal(Graphics2D g){}//end drawFractal

    public void sierpSquare(int x, int y, int side, int depth, Graphics2D g) {
        int sub = side / 3;
        //Draw center square
        g.setColor(Color.BLACK);
        g.fillRect(x + sub, y + sub, sub - 1, sub - 1);

        if(sub >= 3){
            //Draw 8 surrounding squares
            for(int i = 0; i < depth-1; i++){
                for (int j = 0; j < 3; j++){
                    for (int k = 0; k < 3; k++){
                        if (k != 1 || j != 1) sierpSquare(x + j * sub,y + k * sub,sub,depth-1,g);
                    }//end for loop
                }//end for loop
            }//end for loop
        }//end if statement
    }//end sierpSquare

    public void sierp(int x1,int y1,int x2,int y2,int x3,int y3,int depth, Graphics2D g){//Triangle     keleyi.com

        double s = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        g.setColor(Color.WHITE);

        g.drawLine(x1,y1,x2,y2);
        g.drawLine(x2,y2,x3,y3);
        g.drawLine(x1,y1,x3,y3);

        if(s < 3 || depth <= 1) return;

        double x11=(x1*3+x2)/4.0;
        double y11=y1-(s/4)*Math.sqrt(3);
        double x12=(x1+x2*3)/4.0;
        double x13=(x1+x2)/2.0;
        double x21=x1-s/4;
        double y21=(y1+y3)/2.0;
        double x22=x1+s/4.0;
        double x31=x2+s/4.0;
        double y31=(y1+y3)/2.0;
        double x32=x2-s/4.0;

        sierp((int)x11,(int)y11,(int)x12,(int) y11, (int)x13, (int) (double) y1, depth-1, g);
        sierp((int)x21,(int)y21,(int)x22,(int) y21, (int) (double) x1, (int) (double) y3, depth-1, g);
        sierp((int)x31,(int)y31,(int)x32,(int) y21, (int) (double) x2, (int) (double) y3, depth-1, g);
    }//end sierp
}//end Sierpenski