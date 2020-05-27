package hr.fer.zemris.rng.rngimpl;

import hr.fer.zemris.rng.IRNG;
import hr.fer.zemris.rng.IRNGProvider;

public class EVOThread extends Thread implements IRNGProvider {
    private IRNG rng = new RNGRandom();

    public EVOThread() {
    }

    public EVOThread(Runnable target) {
        super(target);
    }

    public EVOThread(String name) {
        super(name);
    }

    public EVOThread(ThreadGroup group, Runnable target) {
        super(group, target);
    }

    public EVOThread(ThreadGroup group, String name) {
        super(group, name);
    }

    public EVOThread(Runnable target, String name) {
        super(target, name);
    }

    public EVOThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
    }

    public EVOThread(ThreadGroup group, Runnable target, String name,
                     long stackSize) {
        super(group, target, name, stackSize);
    }

    @Override
    public IRNG getRNG() {
        return rng;
    }
}