package com.company.Maths;

import com.company.Render;

public class Vec {

    public float[] nums;

    public Vec(float[] Nums){
        nums  = Nums;
    }

    public static float dot(Vec a, Vec b){
        float result = 0;
        for (int i = 0; i < a.nums.length; i ++){
            result += a.nums[i] * b.nums[i];
        }return result;
    }

    public static Vec subtract(Vec a, Vec b){
        float[] result = new float[a.nums.length];
        for (int i = 0; i < a.nums.length; i++){
            result[i] = a.nums[i] - b.nums[i];
        }
        return new Vec(result);
    }

    public static Vec add(Vec a, Vec b){
        float[] result = new float[a.nums.length];
        for (int i = 0; i < a.nums.length; i++){
            result[i] = a.nums[i] + b.nums[i];
        }
        return new Vec(result);
    }

    public static Vec rotate (Vec vec, Vec axis, float theta){
        float dot = Vec.dot(vec, axis);
        float sin = (float) Math.sin(theta);
        float cos = (float) Math.cos(theta);
        float x = (float) (axis.nums[0] * dot *(1d - cos)
                        + vec.nums[0] *Math.cos(theta)
                        + (-axis.nums[2] * vec.nums[1] + axis.nums[1] * vec.nums[2]) * sin);
        float y = (float) (axis.nums[1] * dot *(1d - cos)
                        +  vec.nums[1] * cos
                        + (axis.nums[2] * vec.nums[0] - axis.nums[0] * vec.nums[2]) * sin);
        float z = (float) (axis.nums[2] * dot *(1d - cos)
                        + vec.nums[2] * cos
                        + (-axis.nums[1]*vec.nums[0] + axis.nums[0]*vec.nums[1])* sin);
        return new Vec(new float[]{x, y, z});
    }

    public static String toString(Vec vec){
        StringBuilder s = new StringBuilder();
        for (float f: vec.nums){
            s.append(f);
            s.append("  ");
        }
        return s.toString();
    }

    public static Vec move(Vec point, Vec direction, float distance) {
        float directionMagnitude = Vec.size(direction);
        float scaleFactor = distance/directionMagnitude;
        Vec translation = Vec.scale(direction, scaleFactor);
        return Vec.add(point, translation);

    }

    public static float size(Vec vec){
        float result = 0;
        for (float f: vec.nums){
            result += f * f;
        }
        return (float) Math.sqrt(result);
    }

    public static Vec scale(Vec vec, float factor){
        float[] result = new float[vec.nums.length];
        for (int i = 0; i < vec.nums.length; i++){
            result[i] = vec.nums[i] * factor;
        }
        return new Vec(result);
    }

    public static Vec normalize(Vec vec){
        float sizeOfVec = Vec.size(vec);
        float[] result = new float [vec.nums.length];
        sizeOfVec = (float) Math.sqrt(sizeOfVec);
        for (int i = 0; i < vec.nums.length; i ++){
            result[i] = vec.nums[i]/sizeOfVec;
        }
        return new Vec(result);
    }

    //vec and normal must have negative dot product (the incoming vector must be facing the vector that its reflected off of)
    //think of this as reflecting a vector off of a plane (the plane is represented by the normal)
    public static Vec mirror(Vec vec, Vec normal){
        Vec transformation = Vec.scale(normal, -2 * Vec.dot(vec, normal));
        return Vec.add(vec, transformation);
    }


    public static Vec[] toPerturbedArrays(Vec normal) {
        Vec[] result = new Vec[Render.ambientRayCount];
        for (int i = 0; i < Render.ambientRayCount; i++) {
            float x = (float) Math.random() - .5f;
            float y = (float) Math.random() - .5f;
            float z = (float) Math.random() - .5f;
            float size = (float) Math.sqrt(x * x + y * y + z * z);
            x /= size;
            y /= size;
            z /= size;
            Vec v = new Vec(new float[]{x,y,z});
            if (Vec.dot(v,normal) < 0){
                v = mirror(v, normal);
            }
            result[i] = v;
        }
        return result;
    }

    public static Vec crossProd(Vec a, Vec b){
        float x = a.nums[1] * b.nums[2] - a.nums[2] * b.nums[1];
        float y = a.nums[2] * b.nums[0] - a.nums[0] * b.nums[2];
        float z = a.nums[0] * b.nums[1] - a.nums[1] * b.nums[0];
        Vec vec = new Vec(new float[]{x,y,z});
        return Vec.normalize(vec);
    }

    public Vec copy() {
        return new Vec(new float[]{nums[0], nums[1], nums[2]});
    }
}
