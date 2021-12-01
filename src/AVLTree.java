public class AVLTree {
    private IAVLNode root = null;
    private final IAVLNode EXTERNALNODE = new AVLNode(-1, null, null, null, -1, -1);

    public AVLTree() {
        this.root = new AVLNode(-1, null, EXTERNALNODE, EXTERNALNODE, 0, 0);
        // notice if the external node exsists
    }

    /**
     * public boolean empty()
     * <p>
     * Returns true if and only if the tree is empty.
     */
    public boolean empty() {
        return (this.root.getKey() == -1 && this.root.getValue() == null
                && this.root.getRight() == EXTERNALNODE && this.root.getLeft() == EXTERNALNODE
                && this.root.getHeight() == 0);
    }

    private IAVLNode treePosition(IAVLNode nodeX, int k) {
        //needs to be implemented
        IAVLNode posNode = new AVLNode(0, "null"); //check
        while (nodeX != null) {
            posNode = nodeX;
            if (k == nodeX.getKey()) {
                return nodeX;
            } else if (k < nodeX.getKey()) {
                nodeX = nodeX.getLeft();
            } else {
                nodeX = nodeX.getRight();
            }
        }
        return posNode;

    }


    /**
     * public String search(int k)
     * <p>
     * Returns the info of an item with key k if it exists in the tree.
     * otherwise, returns null.
     */
    public String search(int k) {
       if (this.root != null){
           IAVLNode x = treePosition(this.root,k);
           if (x != null && x.getKey() == k){
               return x.getValue();
           }
       }
       return null;
    }

    /**
     * public int insert(int k, String i)
     * <p>
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
            IAVLNode currNode = new AVLNode(k, i, EXTERNALNODE, EXTERNALNODE, 0, 0);
            currNode.setParent(whereToInsertNode);
            whereToInsertNode.setHeight(1 + maxHeight(whereToInsertNode.getLeft(), whereToInsertNode.getRight()));
            // check properties of the node we need to insert after.
            if (whereToInsertNode.isLeaf()) {
                if (whereToInsertNode.getKey() > currNode.getKey()) {
                    whereToInsertNode.setLeft(currNode);
                } else {
                    whereToInsertNode.setRight(currNode);
                }
            }
            // whereToInsertNode is an Unary node
            if (whereToInsertNode.getLeft() == null && whereToInsertNode.getRight() != null) {
                // only right son
                whereToInsertNode.setLeft(currNode);
            } else {
                whereToInsertNode.setRight(currNode);
            }
        }
        return rebalanceInsert(whereToInsertNode);
    }

    private int maxHeight(IAVLNode leftNode, IAVLNode rightNode) {
        if (leftNode.getHeight() >= rightNode.getHeight()) {
            return leftNode.getHeight();
        } else {
            return rightNode.getHeight();
        }

    }

    private int rebalanceInsert(IAVLNode node) {
        // this is where the rebalancing proccess is done
        // return the number of operation needed in order to maintain the AVL invariants.
        if (node == this.root) {
            return 0; //check that
        }
        if (node.isLeaf()) {
            // if Z is a leaf
            int rankDiffRight = node.getRank() - node.getRight().getRank();
            int rankDiffLeft = node.getRank() - node.getLeft().getRank();
            if ((rankDiffRight == 0 && rankDiffLeft == 1) || (rankDiffRight == 1 && rankDiffLeft == 0)) {
                // case 1a- Z is a (0,1) or (1,0) rank difference node
                promote(node);
                if (node.getParent().getRank() - node.getRank() == 0) {
                    // parent of Z and Z has the same rank
                    return 1 + rebalanceInsert(node.getParent());
                }
            }
            if ((rankDiffRight == 0 && rankDiffLeft == 2) || (rankDiffRight == 2 && rankDiffLeft == 0)) {
                // case1b-Z is a (0,2) or (2,0) rank difference node
                IAVLNode leftSonX = node.getLeft();
                // check rank differences of leftSonX
                int rankDiffXRight = leftSonX.getRank() - leftSonX.getRight().getRank();
                int rankDiffXLeft = leftSonX.getRank() - leftSonX.getLeft().getRank();
                if (rankDiffXLeft == 2 && rankDiffXRight == 1) {
                    //case1b-case3
                    // left rotation x-b, right rotation
                    singleLeftRotation(leftSonX, leftSonX.getRight());
                    singleRightRotation(node, leftSonX);
                    return 5;
                } else if (rankDiffXLeft == 1 && rankDiffXRight == 2) {
                    //case 1b- case 2
                    singleRightRotation(node, leftSonX);
                    demote(node);
                    return 2;

                }
//                // case1b-Z is a (0,2) or (2,0) rank difference node
//                IAVLNode leftSonX=node.getRight();
//                // check rank differences of leftSonX
//                int rankDiffXRight=leftSonX.getHeight()-leftSonX.getRight().getHeight();
//                int rankDiffXLeft=leftSonX.getHeight()-leftSonX.getLeft().getHeight();
//                if (rankDiffXLeft==2 && rankDiffXRight==1) {
//                    //case1b-case3
//                    doubleRotation(node,leftSonX);
//                    //what should i return? i did 2 more steps
//                }
//                else if (rankDiffXLeft==1 && rankDiffXRight==2){
//                    //case 1b- case 2
//                    singleRightRotation(node,leftSonX);
//                    demote(node);
////                    node.setHeight(node.getHeight()-1); //demote Z
//                    return 2; //check that
//
//                }
            }
        }
        return 0;
    }

    private void demote(IAVLNode node) {
        node.setRank(node.getRank() - 1);
    }

    private void promote(IAVLNode node) {
        node.setRank(node.getRank() + 1);
    }





    /**
     * public int delete(int k)
     * <p>
     * Deletes an item with key k from the binary tree, if it is there.
     * The tree must remain valid, i.e. keep its invariants.
     * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
     * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
     * Returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) {
        IAVLNode nodeToDelete = treePosition(this.root, k); // comment that O(logn)
        IAVLNode parent = nodeToDelete.getParent();
        if (nodeToDelete == null){
            return -1;
        }else {
//            no need to rebalance
            if(this.root == nodeToDelete &&
                    !nodeToDelete.getRight().isRealNode() &&
                    !nodeToDelete.getLeft().isRealNode()){
                this.root = null;
                return 0;
            } else {
                if (nodeToDelete.isLeaf()){
                    
                    if(parent == null){
                        this.root = null;

                    }else{
                        if(parent.getRight() == nodeToDelete){
                            parent.setRight(EXTERNALNODE);
                        }

                        else {
                            parent.setLeft(EXTERNALNODE);
                        }
                    }
                    return rebalanceDelete(parent);
                }else{
                    if (!nodeToDelete.getRight().isRealNode()
                        && nodeToDelete.getLeft().isRealNode()){
//                        Unary node
                        if (parent == null){
                            if(nodeToDelete.getRight().isRealNode()){
                                this.root = nodeToDelete.getRight();
                                nodeToDelete.getRight().setParent(null);
                            }
                            if(nodeToDelete.getLeft().isRealNode()){
                                this.root = nodeToDelete.getLeft();
                                nodeToDelete.getLeft().setParent(null);
                            }
                        }
                        return rebalanceDelete(parent);
                    }else {
                        replaceWithSuccessor(nodeToDelete);
                    }
                    
                }
            }
        }
//        if (nodeToDelete.getKey() == k) {
//            // if the node exists in the tree
//            IAVLNode parent = nodeToDelete.getParent();
//            int rankDiffRight = parent.getRank() - parent.getRight().getRank();
//            int rankDiffLeft = parent.getRank() - parent.getLeft().getRank();
//            if (parent.isLeaf()) {
//                if (nodeToDelete == parent.getLeft()) {
//                    parent.setLeft(EXTERNALNODE);
//                }
//            } else {
//                return -1;
//            }
//            return rebalanceInsert(nodeToDelete);
//        }
    }

    private void replaceWithSuccessor(IAVLNode nodeToDelete) {
    }

    private int rebalanceDelete(IAVLNode nodeToDelete) {
    }


    /**
     * public String min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty.
     */
    public String min() {
        return "minDefaultString"; // to be replaced by student code
    }

    /**
     * public String max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty.
     */
    public String max() {
        return "maxDefaultString"; // to be replaced by student code
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        return new int[33]; // to be replaced by student code
    }

    /**
     * public String[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray() {
        return new String[55]; // to be replaced by student code
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     */
    public int size() {
        return 422; // to be replaced by student code
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     */
    public IAVLNode getRoot() {
        return null;
    }

    /**
     * public AVLTree[] split(int x)
     * <p>
     * splits the tree into 2 trees according to the key x.
     * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
     * <p>
     * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
     * postcondition: none
     */
    public AVLTree[] split(int x) {
        return null;
    }

    /**
     * public int join(IAVLNode x, AVLTree t)
     * <p>
     * joins t and x with the tree.
     * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
     * <p>
     * precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be empty (rank = -1).
     * postcondition: none
     */
    public int join(IAVLNode x, AVLTree t) {
        return -1;
    }

    //    things to implement
    private void singleRightRotation(IAVLNode node, IAVLNode leftSonX) {

        IAVLNode rightSonb = leftSonX.getRight();
        node.setParent(leftSonX);
        leftSonX.setRight(node);
        node.setLeft(rightSonb);

    }

    private void singleLeftRotation(IAVLNode node, IAVLNode rightSonX) {
        IAVLNode leftSonb = rightSonX.getLeft();
        node.setParent(rightSonX);
        node.setRight(leftSonb);
        rightSonX.setLeft(node);

    }

    private IAVLNode getdeccessor(IAVLNode whereToInsertNode) {
    }

    private IAVLNode getsuccessor(IAVLNode whereToInsertNode) {
    }

}