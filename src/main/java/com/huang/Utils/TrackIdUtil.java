package com.huang.Utils;

import java.util.UUID;

public class TrackIdUtil {

    public static String generateTrackId(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0,26);
    }
    public static String generateTrackId(String prefix){
        UUID uuid = UUID.randomUUID();
        String random = uuid.toString();
        String tmp = prefix + random;
        return tmp.substring(0,26);
    }
}
