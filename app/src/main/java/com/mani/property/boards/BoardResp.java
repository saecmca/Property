package com.mani.property.boards;

import com.mani.property.common.StatusOf;

import java.util.ArrayList;

/**
 * Created by Mani on 14-08-2017.
 */

public class BoardResp {
    public StatusOf getStatus() {
        return status;
    }

    public void setStatus(StatusOf status) {
        this.status = status;
    }

    StatusOf status;

    public ArrayList<BoardModel> getBoards() {
        return boards;
    }

    public void setBoards(ArrayList<BoardModel> boards) {
        this.boards = boards;
    }

    ArrayList<BoardModel>boards;
}
