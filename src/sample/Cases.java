package sample;

import java.util.ArrayList;
import java.util.List;

public class Cases {
    List<Country> list = new ArrayList<>();

    public Cases() {
    }

    public Cases(List<Country> list) {
        this.list = list;
    }

    public List<Country> getList() {
        return list;
    }

    public void setList(List<Country> list) {
        this.list = list;
    }


}
