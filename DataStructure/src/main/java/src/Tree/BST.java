package src.Tree;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Stack;

/**
 * @author ：xbb
 * @date ：Created in 2020/5/11 11:37 上午
 * @description：二叉搜索树
 * @modifiedBy：
 * @version:
 */
public class BST<T extends Comparable> implements ITree<T> {

    public static void main(String[] args) {
        BST<Integer> t = new BST<Integer>();
        int[] arry = {5, 2, 7, 3, 6, 4, 8, 1, 9, 0};
        for (int i = 0; i < arry.length; i++) {
            t.insert(arry[i]);
        }
        System.out.println("创建BST，arry = {5, 2, 7, 3, 6, 4, 8, 1, 9, 0};");
        System.out.println("先序遍历: ");
        t.preOrder_iterator(t.root);
        System.out.println();
        System.out.println("中序遍历: ");
        t.inOrder_iterator(t.root);
        System.out.println();
        System.out.println("后序遍历: ");
        t.postOrder_iterator(t.root);
        System.out.println();

        System.out.println("依次删除结点5, 3, 7");
        t.delete(5);
        t.delete(3);
        t.delete(7);
        System.out.println("先序遍历: ");
        t.preOrder_iterator(t.root);
        System.out.println();
        System.out.println("中序遍历: ");
        t.inOrder_iterator(t.root);
        System.out.println();
        System.out.println("后序遍历: ");
        t.postOrder_iterator(t.root);
        System.out.println();

    }

    /**
     * @Description: BST节点类
     * @Date: 2020/5/11
     * @Author: xbb1973
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    public class BSTNode<T> extends TreeNode<T> {
        T key;  // 关键字(键值)
        BSTNode<T> left;    // 左孩子
        BSTNode<T> right;   // 右孩子
        BSTNode<T> parent;  // 父结点
    }

    private BSTNode<T> root;

    public BST() {
        root = null;
    }

    public Object getRootNode() {
        return root;
    }

    public boolean insert(BSTNode<T> node) {
        // 1、树为空则该节点为根
        if (root == null) {
            root = node;
            return true;
        }
        // 2、树不为空，根据key比较，找到pre结点
        BSTNode<T> temp = this.root;
        BSTNode<T> pre = null;
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
        return true;
    }

    public boolean insert(T key) {
        BSTNode<T> bstNode = new BSTNode<T>();
        bstNode.setKey(key);
        bstNode.setLeft(null);
        bstNode.setRight(null);
        bstNode.setParent(null);
        insert(bstNode);
        return true;
    }

    public boolean delete(T key) {
        BSTNode<T> node = (BSTNode<T>) search(key);
        delete(node);
        // 1、删除目标没有左孩子

        // 2、删除目标有且只有一个左孩子

        // 3、删除目标有左右孩子

        // 4、
        return true;
    }

    // public boolean delete(Object node) {
    //     BSTNode<T> node1 = (BSTNode<T>) node;
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
     * @Description:
     * @Param:
     * @return:
     * @Date: 2020/5/11
     * @Author: xbb1973
     */
    public boolean delete(BSTNode<T> node) {
        if (node.left == null) {
            // 1、删除目标没有左孩子，直接将删除结点的父结点指向孩子结点
            transpalent(node, node.right);
        } else if (node.right == null) {
            // 2、删除目标没有右孩子，直接将删除结点的父结点指向孩子结点
            transpalent(node, node.left);
        } else {
            // 3、删除目标z有左右孩子，此时应该查找z的后继节点y，并用y来替换z。
            //      3.1、后继结点y是z的右孩子
            //          则需要y提上，且修改y左子树的指向

            //      3.2、后继结点y不是z的右孩子
            //          需要额外操作：修改y右结点和调整后的右子树指向
            BSTNode<T> temp = (BSTNode<T>) minimum(node.right);
            if (temp.parent != node) {
                // 将后继结点y的右结点提上
                transpalent(temp, temp.right);
                // 修改后继结点右子树指向
                temp.right = node.right;
                temp.right.parent = temp;
            }

            // 将后继结点提上
            transpalent(node, temp);
            // 修改后继结点左子树指向
            temp.left = node.left;
            temp.left.parent = temp;
        }
        return true;
    }

    public Object search(T key) {
        BSTNode<T> temp = this.root;
        while (temp != null) {
            if (key.compareTo(temp.key) > 0) {
                temp = temp.right;
            } else if (key.compareTo(temp.key) < 0) {
                temp = temp.left;
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
        BSTNode<T> temp = (BSTNode<T>) node;
        // 1、
        if (temp.left != null) {
            return maximum(temp.left);
        }
        // 2、
        BSTNode<T> pre = temp.parent;
        BSTNode<T> preChild = temp;
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
        BSTNode<T> temp = (BSTNode<T>) node;
        // 1、如果右子树存在，后继节点为右子树的最左结点(中序遍历)
        if (temp.right != null) {
            return minimum(temp.right);
        }
        // 2、如果右子树不存在，则后继结点为其祖先节点 or 该结点为最大结点无后继
        //      简单理解：右子树不存在，则该节点为局部分支的最大结点，
        //      则该结点的后继结点需要不断往上找，找到第一个父辈结点左子树中包含该结点即为所求
        BSTNode<T> pre = temp.parent;
        BSTNode<T> preChild = temp;
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
        BSTNode<T> temp = (BSTNode<T>) node;
        while (temp.right != null) {
            temp = temp.right;
        }
        return temp;
    }

    public Object minimum(Object node) {
        BSTNode<T> temp = (BSTNode<T>) node;
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

    // public Object maximum(BSTNode<T> node) {
    //     BSTNode<T> temp = (BSTNode<T>) node;
    //     while (temp.right != null){
    //         temp = temp.right;
    //     }
    //     return temp;
    // }
    //
    // public Object minimum(BSTNode<T> node) {
    //     BSTNode<T> temp = (BSTNode<T>) node;
    //     while (temp.left != null){
    //         temp = temp.left;
    //     }
    //     return temp;
    // }

    /**
     * @Description: TRANSPLANT 用一棵以 v 为根的子树来替换一棵 以 u 为根的子树时
     * 结点 u 的双亲就变为结点 v 的双亲 ， 并且最后 v 成为 u 的双亲的相应孩子 。
     * 删除结点u，并且使用结点v代替结点u。
     * @Param:
     * @return:
     * @Date: 2020/5/11
     * @Author: xbb1973
     */
    private void transpalent(BSTNode<T> u, BSTNode<T> v) {
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

    //前序遍历(根左右的)
    public void preOrder_iterator(BSTNode<T> node ){
        System.out.print(node.key+" ");
        if(node.left!=null){
            preOrder_iterator(node.left);
        }
        if(node.right!=null){
            preOrder_iterator(node.right);
        }
    }
    
    //中序遍历(左根右)
    public void inOrder_iterator(BSTNode<T> node ){
        if(node.left!=null){
            inOrder_iterator(node.left);
        }
        System.out.print(node.key+" ");
        if(node.right!=null){
            inOrder_iterator(node.right);
        }
    }
    //后序遍历(左右根)
    public void postOrder_iterator(BSTNode<T> node ){
        if(node.left!=null){
            postOrder_iterator(node.left);
        }
        if(node.right!=null){
            postOrder_iterator(node.right);
        }
        System.out.print(node.key+" ");
    }

    /**
     * @Description:  判断是否是BST树
     * @Date: 2020/5/12
     * @Author: xbb1973
     * @param root
     * @return
     */
    public boolean isValidBST(BSTNode<T> root) {
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
        Stack<BSTNode<T>> stack = new Stack<BSTNode<T>>();
        BSTNode<T> pre = null;
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
