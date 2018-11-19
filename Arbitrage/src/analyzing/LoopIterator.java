package analyzing;

public class LoopIterator {
    int iterator;
    int size;

    public LoopIterator(int i, int size) {
        this.iterator = i;
        this.size = size;
    }

    public int next(){
        if (iterator +1 < size){
            iterator++;
        }else{
            iterator = 0;
        }
        return iterator;
    }

    public int getIterator() {
        return iterator;
    }
}
