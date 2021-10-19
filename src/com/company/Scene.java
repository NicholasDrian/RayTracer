package com.company;

import com.company.Geometries.Plane;
import com.company.Geometries.Sphere;
import com.company.Geometries.Tri;
import com.company.Lights.PointLight;

public class Scene {
    static Camera camera;
    static Tri[] triangles;
    static Sphere[] spheres;
    static Plane[] planes;
    static PointLight[] pointLights;
    
    public Scene (Camera Camera, Tri[] Triangles, Sphere[] Spheres, Plane[] Planes, PointLight[] PointLights){
        camera = Camera;
        triangles = Triangles;
        spheres = Spheres;
        planes = Planes;
        pointLights = PointLights;
    }
    
}
