package hr.fer.zemris.rng.rngimpl;

import hr.fer.zemris.rng.IRNG;

import java.util.Random;

public class RNGRandom implements IRNG {

    Random rand;

    public RNGRandom() {
        this.rand = new Random();
    }

    @Override
    public double nextDouble() {
        return rand.nextDouble();
    }

    @Override
    public double nextDouble(double min, double max) {
        return min + rand.nextDouble() * (max - min);
    }

    @Override
    public float nextFloat() {
        return rand.nextFloat();
    }

    @Override
    public float nextFloat(float min, float max) {
        return min + rand.nextFloat() * (max - min);
    }

    @Override
    public int nextInt() {
        return rand.nextInt();
    }

    @Override
    public int nextInt(int min, int max) {
        return min + rand.nextInt(max - min);
    }

    @Override
    public boolean nextBoolean() {
        return rand.nextBoolean();
    }

    @Override
    public double nextGaussian() {
        return rand.nextGaussian();
    }
}
