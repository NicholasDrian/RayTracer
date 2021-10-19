package com.company.Materiality;

import java.awt.*;

//can have more than 255 intensity
public class LightColor {
    public float r;
    public float g;
    public float b;


    public LightColor(float R, float G, float B){
        r = R;
        g = G;
        b = B;
    }

    public LightColor(Color c){
        r = ((float)c.getRed() / 255) + 1;
        g = ((float)c.getGreen() / 255) + 1;
        b = ((float)c.getBlue() / 255) + 1;
    }


    public int getRGB(){

        float rCap = Math.min(1, r);
        float gCap = Math.min(1, g);
        float bCap = Math.min(1, b);


        return ((((int)(0*255)) & 0xFF) << 24) |
                ((((int)(rCap*255)) & 0xFF) << 16) |
                ((((int)(gCap*255)) & 0xFF) << 8)  |
                ((((int)(bCap*255)) & 0xFF) << 0);
    }

    public static LightColor scale(Color c, float factor){
        return new LightColor (
                c.getRed()*factor,
                c.getGreen()*factor,
                c.getBlue()*factor
        );
    }

    //public static void add(LightColor )
}
