package pl.edu.pwr.dawidszewczyk.lab1.lab1;

/**
 * Created by Dawid on 2017-03-19.
 */

import java.security.InvalidParameterException;

public class CountBMIForKgM implements ICountBMI {
    static final float minMass = 10.0F;
    static final float maxMass = 250.0F;
    static final float minHeight = 0.5F;
    static final float maxHeight = 2.5F;

    public boolean isHeightValid(float height) {
        return height > minHeight && height < maxHeight;
    }

    public boolean isMassValid(float mass) {
        return mass > minMass && mass < maxMass;
    }

    public float countBMI(float mass, float height) {
        if(isMassValid(mass) && isHeightValid(height)) {
            return mass / (height * height);
        } else {
            throw new InvalidParameterException("Invalid mass or height");
        }
    }
}
