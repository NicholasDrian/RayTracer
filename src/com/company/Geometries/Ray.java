package com.company.Geometries;

import com.company.Maths.Vec;

//p(t)=o+td
public class Ray {
    public Vec o; //origin
    public Vec d; //direction
    public int depth;

    public Ray(Vec origin, Vec direction, int Depth){
        o = origin;
        d = direction;
        depth = Depth;
    }
    public static Vec getT(Ray ray, float t){
        return Vec.add(ray.o, Vec.scale(ray.d, t));
    }
}
