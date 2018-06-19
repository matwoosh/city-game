package com.matwoosh.heboy;

import com.estimote.sdk.Region;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Minis on 25.10.2015.
 */
public class MyBeacons {
    private UUID UuidBeacons = UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D");


    private Region beacon1 = new Region("", UuidBeacons, 43623, 31400);
    private Region beacon2 = new Region("", UuidBeacons, 25380, 48272);
    private Region beacon3 = new Region("", UuidBeacons, 53633, 54547);
    private Region beacon4 = new Region("", UuidBeacons, 55175, 50165);

    private List<Region> keyList = new ArrayList<>();
    {
        keyList.add(0,beacon1); //fiolet 1
        keyList.add(1,beacon2); //blue
        keyList.add(2,beacon3); //green
        keyList.add(3,beacon4); //fiolet 2
    }

    public List getKeyList(){
        return keyList;
    }
}
