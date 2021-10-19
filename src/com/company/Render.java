package com.company;
import com.company.Geometries.Ray;
import com.company.Materiality.Colors;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public interface Render {

    //render options
    int maxReflectionDepth = 4;
    int maxAmbientDepth = 2;
    int ambientRayCount = 5;
    float ambientIntensity = .2f; //0 == no ambient light, 1 == all ambient light

    static void render(String name){
        Ray[][] rays = Camera.rayGenerator(Scene.camera);
        Intersection[][] intersections = new Intersection[Scene.camera.horRes][Scene.camera.vertRes];
        BufferedImage image = new BufferedImage(Scene.camera.horRes, Scene.camera.vertRes ,BufferedImage.TYPE_INT_RGB);



        //iterate through all pixels and all objects to find all intersections
        for (int i = 0; i < Scene.camera.horRes; i++) {
            for (int j = 0; j < Scene.camera.vertRes; j++) {
                //for each pixel
                intersections[i][j] = Intersection.intersect(rays[i][j]);
                Color color = Colors.toColor(intersections[i][j]);
                image.setRGB(i, j, color.getRGB());
            }
        }
        File file = new File(name);
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static BufferedImage renderLine(BufferedImage image, int line, boolean[] done){
        Ray[] rays = Camera.rayGenerator(line);
        Intersection[] intersections = new Intersection[Scene.camera.vertRes];
        int end = line;
        while (end < Scene.camera.horRes && end - line < 20 &&  done[end] == false) {
            end++;
        }
        for (int i = line; i < end; i++) {
            for (int j = 0; j < Scene.camera.vertRes; j++) {
                intersections[j] = Intersection.intersect(rays[j]);
                Color color = Colors.toColor(intersections[j]);
                image.setRGB(i, j, color.getRGB());
            }
        }
        return image;
    }

    static void renderProgressive(String name){
        JFrame frame = new JFrame();
        BufferedImage image = Render.createImage(Scene.camera.horRes, Scene.camera.vertRes, Color.WHITE);
        ImageIcon icon = new ImageIcon(image);
        JLabel label = new JLabel(icon);
        frame.add(label);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        File file = new File(name);
        int[] lines = new int[Scene.camera.horRes];
        for (int i = 0; i < Scene.camera.horRes; i++){
            lines[i]= i;
        }
        shuffleArray(lines);
        boolean[] done = new boolean[Scene.camera.horRes];
        for (int i : lines){
            Render.renderLine(image, i, done);
            icon = new ImageIcon(image);
            label = new JLabel(icon);
            label.setForeground(Color.WHITE);
            frame.add(label);
            frame.pack();
            done[i] = true;
        }
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void shuffleArray(int[] ar)
    {   //not my code
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    static BufferedImage createImage(int x, int y, Color c){
        BufferedImage image = new BufferedImage(Scene.camera.horRes, Scene.camera.vertRes, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < Scene.camera.horRes; i++){
            for (int j = 0; j < Scene.camera.vertRes; j++){
                image.setRGB(i, j, c.getRGB());
            }
        }
        return image;
    }
}
