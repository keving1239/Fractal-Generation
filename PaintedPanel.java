package com.company;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

abstract class PaintedPanel extends JPanel{
    //define attributes
    private final BufferedImage fractalImg;
    private final Graphics2D img;

    public static class TimerDisplay{
        //attributes
        private final long startTime = System.currentTimeMillis();
        private long msPassed = 0;
        private int count = 0;
        Timer time = new Timer();
        TimerTask update = new TimerTask(){
            @Override
            public void run(){
                count++;
                msPassed = System.currentTimeMillis() - startTime;
                //update every 50 ms
                if(count % 50 == 0) SimulationScreen.updateTimer(msPassed + " ms");
            }//end run
        };
        //start watch
        public void start(){
            time.scheduleAtFixedRate(update,0,1);
        }//end start
        //stop watch
        public void stop(){
            time.cancel();
            SimulationScreen.updateTimer(msPassed + " ms");
        }//end stop
    }//end TimerDisplay

    //constructor
    public PaintedPanel(){
        //create panel
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
        int FULL_SIZE = (int) ((SCREEN_SIZE.getHeight() * 23) / 24);
        int DEF_SIZE = (int) ((SCREEN_SIZE.getHeight() * 11) / 24);
        if (MainScreen.isFullScreen) this.setBounds(0,0, FULL_SIZE, FULL_SIZE);
        else this.setBounds(0,0, DEF_SIZE, DEF_SIZE);
        this.setLayout(null);
        this.setFocusable(true);
        this.setVisible(true);
        if(MainScreen.isFullScreen) fractalImg = new BufferedImage(FULL_SIZE, FULL_SIZE,BufferedImage.TYPE_INT_ARGB);
        else fractalImg = new BufferedImage(DEF_SIZE, DEF_SIZE,BufferedImage.TYPE_INT_ARGB);
        img = fractalImg.createGraphics();
    }//end constructor

    public void paintComponent(Graphics g){
        //create fractal image
        super.paintComponent(g);
        g.drawImage(fractalImg,0,0,this);
    }//end paintComponent

    //getters
    public BufferedImage getBufferedImg(){return fractalImg;}
    public Graphics2D getG2D(){return img;}

    //draw current fractal
    abstract void drawFractal(Graphics2D g);//end drawFractal
}//end PaintedPanel