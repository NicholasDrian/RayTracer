package com.company.Geometries;


import com.company.Materiality.Material;
import com.company.Maths.Constants;
import com.company.Maths.Vec;

//triangle
public class Tri {
    public Vec a;
    public Vec b;
    public Vec c;
    public Vec n; //normal
    public Material m;

    public static float intersect (Ray ray, Tri tri){
        //find intersection
        Plane plane = Tri.toPlane(tri);
        float intersection = Plane.intersect(ray, plane);
        //see if the intersection is within the triangle
        return -1;
    }

    private static Plane toPlane(Tri tri) {
        Vec normal =Tri.getNorm(tri);
        return null;

    }

    private static Vec getNorm(Tri tri) {
        Vec a = Vec.subtract(tri.a, tri.b);
        Vec b = Vec.subtract(tri.a, tri.c);
        return Vec.crossProd(a,b);
    }
}
