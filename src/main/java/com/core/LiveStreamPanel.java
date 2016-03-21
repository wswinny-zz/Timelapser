package com.core;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * Created by Swinny on 3/19/2016.
 */
public class LiveStreamPanel extends JPanel
{
    private VideoCapture capture;
    private Mat currentFrame;
    private Image currentFrameImage = null;

    private JLabel imageLabel;

    public LiveStreamPanel()
    {
        this.setLayout(new GridLayout(1,1));
        this.setBackground(Color.DARK_GRAY);

        this.imageLabel = new JLabel("", JLabel.CENTER);
        this.imageLabel.setBackground(Color.DARK_GRAY);

        this.add(this.imageLabel);

        this.capture = new VideoCapture();
        this.capture.open(Main.VIDEO_SOURCE);

        if(!this.capture.isOpened())
        {
            System.out.println("Could not find the IP Camera. Is it on?");
            System.exit(1337);
        }

        this.currentFrame = new Mat();

        Runnable captureThread = () -> {
            while(true)
            {
                try
                {
                    this.capture.read(this.currentFrame);
                    this.currentFrameImage = toBufferedImage(this.currentFrame);
                    this.imageLabel.setIcon(new ImageIcon(
                            this.currentFrameImage.getScaledInstance(
                                    this.imageLabel.getWidth(),
                                    this.imageLabel.getHeight(),
                                    Image.SCALE_SMOOTH)
                    ));
                }
                catch (Exception e)
                {
                    System.out.println("The graphics object for the LiveStreamPanel is null skipping this frame");
                }
            }
        };
        new Thread(captureThread).start();

        ActionListener imageSaver = (e) -> {
            if(this.currentFrameImage != null)
                ImageLoader.getInstance().saveImage(this.currentFrameImage);
        };
        new Timer(Main.IMAGE_SAVE_RATE, imageSaver).start();
    }

    public Image toBufferedImage(Mat m)
    {
        int type = BufferedImage.TYPE_BYTE_GRAY;

        if ( m.channels() > 1 )
            type = BufferedImage.TYPE_3BYTE_BGR;

        int bufferSize = m.channels() * m.cols() * m.rows();
        byte [] b = new byte[bufferSize];

        m.get(0,0,b); // get all the pixels

        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

        System.arraycopy(b, 0, targetPixels, 0, b.length);

        return image;
    }
}
