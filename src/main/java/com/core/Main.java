package com.core;

import org.opencv.core.Core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

/**
 * Created by Swinny on 3/19/2016.
 */
public class Main implements KeyListener
{
    static
    {
        String opencvpath = System.getProperty("user.dir") + "\\libs\\";
        System.load(opencvpath +  Core.NATIVE_LIBRARY_NAME + ".dll");
        System.load(opencvpath + "opencv_ffmpeg300_64.dll");
    }

    public static final String STORAGE_DIR = "C:\\Users\\Saphixx\\Desktop\\TimeTest\\";
    public static final String VIDEO_SOURCE = "http://217.91.58.189:1024/mjpg/video.mjpg";

    public static final int IMAGE_SAVE_RATE = 1000; //in miliseconds

    private JFrame frame;
    private JPanel sliderPanel;
    private JSlider timeSlider;

    private JLayeredPane layeredPane;

    private JPanel picturePanel;
    private LiveStreamPanel liveStreamPanel;

    public Main()
    {
        File imageDir = new File(Main.STORAGE_DIR);
        if(!imageDir.exists())
        {
            System.out.println("The directory for images in the config does not exist!");
            System.exit(69);
        }

        this.frame = new JFrame();
//        this.frame.setUndecorated(true);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.frame.setSize(new Dimension(1600, 900));
        this.frame.setLocationRelativeTo(null);
        this.frame.getContentPane().setBackground(Color.DARK_GRAY);

        this.layeredPane = new JLayeredPane();
        this.frame.add(this.layeredPane, BorderLayout.CENTER);

        this.picturePanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g)
            {
                g.drawImage(ImageLoader.getInstance().getCurrentImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };

        this.picturePanel.setBackground(Color.DARK_GRAY);
        this.picturePanel.setBounds(0, 0, 1600, 850);
        this.layeredPane.add(this.picturePanel, JLayeredPane.PALETTE_LAYER);

        this.liveStreamPanel = new LiveStreamPanel();
        this.liveStreamPanel.setBounds(0, 0, 1600, 850);
        this.layeredPane.add(this.liveStreamPanel, JLayeredPane.DEFAULT_LAYER);

        this.sliderPanel = new JPanel();
        this.sliderPanel.setLayout(new GridLayout(1, 2));
        this.sliderPanel.setBackground(Color.DARK_GRAY);
        this.frame.add(this.sliderPanel, BorderLayout.SOUTH);

        this.timeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        this.timeSlider.setMinorTickSpacing(1);
        this.timeSlider.setMajorTickSpacing(5);
        this.timeSlider.setPaintTicks(false);
        this.timeSlider.setBackground(Color.DARK_GRAY);

        this.sliderPanel.add(this.timeSlider);

        this.frame.addKeyListener(this);
        this.liveStreamPanel.addKeyListener(this);
        this.sliderPanel.addKeyListener(this);
        this.timeSlider.addKeyListener(this);
        this.layeredPane.addKeyListener(this);
        this.picturePanel.addKeyListener(this);

        this.frame.setVisible(true);
    }

    public static void main(String [] args)
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
        }

        new Main();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar() == ' ') {
            swapPanels();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            ImageLoader.getInstance().getNextImage();
            this.picturePanel.repaint();
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            ImageLoader.getInstance().getPrevImage();
            this.picturePanel.repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void swapPanels()
    {
        int temp = this.layeredPane.getLayer(this.liveStreamPanel);
        this.layeredPane.setLayer(this.liveStreamPanel, this.layeredPane.getLayer(this.picturePanel));
        this.layeredPane.setLayer(this.picturePanel, temp);
    }

}
