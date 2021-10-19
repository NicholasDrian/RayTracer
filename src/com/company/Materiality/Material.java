package com.company.Materiality;

import java.awt.*;

public class Material {
    public float r; // 0-1, 0 = all color is own color, 1 = all color from reflection;
                    // also 0 = all light is own color, 1 = all light from reflection;
    public float rb;// 0-1, 0 = perfectly reflective, 1 = blurry reflection;
    public float h; //base color without lighting info;
    public float s; //saturation of color;
    public float e; // emissivity (glow), amount of light emitted by material;
    public float l; //luminosity
    public Material (float hue, float saturation, float reflectivity, float luminosity){
        h = hue;
        r = reflectivity;
        s = saturation;
        l = luminosity;
    }
}
