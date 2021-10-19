package com.company.Lights;

import com.company.Maths.Vec;

import java.awt.*;

public class PointLight{
    public Vec p; // position
    public float i; //intensity
    public Color c; //color
    public PointLight(Vec position, Color color, float intensity){
        p = position;
        i = intensity;
        c = color;
    }
}
