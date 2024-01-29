package online.ringimpl;
import online.ringimpl.contract.RingIndex;

class RingIndexShort implements RingIndex{
    private int curr;
    private int[] internalBuffer;


    // package scope
    RingIndexShort (int startWith, int numEls) {
        this.internalBuffer = new int[numEls];
        int k = 0;

        this.curr  = 0;
        for (int i = startWith; i < numEls; this.internalBuffer[k++] = i++);
        for (int i = 0; i < startWith; this.internalBuffer[k++] = i++);
    }

    public int getNext() {
        return (this.curr < this.internalBuffer.length) ? this.internalBuffer[this.curr++] : -1;
    }

}
