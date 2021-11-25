public class AVLTree {
    private AVLNode root=null;
    private final AVLNode EXTERNALNODE=new AVLNode(-1,null,null,null,-1);
    /**
     * public boolean empty()
     *
     * Returns true if and only if the tree is empty.
     *
     */
    public boolean empty() {
        return false; // to be replaced by student code
    }

    /**
     * public String search(int k)
     *
     * Returns the info of an item with key k if it exists in the tree.
     * otherwise, returns null.
     */
    public String search(int k)
    {
        AVLNode x;
        x = help(this.root,k);
        if (x == null){
            return null;
        } else{
            return x.getValue();
        }
    }
    private AVLNode help(AVLNode x, int k){
        while (x != null){
            if( k == x.getKey()){
                return x;
            }
            else if (k < x.getKey()){
                x = (AVLNode) x.getLeft();
            }
            else{
                x = (AVLNode) x.getRight();
            }
        }
        return x;
    }

    /**
     * public int insert(int k, String i)
     *
     * Inserts an item with key k and info i to the AVL tree.
     * The tree must remain valid, i.e. keep its invariants.
     * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
     * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
     * Returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, String i) {
        IAVLNode whereToInsertNode = treePosition(this.root, k); // comment that O(logn)
        if (whereToInsertNode.getKey() == k) // if the node exists in the tree
            return -1;
        else { // if the node doesnt exists in the tree
            IAVLNode currNode = new AVLNode(k, i, EXTERNALNODE, EXTERNALNODE, 0);
            currNode.setParent(whereToInsertNode);
            // check properties of the node we need to insert after.
            if (whereToInsertNode.isLeaf()) {
                if (whereToInsertNode.getKey() > currNode.getKey()) {
                    whereToInsertNode.setLeft(currNode);
                } else {
                    whereToInsertNode.setRight(currNode);
                }
            }
            // whereToInsertNode is an Unary node
            if (whereToInsertNode.getLeft()==null &&whereToInsertNode.getRight()!=null){
                // only right son
                whereToInsertNode.setLeft(currNode);}
            else { whereToInsertNode.setRight(currNode);

            }
        }
        return rebalanceInsert(whereToInsertNode);
    }
    private int rebalanceInsert(IAVLNode node) {
        // this is where the rebalancing proccess is done
        // return the number of operation needed in order to maintain the AVL invariants.
        if (node==this.root){
            return 0; //check that
        }
        if(node.getRight().getHeight()==-1 && node.getLeft().getHeight()==-1) {
            // if Z is a leaf
            int rankDiffRight= node.getHeight()-node.getRight().getHeight();
            int rankDiffLeft= node.getHeight()-node.getLeft().getHeight();
            if ((rankDiffRight==0 && rankDiffLeft==1)||(rankDiffRight==1&&rankDiffLeft==0)){
                // case 1a- Z is a (0,1) or (1,0) rank difference node
                node.setHeight(node.getHeight()+1); // promote z
                if (node.getParent().getHeight()-node.getHeight()==0){
                    // parent of Z and Z has the same rank
                    return 1+rebalanceInsert(node.getParent());
                }
            }
            if((rankDiffRight==0 && rankDiffLeft==2)||(rankDiffRight==2&&rankDiffLeft==0)){
                // case1b-Z is a (0,2) or (2,0) rank difference node
                IAVLNode rightSonX=node.getRight();
                // check rank differences of rightSonX
                int rankDiffXRight=rightSonX.getHeight()-rightSonX.getRight().getHeight();
                int rankDiffXLeft=rightSonX.getHeight()-rightSonX.getLeft().getHeight();
                if (rankDiffXRight==1 && rankDiffXLeft==2) {
                    //case1b-case2
                    doubleRotation(node,rightSonX);
                    //what should i return? i did 2 more steps
                }
                else if (rankDiffXRight==2 && rankDiffXLeft==1){
                    //case 1b- case 3
                    singleRightRotation(node,rightSonX);
                    node.setHeight(node.getHeight()-1); //demote Z
                    return 2; //check that

                }
            }
        }
        return 0;
    }


    private IAVLNode treePosition(IAVLNode nodeX,int k) {
        //needs to be implemented
        IAVLNode posNode=new AVLNode(0,"null"); //check
        while (nodeX!=null){
            posNode=nodeX;
            if (k==nodeX.getKey()){return nodeX;}
            else if(k<nodeX.getKey()){ nodeX=nodeX.getLeft();}
            else {nodeX=nodeX.getRight();}
        }
        return  posNode;

    }


    /**
     * public int delete(int k)
     *
     * Deletes an item with key k from the binary tree, if it is there.
     * The tree must remain valid, i.e. keep its invariants.
     * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
     * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
     * Returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k)
    {
        IAVLNode whereToInsertNode = treePosition(this.root, k); // comment that O(logn)
        if (whereToInsertNode.getKey() == k) // if the node doesn't exist in the tree
//            do something
        { // if the node exists in the tree
            if (whereToInsertNode.isLeaf()) {
                if (whereToInsertNode.getKey() > k) {
                    whereToInsertNode.setLeft(getdeccessor(whereToInsertNode));
                } else {
                    whereToInsertNode.setRight(getsuccessor(whereToInsertNode));
                }
            }
            // whereToInsertNode is an Unary node
            if (whereToInsertNode.getLeft() == null && whereToInsertNode.getRight() != null) {
                // only right son
                whereToInsertNode.setLeft(currNode);
            } else {
                whereToInsertNode.setRight(currNode);

            }
        } else {
            return -1;
        }
        return rebalanceInsert(whereToInsertNode);
    }




    /**
     * public String min()
     *
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty.
     */
    public String min()
    {
        return "minDefaultString"; // to be replaced by student code
    }

    /**
     * public String max()
     *
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty.
     */
    public String max()
    {
        return "maxDefaultString"; // to be replaced by student code
    }

    /**
     * public int[] keysToArray()
     *
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray()
    {
        return new int[33]; // to be replaced by student code
    }

    /**
     * public String[] infoToArray()
     *
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray()
    {
        return new String[55]; // to be replaced by student code
    }

    /**
     * public int size()
     *
     * Returns the number of nodes in the tree.
     */
    public int size()
    {
        return 422; // to be replaced by student code
    }

    /**
     * public int getRoot()
     *
     * Returns the root AVL node, or null if the tree is empty
     */
    public IAVLNode getRoot()
    {
        return null;
    }

    /**
     * public AVLTree[] split(int x)
     *
     * splits the tree into 2 trees according to the key x.
     * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
     *
     * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
     * postcondition: none
     */
    public AVLTree[] split(int x)
    {
        return null;
    }

    /**
     * public int join(IAVLNode x, AVLTree t)
     *
     * joins t and x with the tree.
     * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
     *
     * precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be empty (rank = -1).
     * postcondition: none
     */
    public int join(IAVLNode x, AVLTree t)
    {
        return -1;
    }

//    things to implement
    private void singleRightRotation(IAVLNode node, IAVLNode rightSonX) {
    }

    private void doubleRotation(IAVLNode node, IAVLNode rightSonX) {
    }
    private IAVLNode getdeccessor(IAVLNode whereToInsertNode) {
    }
    private IAVLNode getsuccessor(IAVLNode whereToInsertNode) {
    }

}
