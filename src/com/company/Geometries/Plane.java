package com.company.Geometries;

import com.company.Intersection;
import com.company.Materiality.Material;
import com.company.Maths.Vec;
import com.company.Scene;

public class Plane {
    public Material m; //material
    public float d; // displacement from origin
    public Vec n; // normal

    public Plane(Material material, float displacement, Vec normal){
        m = material;
        d = displacement;
        n = normal;
    }

    //intersection: t = - (d + planeN dot rayO) / (planeN dot rayD)
    //returns -1 if there is no intersection
    public static float intersect (Ray ray, Plane plane){
        float denominator = Vec.dot(plane.n, ray.d);
        if (denominator == 0){
            //no intersection
            return -1;
        } else {
            float numerator = -(plane.d + Vec.dot(plane.n, ray.o));
            float result = numerator/denominator;
            if (result > 0){
                return result;
            } else {
                return -1;
            }
        }
    }

    public static Intersection closestIntersection (Ray ray, Plane[] planes){
        Intersection intersection = new Intersection();
        intersection.incomingDirection = ray.d;
        for (Plane plane : planes) {
            //find time of intersection if any exists
            float t = Plane.intersect(ray, plane);
            if (t != -1) {
                if (intersection.t == Float.MAX_EXPONENT || (t > 0 && t < intersection.t)) {
                    //found new closest intersection
                    Vec position = Ray.getT(ray, t);
                    Vec normal = plane.n;// unit vector
                    intersection = new Intersection(t, plane.m, position, normal, ray.d, ray.depth);
                }
            }
        }
        return intersection;
    }

}
