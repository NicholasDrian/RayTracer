package com.company;

import com.company.Geometries.Ray;
import com.company.Maths.Vec;

public class Camera {
    Vec p; // position;
    Vec d; //normal direction;
    Vec x; //x axis;
    Vec y; //y axis;
    float lensLength; //distance between position and screen center;
    float vertFOV; //vertical fov in radians
    float horFOV; //horizontal fov in radians
    int vertRes; //vertical resolution;
    int horRes; //horizontal resolution;

    public Camera(Vec position,
                  Vec direction, //unit vector
                  Vec xAxis, //unit vector
                  Vec yAxis, //unit vector
                  float LensLength,
                  float fovVert,
                  float fovHor,
                  int verticalRes){
        p = position;
        d = direction;
        x = xAxis;
        y = yAxis;
        lensLength = LensLength;
        vertFOV =  fovVert; //radians
        horFOV = fovHor; //radians
        vertRes = verticalRes;
        horRes = Math.round(horFOV * vertRes / vertFOV);
    }

    //generates rays that begin at camera position and t = 1 is when it intersects a spherical screen.
    //unit length
    public static Ray[][] rayGenerator(Camera camera) {
        Ray[][] result = new Ray[camera.horRes][camera.vertRes];
        Vec screenCenter = Vec.move(camera.p, camera.d, camera.lensLength);
        float xDistanceToEdge = (float) (Math.tan(camera.horFOV/2) * camera.lensLength);
        float yDistanceToEdge = (float) (Math.tan(camera.vertFOV/2) * camera.lensLength);
        for (int i = 0; i < camera.horRes; i++){
            for (int j = 0; j < camera.vertRes; j++){
                float negativeOneToOneX = ((2f * i)/ (camera.horRes - 1))-1;
                float negativeOneToOneY = -(((2f * j)/ (camera.vertRes - 1))-1);
                float xMotion = xDistanceToEdge * negativeOneToOneX;
                float yMotion = yDistanceToEdge * negativeOneToOneY;
                Vec xTranslation = Vec.scale(camera.x, xMotion);
                Vec yTranslation = Vec.scale(camera.y, yMotion);
                Vec translation = Vec.add(xTranslation, yTranslation);
                Vec pointOnScreen = Vec.add(screenCenter, translation);
                Vec rayDirection = Vec.subtract(pointOnScreen, camera.p);
                rayDirection = Vec.normalize(rayDirection);
                Ray ray = new Ray(camera.p, rayDirection, 0);
                result[i][j] = ray;
            }
        }
        return result;
    }

    public static Ray[] rayGenerator(int line) {
        Ray[] result = new Ray[Scene.camera.vertRes];
        Vec screenCenter = Vec.move(Scene.camera.p, Scene.camera.d, Scene.camera.lensLength);
        float xDistanceToEdge = (float) (Math.tan(Scene.camera.horFOV/2) * Scene.camera.lensLength);
        float yDistanceToEdge = (float) (Math.tan(Scene.camera.vertFOV/2) * Scene.camera.lensLength);
            for (int j = 0; j < Scene.camera.vertRes; j++){
                float negativeOneToOneX = ((2f * line)/ (Scene.camera.horRes - 1))-1;
                float negativeOneToOneY = -(((2f * j)/ (Scene.camera.vertRes - 1))-1);
                float xMotion = xDistanceToEdge * negativeOneToOneX;
                float yMotion = yDistanceToEdge * negativeOneToOneY;
                Vec xTranslation = Vec.scale(Scene.camera.x, xMotion);
                Vec yTranslation = Vec.scale(Scene.camera.y, yMotion);
                Vec translation = Vec.add(xTranslation, yTranslation);
                Vec pointOnScreen = Vec.add(screenCenter, translation);
                Vec rayDirection = Vec.subtract(pointOnScreen, Scene.camera.p);
                rayDirection = Vec.normalize(rayDirection);
                Ray ray = new Ray(Scene.camera.p, rayDirection, 0);
                result[j] = ray;
            }
        return result;
    }
}
