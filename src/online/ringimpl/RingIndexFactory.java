package online.ringimpl;

import online.ringimpl.contract.RingIndex;

public class RingIndexFactory {

    public static RingIndex create (int startWith, int numEls) {
        if(numEls > 128) {
            return  new RingIndexLong(startWith, numEls);
        }

        return new RingIndexShort(startWith, numEls);
    }
}
