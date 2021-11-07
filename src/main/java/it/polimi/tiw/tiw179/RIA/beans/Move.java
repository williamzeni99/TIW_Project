package it.polimi.tiw.tiw179.RIA.beans;

public class Move {
    private int start_id;
    private int dest_id;

    public Move(int start_id, int dest_id){
        this.start_id=start_id;
        this.dest_id=dest_id;
    }

    public int getDestId() {
        return dest_id;
    }

    public int getStartId() {
        return start_id;
    }
}
