package com.xuxd.dispatcher.common;

/**
 * webhook-dingtalk-dispatcher.
 *
 * @author xuxd
 * @date 2021-12-03 14:56:28
 **/
public class Trie {

    private Node root = new Node();

    public Trie(String[] words) {
        for (String word : words) {
            insert(word.trim());
        }
    }

    public boolean matchAny(String str) {
        char[] cs = str.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            if (cs[i] < 0 || cs[i] > 127) {
                continue;
            }
            if (root.contains(cs[i])) {
                Node node = root.get(cs[i]);
                for (int j = i + 1; j < cs.length; j++) {
                    node = node.get(cs[j]);
                    if (node == null) {
                        break;
                    }
                    if (node.isEnd()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void insert(String word) {
        Node node = root;
        for (char c : word.toCharArray()) {
            node = node.contains(c) ? node.get(c) : node.put(c);
        }
        node.setEnd();
    }

    class Node {

        private Node[] nods = new Node[128];
        private boolean end = false;

        public Node put(char c) {
            return (nods[c] = new Node());
        }

        public Node get(char c) {
            return nods[c];
        }

        public boolean contains(char c) {
            return nods[c] != null;
        }

        public boolean isEnd() {
            return end;
        }

        public void setEnd() {
            end = true;
        }
    }
}
