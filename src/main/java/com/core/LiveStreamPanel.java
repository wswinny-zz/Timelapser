package com.core;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * Created by Swinny on 3/19/2016.
 */
public class LiveStreamPanel extends JPanel
{
    private VideoCapture capture;
    private Mat currentFrame;

    public LiveStreamPanel()
    {
        this.capture = new VideoCapture("D:\\Users\\Swinny\\Desktop\\test.mp4");

        if(!this.capture.isOpened())
        {
            System.out.println("Could not capture the video");
            //System.exit(1337);
        }

        this.currentFrame = new Mat();

        Runnable captureThread = () -> {

            while(true)
            {
                this.capture.read(this.currentFrame);

                Image frame = toBufferedImage(this.currentFrame);
                this.getGraphics().drawImage(frame, 0, 0, null);
            }
        };

        new Thread(captureThread).start();
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
