package hr.fer.zemris.rng.rngimpl;

import hr.fer.zemris.rng.IRNGProvider;
import hr.fer.zemris.rng.IRNG;

public class ThreadLocalRNGProvider implements IRNGProvider {

    private ThreadLocal<IRNG> threadLocal = new ThreadLocal<>();

    @Override
    public IRNG getRNG() {
        if (threadLocal.get() == null){
            threadLocal.set(new RNGRandom());
        }
        return threadLocal.get();
    }
}
