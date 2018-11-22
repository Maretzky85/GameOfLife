package Model;

/**
 * Dot Class
 * holds number of generations lived
 * overrides toString method for console output printing
 */
public class Dot {

    private int generation = 0;

    @Override
    public String toString() {
        return "*";
    }

    public int getGeneration() {
        if (generation < 255) {
            generation++;
        }
        return generation;
    }
}
