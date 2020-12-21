package com.zfw.core.sys.entity;

import java.util.List;

/**
 * @Author:zfw
 * @Date:2020/4/16
 * @Content:
 */
public class Tree<T,D> {
    private T t;
    private List<D> children;

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public List<D> getChildren() {
        return children;
    }

    public void setChildren(List<D> children) {
        this.children = children;
    }
}
