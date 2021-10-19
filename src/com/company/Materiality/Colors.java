package com.company.Materiality;

import com.company.Intersection;
import com.company.Maths.Vec;
import com.company.Render;

import java.awt.*;

public class Colors {

    //probably could do this faster but lose some quality
    //weights must sum to one
    public static Color weightedAverage(Color[] colors, float[] weights){
        float r = 0;
        float g = 0;
        float b = 0;
        for (int i = 0; i < colors.length; i++){
            r += colors[i].getRed() * colors[i].getRed() * weights[i];
            g += colors[i].getGreen() * colors[i].getGreen() * weights[i];
            b += colors[i].getBlue() * colors[i].getBlue() * weights[i];
        }
        return new Color((int) Math.min(Math.sqrt(r), 255), (int) Math.min(Math.sqrt(g), 255), (int) Math.min(Math.sqrt(b), 255));
    }

    //adding subtractively if that makes any sense?
    // 0 < intensity < 1
    public static Color addLight(Color color, LightColor light){
        return new Color(
                Math.min(((float)color.getRed()/255) * light.r, 1),
                Math.min(((float)color.getGreen()/255) * light.g, 1),
                Math.min(((float)color.getBlue()/255) * light.b, 1)
        );
    }

    public static Color toColor (Intersection i) {
        if (i.m == null) {
            //no intersection, return background color TO DO
            return Color.DARK_GRAY;
        } else {

            //find local color according to direct light
            Color color = Color.getHSBColor(i.m.h, i.m.s, i.m.l);
            //Color.getHSBColor()
            color = Colors.addLight(color, LightColor.scale(i.colorOfLight, i.totalLightPerArea));

            //average in ambient lighting;
            if (i.ambientChildren != null) {
                Color[] ambientColors = new Color[Render.ambientRayCount];
                float[] weights = new float[Render.ambientRayCount];
                for (int j = 0; j < Render.ambientRayCount; j++) {
                    ambientColors[j] = toColor(i.ambientChildren[j]);
                    weights[j] = Vec.dot(i.n, i.ambientChildren[j].incomingDirection) / (Render.ambientRayCount);
                }
                Color ambientColor = Colors.weightedAverage(ambientColors, weights);
                ambientColor = Colors.addLight(Color.getHSBColor(i.m.h, i.m.s, i.m.l), new LightColor(ambientColor));
                color = Colors.weightedAverage(new Color[]{ambientColor, color}, new float[]{Render.ambientIntensity, 1 - Render.ambientIntensity});
            }

            //average in reflected color
            if (i.reflectedChild != null) {
                //time for recursion
                Color[] colors = new Color[]{color, Colors.toColor(i.reflectedChild)};
                float[] weights = new float[]{1 - i.m.r, i.m.r};
                color = Colors.weightedAverage(colors, weights);
            }
            return color;
        }
    }
}
