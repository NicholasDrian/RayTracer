package com.company;

import com.company.Geometries.Plane;
import com.company.Geometries.Sphere;
import com.company.Geometries.Tri;
import com.company.Lights.PointLight;
import com.company.Materiality.Colors;
import com.company.Materiality.LightColor;
import com.company.Materiality.Material;
import com.company.Maths.Vec;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.PI;

public class Main {

    public static void test(){
        //camera info
        Vec position = new Vec(new float[]{0f,0f,2f});
        Vec direction = new Vec(new float[]{1f,0f,0f});
        Vec xAxis = new Vec(new float[]{0f,-1f,0f});
        Vec yAxis = new Vec(new float[]{0f,0f,1f});
        Camera camera = new Camera(
                position,
                direction,
                xAxis,
                yAxis,
                1,
                (float)PI/3,
                (float)PI/2, 400);

        //material info
        Material orange = new Material(.5f, 1,0.5f, .5f);
        Material red = new Material(.3f, 1f,0.8f, .5f);
        Material blue = new Material(.7f, 1f,0.02f, .5f);
        Material grey = new Material(0f, 0f,0.2f, .2f);

        //spheres
        Vec spherePosition1 = new Vec(new float[]{4f,2f,1.5f});
        Sphere sphere1 = new Sphere(spherePosition1, 1.5f, red);

        Vec spherePosition2 = new Vec(new float[]{14,-3,3});
        Sphere sphere2 = new Sphere(spherePosition2, 6, orange);

        Vec spherePosition3 = new Vec(new float[]{7,0.5f,4.5f});
        Sphere sphere3 = new Sphere(spherePosition3, .75f, blue);

        //planes
        Vec normal = new Vec(new float[] {0,0,1});
        Plane plane = new Plane(grey, 0, normal);

        //Vec normal2 = new Vec(new float[] {0,0,1});
        //Plane plane2 = new Plane(grey, -100, normal2);

        //lights
        Vec lightPosition = new Vec(new float[]{10,-15,30});
        PointLight light = new PointLight(lightPosition, Color.white, 1);

        Vec lightPosition2 = new Vec(new float[]{-50,15,30});
        PointLight light2 = new PointLight(lightPosition2, Color.WHITE, 1);

        Vec lightPosition3 = new Vec(new float[]{20,-10,30});
        PointLight light3 = new PointLight(lightPosition3, Color.white, 1);

        Vec lightPosition4 = new Vec(new float[]{0,5,30});
        PointLight light4 = new PointLight(lightPosition4, Color.WHITE, 1);

        //scene
        Tri[] Triangles = new Tri[]{};
        Sphere[] Spheres = new Sphere[]{sphere1, sphere2, sphere3};
        Plane[] Planes = new Plane[]{plane};
        PointLight[] PointLights = new PointLight[]{light, light2, light3, light4};
        new Scene(camera, Triangles, Spheres, Planes, PointLights);
/* //Light animation
        for (int i = 0; i < 100; i++){
            float j = i;
            j /= 50;
            j *= Math.PI;
            System.out.println(i);
            float height = (float) (15 * Math.sin(j) + 30);
            Vec axis = new Vec(new float[]{0,0,1});
            float t = (float) (2 * Math.PI / 100);
            light.p = Vec.rotate(light.p, axis, t);
            light2.p = Vec.rotate(light2.p, axis, t);
            light3.p = Vec.rotate(light3.p, axis, t);
            light4.p = Vec.rotate(light4.p, axis, t);
            light.p.nums[2] = height;
            light2.p.nums[2] = height;
            light3.p.nums[2] = height;
            light4.p.nums[2] = height;
            Render.render("image" + i + ".png");
        }
 */
        Render.renderProgressive("image.png");
    }

    public static void colorTest(){
        Color a = new Color(.5f,.3f,.7f);
        LightColor b = new LightColor(1.1f,1.1f,1.6f);
        Color c = Colors.addLight(a, b);
        BufferedImage image = new BufferedImage(3, 1 ,BufferedImage.TYPE_INT_RGB);
        image.setRGB(0,0,a.getRGB());
        image.setRGB(1,0,b.getRGB());
        image.setRGB(2,0,c.getRGB());
        File file = new File("colorTest.jpg");
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        test();
        //colorTest();
    }
}
