package hr.fer.zemris.rng;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class RNG {
    private static IRNGProvider rngProvider;
    static {
        // Stvorite primjerak razreda Properties;
        // Nad Classloaderom razreda RNG tražite InputStream prema resursu rng-config.properties
        // recite stvorenom objektu razreda Properties da se učita podatcima iz tog streama.
        // Dohvatite ime razreda pridruženo ključu "rng-provider"; zatražite Classloader razreda
        // RNG da učita razred takvog imena i nad dobivenim razredom pozovite metodu newInstance()
        // kako biste dobili jedan primjerak tog razreda; castajte ga u IRNGProvider i zapamtite.

        Properties prop = new Properties();

        try {
            prop.load(RNG.class.getClassLoader().getResourceAsStream("rng-config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String className = prop.getProperty("rng-provider");
        try {
            rngProvider = ((IRNGProvider) RNG.class.getClassLoader().loadClass(className).getDeclaredConstructor()
                    .newInstance());
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }
    public static IRNG getRNG() {
        return rngProvider.getRNG();
    }
}