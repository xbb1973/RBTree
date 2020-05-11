package src.RBTree;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ：xbb
 * @date ：Created in 2020/5/11 10:22 上午
 * @description：红黑树实现
 * @modifiedBy：
 * @version:
 */
public class RBT<T extends Comparable<T>> implements ITree<T> {

    /**
     * @Description: RBT节点类
     * @Date: 2020/5/11
     * @Author: xbb1973
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    private class RBTNode<T extends Comparable<T>> extends TreeNode<T> {
        Boolean color;        // 颜色
        T key;                // 关键字(键值)
        RBTNode<T> left;    // 左孩子
        RBTNode<T> right;    // 右孩子
        RBTNode<T> parent;    // 父结点
    }


    public static final Boolean RED   = false;
    public static final Boolean BLACK = true;

    private RBTNode<T> root;

    public RBT(){
        root = null;
    }

    public boolean insert(T key) {
        return false;
    }

    public boolean delete(T key) {
        return false;
    }

    public Object search(T key) {
        return null;
    }

    public Object predecessor() {
        return null;
    }

    public Object successor() {
        return null;
    }

    public Object predecessor(Object node) {
        return null;
    }

    public Object successor(Object node) {
        return null;
    }

    public Object maximum() {
        return null;
    }

    public Object minimum() {
        return null;
    }

    public Object maximum(Object node) {
        return null;
    }

    public Object minimum(Object node) {
        return null;
    }

    public boolean containsKey(T key) {
        if (search(key) != null) {
            return true;
        }
        return false;
    }

    public Object getRootNode() {
        return root;
    }

}
