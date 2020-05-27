package hr.fer.zemris.rng.rngimpl;

import hr.fer.zemris.rng.IRNGProvider;
import hr.fer.zemris.rng.IRNG;

public class ThreadBoundRNGProvider implements IRNGProvider {
    @Override
    public IRNG getRNG() {
        return ((EVOThread) Thread.currentThread()).getRNG();
    }
}
