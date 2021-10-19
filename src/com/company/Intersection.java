package com.company;

import com.company.Geometries.Plane;
import com.company.Geometries.Ray;
import com.company.Geometries.Sphere;
import com.company.Lights.PointLight;
import com.company.Materiality.Colors;
import com.company.Materiality.Material;
import com.company.Maths.Constants;
import com.company.Maths.Vec;

import java.awt.*;
import java.util.*;

import static com.company.Materiality.Colors.toColor;


public class Intersection {
    public float t; //distance between projection point and intersection point
    public Material m;
    public Vec p; //position of intersection
    public Vec n; //unit normal
    public Vec incomingDirection; // unit vector
    public float totalLightPerArea; // only direct light
    public Color colorOfLight;
    public int d;   //recursiveDepth:
                    // an intersection has depth 0 if its ray came from the camera
                    // an intersection has depth 1 if its ray came from an intersection with depth 0
                    // an intersection has depth n if its ray came from an intersection with depth n-1
    public Intersection reflectedChild; //the intersection that the ray hits after it bounces off this one
    public float c; //amount of light calculated so far, 0 < c < 1,  being this is the first ray, 1 meaning all light has b
                    //if this value
    public Intersection[] ambientChildren;

    //default intersection is by default really really far away
    public Intersection(){
        t = Float.MAX_EXPONENT;
    }

    //Intersection constructor
    //each intersection spawns new rays to compute incoming light unless the depth limit is reached
    public Intersection(float time,
                        Material material,
                        Vec position,
                        Vec normal,
                        Vec IncomingDirection,
                        int depth){
        d = depth;
        t = time;
        m = material;
        p = position;
        n = normal;
        incomingDirection = IncomingDirection;

        if (depth < Render.maxReflectionDepth && m.r != 0){
            //time to bounce the ray off the intersection to find the next intersection
            Vec mirroredRay = Vec.mirror(IncomingDirection, normal);
            Ray outgoing = new Ray(p, mirroredRay, d + 1);
            reflectedChild = Intersection.intersect(outgoing);
        }

        if (depth < Render.maxAmbientDepth){
            //time to find ambient children
            Vec[] perturbedRays = Vec.toPerturbedArrays(normal);
            ambientChildren = new Intersection[Render.ambientRayCount];
            for (int i = 0; i < Render.ambientRayCount; i++){
                Ray outgoing = new Ray(p, perturbedRays[i], d + 1);
                ambientChildren[i] = Intersection.intersect(outgoing);
            }
        }
    }

    // this method finds the closest intersection of a ray with every object in the scene
    public static Intersection intersect(Ray ray) {

        Intersection spheresIntersection = Sphere.closestIntersection(ray, Scene.spheres);
        Intersection planesIntersection = Plane.closestIntersection(ray, Scene.planes);
        Collection possibleIntersections = new ArrayList();
        possibleIntersections.add(spheresIntersection);
        possibleIntersections.add(planesIntersection);
        Intersection intersection =  Collections.min(possibleIntersections, Comparator.comparing((Intersection i) -> i.getT()));

        if (intersection.t != Float.MAX_EXPONENT) {
            Intersection.getDirectLight(intersection);
        }

        return intersection;
    }


    public float getT() {
        return t;
    }


    public static void getDirectLight(Intersection i){
        float result = 0;
        float r = 0;
        float g = 0;
        float b = 0;
        for (PointLight light : Scene.pointLights){
            Vec toLight = Vec.subtract(i.p, light.p);
            float distance = Vec.size(toLight);
            if (Intersection.isVisible(light.p, i, distance)){
                //found visible light
                Vec toLightNorm = Vec.normalize(toLight);
                float factor = Math.abs((light.i * Vec.dot(i.n, toLightNorm)) / (distance * distance));
                result += factor;
                r += light.c.getRed() * factor;
                g += light.c.getGreen() * factor;
                b += light.c.getBlue() * factor;
            }
        }
        r /= result * 255.01;
        g /= result * 255.01;
        b /= result * 255.01;
        i.colorOfLight = new Color(r,g,b);
        i.totalLightPerArea = result;
    }

    public static void getLight(Intersection i){
        float result = 0;
        float r = 0;
        float g = 0;
        float b = 0;
        for (PointLight light : Scene.pointLights){
            Vec toLight = Vec.subtract(i.p, light.p);
            float distance = Vec.size(toLight);
            if (Intersection.isVisible(light.p, i, distance)){
                //found visible light
                Vec toLightNorm = Vec.normalize(toLight);
                float factor = Math.abs((light.i * Vec.dot(i.n, toLightNorm)) / (distance * distance));
                result += factor;
                r += light.c.getRed() * factor;
                g += light.c.getGreen() * factor;
                b += light.c.getBlue() * factor;
            }
        }

        if (i.ambientChildren != null) {
            Color[] ambientColors = new Color[Render.ambientRayCount];
            float[] weights = new float[Render.ambientRayCount];
            for (int j = 0; j < Render.ambientRayCount; j++) {
                ambientColors[j] = toColor(i.ambientChildren[j]);
                weights[j] = Vec.dot(i.n, i.ambientChildren[j].incomingDirection) / (Render.ambientRayCount);
            }
            Color ambientColor = Colors.weightedAverage(ambientColors, weights);
            r += ambientColor.getRed() * Render.ambientIntensity;
            g += ambientColor.getGreen() * Render.ambientIntensity;
            b += ambientColor.getBlue() * Render.ambientIntensity;
        }

        r /= result * 255.01;
        g /= result * 255.01;
        b /= result * 255.01;

        r = Math.min(r, 1);
        g = Math.min(g, 1);
        b = Math.min(b, 1);

        i.colorOfLight = new Color(r,g,b);
        i.totalLightPerArea = result;
    }


    //determines if the light shines on the intersection
    public static boolean isVisible(Vec point, Intersection i, float distance){
        Vec toLight = Vec.subtract(point, i.p);
        toLight = Vec.normalize(toLight);
        Ray ray = new Ray(i.p, toLight, i.d + 1);
        for (Sphere sphere : Scene.spheres) {
            float t = Sphere.intersect(ray, sphere);
            if (t > Constants.fudge && t < distance){
                return false;
            }
        }
        for (Plane plane : Scene.planes) {
            //find time of intersection if any exists
            float t = Plane.intersect(ray, plane);
            if (t > Constants.fudge && t < distance){
                return false;
            }
        }
        return true;
    }


}
