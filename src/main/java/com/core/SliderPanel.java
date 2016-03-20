package com.core;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Swinny on 3/20/2016.
 */
public class SliderPanel extends JPanel
{
    private JSlider timeSlider;

    public SliderPanel()
    {
        this.setLayout(new GridLayout(1, 2));

        this.timeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        this.timeSlider.setMinorTickSpacing(1);
        this.timeSlider.setMajorTickSpacing(5);
        this.timeSlider.setPaintTicks(false);
        this.add(this.timeSlider);
    }

    public void setImageCount(int imageCount)
    {
        this.timeSlider.setMaximum(imageCount);
    }
}
