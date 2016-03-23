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
import java.util.*;

/**
 * Created by Swinny on 3/19/2016.
 */
public class ImageLoader
{
    private static ImageLoader instance = null;

    private int bufferNumber = 513;                                     //How many images will be buffered at a time
    private short interval = 1;                                         //How often a picture from the livestream is saved in seconds
    private String extension = "jpg";                                   //What pictures are saved as
    private int nextImage = 0;                                          //The number the next picture should be saved as
    private int currentImage = 0;                                       //The current spot in the arraylist
    private int imageCount = 0;                                         //The number of images loaded
    private int totalImages = 0;                                        //The total number of images saved
    private int realCurrent = 0;                                        //The image we are currently on of the saved images
    private ArrayList<Image> imageRoll =  new ArrayList<>(this.bufferNumber);

    //**************************************************************
    // For debug perposes
    //**************************************************************
    private void report ()
    {
        System.out.println("currentImage: " + this.currentImage);
        System.out.println("imageCount  : " + this.imageCount);
        System.out.println("totalImages : " + this.totalImages);
        System.out.println("realCurrent : " + this.realCurrent);
        System.out.println();
    }

    //**************************************************************
    // Default constructor
    // Makes sure that we start adding images at the correct position
    // Which could be 0 on first start or anytime the program was restarted
    // Also loads up to 513 Images into imageRoll
    //**************************************************************
    private ImageLoader()
    {
    //    int numToLoad = this.bufferNumber - 1;

        if(this.nextImage == 0)
        {
            File dir =  new File(Main.STORAGE_DIR);

            //gets the total number of images already saved
            this.totalImages = dir.list().length;

        //    if(this.totalImages < numToLoad)
        //        numToLoad = this.totalImages;

        //    for (int a = 0; a <= numToLoad; a++)
        //        addImage(a, false);

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
        String saveFormat;

        if (image == null)
        {
            System.out.println("No image recived");
            return false;
        }

        saveFormat = String.format("%010d", this.nextImage);
        File imageSave = new File(Main.STORAGE_DIR + "IMG_" + saveFormat + "." + this.extension);

        try
        {
            ImageIO.write((BufferedImage)image, this.extension, imageSave);
        }
        catch (IOException e)
        {
            return false;
        }

        this.nextImage++;
    //    if(this.imageCount < this.bufferNumber)
    //        addImage(this.nextImage - 1, false);
        this.totalImages++;  //creates race condition

        return true;
    }
    /*
    //**************************************************************
    // Takes in an integer imageNum
    // takes in a boolean frontBack that determines if the image should be added to the front (false) or the back (true)
    // Adds the image that corresponds to that imageNum to imageRoll
    //**************************************************************
    private boolean addImage(int imageNum, boolean frontBack)
    {
        //this.report();

        String saveFormat;
        Image tmpImage;

        saveFormat = String.format("%010d", imageNum);
        File imagePath = new File(Main.STORAGE_DIR + "IMG_" + saveFormat + "." + this.extension);

        try
        {
            tmpImage = ImageIO.read(imagePath);

            if(frontBack)
                this.imageRoll.add(0, tmpImage);
            else
                this.imageRoll.add(tmpImage);

            this.imageCount++;
        }
        catch (IOException e)
        {
            return false;
        }

        return true;
    }

    //**************************************************************
    // Takes in an integer imageNum
    // Removes the image that corresponds to that imageNum position in imageRoll
    //**************************************************************
    private boolean removeImage(int imageNum)
    {
        if(this.imageRoll.remove(imageNum) ==  null)
            return false;

        this.imageCount--;
        return true;
    }

    //**************************************************************
    // Removes an image from the beginning of imageRoll
    // Adds the previous image to the end of imageRoll
    //**************************************************************
    private boolean addNextImage()
    {
        if(!removeImage(0))
            return false;

        if(addImage(this.realCurrent + ((this.bufferNumber - 1) / 2), false))
            return true;
        else
            return false;
    }

    //**************************************************************
    // Removes an image from the end of imageRoll
    // Adds the next image needed to the beginning of imageRoll
    //**************************************************************
    private boolean addPrevImage()
    {
        if(!removeImage(512))
            return false;

        if(addImage(this.realCurrent - ((this.bufferNumber - 1) / 2), true))
            return true;
        else
            return false;
    }

    //**************************************************************
    // Controls if the current image number should increase or if the imageRoll should move one forward
    // returns false if their are no images left to move forward through
    // returns true otherwise
    //**************************************************************
    private boolean oneForward()
    {
        //Prevents the Real image from trying to increase past the lsat saved image
        if(this.totalImages - (this.realCurrent) <= 0)
            return false;

        //If there are more then 256 left outside the imageRoll
        if(this.totalImages - this.realCurrent > ((this.bufferNumber - 1) / 2))
        {
            //Just advances the imageRoll by one if we are already at the center of the roll
            if (this.currentImage == ((this.bufferNumber - 1) / 2))
            {
                if (!this.addNextImage())
                    return false;

                this.realCurrent++;
                return true;
            }
            //If we are past the center of the roll moves us back to the center while only advancing one image
            else if(this.currentImage > ((this.bufferNumber - 1) / 2))
            {
                for(int a = 0; a < ((this.currentImage - ((this.bufferNumber - 1) / 2)) + 1); a++)
                    if (this.addNextImage())
                        return false;

                this.realCurrent++;
                this.currentImage = ((this.bufferNumber - 1) / 2);
                return true;
            }
        }

        //if their is 256 or less image left or if their are more then 256 left but the current image hasn't reached the center of imageRoll
        this.currentImage++;
        this.realCurrent++;
        return true;

    }

    //**************************************************************
    // Controls if the current image number should decrease or if the imageRoll should move one back
    // returns false if their are no images left to go back through
    // returns true otherwise
    //**************************************************************
    private boolean oneBack()
    {
        //Prevents trying to get images before the first
        if(this.currentImage == 0)
            return false;
        //Just moves back imageRoll by one when we are already at the center of the Roll and have more then 128 image lef tot move back through
        else if(this.currentImage == ((this.bufferNumber - 1) / 2) && this.realCurrent > ((this.bufferNumber - 1) / 2))
        {
            if (this.addPrevImage())
                return false;

            this.realCurrent--;
            return true;
        }
        //moves the current image back when there are less then 128 image lef tto move back through
        else
        {
            this.currentImage--;
            this.realCurrent--;
            return true;
        }
    }
    */

    //**************************************************************
    // Moves one the current image forward one and returns the new current
    // returns null on failure
    //**************************************************************
    public Image getNextImage()
    {

     //   this.report();
        /*
        if(this.currentImage + 1 > this.bufferNumber)
        {
            return null;
        }
        if (this.realCurrent == this.totalImages)
        {
            System.out.println("Returned Current");
            return this.imageRoll.get(this.currentImage);
        }
        else
        {
            System.out.println("Return Next");
            if(oneForward())
                return this.imageRoll.get(this.currentImage);
            else
                return null;
        }
        */

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
    //    this.report();
        /*
        if(this.currentImage - 1 < 0)
        {
            return null;
        }
        else
        {
            if(oneBack())
                return this.imageRoll.get(this.currentImage);
            else
                return null;
        }
        */

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
    //    this.report();
        /*
        if(this.currentImage > this.bufferNumber - 2)
        {
            return null;
        }
        else if (this.currentImage < 0)
        {
            return null;
        }
        else
        {
            return this.imageRoll.get(this.currentImage);
        }
        */

        return this.getImage(this.getImagePath(this.currentImage));
    }

    public Image getLastImage()
    {
        return this.getImage(this.getImagePath(this.totalImages));
    }

    //**************************************************************
    // Returns the date and time the current image was created
    //**************************************************************
    public String getCurrentDateTime()
    {
        String saveFormat;
        String dateTime =  null;

        saveFormat = String.format("%010d", this.realCurrent);
        Path file = Paths.get(Main.STORAGE_DIR + "IMG_" + saveFormat + "." + this.extension);

        try {
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);

            dateTime = attr.creationTime().toString();
        }
        catch (IOException e)
        {
            System.out.println("Didn't work");
        }

        return dateTime;
    }

    //**************************************************************
    // Returns the date and time the current image was created
    //**************************************************************
    public String getCurrentName()
    {
        String saveFormat;

        saveFormat = String.format("%010d", this.realCurrent);

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