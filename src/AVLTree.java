import java.util.Arrays;

public class AVLTree {
    private IAVLNode root = null;
    private final IAVLNode EXTERNALNODE = new AVLNode(-1, null, null, null, -1, -1);
    protected IAVLNode maxNode = null; // pointer to the node with the maxNode key
    private IAVLNode minNode =null;
    private int size=0;

    public AVLTree() { // build an empty tree
        this.root = null;
    }

    public AVLTree(IAVLNode root) { // build a leaf
        this.root = root;
        root.setParent(null);
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

    /*
     * returns an array of rank differences with parent of node
     */
    public static int[] rankDifference(IAVLNode node){
        return new int[] {node.getHeight() - node.getLeft().getHeight(),
                node.getHeight() - node.getRight().getHeight()};
    }

    /*
     * Supposed to be used like enums, without creating new classes
     * return which case are we at currently, to ease on dealing with symmetrical cases while inserting node
     */
    private String insertionCase(IAVLNode node){
        // TODO: 04/12/2021 input is parent, no need to change because we call rebalance on parent = node
        if (node != null && node.getParent() != null) {
            int[] rankDiffNode = rankDifference(node);
            int[] rankDiffParent = rankDifference(node.getParent());
            if ((Arrays.equals(rankDiffParent, new int[]{1, 0}) ||
                    (Arrays.equals(rankDiffParent, new int[]{0, 1})))) {
                // TODO: 03/12/2021 need to promote parent + return1
                return "case1";
            }
            // TODO: 03/12/2021 return 0
            if (((Arrays.equals(rankDiffNode, new int[]{1, 1}) // after insertion it's Unary
                    && (Arrays.equals(rankDiffParent, new int[]{1, 2}) ||
                    (Arrays.equals(rankDiffParent, new int[]{2, 1})))))) {
                return "caseB";
            }
            // TODO: 03/12/2021 single rotation right & demote z
            if (Arrays.equals(rankDiffNode, new int[]{1, 2})
                    && Arrays.equals(rankDiffParent, new int[]{0, 2})) {
                return "case2left";
            }
            // TODO: 03/12/2021  double rotation & promote/demote b,x,z & return +5
            if (Arrays.equals(rankDiffNode, new int[]{1, 2})
                    && Arrays.equals(rankDiffParent, new int[]{2, 0})) {
                return "case2right";
            }
            if (Arrays.equals(rankDiffNode, new int[]{2, 1})
                    && Arrays.equals(rankDiffParent, new int[]{0, 2})) {
                return "case3left";
            }

            // TODO: 03/12/2021  single rotation & demote z & return +2
            if (Arrays.equals(rankDiffNode, new int[]{2, 1})
                    && Arrays.equals(rankDiffParent, new int[]{2, 0})) {
                return "case3right";
            }
            if (Arrays.equals(rankDiffNode, new int[]{1, 2})
                    && Arrays.equals(rankDiffParent, new int[]{0, 2})) {
                return "case2left";
            }
        }
        // TODO: 04/12/2021 rebalance parent when needed
        // TODO: 03/12/2021 special cases for join
        return "";
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
            whereToInsertNode.setHeight(1+maxHeight(whereToInsertNode.getLeft(),whereToInsertNode.getRight()));
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
        if (node==this.root){
            return 0; //check that
        }
        if(isRebalancingInSertDone(node)){
            //when the tree is balanced when we call the method
            promote(node);
            return 1;
        }
        if (node.getParent()!=null) {
            // in order to check rank diff between node and his parent we need to make sure its not null
            //}
            int parRank = node.getParent().getHeight();
            if (node.isLeaf()) {
                // if Z is a leaf
                int rankDiffLeft = rankDifference(node)[0];
                int rankDiffRight = rankDifference(node)[1];
                if ((rankDiffRight == 0 && rankDiffLeft == 1) || (rankDiffRight == 1 && rankDiffLeft == 0)) {
                    // case 1a- Z is a (0,1) or (1,0) rank difference node
                    promote(node);
                    if (node.getParent().getHeight() - node.getHeight() == 0) {
                        // parent of Z and Z has the same rank
                        return 1 + rebalanceInsert(node.getParent());
                    }
                }
                if ((rankDiffRight == 0 && rankDiffLeft == 2) || (rankDiffRight == 2 && rankDiffLeft == 0)) {
                    // case1b-Z is a (0,2) or (2,0) rank difference node
                    IAVLNode leftSonX = node.getLeft();
                    // check rank differences of leftSonX
                    int rankDiffXRight = leftSonX.getHeight() - leftSonX.getRight().getHeight();
                    int rankDiffXLeft = leftSonX.getHeight() - leftSonX.getLeft().getHeight();
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
                    if (rankDiffXLeft == 1 && rankDiffXRight == 1) {
                        // special case for join method only
                        singleRightRotation(leftSonX.getParent(), leftSonX);
                        promote(leftSonX.getLeft());// after rotation leftSonX is the parent of node
                        return 2 + rebalanceInsert(leftSonX.getParent());//check
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
                    //special case for join

                }

            }
        }
        return 0;
    }

    private void demote(IAVLNode node) {
        node.setHeight(node.getHeight() - 1);
    }

    private void promote(IAVLNode node) {
        node.setHeight(node.getHeight() + 1);
    }

    /*
    * gets the node we want to delete
     * return which case are we at currently, to ease on dealing with symmetrical cases
     * Supposed to be used like enums, without creating new classes
     */
    private String deletetionCase(IAVLNode parent) {
        // TODO: 04/12/2021 check cases of right / left child outside
        // TODO: 04/12/2021 create arrays one time in order to save in space complexity
        int[] rankDiffParent = rankDifference(parent);
        int[] rankDiffRChild = rankDifference(parent.getRight());
        int[] rankDiffLChild = rankDifference(parent.getLeft());

//      TODO: 03/12/2021 check if node is right or left son and replace with External node + return 0
        if ((Arrays.equals(rankDiffParent, new int[]{2, 1}) || (Arrays.equals(rankDiffParent, new int[]{1, 2})) &&
                (parent.getLeft().isRealNode() && !parent.getRight().isRealNode()) ||
                (!parent.getLeft().isRealNode() && parent.getRight().isRealNode()))) {
            return "case0";
        }
        //      TODO: 03/12/2021 rotation left on z-y, promote(y) demote(z)
        //      TODO: 03/12/2021 return 3 + rebalance (parent)
        //      parent is Unary
        if (((Arrays.equals(rankDiffParent, new int[]{3, 1}) &&
                (parent.getRight().isRealNode()) && (!parent.getLeft().isRealNode()) &&
                (Arrays.equals(rankDiffRChild, new int[]{1, 1}))))) {
            return "case2right";
        }
        //      TODO: 03/12/2021 rotation right on z-y, promote(y) demote(z)
        //      TODO: 03/12/2021 return 3 + rebalance (parent)
        if (((Arrays.equals(rankDiffParent, new int[]{1, 3}) &&
                (!parent.getRight().isRealNode()) && (parent.getLeft().isRealNode()) &&
                (Arrays.equals(rankDiffLChild, new int[]{1, 1}))))) {
            return "case2left";
        }
        //      TODO: 03/12/2021 rotation left on z-y, 2xdemote(z)
        //      TODO: 03/12/2021 return 3 + rebalance (parent)
        if (((Arrays.equals(rankDiffParent, new int[]{3, 1}) &&
                (parent.getRight().isRealNode()) && (!parent.getLeft().isRealNode()) &&
                (Arrays.equals(rankDiffRChild, new int[]{2, 1}))))) {
            return "case3right";
        }
        //      TODO: 03/12/2021 rotation right on z-y, 2xdemote(z)
        //      TODO: 03/12/2021 return 3 + rebalance (parent)
        if (((Arrays.equals(rankDiffParent, new int[]{1, 3}) &&
                (!parent.getRight().isRealNode()) && (parent.getLeft().isRealNode()) &&
                (Arrays.equals(rankDiffLChild, new int[]{1, 2}))))) {
            return "case3left";
        }
        //      TODO: 03/12/2021 rotation right on y-a, rotation left on a-z, 2xdemote(z), demote(y), promote(a)
        //      TODO: 03/12/2021 return 6 + rebalance (parent)
        if (((Arrays.equals(rankDiffParent, new int[]{3, 1}) &&
                (parent.getRight().isRealNode()) && (!parent.getLeft().isRealNode()) &&
                (Arrays.equals(rankDiffRChild, new int[]{1, 2}))))) {
            return "case4right";
        }
        //      TODO: 03/12/2021 rotation left on y-a, rotation right on a-z, 2xdemote(z), demote(y), promote(a)
        //      TODO: 03/12/2021 return 6 + rebalance (parent)
        if (((Arrays.equals(rankDiffParent, new int[]{1, 3}) &&
                (!parent.getRight().isRealNode()) && (parent.getLeft().isRealNode()) &&
                (Arrays.equals(rankDiffLChild, new int[]{2, 1}))))) {
            return "case4left";
        }
        return "";
    }

    private void deleteLeaf(IAVLNode nodeToDelete) {
        IAVLNode parent = nodeToDelete.getParent();
        if(parent == null) { // size is 1
            this.root = null;
        }else {
            if (parent.getRight() == nodeToDelete) { // delete right child
                parent.setRight(EXTERNALNODE);
            } else { // delete left child
                parent.setLeft(EXTERNALNODE);
            }
        }
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
        // maybe use switch case in order to make it a recursion
        IAVLNode nodeToDelete = treePosition(this.root, k); // comment that O(logn)
        IAVLNode parent = nodeToDelete.getParent();
        if (nodeToDelete == null){ // no need to delete
            return -1;
        }else {
            if(this.root == nodeToDelete &&
                    !nodeToDelete.getRight().isRealNode() &&
                    !nodeToDelete.getLeft().isRealNode()){ // tree is just a leaf, no need to rebalance
                this.root = null;
                return 0;
            } else {
                if(nodeToDelete.isLeaf()){
                    deleteLeaf(nodeToDelete);
                    if (parent!=null){
                        reSize(parent,-1);
                    }
                    return rebalanceDelete(parent);
                }else {
                    if (isUnary(nodeToDelete)){
                        deleteUnary(nodeToDelete);
                        if (parent!=null){
                            reSize(parent,-1);
                        }
                        return rebalanceDelete(parent);
                    }else {
                        IAVLNode replace = replaceWithSuccessor(nodeToDelete);
                        if (replace!=null){
                            reSize(replace, -1);
                        }
                        return rebalanceDelete(replace);
                    }
                }
            }
        }
    }

    private IAVLNode findSuccessor(IAVLNode nodeToDelete) {
        if (nodeToDelete.getRight().isRealNode()) {
            IAVLNode successor = nodeToDelete.getRight();
            while (successor.getLeft() != null) {
                successor = successor.getLeft();
            }
            return successor;
        }else{
            IAVLNode successor = nodeToDelete.getParent();
            while (nodeToDelete!=null){
                if (nodeToDelete.getKey()>successor.getKey()){
                    return successor;
                }
                successor = successor.getParent();
            }
        }
        return null;
    }

    private boolean isUnary(IAVLNode nodeToDelete) {
        return ((!nodeToDelete.getRight().isRealNode()) && nodeToDelete.getLeft().isRealNode()) ||
                (!nodeToDelete.getRight().isRealNode() && nodeToDelete.getLeft().isRealNode());
    }

    /**
     * resize from node to root
     * i = -1 if we delete
     * i = 1 if we insert
     */
    private void reSize(IAVLNode parent, int i) {
        while(parent != null) {
            parent.setSize(parent.getSize() + i);
            parent = parent.getParent();
        }
    }

    /**
     * recalculate Height from node to root
     * */
    public static void reHeight(IAVLNode parent) {
        int maxHeight = Math.max(parent.getLeft().getHeight(), parent.getRight().getHeight());
        parent.setHeight(maxHeight+1);
    }

    private void deleteUnary(IAVLNode nodeToDelete) {
        IAVLNode parent = nodeToDelete.getParent();
        if(parent == null){  // lace tree with 2 nodes
            if(nodeToDelete.getRight().isRealNode()){
                this.root = nodeToDelete.getRight();
                nodeToDelete.getRight().setParent(null);
            }
            if(nodeToDelete.getLeft().isRealNode()){
                this.root = nodeToDelete.getLeft();
                nodeToDelete.getLeft().setParent(null);
            }
        }
        else {
            if (parent.getRight() == nodeToDelete){
                if(nodeToDelete.getLeft().isRealNode()){
                    parent.setRight(nodeToDelete.getLeft());
                    nodeToDelete.getLeft().setParent(parent);
                }else {
                    parent.setRight(nodeToDelete.getRight());
                    nodeToDelete.getRight().setParent(parent);
                }
            }
            else {
                if(nodeToDelete.getRight().isRealNode()){
                    parent.setLeft(nodeToDelete.getRight());
                    nodeToDelete.getRight().setParent(parent);
                }else {
                    parent.setLeft(nodeToDelete.getLeft());
                    nodeToDelete.getLeft().setParent(parent);
                }
            }
        }

    }


    private IAVLNode replaceWithSuccessor(IAVLNode nodeToDelete) {
        IAVLNode parent = nodeToDelete.getParent();
        IAVLNode successor = findSuccessor(nodeToDelete);
        IAVLNode srParent = successor.getParent();

        if (nodeToDelete != srParent) {
            //successor is not right child of nodeToDelete
            nodeToDelete.getRight().setParent(successor);
            if (successor.getRight().isRealNode()) {
                srParent.setLeft(successor.getRight());
                successor.getRight().setParent(srParent);
            }else {
                srParent.setLeft(EXTERNALNODE);
            }
            successor.setRight(nodeToDelete.getRight());
        }

        nodeToDelete.getLeft().setParent(successor);
        successor.setLeft(nodeToDelete.getLeft());
        successor.setHeight(nodeToDelete.getHeight());
        successor.setSize(nodeToDelete.getSize());
        successor.setParent(parent);

        if (parent == null) { //nodeToDelete is root
            this.root = successor;
        } else {
            if (parent.getRight() == nodeToDelete) {
                parent.setRight(successor);
            } else {
                parent.setLeft(successor);
            }
        }
        if (nodeToDelete == srParent) { //successor is right child
            return successor;
        }
        return srParent;
    }

    private int rebalanceDelete(IAVLNode nodeToDelete) {
    }


    /**
     * public String minNode()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty.
     */
    public String min()
    {
//        using a pointer to maxNode node for O(1)
//        return this.min;
        if (this.empty()){
            return null;
        }
        IAVLNode currNode = this.root;
        while (currNode.getLeft()!=null){
            currNode=currNode.getLeft();
        }
        return currNode.getValue();
    }


    /**
     * public String maxNode()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty.
     */
    public String max() {
//        using a pointer to maxNode node for O(1)
//        return this.max;
        if (this.empty()){
            return null;
        }
        IAVLNode currNode = this.root;
        while (currNode.getRight()!=null){
            currNode=currNode.getRight();
        }
        return currNode.getValue();
    }


    private int[] KeysToArrayRecHelper(IAVLNode root,int[] keysArray, int pos) {
        if(root != null && root.isRealNode() && root != EXTERNALNODE && pos < keysArray.length) {
            KeysToArrayRecHelper(root.getLeft(), keysArray, pos);
            pos++;
            keysArray[pos] = root.getKey();// insert curr root in the corresponding pos
            KeysToArrayRecHelper(root.getRight(), keysArray, pos);
            return keysArray;//check
        }
        return keysArray;// missing return statement error
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        if (this.empty()){
            return new int[]{};// return empty array
        }
        else { // this tree isnt empty
            int[] keysArray=new int[this.size()];
            return KeysToArrayRecHelper(this.root,keysArray,0);
        }
    }
    private String[] infoToArrayRecHelper(IAVLNode root,String[] keysArray, int pos) {
        if(root != null && root.isRealNode() && root != EXTERNALNODE && pos < keysArray.length) {
            infoToArrayRecHelper(root.getLeft(), keysArray, pos);
            pos++;
            keysArray[pos] = root.getValue();// insert curr root in the corresponding pos
            infoToArrayRecHelper(root.getRight(), keysArray, pos);
            return keysArray;//check
        }
        return keysArray;// missing return statement error
    }

    /**
     * public String[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray() {
        if(this.empty()){
            return new String[]{};
        }
        String[] infoArray = new String[this.size];
        infoToArrayRecHelper(this.root, infoArray,0);
        return infoArray;
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     */
    public int size() {
        // update size field during tree functions
        return this.size;
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     */
    public IAVLNode getRoot() {
        return this.root;
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
        AVLTree biggerSubT = new AVLTree(); // holds nodes with keys bigger than x
        AVLTree smallerSubT = new AVLTree(); // hold nodes keys smaller than x
        IAVLNode whereTosplit = treePosition(this.root, x);
        IAVLNode parent = whereTosplit.getParent();
        AVLTree treeToJoin;
        IAVLNode node = whereTosplit; // pointer for the joins

        if (node.getRight().isRealNode()){
            biggerSubT = new AVLTree(node.getRight());
        }
        if (node.getLeft().isRealNode()) {
            smallerSubT = new AVLTree(node.getLeft());
        }

        while(parent != null) {
            if (node == parent.getRight()) {
                // parent and left tree is smaller than x
                node = parent;
                parent = parent.getParent();

                if (node.getLeft().isRealNode()) {
                    treeToJoin = new AVLTree(node.getLeft());

                } else {
                    treeToJoin = new AVLTree();
                }
                node.setParent(null);
                node.setLeft(this.EXTERNALNODE);
                node.setRight(this.EXTERNALNODE);
                node.setHeight(0);
                smallerSubT.join(node, treeToJoin);
            } else {
                node = parent;
                parent = parent.getParent();
                if (node.getRight().getHeight() != -1) {
                    treeToJoin = new AVLTree(node.getRight());

                } else {
                    treeToJoin = new AVLTree();
                }
                node.setParent(null);
                node.setLeft(this.EXTERNALNODE);
                node.setRight(this.EXTERNALNODE);
                node.setHeight(0);
                biggerSubT.join(node, treeToJoin);
            }
        }
        return new AVLTree[] {smallerSubT,biggerSubT}; // Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
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
        // take care of empty trees (both,one)
        if (t.empty()) {
            if (this.empty()) {
                // both this and t are empty- we will connect to this as needed
                this.root = new AVLNode(x.getKey(), x.getValue(), EXTERNALNODE, EXTERNALNODE, 0, 0);
                //todo-set min and max
                if (x.isRealNode()) {
                    x.setHeight(0);
                    // todo- set Size
                }
                return 1;// rank diff: 0-0+1
            }
            //only t is empty - this isnt empty and t is empty
            //we should insert x as regular insert to this
            this.insert(x.getKey(), x.getValue());
            //todo- set max and min
            return this.getRoot().getHeight() + 2;
        } else if (this.empty()) {
            //only this is empty -this is empty and t isnt
            // we should insert x as regular insert to t and then set this as the new tree
            t.insert(x.getKey(), x.getValue());
            this.root = t.getRoot();
            //todo- set max and min
            return this.getRoot().getHeight() + 2;
        }
        //both of them are not empty
        int rankThisRoot = this.getRoot().getHeight();
        int rankTRoot = t.getRoot().getHeight();
        int checkRanksDiff = rankThisRoot - rankTRoot; // if 0 same rank, if >0 this is higher, if <0 this is less high
        boolean isThisbiggerKeys = t.root.getKey() < this.root.getKey();
        IAVLNode currNode;
        if (checkRanksDiff > 0) {
            // if >0 this is higher
            if (isThisbiggerKeys) {
                currNode = findInsertPos(this.root, 'L', rankThisRoot, rankTRoot);
                connect(currNode, x, t.root, 'L');
            } else { //  keys(this)< x < keys(t)
                currNode = findInsertPos(this.root, 'R', rankThisRoot, rankTRoot);
                connect(currNode, x, t.root, 'R');
            }
        }
        if (checkRanksDiff == 0) {
            //same ranks for both trees- x should be the root and connection to its sons will be based on
            // the order of the keys.
            if (isThisbiggerKeys) {
                // keys(this) > x > keys(t)
                connect(x, t.root, this.root);// this should be the right son since his keys are bigger
            } else {//  keys(this)< x < keys(t)
                connect(x, this.root, t.root);// t should be the right son since his keys are bigger
            }
            this.root = x;// we join to this then we should update its root
        }
        else {  // if <0 this is lower
            if (isThisbiggerKeys){
                // keys(this) > x > keys(t)
                currNode=findInsertPos(t.root,'R',rankTRoot,rankThisRoot);
                connect(currNode,x,this.root,'R');
            }
            if (!isThisbiggerKeys){
                // keys(this) < x < keys(t)
                currNode= findInsertPos(t.root,'L',rankTRoot,rankThisRoot);
                connect(currNode,x,this.root,'L');
            }
        }
        //determine which tree should be sent to rebalance
        if (checkRanksDiff>0){
            //this tree is higher
            this.rebalanceInsert(x);
        }
        else { //t is higher
            t.rebalanceInsert(x);
            this.root=t.root; // we want to join x and t to this tree;
        }
        //todo-set max,min,size

        // if the tree-this is higher than rankDiff are rankThisRoot-rankTRoot+1
        // otherwise, rankDiff are rankTRoot-rankThisRoot+1
        return checkRanksDiff>0 ? rankThisRoot-rankTRoot+1: rankTRoot-rankThisRoot+1;
    }

    private void connect(IAVLNode currNodeInHigher, IAVLNode x, IAVLNode rootLower,char side) {
        IAVLNode parentC=currNodeInHigher.getParent();
        switch (side) {
            case 'R':
                // if we moved right through the higher tree
                currNodeInHigher.setParent(x);
                x.setLeft(currNodeInHigher);
                x.setRight(rootLower);
                parentC.setRight(x);
                break;
            case 'L':
                // if we moved left through the higher tree
                currNodeInHigher.setParent(x);
                x.setLeft(rootLower);
                x.setRight(currNodeInHigher);
                parentC.setLeft(x);
                break;
            default:
                break;
        }
    }

    private void connect(IAVLNode root, IAVLNode leftSon, IAVLNode rightSon) {
        // connection between same rank trees: leftSon,rightSon, X should be thier parent
        root.setLeft(leftSon);
        root.setRight(rightSon);
        leftSon.setParent(root);
        rightSon.setParent(root);
        root.setHeight(maxHeight(leftSon,rightSon)+1);
    }

    private IAVLNode findInsertPos(IAVLNode root, char side, int rankHigher,int rankLowerRoot) {
        IAVLNode currNode = root;
        switch (side) {
            case 'R':
                while (currNode.getHeight() > rankLowerRoot) {
                    // we will go left until we find currNode.getRank<=rankt
                    currNode = currNode.getRight();
                }
                return currNode;

            case 'L':
                while (currNode.getHeight() > rankLowerRoot) {
                    // we will go left until we find currNode.getRank<=rankt
                    currNode = currNode.getLeft();
                }
                return currNode;

            default:
                break;
        }
        return currNode;
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
    private boolean isRebalancingInSertDone(IAVLNode parent){
        if(parent.getHeight()-parent.getLeft().getHeight()==1 && parent.getHeight()-parent.getRight().getHeight()==1){
            //case in which the rebalancing is not done.
            return false;
        }
        return true;
    }

}


