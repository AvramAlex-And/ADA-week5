public class IntegerBTree{
    private int T;
    private class BTreeNode{
        private int n;
        Integer[] key=new Integer[2*T-1];
        BTreeNode[] child=new BTreeNode[2*T];
        boolean Leaf=true;
    }
    private BTreeNode root;
    public IntegerBTree(int t){
        T=t;
        root = new BTreeNode();
        root.n=0;
        root.Leaf=true;
        constructTree();
    }
    private void constructTree(){
        BTreeNode child0=createLeaf(new int[]{1,2,3});
        BTreeNode child1 = createLeaf(new int[]{6, 7, 9, 10, 11});
        BTreeNode child2 = createLeaf(new int[]{13, 14});
        BTreeNode child3 = createLeaf(new int[]{18, 19, 20});
        BTreeNode child4 = createLeaf(new int[]{24, 26});
        BTreeNode child5 = createLeaf(new int[]{32, 36});
        root=new BTreeNode();
        int[] rootKeys={5,12,16,23,30};
        for (int i = 0; i < rootKeys.length; i++) {
            root.key[i] = rootKeys[i];
        }
        root.n = rootKeys.length;
        root.Leaf = false;
        root.child[0]=child0;
        root.child[1]=child1;
        root.child[2]=child2;
        root.child[3]=child3;
        root.child[4]=child4;
        root.child[5]=child5;
    }

    private BTreeNode createLeaf(int[] keys) {
        BTreeNode node = new BTreeNode();
        for (int i = 0; i < keys.length; i++) {
            node.key[i] = keys[i];
        }
        node.n = keys.length;
        node.Leaf = true;
        return node;
    }

    public boolean contains(Integer key) {
        BTreeNode found = search(root, key);
        return found != null;
    }

    private BTreeNode search(BTreeNode x, Integer key) {
        if (x == null) return null;

        int i = 0;
        while (i < x.n && key > x.key[i]) i++;

        if (i < x.n && key.equals(x.key[i])) {
            return x;
        }
        if (x.Leaf) return null;

        return search(x.child[i], key);
    }

    public int height() {
        return heightRecursive(root);
    }

    private int heightRecursive(BTreeNode node) {
        if (node.Leaf) return 1;
        return 1 + heightRecursive(node.child[0]);
    }

    public int level(int key) {
        return levelRecursive(root, key, 1);
    }

    private int levelRecursive(BTreeNode node, int key, int level) {
        int i = 0;
        while (i < node.n && key > node.key[i]) i++;

        if (i < node.n && key == node.key[i]) return level;
        if (node.Leaf) return -1;
        return levelRecursive(node.child[i], key, level + 1);
    }

    public int min(){
        BTreeNode node = root;
        while (!node.Leaf) node = node.child[0];
        return node.key[0];
    }

    public int max(){
        BTreeNode node = root;
        while (!node.Leaf) node = node.child[node.n];
        return node.key[node.n-1];
    }

    public void inorder() {
        inorderRecursive(root);
        System.out.println();
    }

    private void inorderRecursive(BTreeNode node) {
        int i;
        for (i = 0; i < node.n; i++) {
            if (!node.Leaf) inorderRecursive(node.child[i]);
            System.out.print(node.key[i] + " ");
        }
        if (!node.Leaf) inorderRecursive(node.child[i]);
    }

    public int successor(int key) {
        int[] inorderList = buildInorder();
        for (int i = 0; i < inorderList.length - 1; i++) {
            if (inorderList[i] == key) return inorderList[i + 1];
        }
        throw new RuntimeException("No successor found for key " + key);
    }

    public int predecessor(int key) {
        int[] inorderList = buildInorder();
        for (int i = 1; i < inorderList.length; i++) {
            if (inorderList[i] == key) return inorderList[i - 1];
        }
        throw new RuntimeException("No predecessor found for key " + key);
    }
    private int[] buildInorder() {
        java.util.List<Integer> list = new java.util.ArrayList<>();
        collectInorder(root, list);
        return list.stream().mapToInt(i -> i).toArray();
    }

    private void collectInorder(BTreeNode node, java.util.List<Integer> list) {
        int i;
        for (i = 0; i < node.n; i++) {
            if (!node.Leaf) collectInorder(node.child[i], list);
            list.add(node.key[i]);
        }
        if (!node.Leaf) collectInorder(node.child[i], list);
    }

    public void printStructure() {
        if (root == null) return;

        java.util.Queue<BTreeNode> queue = new java.util.LinkedList<>();
        java.util.Queue<Integer> levels = new java.util.LinkedList<>();

        queue.add(root);
        levels.add(0);
        int currentLevel = 0;

        System.out.println("\nB-Tree Structure:");

        while (!queue.isEmpty()) {
            BTreeNode node = queue.poll();
            int level = levels.poll();

            if (level > currentLevel) {
                System.out.println();
                currentLevel = level;
            }

            System.out.print("[");
            for (int i = 0; i < node.n; i++) {
                System.out.print(node.key[i]);
                if (i < node.n - 1) System.out.print(", ");
            }
            System.out.print("]  ");
            
            if (!node.Leaf) {
                for (int i = 0; i <= node.n; i++) {
                    queue.add(node.child[i]);
                    levels.add(level + 1);
                }
            }
        }
        System.out.println("\n");
    }

    public static void main(String[] args) {
        IntegerBTree tree = new IntegerBTree(3);
        tree.printStructure();
        tree.inorder();
        System.out.println("Contains 14: " + tree.contains(14));
        System.out.println("Contains 22: " + tree.contains(22));
        System.out.println("Height: " + tree.height());
        System.out.println("Level of 19: " + tree.level(19));
        System.out.println("Min: " + tree.min());
        System.out.println("Max: " + tree.max());
        System.out.println("Successor of 19: " + tree.successor(19));
        System.out.println("Predecessor of 19: " + tree.predecessor(19));
        System.out.println("Predecessor of 22: " + tree.predecessor(22));
    }
}