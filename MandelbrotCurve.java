package com.company;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class MandelbrotCurve extends PaintedPanel implements Runnable{

    //create listener
    public class ZoomListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton()==MouseEvent.BUTTON1) {
                if(zoom <= 0.25) return; //set max zoom
                double xClick = e.getX(),yClick = e.getY();
                //if on the left half on screen, else on right half
                if (xClick / fractalImg.getWidth() <= 0.5) setXOffset(xOffset - (xClick%(fractalImg.getWidth())/2)/(fractalImg.getWidth())/2);
                else setXOffset(xOffset + (xClick%(fractalImg.getWidth())/2)/(fractalImg.getWidth())/2);
                //if on top half of screen, else on bottom half
                if (yClick / fractalImg.getHeight() <= 0.5) setYOffset(yOffset + (yClick%(fractalImg.getHeight())/2)/(fractalImg.getHeight())/2);
                else setYOffset(yOffset - (yClick%(fractalImg.getHeight())/2)/(fractalImg.getHeight())/2);
                //zoom in
                setZoom(zoom - 0.25);
                adjustFractal();
            }//end if statement
            else if(e.getButton()==MouseEvent.BUTTON3){
                if(zoom >= 3.25) return; //set min zoom
                //zoom out
                setZoom(zoom + 0.25);
                adjustFractal();
            }//end else if statement
        }//end mouseClicked
        @Override
        public void mousePressed(MouseEvent e){/*do nothing*/}
        @Override
        public void mouseReleased(MouseEvent e){/*do nothing*/}
        @Override
        public void mouseEntered(MouseEvent e){/*do nothing*/}
        @Override
        public void mouseExited(MouseEvent e){/*do nothing*/}
    }//end ZoomListener

    //attributes
    private int xPixel,yPixel,startBound,endBound;
    private final int itr,split,power;
    private final Graphics2D img;
    private final BufferedImage fractalImg;
    private static double xOffset,yOffset,zoom;
    //constructor
    public MandelbrotCurve(int i,int p,int s,double x,double y,double z){
        super();
        //itr is total iterations
        itr = i;
        power = p;
        //split is the number of threads the fractal is split into
        split = s;
        xOffset = x;
        yOffset = y;
        zoom = z;
        xPixel = yPixel = 0;
        ZoomListener zList = new ZoomListener();
        super.addMouseListener(zList);
        fractalImg = super.getBufferedImg();
        img = super.getG2D();
        //create and run thread
        Thread mandThread = new Thread(this,"Mandelbrot Thread");
        mandThread.start();
    }//end constructor
    @Override
    public void run(){
        //create and start clock
        TimerDisplay clock = new TimerDisplay();
        clock.start();
        //split task into split number of threads
        for(int i = 1; i <= split; ++i){
            startBound = (fractalImg.getWidth()*(i-1))/split;
            xPixel = startBound;
            if(i < split) endBound = (fractalImg.getWidth()*(i))/split;
            else endBound = fractalImg.getWidth();
            //end run
            Thread mandDisplay = new Thread(() -> {
                for(int j = 0; j < fractalImg.getHeight(); j++){
                    for(int k = 0; k < fractalImg.getWidth()/split; k++){
                        drawFractal(img);
                        cursorForward();
                    }//end for loop
                    repaint(1);
                }//end for loop
            });
            try{
                //run each thread sequentially
                mandDisplay.start();
                mandDisplay.join();
            }//end try statement
            catch(Exception e){System.out.println(e.getMessage());}
        }//end for loop
        clock.stop();
    }//end run

    @Override
    void drawFractal(Graphics2D g){
        double x0 = xOffset - (zoom / 2) + ((zoom * xPixel) / fractalImg.getHeight());
        double y0 = yOffset - (zoom / 2) + ((zoom * yPixel) / fractalImg.getWidth());
        ComplexNumber z0 = new ComplexNumber(x0,y0);
        //logic for color choice
        Color c;
        int pigment = itr - mand(z0);
        if(pigment > (itr*9)/10) c = new Color(255,0,0);
        else if(pigment > (itr*4)/5) c = new Color(255,125,0);
        else if(pigment > (itr*7)/10) c = new Color(255,255,0);
        else if(pigment > (itr*3)/5) c = new Color(125,255,0);
        else if(pigment > (itr)/2) c = new Color(0,255,125);
        else if(pigment > (itr)*2/5) c = new Color(0,255,255);
        else if(pigment > (itr*3)/10) c = new Color(0,0,255);
        else if(pigment > (itr)/5) c = new Color(125,0,255);
        else if(pigment > (itr)/10) c = new Color(255, 0, 255);
        else if(pigment > 0) c = new Color(255,125,255);
        else c = new Color(0,0,0);
        //draw pixel
        g.setColor(c);
        g.drawRect(xPixel, fractalImg.getHeight() - 1 - yPixel, 1 , 1);
    }//end drawFractal

    //move pixel forward
    public void cursorForward(){
        if(xPixel < endBound) ++yPixel;
        else xPixel = startBound;
        if(yPixel > fractalImg.getHeight()){
            ++xPixel;
            yPixel = 0;
        }//end if statement
    }//end cursorForward

    public void setXOffset(double x){xOffset = x;}
    public void setYOffset(double y){yOffset = y;}
    public void setZoom(double z){zoom = z;}

    //remove current fractal and draw an adjusted one
    public void adjustFractal(){
        //remove old drawing
        this.removeAll();
        //create new drawing
        this.add(new MandelbrotCurve(itr,power,split,xOffset,yOffset,zoom));
        this.repaint(1);
        this.setVisible(true);
    }//end adjustZoom

    //carry out mandelbrot equation
    public int mand(ComplexNumber c){
        //initial z is zero
        ComplexNumber z = new ComplexNumber();
        //following z values are (z(n-1))^2 + c
        for (int t = 0; t < itr; t++) {
            if (z.mod() > 2.0) return t;
            z = ComplexNumber.pow(z,power);
            z.add(c);
        }//end for loop
        //return max;
        return itr;
    }//end mand
}//end MandelbrotCurve