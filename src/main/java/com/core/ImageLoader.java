package com.core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Swinny on 3/19/2016.
 */
public class ImageLoader
{
    private static ImageLoader instance = null;

    private short interval = 1;                                         //How often a picture from the livestream is saved in seconds
    private String extension = "jpg";                                  //What pictures are saved as
    private ArrayList<Image> imageRoll =  new ArrayList<>(513);

    private ImageLoader()
    {

    }

    public static ImageLoader getInstance()
    {
        if (ImageLoader.instance == null)
        {
            ImageLoader.instance = new ImageLoader();
        }

        return ImageLoader.instance;
    }

    public boolean saveImage(Image image)
    {
        if (image == null)
        {
            System.out.println("No image recived");
            return false;
        }

        try
        {
            File imageSave = new File(Main.STORAGE_DIR + "IMG_" + System.currentTimeMillis() + "." + extension);
            ImageIO.write((BufferedImage)image, extension, imageSave);
        }
        catch (IOException e)
        {
            return false;
        }

        return true;
    }

//    public Image getNext()
//    {
//
//    }
//
//    public Image getPrev()
//    {
//
//    }
//
//    public Image getCurrent()
//    {
//
//    }

}
