package src.RBTree;

import com.jhlabs.math.RidgedFBM;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Stack;

/**
 * @author ：xbb
 * @date ：Created in 2020/5/11 10:22 上午
 * @description：红黑树实现 一棵红黑树是满足下面红黑性质的二叉搜索树 ：
 * 1. 每个结点或是红色的， 或是黑色的。
 * 2. 根结点是黑色的。
 * 3. 每个叶结点 (NIL)是黑色的。
 * 4. 如果 一 个结点是红色的， 则它的两个子结点都是黑色的。
 * 5. 对每个结点，从该结点到其所有后代叶结点的简单路径上， 均包含相同数目的黑色结点。
 * <p>
 * 如果 一 个结点没有子结点或父 结点， 则该结点相应指针属性的值为 NIL 。
 * 我们可以把这些 NIL 视为指 向二叉搜索树的叶结点 （外部结点）的指针，
 * 而把带关键字的结点视为树的内部结点。
 * @modifiedBy：
 * @version:
 */
public class RBT<T extends Comparable<T>> implements ITree<T> {

    public static void main(String[] args) {
        RBTree<Integer> t = new RBTree<Integer>();

        int[] arry = {1, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

        for (int i = 0; i < arry.length; i++) {
            t.insert(arry[i]);
            // t.print();
        }
        // t.preOrder_iterator(t.root);
        // System.out.println();
        // t.inOrder_iterator(t.root);
        // System.out.println();
        // t.postOrder_iterator(t.root);
        // System.out.println();
        System.out.println("创建BST，arry = {1, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}");
        System.out.println("先序遍历: ");
        t.preOrder();
        System.out.println();
        System.out.println("中序遍历: ");
        t.inOrder();
        System.out.println();
        System.out.println("后序遍历: ");
        t.postOrder();
        System.out.println();
        // t.print();

        System.out.println("依次删除14，9，5三个结点");
        t.remove(14);
        t.remove(9);
        t.remove(5);
        System.out.println("先序遍历: ");
        t.preOrder();
        System.out.println();
        System.out.println("中序遍历: ");
        t.inOrder();
        System.out.println();
        System.out.println("后序遍历: ");
        t.postOrder();
        System.out.println();
        // t.print();
    }

    /**
     * @Description: RBT节点类
     * @Date: 2020/5/11
     * @Author: xbb1973
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    private class RBTNode<T extends Comparable<T>> extends TreeNode<T> {
        String color;        // 颜色
        T key;                // 关键字(键值)
        RBTNode<T> left;    // 左孩子
        RBTNode<T> right;    // 右孩子
        RBTNode<T> parent;    // 父结点
    }


    public static final String RED = "red";
    public static final String BLACK = "black";
    private RBTNode<T> root;

    public RBT() {
        root = null;
    }


    private RBTNode<T> getParent(RBTNode<T> node) {
        return node != null ? node.parent : null;
    }

    private RBTNode<T> getLeftChild(RBTNode<T> node) {
        return (node != null && node.left != null) ? node.left : null;
    }

    private RBTNode<T> getRightChild(RBTNode<T> node) {
        return (node != null && node.right != null) ? node.right : null;
    }

    private String getColor(RBTNode<T> node) {
        return node != null ? node.color : BLACK;
    }

    private boolean isRed(RBTNode<T> node) {
        return ((node != null) && (node.color.equals(RED))) ? true : false;
    }

    private boolean isBlack(RBTNode<T> node) {
        return !isRed(node);
    }

    private void setBlack(RBTNode<T> node) {
        if (node != null) {
            node.color = BLACK;
        }
    }

    private void setRed(RBTNode<T> node) {
        if (node != null) {
            node.color = RED;
        }
    }

    private void setParent(RBTNode<T> node, RBTNode<T> parent) {
        if (node != null) {
            node.parent = parent;
        }
    }

    private void setColor(RBTNode<T> node, String color) {
        if (node != null) {
            node.color = color;
        }
    }

    public Object getRootNode() {
        return root;
    }

    public boolean insert(RBTNode<T> node) {
        // 1、树为空则该节点为根
        if (root == null) {
            root = node;
            return true;
        }
        // 2、树不为空，根据key比较，找到pre结点
        RBTNode<T> temp = this.root;
        RBTNode<T> pre = null;
        while (temp != null) {
            pre = temp;
            if (node.key.compareTo(temp.key) > 0) {
                temp = temp.right;
            } else if (node.key.compareTo(temp.key) < 0) {
                temp = temp.left;
            } else {
                return false;
            }
        }
        // 3、和pre结点比较，插入
        node.setParent(pre);
        if (node.key.compareTo(pre.key) > 0) {
            pre.setRight(node);
        } else {
            pre.setLeft(node);
        }
        insertFixup(node);
        return true;
    }

    /**
     * @Description: 1、参考算法导论的insertFixup代码编写
     * while 循环在每次迭代的开头保持下列 3 个部分的不变式：
     * a . 结点 z 是红结点。
     * b. 如果 z . p 是根结点， 则 z . p 是黑结点。
     * c . 如果有任何红黑性质被破坏 ， 则至多只有 1 条被破坏 ，或是性质 2 , 或是性质 4 。
     * 如果性质 2被破坏 ，其原 因 为 z 是根结点且是红结点。
     * 如果性质 4 被破坏 ， 其原 因 为 z 和 z . p 都是红 结点 。
     * <p>
     * 2、第二种理解，判断结点z以及父结点zParent是左结点还是右结点，分有4种情况处理：
     * 1、z左 zParent左   右旋zGraParent，z黑 zParent红 zGraParent黑
     * 2、z左 zParent右   右旋zParent 左旋zGraParent，z红 zParent黑 zGraParent黑
     * 3、z右 zParent左   左旋zParent 右旋zGraParent，z红 zParent黑 zGraParent黑
     * 4、z右 zParent右   左旋zGraParent，z黑 zParent红 zGraParent黑
     * @Param:
     * @return:
     * @Date: 2020/5/12
     * @Author: xbb1973
     */
    private void insertFixup(RBTNode<T> z) {
        RBTNode<T> y;
        RBTNode<T> zParent;
        RBTNode<T> zGraParent;
        // 父结点存在且为红色结点，违背红黑树性质4，进行修复调整
        while (((zParent = getParent(z)) != null) && zParent.color.equals(RED)) {
            zGraParent = getParent(zParent);
            // 1、父结点处于左子树上
            if (zParent == getLeftChild(zGraParent)) {
                y = getRightChild(zGraParent);
                if (y.color.equals(RED)) {
                    // Case1、叔叔结点y为红色，
                    //      将叔父结点都调整为黑色
                    //      将祖父结点调整为红色
                    setBlack(zParent);
                    setBlack(y);
                    setRed(zGraParent);
                    // 若使用如下直接赋值会导致空指针异常
                    // 若在fixup方法内进行判断则代码过于冗余
                    // 创建内部Set方法，方法中判断空指针
                    // zParent.color = BLACK;
                    // y.color = BLACK;
                    // zGraParent.color = RED;
                    z = zGraParent;
                } else if (z == getRightChild(zParent)) {
                    // Case2、叔叔结点y为黑色，且当前结点为右孩子
                    //      左旋父结点
                    z = zParent;
                    leftRotate(z);
                }
                // Case3、叔叔结点y为黑色，且当前结点为左孩子
                //      将父结点都调整为黑色，祖父结点调整为红色
                //      右旋祖父结点
                setBlack(zParent);
                setRed(zGraParent);
                rightRotate(zGraParent);

                /**
                 * 2、父结点处于右子树上，左右与1、相反
                 */
            } else if (zParent == getRightChild(zGraParent)) {
                // 2、父结点处于右子树上
                // y = zGraParent.left;
                if ((y = getLeftChild(zGraParent)) != null && y.color.equals(RED)) {
                    setBlack(zParent);
                    setBlack(y);
                    setRed(zGraParent);
                    z = zGraParent;
                }
                if (z == getLeftChild(zParent)) {
                    z = zParent;
                    rightRotate(z);
                }
                setBlack(zParent);
                setRed(zGraParent);
                leftRotate(zGraParent);
            } else { // do nothing
            }
        }
        root.color = BLACK;
    }

    public boolean insert(T key) {
        RBTNode<T> rbtNode = new RBTNode<T>();

        rbtNode.setKey(key);
        rbtNode.setLeft(null);
        rbtNode.setRight(null);
        rbtNode.setParent(null);
        rbtNode.setColor(RED);
        insert(rbtNode);
        return true;
    }

    public boolean delete(T key) {
        RBTNode<T> node = (RBTNode<T>) search(key);
        // 1、删除目标没有左孩子
        // 2、删除目标有且只有一个左孩子
        // 3、删除目标有左右孩子
        // 4、deleteFixup
        delete(node);
        return true;
    }

    // public boolean delete(Object node) {
    //     RBTNode<T> node1 = (RBTNode<T>) node;
    //     delete(node1);
    //     // 1、删除目标没有左孩子
    //
    //     // 2、删除目标有且只有一个左孩子
    //
    //     // 3、删除目标有左右孩子
    //
    //     // 4、
    //     return true;
    // }

    /**
     * @Description: 过程 RB-DELETE 与 TREE-DELETE类 似，只 是多 了几行伪代码。
     * 多 出 的儿行代码记录结 点 y 的踪迹，y 有可能导致红黑性质的破坏。
     * 当想要删除结点 z, 且此时 z 的子结点少于2 个时， z 从树中删除， 并让 y 成为 z。
     * 当 z 有两个子结点时，y 应该 是 z 的后继，并且 y 将移至树中的 z 位置。
     * 在结点被移除或者在树中移动之前，必须记住 y 的颜色，并且记录结点 x 的踪迹，
     * 将 x 移 至树中 y 的原 来位置，因 为结点 x 也可能引起红黑性质的破坏。
     * 删 除结点 z 之 后， RB-DELETE 调用一个辅助过程 RB-DELETE-FIXUP ,
     * 该 过程通过改变颜色和执行旋转来恢复红黑性质。
     * @Param:
     * @return:
     * @Date: 2020/5/11
     * @Author: xbb1973
     */
    public boolean delete(RBTNode<T> z) {
        /**
         * Case1、2 删除y：x用于表示替代结点z的孩子,此时y也是z，最终用x替代y/z
         * Case3 移动y且修改颜色：y是z后继结点用于替代z，此时x为y右孩子(y能作为后继就是因为没有左孩子)
         * 调整红黑树策略：y原来为黑色时，调整x，因为删除/移动y都可能会导致红黑树失衡
         */
        RBTNode<T> y = z;
        RBTNode<T> x;
        String yOriColor = getColor(y);
        // 基本操作与Tree-delete类似，但需要跟踪结点x和y的颜色，防止红黑树性质被破坏
        if (getLeftChild(z) == null) {
            // 1、删除目标没有左孩子，直接将删除结点的父结点指向孩子结点
            x = getRightChild(z);
            transplant(z, x);
        } else if (getRightChild(z) == null) {
            // 2、删除目标没有右孩子，直接将删除结点的父结点指向孩子结点
            x = getLeftChild(z);
            transplant(z, x);
        } else {
            // 3、删除目标z有左右孩子，此时应该查找z的后继节点y，并用y来替换z。
            //      3.1、后继结点y不是z的右孩子
            //          需要额外操作：修改y右结点和调整后的右子树指向
            y = (RBTNode<T>) minimum(getRightChild(z));
            yOriColor = getColor(y);
            x = getRightChild(y);
            if (getParent(y) != z) {
                // 将后继结点y的右结点提上
                transplant(y, y.getRight());
                // 修改后继结点右子树指向
                y.setRight(getRightChild(z));
                y.getRight().setParent(y);
            }
            //      3.2、后继结点y是z的右孩子
            //          则需要y提上，且修改y左子树的指向
            // 将后继结点提上
            transplant(z, y);
            // 修改后继结点左子树指向
            y.setLeft(getLeftChild(z));
            y.getLeft().setParent(y);
            // 改变y的颜色为被替换结点z的颜色
            y.setColor(getColor(z));
        }
        // 跟踪结点x和y颜色
        // 在结点被移除或者在树中移动之前，必须记住y的颜色，并且记录结点x的踪迹，
        // 将x移至树中y的原来位置，因为结点x也可能引起红黑性质的破坏。
        if (yOriColor.equals(BLACK)) {
            deleteFixup(x);
        }
        return true;
    }

    /**
     * @Description: 删除修正，参考插入修正
     * @Param:
     * @return:
     * @Date: 2020/5/13
     * @Author: xbb1973
     */
    private void deleteFixup(RBTNode<T> x) {
        RBTNode<T> xParent;
        RBTNode<T> xBro;
        while (x != root && x.color == BLACK) {
            xParent = getParent(x);
            // 1、x为左结点
            if (x == getParent(x).getLeft()) {
                xBro = xParent.getRight();
                // Case1 x的兄弟w是红色的
                if (getColor(xBro) == RED) {
                    xBro.setColor(BLACK);
                    xParent.setColor(RED);
                    leftRotate(xParent);
                    xBro = xParent.getRight();
                }
                // Case2 左右孩子都为黑 x的兄弟w是黑色，且w的俩个孩子也都是黑色的
                if (getColor(xBro.getLeft()).equals(BLACK) && getColor(xBro.getRight()).equals(BLACK)) {
                    setBlack(xBro);
                    x = xParent;
                } else {
                    // Case3 x的兄弟w是黑色的，并且w的左孩子是红色，右孩子为黑色。
                    if (getColor(xBro.getRight()).equals(BLACK)) {
                        setBlack(xBro.getLeft());
                        setRed(xBro);
                        rightRotate(xBro);
                        xBro = getRightChild(xParent);
                    }
                    // Case4 x的兄弟w是黑色的；并且w的右孩子是红色的，左孩子任意颜色。
                    setColor(xBro, getColor(xParent));
                    setBlack(xParent);
                    setBlack(xBro.getRight());
                    leftRotate(xParent);
                    x = root;
                }
                /**
                 * 2、x为右结点，左右互换
                 */
            } else {
                // 2、x为右结点
                xBro = xParent.getLeft();
                // Case1 x的兄弟w是红色的
                if (getColor(xBro) == RED) {
                    xBro.setColor(BLACK);
                    xParent.setColor(RED);
                    rightRotate(xParent);
                    xBro = xParent.getLeft();
                }
                // Case2 左右孩子都为黑 x的兄弟w是黑色，且w的俩个孩子也都是黑色的
                if (getColor(xBro.getLeft()).equals(BLACK) && getColor(xBro.getRight()).equals(BLACK)) {
                    setBlack(xBro);
                    x = xParent;
                } else {
                    // Case3 x的兄弟w是黑色的，并且w的左孩子是红色，右孩子为黑色。
                    if (getColor(xBro.getLeft()).equals(BLACK)) {
                        setBlack(xBro.getRight());
                        setRed(xBro);
                        leftRotate(xBro);
                        xBro = getRightChild(xParent);
                    }
                    // Case4 x的兄弟w是黑色的；并且w的右孩子是红色的，左孩子任意颜色。
                    setColor(xBro, getColor(xParent));
                    setBlack(xParent);
                    setBlack(xBro.getLeft());
                    RidgedFBM(xParent);
                    x = root;
                }
            }
        }
    }


    public Object search(T key) {
        RBTNode<T> temp = this.root;
        while (temp != null) {
            if (key.compareTo(temp.key) > 0) {
                temp = temp.getRight();
            } else if (key.compareTo(temp.key) < 0) {
                temp = temp.getLeft();
            } else {
                return temp;
            }
        }
        return null;
    }

    public Object predecessor() {
        predecessor(root);
        return true;
    }

    public Object successor() {
        successor(root);
        return true;
    }

    public Object predecessor(Object node) {
        RBTNode<T> temp = (RBTNode<T>) node;
        // 1、
        if (temp.left != null) {
            return maximum(temp.left);
        }
        // 2、
        RBTNode<T> pre = temp.parent;
        RBTNode<T> preChild = temp;
        while (pre != null) {
            if (pre.right == preChild) {
                break;
            }
            preChild = pre;
            pre = pre.parent;
        }
        return pre;
    }

    public Object successor(Object node) {
        RBTNode<T> temp = (RBTNode<T>) node;
        // 1、如果右子树存在，后继节点为右子树的最左结点(中序遍历)
        if (temp.right != null) {
            return minimum(temp.right);
        }
        // 2、如果右子树不存在，则后继结点为其祖先节点 or 该结点为最大结点无后继
        //      简单理解：右子树不存在，则该节点为局部分支的最大结点，
        //      则该结点的后继结点需要不断往上找，找到第一个父辈结点左子树中包含该结点即为所求
        RBTNode<T> pre = temp.parent;
        RBTNode<T> preChild = temp;
        while (pre != null) {
            if (pre.left == preChild) {
                break;
            }
            preChild = pre;
            pre = pre.parent;
        }
        return pre;
    }

    // public Object predecessor(TreeNode<T> node) {
    //
    // }
    //
    // public Object successor(TreeNode<T> node) {
    //
    // }

    public Object maximum() {
        maximum(root);
        return true;
    }

    public Object minimum() {
        minimum(root);
        return true;
    }

    public Object maximum(Object node) {
        RBTNode<T> temp = (RBTNode<T>) node;
        while (temp.right != null) {
            temp = temp.right;
        }
        return temp;
    }

    public Object minimum(Object node) {
        RBTNode<T> temp = (RBTNode<T>) node;
        while (temp.left != null) {
            temp = temp.left;
        }
        return temp;
    }

    public boolean containsKey(T key) {
        if (search(key) != null) {
            return true;
        }
        return false;
    }

    // public Object maximum(RBTNode<T> node) {
    //     RBTNode<T> temp = (RBTNode<T>) node;
    //     while (temp.right != null){
    //         temp = temp.right;
    //     }
    //     return temp;
    // }
    //
    // public Object minimum(RBTNode<T> node) {
    //     RBTNode<T> temp = (RBTNode<T>) node;
    //     while (temp.left != null){
    //         temp = temp.left;
    //     }
    //     return temp;
    // }

    /**
     * @Description: 过程 RB - TRANSPLANT 与 TRANSPLANT有 两点不同。
     * 首先，第 1 行引用哨兵 T.nil而不 是 NI L 。
     * 其次，第6 行对 v.p 的赋值是无条件执行： 即使 v 指向哨兵， 也要对 v.p 赋值。
     * 实际上，当 v = T.nil 时，也能给 v.p 赋值。
     * @Param:
     * @return:
     * @Date: 2020/5/11
     * @Author: xbb1973
     */
    private void transplant(RBTNode<T> u, RBTNode<T> v) {
        if (u.parent == null) {
            // 1、u父结点为空，u为root结点，用v代替root结点
            root = v;
        } else if (u == u.parent.left) {
            // 2、u为左子树结点
            u.parent.left = v;
        } else {
            // 3、u为右子树结点
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    /**
     * @Description: 对结点x进行左旋，x将变为左子树的结点
     * 对红黑树的节点(x)进行左旋转
     * 左旋示意图(对节点x进行左旋)：
     * px                             px
     * /                              /
     * x                              y
     * /  \      --(左旋)->           / \
     * lx   y                       x  ry
     * /   \                      /  \
     * ly   ry                   lx  ly
     * @Param:
     * @return:
     * @Date: 2020/5/11
     * @Author: xbb1973
     */
    private void leftRotate(RBTNode<T> x) {
        RBTNode<T> y = x.right;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent;

        // 1、修改y的父结点指向
        if (x.parent == null) {
            // 1.1、若x是根结点，则y变为根结点
            root = y;
        } else if (x.parent.left == x) {
            // 1.2、若x是左子树结点，则y变为左xxx
            x.parent.left = y;
        } else {
            // 1.3、若x是右子树结点，则y变为右xxx
            x.parent.right = y;
        }
        // 2、修改x结点和y结点之间的关系
        //      y左子树给x当右子树，y左子树改为x，修改各自parent
        y.left = x;
        x.parent = y;
    }

    /**
     * @Description: 对结点x进行右旋旋，x将变为左子树的结点
     * 对红黑树的节点(x)进行右旋转
     * 右旋示意图(对节点x进行右旋)：
     * px                             px
     * /                              /
     * y                              x
     * /  \      <-(右旋)--           / \
     * lx   x                         y  rx
     * /   \                     /  \
     * ly   ry                   ly  ry
     * @Param:
     * @return:
     * @Date: 2020/5/11
     * @Author: xbb1973
     */
    private void rightRotate(RBTNode<T> x) {
        RBTNode<T> y = x.left;
        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }
        y.parent = x.parent;

        // 1、修改y的父结点指向
        if (x.parent == null) {
            // 1.1、若x是根结点，则y变为根结点
            root = y;
        } else if (x.parent.left == x) {
            // 1.2、若x是左子树结点，则y变为左xxx
            x.parent.left = y;
        } else {
            // 1.3、若x是右子树结点，则y变为右xxx
            x.parent.right = y;
        }
        // 2、修改x结点和y结点之间的关系
        //      y右子树给x当左子树，y右子树改为x，修改各自parent
        y.right = x;
        x.parent = y;
    }

    //前序遍历(根左右)
    public void preOrder_iterator(RBTNode<T> node) {
        System.out.print(node.key + " " + node.color + " ");
        if (node.left != null) {
            preOrder_iterator(node.left);
        }
        if (node.right != null) {
            preOrder_iterator(node.right);
        }
    }

    //中序遍历(左根右)
    public void inOrder_iterator(RBTNode<T> node) {
        if (node.left != null) {
            inOrder_iterator(node.left);
        }
        System.out.print(node.key + " " + node.color + " ");
        if (node.right != null) {
            inOrder_iterator(node.right);
        }
    }

    //后序遍历(左右根)
    public void postOrder_iterator(RBTNode<T> node) {
        if (node.left != null) {
            postOrder_iterator(node.left);
        }
        if (node.right != null) {
            postOrder_iterator(node.right);
        }
        System.out.print(node.key + " " + node.color + " ");
    }

    /*
     * 打印"红黑树"
     *
     * key        -- 节点的键值
     * direction  --  0，表示该节点是根节点;
     *               -1，表示该节点是它的父结点的左孩子;
     *                1，表示该节点是它的父结点的右孩子。
     */
    private void print(RBTNode<T> tree, T key, int direction) {
        if (tree != null) {

            if (direction == 0)    // tree是根节点
                System.out.printf("%2d(B) is root\n", tree.key);
            else                // tree是分支节点
                System.out.printf("%2d(%s) is %2d's %6s child\n", tree.key, tree.color.equals(RED) ? "R" : "B", key, direction == 1 ? "right" : "left");

            print(tree.left, tree.key, -1);
            print(tree.right, tree.key, 1);
        }
    }

    public void print() {
        if (root != null) {
            print(root, root.key, 0);
        }
    }

    /**
     * @param root
     * @return
     * @Description: 判断是否是RBT树
     * @Date: 2020/5/12
     * @Author: xbb1973
     */
    public boolean isValidBST(RBTNode<T> root) {
        /*
        // 解法一、递归，常规思路有问题，需要每次遍历得到左右子树的最大最小值
        // 无法处理如下情况
        // [10,5,15,null,null,6,20]
        //      10
        //     /  \
        //    5   15
        //       /  \
        //      6   20
        if (root == null || root.left == null && root.right == null) {
            return true;
        }
        if (isValidBST(root.left)) {
            if (root.left != null) {
                if (root.val <= getMaxOfBST(root.left)) {
                    return false;
                }
            }
        } else {
            return false;
        }
        if (isValidBST(root.right)) {
            if (root.right != null) {
                if (root.val >= getMinOfBST(root.right)) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;

        // 解法二、递推，自顶向下，通过[min, max]进行限制
        // 可以观察到，左孩子的范围是 （父结点左边界，父节点的值），右孩子的范围是（父节点的值，父节点的右边界）。
        return getAns(root, null, null);

        // 解法三、中序遍历，形成一个递增序列，如果后遍历的数小于先遍历的数则返回false
        Integer pre = null; // 一开始使用Integer.MIN，不符合测试用例边界需求，使用null代替无穷小
        if (root == null) return true;
        Stack<TreeNode> stack = new Stack<>();
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            if (!stack.isEmpty()) {
                TreeNode pop = stack.pop();
                if (pre == null) {
                    pre = pop.val;
                } else if (pre >= pop.val) {
                    return false;
                } else {
                    pre = pop.val;
                }
                root = pop.right;
            }
        }
        return true;
        */

        // 解法三、中序遍历-改进
        if (root == null) {
            return true;
        }
        Stack<RBTNode<T>> stack = new Stack<>();
        RBTNode<T> pre = null;
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            if (pre != null && root.key.compareTo(pre.key) <= 0) {
                return false;
            }
            pre = root;
            root = root.right;
        }
        return true;
    }
}
