package com.core;

import java.io.*;

/**
 * Created by Swinny on 3/23/2016.
 */
public class Config
{
    private static Config _instance = null;

    private final String filepath = System.getProperty("user.home") + File.separator + "timelapser.conf";

    private BufferedReader configReader;
    private String imageDirPath = System.getProperty("user.home");
    private String videoSourceString = "http://217.91.58.189:1024/mjpg/video.mjpg";

    private Config() {
        try
        {
            this.configReader = new BufferedReader(new FileReader(new File(filepath)));

            this.imageDirPath = this.configReader.readLine();
            this.videoSourceString = this.configReader.readLine();

            if(!this.imageDirPath.endsWith("/") || !this.imageDirPath.endsWith("\\"))
                this.imageDirPath += File.separator;

            this.printDebug();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Config file: " + filepath + " not found, creating and using defaults");
            this.writeNewConfig();
        }
        catch (IOException e)
        {
            System.out.println("Config file: " + filepath + " could not be read from using defaults");
        }
    }

    private void printDebug()
    {
        System.out.printf("Using %s as img dir\n", this.imageDirPath);
        System.out.printf("Using %s as video source\n", this.videoSourceString);
    }

    private void writeNewConfig()
    {
        try
        {
            BufferedWriter configWriter = new BufferedWriter(new FileWriter(new File(filepath)));

            configWriter.write(this.imageDirPath);
            configWriter.write("\n");
            configWriter.write(this.videoSourceString);

            configWriter.close();

            System.out.println("Wrote new config to " + filepath);
        }
        catch (IOException e)
        {
            System.out.println("Could not write new config is the directory write protected.");
        }
    }

    public static Config getInstance() {
        return _instance == null ? _instance = new Config() : _instance; //just for aaron
    }

    public String getImageDirPath() {
        return imageDirPath;
    }

    public String getVideoSourceString() {
        return videoSourceString;
    }
}
