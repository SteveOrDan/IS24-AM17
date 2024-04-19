package com.example.pf_soft_ing.Messages;

public class TableMessage extends Message{
    int firstResourceDeckCardID;
    int visibleResourceCard1ID;
    int visibleResourceCard2ID;

    int firstGoldenDeckCardID;
    int visibleGoldenCard1ID;
    int visibleGoldenCard2ID;

    public TableMessage(int firstResourceDeckCardID, int visibleResourceCard1ID, int visibleResourceCard2ID,
                        int firstGoldenDeckCardID, int visibleGoldenCard1ID, int visibleGoldenCard2ID){
        this.firstResourceDeckCardID = firstResourceDeckCardID;
        this.visibleResourceCard1ID = visibleResourceCard1ID;
        this.visibleResourceCard2ID = visibleResourceCard2ID;
        this.firstGoldenDeckCardID = firstGoldenDeckCardID;
        this.visibleGoldenCard1ID = visibleGoldenCard1ID;
        this.visibleGoldenCard2ID = visibleGoldenCard2ID;
    }

    public int getFirstResourceDeckCardID() {
        return firstResourceDeckCardID;
    }

    public int getVisibleResourceCard1ID() {
        return visibleResourceCard1ID;
    }

    public int getVisibleResourceCard2ID() {
        return visibleResourceCard2ID;
    }

    public int getFirstGoldenDeckCardID() {
        return firstGoldenDeckCardID;
    }

    public int getVisibleGoldenCard1ID() {
        return visibleGoldenCard1ID;
    }

    public int getVisibleGoldenCard2ID() {
        return visibleGoldenCard2ID;
    }
}
