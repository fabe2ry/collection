package com.fabe2ry.Exception;

/**
 * Created by xiaoxq on 2018/9/28.
 */
public class CellTransformException extends RuntimeException {

    public CellTransformException() {
    }

    public CellTransformException(String message) {
        super("类型转换错误：" + message);
    }
}
