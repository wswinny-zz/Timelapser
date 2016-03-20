package com.core;

import org.opencv.core.Core;

/**
 * Created by Swinny on 3/19/2016.
 */
public class Main
{
    static {
        String opencvpath = System.getProperty("user.dir") + "\\libs\\";
        System.load(opencvpath +  Core.NATIVE_LIBRARY_NAME + ".dll");
    }

    public static String STORAGE_DIR = "path to images";

    public Main()
    {

    }

    public static void main(String [] args)
    {
        new Main();
    }
}
