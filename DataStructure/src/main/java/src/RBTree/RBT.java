package src.RBTree;

/**
 * @author ：xbb
 * @date ：Created in 2020/5/11 10:22 上午
 * @description：红黑树实现
 * @modifiedBy：
 * @version:
 */
public class RBT<T extends Comparable<T>> {

    private static final Boolean RED   = false;
    private static final Boolean BLACK = true;

    /**
     * @Description: RBT节点类
     * @Date: 2020/5/11
     * @Author: xbb1973
     */
    private class RBTNode<T extends Comparable<T>>{
        Boolean color;        // 颜色
        T key;                // 关键字(键值)
        RBTNode<T> left;    // 左孩子
        RBTNode<T> right;    // 右孩子
        RBTNode<T> parent;    // 父结点

        public RBTNode(T key, boolean color, RBTNode<T> parent, RBTNode<T> left, RBTNode<T> right) {
            this.key = key;
            this.color = color;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        public T getKey() {
            return key;
        }

        @Override
        public String toString() {
            return ""+key+(this.color==RED?"(R)":"B");
        }
    }
}
