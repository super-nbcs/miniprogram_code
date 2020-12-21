package com.zfw.core.entity;

import java.io.Serializable;

/**
 * 树形节点接口,实体类如果是树形结构，请实现此接口
 *
 * @param <ID>
 */
public interface TreeEntity<ID extends Serializable> {

    /**
     * 是否有孩子节点
     *
     * @return
     */
    boolean isHasChildren();

    /**
     * 是否为根节点
     *
     * @return
     */
    boolean isRoot();
}
