package logic;

import java.util.ArrayList;

/**
 * Created by joseroman on 18/11/17.
 */

public class Bus {
    private String code;

    public Bus(){

    }

    public Bus (String code){
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }

    public void setCode(String code){
    }

    public static String[] buses_codes(ArrayList<Bus> buses){

        String[] list = new String[buses.size()];
        for (int i = 0; i < buses.size(); i++) {
            list[i] = buses.get(i).getCode();
        }
        return list;
    }
}
