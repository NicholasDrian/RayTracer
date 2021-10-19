package com.company.Geometries;

import com.company.Intersection;
import com.company.Materiality.Material;
import com.company.Maths.Constants;
import com.company.Maths.Vec;

//Sphere: dot((P−C),(P−C))=r2
public class Sphere {
    public Vec c;//center
    public float r;//radius
    public Material m;

    //returns soonest positive t where ray intersects sphere.
    //returns -1 if ray does not intersect sphere.

    public Sphere (Vec center, float radious, Material material){
        c = center;
        r = radious;
        m = material;
    }

    public static float intersect(Ray ray, Sphere sphere){
        Vec oc = Vec.subtract(ray.o, sphere.c);
        float a = Vec.dot(ray.d, ray.d);
        float b = (float) (2.0 * Vec.dot(oc, ray.d));
        float c = Vec.dot(oc,oc) - sphere.r * sphere.r;
        float d = b * b - 4 * a * c;
        if (d < 0){
            //no intersection
            return -1;
        }else{
            float sqrt = (float) Math.sqrt(d);
            float maybeThis = (float) ((-b - sqrt) / (2.0 * a));
            float maybeThat = (float) ((-b + sqrt) / (2.0 * a));
            if (maybeThis > Constants.fudge){ return maybeThis; }
            if (maybeThat > Constants.fudge){ return maybeThat; }
            return -1;
        }
    }

    public static Intersection closestIntersection(Ray ray, Sphere[] spheres) {
        Intersection intersection = new Intersection();
        intersection.incomingDirection = ray.d;
        for (Sphere sphere : spheres) {
            //find time of intersection if any exists
            float t = Sphere.intersect(ray, sphere);
            if (t != -1) {
                if (intersection.t == Float.MAX_EXPONENT || (t > 0 && t < intersection.t)) {
                    //found new closest intersection
                    Vec position = Ray.getT(ray, t);
                    Vec normal = Vec.subtract(position, sphere.c);
                    normal = Vec.normalize(normal); // unit vector
                    intersection = new Intersection(t, sphere.m, position, normal, ray.d, ray.depth);
                }
            }
        }
        return intersection;
    }
}
