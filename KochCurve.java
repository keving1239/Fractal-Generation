package com.company;
import java.util.*;
import java.awt.*;

public class KochCurve extends PaintedPanel implements Runnable{
    //used to store and classify lines in fractal
    public static class Line{
        //attributes
        private final int startX,startY,endX,endY,type;

        //constructor
        public Line(int x1,int y1,int x2,int y2,int t){
            startX = x1;
            startY = y1;
            endX = x2;
            endY = y2;
            //type is the direction of the line
            type = t;
        }//end constructor

        //getters and setters
        public int getStartX(){return startX;}
        public int getStartY(){return startY;}
        public int getEndX(){return endX;}
        public int getEndY(){return endY;}
        public int getType(){return type;}
    }//end line

    //declare attributes
    private ArrayList<Line> currentGen;
    private final Graphics2D img;
    private final int itr;
    private final String type;

    //constructor
    public KochCurve(int i,String t){
        //create JPanel
        super();
        super.setBackground(Color.BLACK);
        //create initial triangle
        currentGen = new ArrayList<>();
        int size;
        Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
        int DEF_SIZE = (int) ((SCREEN_SIZE.getHeight() * 11) / 24);
        int FULL_SIZE = (int) ((SCREEN_SIZE.getHeight() * 23) / 24);
        if(MainScreen.isFullScreen) size = FULL_SIZE;
        else size = DEF_SIZE;
        int height = (int) ((Math.sqrt(3)/2)*(size*3/4));
        Line genZero1 = new Line(size/8,size/4,(size*7)/8,size/4,5);
        Line genZero2 = new Line(size/8, size/4,size/2,(size/4) + height,3);
        Line genZero3 = new Line((size*7)/8, size/4,size/2,(size/4) + height,2);
        currentGen.add(genZero1);
        currentGen.add(genZero2);
        currentGen.add(genZero3);

        itr = i;
        type = t;
        img = super.getG2D();
        //start thread
        Thread kochThread = new Thread(this,"Koch Thread");
        kochThread.start();
    }//end constructor

    //methods
    @Override
    public void run(){
        TimerDisplay clock = new TimerDisplay();
        clock.start();
        for(int i = 0; i < itr-1; ++i){
            if(type.equals("Snowflake")) newGeneration();
            else antiGeneration();
        }//end for loop
        drawFractal(img);
        repaint(1);
        clock.stop();
    }//end run

    @Override
    public void drawFractal(Graphics2D g) {
        for(Line l: currentGen){
            if(l.getType() == 1) g.setColor(new Color(255,0,0));
            if(l.getType() == 2) g.setColor(new Color(255,85,0));
            if(l.getType() == 3) g.setColor(new Color(255,255,0));
            if(l.getType() == 4) g.setColor(new Color(0,255,85));
            if(l.getType() == 5) g.setColor(new Color(0,0,255));
            if(l.getType() == 6) g.setColor(new Color(170,0,255));
            g.drawLine(l.getStartX(),l.getStartY(),l.getEndX(),l.getEndY());
        }//end for loop
    }//end drawFractal

    public void newGeneration(){
        ArrayList<Line> nextGen = new ArrayList<>();
        int ax,ay,bx,by,cx,cy,dx,dy,ex,ey;
        for(Line l : currentGen){
            ax = l.getStartX();
            ay = l.getStartY();
            ex = l.getEndX();
            ey = l.getEndY();
            bx = (int) (ax+((1.0/3)*(ex-ax)));
            by = (int) (ay+((1.0/3)*(ey-ay)));
            dx = (int) (ax+((2.0/3)*(ex-ax)));
            dy = (int) (ay+((2.0/3)*(ey-ay)));
            double dist = (Math.sqrt(Math.pow(ax-ex,2) + Math.pow(ay-ey,2)));
            double h = (((Math.sqrt(3))/2) * (dist/3));
            switch(l.getType()){
                case 1:
                    nextGen.add(new Line(ax,ay,bx,by,1));
                    cx = (dx+bx)/2 - (int) (h*Math.sin(Math.PI*2/3));
                    cy = (dy+by)/2 + (int) (h*Math.cos(Math.PI*2/3));
                    nextGen.add(new Line(cx,cy,bx,by,3));
                    nextGen.add(new Line(cx,cy,dx,dy,5));
                    nextGen.add(new Line(dx,dy,ex,ey,1));
                    break;
                case 2:
                    nextGen.add(new Line(ax,ay,bx,by,2));
                    cx = (dx+bx)/2 + (int) (h*Math.cos(-Math.PI/6));
                    cy = (dy+by)/2 - (int) (h*Math.sin(-Math.PI/6));
                    nextGen.add(new Line(cx,cy,bx,by,4));
                    nextGen.add(new Line(cx,cy,dx,dy,6));
                    nextGen.add(new Line(dx,dy,ex,ey,2));
                    break;
                case 3:
                    nextGen.add(new Line(ax,ay,bx,by,3));
                    cx = (dx+bx)/2 - (int) (h*Math.sin(Math.PI/3));
                    cy = (dy+by)/2 + (int) (h*Math.cos(Math.PI/3));
                    nextGen.add(new Line(cx,cy,bx,by,1));
                    nextGen.add(new Line(dx,dy,cx,cy,6));
                    nextGen.add(new Line(dx,dy,ex,ey,3));
                    break;
                case 4:
                    nextGen.add(new Line(ax,ay,bx,by,4));
                    cx = (dx+bx)/2 + (int) (h*Math.cos(Math.PI/6));
                    cy = (dy+by)/2 - (int) (h*Math.sin(Math.PI/6));
                    nextGen.add(new Line(cx,cy,bx,by,2));
                    nextGen.add(new Line(dx,dy,cx,cy,5));
                    nextGen.add(new Line(dx,dy,ex,ey,4));
                    break;
                case 5:
                    nextGen.add(new Line(ax,ay,bx,by,5));
                    cx = (dx+bx)/2;
                    cy = by - (int) h;
                    nextGen.add(new Line(bx,by,cx,cy,1));
                    nextGen.add(new Line(dx,dy,cx,cy,4));
                    nextGen.add(new Line(dx,dy,ex,ey,5));
                    break;
                case 6:
                    nextGen.add(new Line(ax,ay,bx,by,6));
                    cx = (dx+bx)/2;
                    cy = by + (int) h;
                    nextGen.add(new Line(bx,by,cx,cy,2));
                    nextGen.add(new Line(cx,cy,dx,dy,3));
                    nextGen.add(new Line(dx,dy,ex,ey,6));
                    break;
            }//end switch statement
        }//end for loop
        //replace old generation with new one
        currentGen = nextGen;
    }//end newGeneration

    public void antiGeneration(){
        ArrayList<Line> antiGen = new ArrayList<>();
        int ax,ay,bx,by,cx,cy,dx,dy,ex,ey;
        for(Line l : currentGen){
            ax = l.getStartX();
            ay = l.getStartY();
            ex = l.getEndX();
            ey = l.getEndY();
            bx = (int) (ax+((1.0/3)*(ex-ax)));
            by = (int) (ay+((1.0/3)*(ey-ay)));
            dx = (int) (ax+((2.0/3)*(ex-ax)));
            dy = (int) (ay+((2.0/3)*(ey-ay)));
            double dist = (Math.sqrt(Math.pow(ax-ex,2) + Math.pow(ay-ey,2)));
            double h = (((Math.sqrt(3))/2) * (dist/3));
            switch(l.getType()){
                case 1:
                    antiGen.add(new Line(ax,ay,bx,by,1));
                    cx = (dx+bx)/2 + (int) (h*Math.sin(Math.PI*2/3));
                    cy = (dy+by)/2 - (int) (h*Math.cos(Math.PI*2/3));
                    antiGen.add(new Line(bx,by,cx,cy,5));
                    antiGen.add(new Line(dx,dy,cx,cy,3));
                    antiGen.add(new Line(dx,dy,ex,ey,1));
                    break;
                case 2:
                    antiGen.add(new Line(ax,ay,bx,by,2));
                    cx = (dx+bx)/2 - (int) (h*Math.cos(Math.PI/6));
                    cy = (dy+by)/2 - (int) (h*Math.sin(Math.PI/6));
                    antiGen.add(new Line(bx,by,cx,cy,6));
                    antiGen.add(new Line(dx,dy,cx,cy,4));
                    antiGen.add(new Line(dx,dy,ex,ey,2));
                    break;
                case 3:
                    antiGen.add(new Line(ax,ay,bx,by,3));
                    cx = (dx+bx)/2 - (int) (h*Math.sin(-Math.PI*2/3));
                    cy = (dy+by)/2 + (int) (h*Math.cos(-Math.PI*2/3));
                    antiGen.add(new Line(cx,cy,bx,by,6));
                    antiGen.add(new Line(dx,dy,cx,cy,1));
                    antiGen.add(new Line(dx,dy,ex,ey,3));
                    break;
                case 4:
                    antiGen.add(new Line(ax,ay,bx,by,4));
                    cx = (dx+bx)/2 - (int) (h*Math.cos(Math.PI/6));
                    cy = (dy+by)/2 + (int) (h*Math.sin(Math.PI/6));
                    antiGen.add(new Line(cx,cy,bx,by,5));
                    antiGen.add(new Line(dx,dy,cx,cy,2));
                    antiGen.add(new Line(dx,dy,ex,ey,4));
                    break;
                case 5:
                    antiGen.add(new Line(ax,ay,bx,by,5));
                    cx = (dx+bx)/2;
                    cy = by + (int) h;
                    antiGen.add(new Line(cx,cy,bx,by,4));
                    antiGen.add(new Line(cx,cy,dx,dy,1));
                    antiGen.add(new Line(dx,dy,ex,ey,5));
                    break;
                case 6:
                    antiGen.add(new Line(ax,ay,bx,by,6));
                    cx = (dx+bx)/2;
                    cy = by - (int) h;
                    antiGen.add(new Line(cx,cy,bx,by,3));
                    antiGen.add(new Line(cx,cy,dx,dy,2));
                    antiGen.add(new Line(dx,dy,ex,ey,6));
                    break;
            }//end switch statement
        }//end for loop
        //replace old generation with new one
        currentGen = antiGen;
    }//end antiGeneration
}//end KochCurve