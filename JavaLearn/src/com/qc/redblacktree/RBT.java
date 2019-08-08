package com.qc.redblacktree;

/**
 * 红黑树
 * 
 * 使用泛型定义，红黑树在Java中是TreeMap和TreeSet的底层，对于Map这种数据结构，需要使用Key-Value的结构
 * 为了保证顺序，令Key实现Comparable接口
 * @author Qc
 * @see https://blog.csdn.net/iwts_24/article/details/86754155
 */
public class RBT<Key extends Comparable<Key>, Value> {
	private static final boolean BLACK = false;
	private static final boolean RED = true;
	
	private Node root;//根节点
	
	/**
	 * 节点类定义
	 */
	private class Node{
		private Key key;//键，类型来自泛型类定义
		private Value value;//值，类型来自泛型类定义
		private Node left;//左子树
		private Node right;//右子树
		private boolean color;//节点颜色
		
		public Node(Key key, Value value){
			this.key = key;
			this.value = value;
			this.color = RED;
		}
	}
	
	/*-----------------常规操作------------------------*/
	
	public Value get(Key key){
		if(key == null)
			return null;
		return get(this.root, key);
	}
	
	public Value get(Node root, Key key){
		while(root!=null){
			int cmp = key.compareTo(root.key);
			if(cmp == 0)
				return root.value;
			if(cmp < 0){
				root = root.left;
			}else{
				root = root.right;
			}
		}
		return null;
	}
	
	/**
	 * 寻找最小值
	 */
	public Value min(){
		if(root == null)
			return null;
		Node temp = min(root);
		return temp.value;
	}
	
	/**
	 * 寻找最小值的递归方法
	 */
	public Node min(Node root){
		if(root == null)
			return null;
		while(root.left != null){
			root = root.left;
		}
		return root;
	}
	
	/*-----------------左右旋及颜色转换------------------------*/
	
	/**
	 * 判断是否红色节点，空节点都为黑色 
	 */
	private boolean isRed(Node node){
		if(node == null)
			return false;
		return node.color == RED;
	}
	
	/**
	 * 左旋
	 * 对h节点进行左旋，左旋是必须要求有右子树，不一定有左子树。
	 * h的右孩子一定是红色的，因为红黑树的旋转一定是因为颜色。
	 * 而连接h的可能是左链接也可能是右链接，我们只需要返回左旋后成为根的节点x，上上层来处理。
	 * 这个链接也可能为红或者为黑，那么令 x.color = h.color 就完美解决问题了。 
	 * 
	 *     |             |
	 *     h             x
	 *    / \           / \
	 *   a   x   ==>   h   c
	 *      / \       / \   
	 *     b   c     a   b
	 * 
	 */
	public Node rotateLeft(Node h){
		Node x = h.right;
		h.right = x.left;
		x.left = h;
		x.color = h.color;
		h.color = RED;
		return x;
	}
	
	/**
	 * 右旋
	 * 和左旋类似，方向相反
	 * 
	 *     |             |
	 *     h             x
	 *    / \           / \
	 *   x   c   ==>   a   h
	 *  / \               / \
	 * a   b             b   c
	 * 
	 */
	public Node rotateRight(Node h){
		Node x = h.left;
		h.left = x.right;
		x.right = h;
		x.color = h.color;
		h.color = RED;
		return x;
	}
	
	public void put(Key key, Value value){
		root = put(root, key,value);
		root.color = BLACK;//红黑树性质：根节点一定为黑色
	}
	
	/**
	 * 红黑树插入
	 * （认为新增结点一定是红结点）
	 * 分情况讨论：
	 * （如果和已有结点相等，则直接更新value，这里先忽略相等的情况）
	 * 【1】向单个黑节点插入（只存在根结点）
	 *   1.1 新结点小于根结点  --> 插入后左链接为红色，符合红黑树性质，不需处理
	 *   1.2 新结点大于根结点  --> 插入后右链接为黑色，不符合红黑树性质，需要再左旋变成1.1
	 * 【2】向一个黑结点插入（和1一样）
	 *   2.1 新结点小于父结点 --> 插入后左链接为红色，符合红黑树性质，不需处理
	 *   2.2 新结点大于父结点 --> 插入后右链接为黑色，不符合红黑树性质，需要再左旋变成2.1
	 * 【3】向一个红结点插入
	 *   如图所示，设已有红黑树树，父结点为P，左链接为红色结点C, 
	 *   则有三个插入位置，大小关系分别为  α < C < β < P < γ
	 *        P
	 *       / \
	 *      C   γ
	 *     / \
	 *    α   β
	 *   设插入的新键为T，讨论着三个插入位置的情景，r(x)表示红结点：
	 *   
	 *   ·情景 3.1 （新键最大，插入到γ位置）
	 *       P      左右链接变为黑色         r(P)
	 *      / \         ==>       / \ 
	 *    r(C) r(T)   父结点变为红色      C   T
	 *   
	 *   ·情景 3.2 （新键最小，插入到α位置）
	 *       P  出现两个红左链接          C              r(C)
	 *      /    对P右旋                     / \             / \
	 *    r(C)    ==>     r(T) r(P)   ==>   T   P
	 *    /     转化成3.1情景
	 *   r(T)
	 *   
	 *   ·情景 3.3（新建介于两者之间）
	 *        P  出现红色右链接            P                 T              r(T)
	 *       /     对C左旋                /                 / \             / \
	 *     r(C)     ==>     r(T)      ==>    r(C) r(P)  ==>    C  P
	 *       \   转化为3.2情景     /     转化为3.1场景
	 *       r(T)          r(C)
	 *   
	 *   故情景3概括如下:
	 *   3.1 左右链接均为红色时，两链接变为黑色，父结点颜色变红；
	 *   3.2 连续两个左链接均为红色时，父结点右旋，然后变成1的情况；
	 *   3.3 出现红色右链接，父结点左旋，然后变成2的情况。
	 */
	private Node put(Node root,Key key, Value value){
		//如果树不存在，则new出来根。
		if(root==null){
			return new Node(key, value);
		}
		//搜索查找插入位置
		int cmp = key.compareTo(root.key);
		if(cmp == 0){
			root.value = value;//如果key已存在，则更新value;
		}else{
			if(cmp>0){
				//如果比当前节点value大，则继续搜索右子树
				root.right = put(root.right, key, value);
			}else{
				//如果比当前节点value小，则继续搜索左子树
				root.left = put(root.left, key, value);
			}
		}
		//插入元素处理
		if(isRed(root.right) && !isRed(root.left)){
			root = rotateLeft(root);//情景3.3：出现红色右链接，父结点左旋，然后变成2的情况。
		}
		if(isRed(root.left) && isRed(root.left.left)){
			root = rotateRight(root);//情景3.2：连续两个左连接均为红色，父结点右旋，变成1的情况
		}
		if(isRed(root.left) && isRed(root.right)){
			flipColors(root);//情景3.1：左右连接均为红色， 两链接变为黑色，父结点变红
		}
		return root;
	}
	
	/**
	 * 两链接结点颜色变为黑色，父结点颜色变为红色
	 */
	private void flipColors(Node root){
		root.left.color = BLACK;
		root.right.color = BLACK;
		root.color = RED;
	}
	/*-----------------删除最小值------------------------*/
	/**
	 * 删除辅助方法
	 */
	private Node moveRedLeft(Node root){
		flipColors(root);
		if(isRed(root.right.left)){
			root.right = rotateRight(root.right);
			root = rotateLeft(root);
			flipColors(root);
		}
		return root;
	}
	
	/**
	 * 删除最小值
	 */
	public void deleteMin(){
		if(!isRed(root.left) && !isRed(root.right))
			root.color = RED;
		root = deleteMin(root);
		if(root != null)
			root.color = BLACK;
	}
	
	/**
	 * 删除最小值递归方法 
	 */
	private Node deleteMin(Node root){
		if(root.left == null)
			return null;
		if(!isRed(root.left) && !isRed(root.left.left))
			root = moveRedLeft(root);
		root.left = deleteMin(root.left);
		return balance(root);
	}
	
	/**
	 * 删除后可能生成了临时的4-结点，或者其他不满足性质的条件，应该向上回溯。
	 * 这个balance方法其实就是put方法最后几行的代码
	 */
	private Node balance(Node root){
		if(isRed(root.right))
			root = rotateLeft(root);
		if(isRed(root.left) && isRed(root.left.left))
			root = rotateRight(root);
		if(isRed(root.left) && isRed(root.right))
			flipColors(root);
		return root;
	}
	
	/*-----------------删除最大值------------------------*/
	/**
	 * 删除辅助方法
	 */
	private Node moveRedRight(Node root){
		flipColors(root);
		if(isRed(root.left.left)){
			root = rotateRight(root);
			flipColors(root);
		}
		return root;
	}
	
	/**
	 * 删除最大值 
	 */
	public void deleteMax(){
		if(!isRed(root.left) && !isRed(root.right))
			root.color = RED;
		root = deleteMax(root);
		if(root != null)
			root.color = BLACK;
	}
	
	/**
	 * 删除最大值递归方法 
	 */
	public Node deleteMax(Node root){
		if(isRed(root.left))
			root = rotateRight(root);
		if(root.right == null)
			return null;
		if(!isRed(root.right) && !isRed(root.right.left))
			root = moveRedRight(root);
		root.right = deleteMax(root.right);
		return balance(root);
	}
	
	/*-----------------完整的删除------------------------*/
	public void delete(Key key){
		if(!isRed(root.left) && !isRed(root.right))
			root.color = RED;
		root = delete(root,key);
		if(root != null)
			root.color = BLACK;
	}
	
	private Node delete(Node root, Key key){
		int cmp = key.compareTo(root.key);
		if(cmp < 0){
			//左递归，跟删除最小值的一样
			if(!isRed(root.left) && !isRed(root.left.left))
				root = moveRedLeft(root);
			root.left = delete(root.left, key);
		}else{
			//右递归，先判定了是否需要删除最小值的第一次旋转保证不会丢失左子树
			if(isRed(root.left))
				root = rotateRight(root);
			//找到了，该删了，但是需要确保不会丢失右子树
			if(cmp == 0 && (root.right == null))
				return null;
			//这个是删除最大值中的算法
			if(!isRed(root.right) && !isRed(root.right.left))
				root = moveRedRight(root);
			//此时，完美解决所有丢失情况，可以正常删除了，这个过程实际上就是二叉搜索树树的删除
			if(cmp == 0){
				Node x = min(root.right);
				root.key = x.key;
				root.value = x.value;
				root.right = deleteMin(root.right);
			}else{
				root.right = delete(root.right, key);
			}
		}
		return balance(root);
	}
}
