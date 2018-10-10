package com.fabe2ry.Exception;

/**
 * Created by xiaoxq on 2018/9/28.
 */
public class TransportExpection extends RuntimeException {

    public TransportExpection() {
    }

    public TransportExpection(String message) {
        super(message);
    }
}
