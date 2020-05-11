package src.RBTree;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author ：xbb
 * @date ：Created in 2020/5/11 9:29 上午
 * @description：树范型接口
 * @modifiedBy：
 * @version:
 */
public interface ITree<T extends Comparable<T>> {
    //搜索树 数 据 结 构支 待 许 多 动 态 集 合 操 作， 包括
    //SEARCH、 MINIMUM、 MAXIMUM、 PREDECESSOR、 SUCCESSOR、 INSERT 和 DELETE
    //第 12 章介绍了一棵高度为 h 的二叉搜索树， 它可以支持任何 一 种基本动态集合操作，
    //SEARCH、 PREDECESSOR、 SUCCESSOR、 MINIMUM、 MAXIMUM、 INSERT 和 DELETE 等

    void insert();

    void delete();

    void search();

    void predecessor();

    void successor();

    void maximum();

    void minimum();

    // int size();
    //
    // boolean isEmpty();

    // boolean contains(Object o);
    //
    // Object[] toArray();
    //
    // <T> T[] toArray(T[] a);

    // boolean insert(E e);
    //
    // boolean remove(Object o);
    //
    // boolean clear();
    //
    // ITree<E> search(ITree<E> tree, E e);
    //
    // void print();
}
