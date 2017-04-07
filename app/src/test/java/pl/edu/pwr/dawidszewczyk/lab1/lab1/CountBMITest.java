package pl.edu.pwr.dawidszewczyk.lab1.lab1;

/**
 * Created by Dawid on 2017-03-19.
 */

import junit.framework.TestCase;
import org.junit.Test;

import java.security.InvalidParameterException;

public class CountBMITest {
    @Test
    public void massUnderZeroIsInvalid() {
        float testMass = -1.0F;
        CountBMIForKgM countBMI = new CountBMIForKgM();
        boolean actual = countBMI.isMassValid(testMass);
        TestCase.assertFalse(actual);
    }
    @Test
    public void BMIForKgMIsCorrect() {
        float mass = 62.0F;
        float height = 1.75F;
        CountBMIForKgM countBMI = new CountBMIForKgM();
        float actual = countBMI.countBMI(mass, height);
        TestCase.assertEquals(20.24F, actual, 0.1F);
    }

    @Test(expected = InvalidParameterException.class)
    public void heightUnderHalfMetreThrowException() {
        float mass = 50f;
        float height = 0.4f;
        CountBMIForKgM countBMI = new CountBMIForKgM();
        countBMI.countBMI(mass, height);
    }

    @Test
    public void massBetween250And550ftIsCorrect() {
        float mass = 400f;
        CountBMIForLbFt countBMI = new CountBMIForLbFt();
        TestCase.assertTrue(countBMI.isMassValid(mass));
    }

    @Test
    public void BMIForKgMEqualsBMIForLbFt() {
        float kilograms = 62f;
        float meters = 1.75f;
        float pounds = 136.69f;
        float feet = 5.74f;
        CountBMIForKgM countBMIForKgM = new CountBMIForKgM();
        CountBMIForLbFt countBMIForLbFt = new CountBMIForLbFt();
        float BMIForKgM = countBMIForKgM.countBMI(kilograms, meters);
        float BMIForLbFt = countBMIForLbFt.countBMI(pounds, feet);
        TestCase.assertEquals(BMIForKgM, BMIForLbFt, 0.02f);
    }
}

