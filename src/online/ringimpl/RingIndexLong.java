package online.ringimpl;
import online.ringimpl.contract.RingIndex;

 class RingIndexLong implements RingIndex {

    private int curr;
    private int startWith;
    private int lastInd;
    private boolean first;

    // package scope
    RingIndexLong (int startWith, int numEls) {
        this.lastInd = numEls-1;
        this.startWith = startWith;
        curr = startWith;
        first = true;
    }


    public  int getNext () {
    if ( curr == -1 || (curr == startWith && !first) ) {
        curr = -1;

        return -1;
    }

    int res = curr;

    if ( curr == lastInd && startWith > 0 ) {
        curr = 0;
    } else if ( curr < startWith || curr < lastInd ) {
        ++curr;
    } else {
        curr = -1;
    }

    first = false;

    return res;
}


    // private  void test(int starsWith, int numEls)  {
    //     RingIndex ri = new RingIndex(starsWith,numEls);
    //     int i =0;

    //     while (i >=0 ) {
    //         i = ri.getNext();
    //         System.out.print("  "+ i);
    //     }

    //     System.out.print("\n");
    // }

    // public static void main(String[] args) {
    //     RingIndex ri = new RingIndex(0, 0);
    //     ri.test (34,111);
    //     ri.test (0,11);
    //     ri.test (7,11);
    //     ri.test (10,11);
    // }
}