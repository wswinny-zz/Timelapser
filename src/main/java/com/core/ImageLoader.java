package com.core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Swinny on 3/19/2016.
 */
public class ImageLoader
{
    private static ImageLoader instance = null;

    private String extension = "jpg";                                   //What pictures are saved as
    private int nextImage = 0;                                          //The number the next picture should be saved as
    private int currentImage = 0;                                       //The current spot in the arraylist
    private int totalImages = 0;                                        //The total number of images saved

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE hh:mm:ss 'on' MMMM dd, yyyy");

    //**************************************************************
    // Default constructor
    // Makes sure that we start adding images at the correct position
    // Which could be 0 on first start or anytime the program was restarted
    // Also loads up to 513 Images into imageRoll
    //**************************************************************
    private ImageLoader()
    {
        if(this.nextImage == 0)
        {
            File dir =  new File(Main.STORAGE_DIR);

            //gets the total number of images already saved
            this.totalImages = dir.list().length;

            //Used to start saving from where the program last ended
            this.nextImage = this.totalImages;
        }
    }

    public static ImageLoader getInstance()
    {
        if (ImageLoader.instance == null)
        {
            ImageLoader.instance = new ImageLoader();
        }

        return ImageLoader.instance;
    }


    //**************************************************************
    // Takes in a Image
    // Makes sure the image exists
    // Trys to save it to the storage directory defined in main
    //**************************************************************
    public boolean saveImage(Image image)
    {
        if (image == null)
        {
            System.out.println("No image recived");
            return false;
        }

        File imageSave = this.getImagePath(this.nextImage);

        try
        {
            ImageIO.write((BufferedImage)image, this.extension, imageSave);
        }
        catch (IOException e)
        {
            return false;
        }

        this.nextImage++;
        this.totalImages++;

        return true;
    }

    //**************************************************************
    // Moves one the current image forward one and returns the new current
    // returns null on failure
    //**************************************************************
    public Image getNextImage()
    {
        if (this.currentImage < this.totalImages - 1)
        {
            this.currentImage++;

            return this.getImage(this.getImagePath(this.currentImage));
        }

        return null;
    }

    //**************************************************************
    // Moves one the current image backwards one and returns the new current
    // returns null on failure
    //**************************************************************
    public Image getPrevImage()
    {
        if (this.currentImage > 0)
        {
            this.currentImage--;

            return this.getImage(this.getImagePath(this.currentImage));
        }

        return null;
    }

    //**************************************************************
    // returns the current image
    // returns null on failure
    //**************************************************************
    public Image getCurrentImage()
    {
        return this.getImage(this.getImagePath(this.currentImage));
    }

    public Image getLastImage()
    {
        this.currentImage = this.totalImages;

        return this.getImage(this.getImagePath(this.currentImage));
    }

    public int getTotalOfImages()
    {
        return this.totalImages;
    }

    //**************************************************************
    // Returns the date and time the current image was created
    //**************************************************************
    public String getCurrentDateTimeAsString()
    {
        Path file = Paths.get(this.getImagePath(this.currentImage).getAbsolutePath());
        Date date = new Date();

        try {
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
            date = new Date(attr.creationTime().toMillis());
        }
        catch (IOException e)
        {
            System.out.println("Didn't work");
        }

        return dateFormatter.format(date);
    }

    //**************************************************************
    // Returns the date and time the current image was created
    //**************************************************************
    public String getCurrentName()
    {
        String saveFormat;

        saveFormat = String.format("%010d", this.currentImage);

        return saveFormat;
    }

    public void setExtension(String ext)
    {
        this.extension = ext;
    }

    private File getImagePath(int num)
    {
        File imagePath = new File(Main.STORAGE_DIR + "IMG_" + String.format("%010d", num) + "." + this.extension);

        return imagePath;
    }

    private Image getImage(File file)
    {
        Image tmpImage =  null;

        try
        {
            tmpImage = ImageIO.read(file);

        }
        catch (IOException e)
        {
            System.out.println("Could not get Image: " + file.getName());
        }

        return tmpImage;
    }
}