package com.core;

import javafx.scene.control.Slider;
import org.opencv.core.Core;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;

/**
 * Created by Swinny on 3/19/2016.
 */
public class Main
{
    static
    {
        String opencvpath = System.getProperty("user.dir") + "\\libs\\";
        System.load(opencvpath +  Core.NATIVE_LIBRARY_NAME + ".dll");
    }

    public static String STORAGE_DIR = "path";
    private JFrame frame;
    private SliderPanel sliderPanel;
    private LiveStreamPanel liveStreamPanel;

    private int totalImageCount = 0;

    public Main()
    {
        File imageDir = new File(Main.STORAGE_DIR);
        if(!imageDir.exists())
        {
            System.out.println("The directory for images in the config does not exist!");
            System.exit(69);
        }

        this.totalImageCount = imageDir.listFiles().length;

        this.frame = new JFrame();
        this.frame.setUndecorated(true);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.frame.setLocationRelativeTo(null);
        this.frame.getContentPane().setBackground(Color.DARK_GRAY);

        //this.liveStreamPanel = new LiveStreamPanel();
        //this.frame.add(this.liveStreamPanel, BorderLayout.CENTER);

        this.sliderPanel = new SliderPanel();
        this.frame.add(this.sliderPanel, BorderLayout.SOUTH);

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
}
