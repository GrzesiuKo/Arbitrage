package analyzing;

public class LoopIterator {
    int iterator;
    int size;
    int begin;
    boolean hasLoopEnded;

    public LoopIterator(int begin, int size) {
        this.iterator = begin;
        this.begin = begin;
        this.size = size;
    }

    public int next() {
        hasLoopEnded = false;

        if (iterator + 1 < size) {
            iterator++;
        } else {
            iterator = 0;
        }
        if (iterator == begin) {
            hasLoopEnded = true;
        }
        return iterator;
    }

    public int getIterator() {
        return iterator;
    }

    public boolean hasLoopEnded() {
        return hasLoopEnded;
    }
}
