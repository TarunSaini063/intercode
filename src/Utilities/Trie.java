/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tarun
 */
public class Trie {

    public class TrieNode {

        private final Map<Character, TrieNode> children = new HashMap<>();
        private boolean endOfWord;

        Map<Character, TrieNode> getChildren() {
            return children;
        }

        boolean isEndOfWord() {
            return endOfWord;
        }

        void setEndOfWord(boolean endOfWord) {
            this.endOfWord = endOfWord;
        }
    }
    private TrieNode root;

    public Trie(String Document) {
        root = new TrieNode();
        String[] words = Document.split("\\W+");
//        System.out.println("Trie constructor : ");

        for (String word : words) {
//            System.out.print(word + ", ");
            insert(word);  //insert all words
        }
//        System.out.println();
    }

    void insert(String word) {
        TrieNode current = root;
        for (char l : word.toCharArray()) {
            current = current.getChildren().computeIfAbsent(l, c -> new TrieNode());
        }
        current.setEndOfWord(true);
    }

    List<String> words;

    public List<String> getWordsForPrefix(String prefix) {
        words = new ArrayList<String>();
        TrieNode current = root, node = null;
        int len = prefix.length();
        for (int i = 0; i < len; i++) {
            char ch = prefix.charAt(i);
            node = current.getChildren().get(ch);
            if (node == null) {
                break;
            }
            System.out.println(ch);
            current = node;
        }
        getWordsForPrefix(prefix, node);
//        System.out.println("Printing auto complete words prefix: " + prefix);
//        for (String s : words) {
//            System.out.print(s + ", ");
//        }
//        System.out.println();
        return words;
    }

    void getWordsForPrefix(String prefix, TrieNode current) {
        if (words.size() >= 5 || current == null) {
            return;
        }
        if (current.isEndOfWord()) {
            words.add(prefix);
        }

        TrieNode node = null;
        for (char ch = 'A'; ch <= 'Z'; ++ch) {
            node = current.getChildren().get(ch);
            if (node == null) {
                continue;
            }
            String next = new String(prefix + ch);
//            System.out.println(next);
            getWordsForPrefix(next, node);
        }
        for (char ch = 'a'; ch <= 'z'; ++ch) {
            node = current.getChildren().get(ch);
            if (node == null) {
                continue;
            }
            String next = new String(prefix + ch);
//            System.out.println(next);
            getWordsForPrefix(next, node);
        }
        String remaning = "0123456789_";
        for (char ch : remaning.toCharArray()) {
            node = current.getChildren().get(ch);
            if (node == null) {
                continue;
            }
            String next = new String(prefix + ch);
//            System.out.println(next);
            getWordsForPrefix(next, node);
        }
    }
}
