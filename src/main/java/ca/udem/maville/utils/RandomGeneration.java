package ca.udem.maville.utils;

import java.util.Random;

public final class RandomGeneration {

    public static int getRandomUniformInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(min, max);
    }

    public static double getRandomUniformDouble(double min, double max) {
        Random random = new Random();
        return random.nextDouble(min, max);
    }

}
