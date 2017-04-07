package pl.edu.pwr.dawidszewczyk.lab1.lab1;

/**
 * Created by Dawid on 2017-03-19.
 */

public interface ICountBMI {
    boolean isMassValid(float mass);
    boolean isHeightValid(float height);
    float countBMI(float mass, float height);
}
