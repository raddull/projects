import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by raddull on 28-Sep-16.
 */
public class Main {
    public static void main(String[] args) {

        Tree t = new Tree();
        List<Integer> inOrder = t.loadInOrder("C:\\Users\\raddull\\Desktop\\challenge\\preorder.txt");
        List<Integer> preOrder = t.loadPreOrder("C:\\Users\\raddull\\Desktop\\challenge\\inorder.txt");
        StringBuffer buffer = new StringBuffer();
        Tree.Node root = t.recreateTree(inOrder, preOrder, buffer);
        System.out.println(root + ", " + buffer.toString());

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(buffer.toString().getBytes());
            StringBuffer result = new StringBuffer();
            for (int i=0; i < digest.length; i++) {
                result.append(Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 ));
            }
            System.out.println(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static class Tree {

        public class Node {
            int value;
            Node left;
            Node right;
            Node(int val, Node left, Node right) {
                value = val;
                this.left = left;
                this.right = right;
            }
        }

        List<Integer> loadFromFile(String filepath) {
            List<Integer> ret = new ArrayList<>(10000);
            try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    line = br.readLine();
                }
                String[] arr = sb.toString().split(" ");
                for (String s : arr) {
                    Integer i = Integer.parseInt(s);
                    ret.add(i);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ret;
        }

        List<Integer> loadInOrder(String file) {
            //return Arrays.asList(2,1,0,11,7,13,10,12,3,9,1,8,4);
            return loadFromFile(file);
        }

        List<Integer> loadPreOrder(String file) {
            //return Arrays.asList(11,0,1,13,7,12,10,2,9,3,8,1,4);
            return loadFromFile(file);
        }

        Node recreateTree(List<Integer> inOrder, List<Integer> preOrder, StringBuffer buffer) {
            if (inOrder.size() == 0) {
                return null;
            }
            Integer in = inOrder.get(0);
            if (inOrder.size() == 1) {
                buffer.append(in);
                return new Node(in, null, null);
            }
            int index = preOrder.indexOf(in);
            List<Integer> leftPreSubtree = preOrder.subList(0, index);
            List<Integer> rightPreSubtree = preOrder.subList(index + 1, preOrder.size());
            List<Integer> leftInSubtree = inOrder.subList(1, leftPreSubtree.size() + 1);
            List<Integer> rightInSubtree = inOrder.subList(leftPreSubtree.size() + 1, inOrder.size());

            Node left = recreateTree(leftInSubtree, leftPreSubtree, buffer);
            Node right = recreateTree(rightInSubtree, rightPreSubtree, buffer);
            return new Node(in, left, right);
        }
    }

}



