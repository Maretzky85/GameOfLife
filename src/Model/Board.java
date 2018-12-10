package Model;

public interface Board {

    void nextGen();

    int getGeneration();

    void initExampleBoard();

    void clearBoard();

    void changeOnPosition(int x, int y);

    boolean isBusy();

    Dot[][] getBoard();

    void setRules(int i);
}
