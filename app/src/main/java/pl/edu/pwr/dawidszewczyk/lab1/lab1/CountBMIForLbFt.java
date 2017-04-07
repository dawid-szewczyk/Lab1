package pl.edu.pwr.dawidszewczyk.lab1.lab1;

import java.security.InvalidParameterException;

/**
 * Created by Dawid on 2017-03-28.
 */

public class CountBMIForLbFt implements ICountBMI {
    static final float minMass = 22F;
    static final float maxMass = 550F;
    static final float minHeight = 1.64F;
    static final float maxHeight = 8.2F;

    public boolean isHeightValid(float height) {
        return height > minHeight && height < maxHeight;
    }

    public boolean isMassValid(float mass) {
        return mass > minMass && mass < maxMass;
    }

    public float countBMI(float mass, float height) {
        if(isMassValid(mass) && isHeightValid(height)) {
            height = height * 12f;  //Stopy na cale
            return (mass * 703) / (height * height);
        } else {
            throw new InvalidParameterException("Invalid mass or height!");
        }
    }
}
