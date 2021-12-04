/**
 * public class AVLNode
 *
 * If you wish to implement classes other than AVLTree
 * (for example AVLNode), do it in this file, not in another file.
 *
 * This class can and MUST be modified (It must implement IAVLNode).
 */
public class AVLNode implements IAVLNode{
    private int key;
    private String info;
    private IAVLNode parent;
    private IAVLNode left;
    private IAVLNode right;
    private int height;
    private int size;

    public AVLNode(int key, String value){
        // if is leaf
        this.key=key;
        this.info=value;
        this.height = 1;
    }

    public AVLNode(int key, String value, AVLNode right, AVLNode left, int height){
        this.key=key;
        this.info=value;
        this.left=left;
        this.right=right;
        this.height=height;
    }

    // TODO: 04/12/2021 update constructor for ExtrenalLeaf
    public int getKey() {
        return this.key;
    }
    public String getValue() {
        return this.info;
    }
    public void setLeft(IAVLNode node)
    {
        this.left=node;
    }

    public IAVLNode getLeft() {
        return this.left;
    }
    public void setRight(IAVLNode node)
    {
        this.right=node;
    }
    public IAVLNode getRight()
    {
        return this.right;
    }
    public void setParent(IAVLNode node)
    {
        this.parent=node;
    }
    public IAVLNode getParent()
    {
        return this.parent;
    }
    public boolean isRealNode()
    {
        return (this.height != -1 && this.key != -1 && this.info != null);
    }
    public void setHeight(int height)
    {
        this.height=height;
    }
    public int getHeight()
    {
        return this.height;
    }
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    public boolean isLeaf() {
        return this.getRight().getHeight() == -1 && this.getLeft().getHeight() == -1;
    }
}