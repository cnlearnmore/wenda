package com.syj.wenda.service;

import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);
    private static final String DEFAULT_REPLACEMENT = "敏感词";

    @Override
    public void afterPropertiesSet() throws Exception {
        root = new TrieNode();
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                lineTxt = lineTxt.trim();
                addWord(lineTxt);
            }
            read.close();
        } catch (Exception e) {
            logger.error("读取敏感词文件失败" + e.getMessage());
        }
    }

    private class TrieNode {
        private boolean end = false;
        private Map<Character, TrieNode> subNodes = new HashMap<Character, TrieNode>();

        void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }

        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        boolean isKeywordEnd() {
            return end;
        }

        void setKeywordEnd(boolean end) {
            this.end = end;
        }

        public int getSubNodeCount() {
            return subNodes.size();
        }

    }

    private TrieNode root = new TrieNode();

    private boolean isSymbol(char c) {
        int ic = (int) c;
        return CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    public String filter(String text) {
        if (text == null || text.length() == 0) {
            return text;
        }
        String replacement = DEFAULT_REPLACEMENT;
        StringBuilder result = new StringBuilder();

        TrieNode tempNode = root;
        int begin = 0;
        int position = 0;

        while (position < text.length()) {
            char c = text.charAt(position);
//            System.out.println("currentJudge:" + c);    //这句测试语句可以清晰看到有些字会经过多次判断。
            if (isSymbol(c)) {
                //如果遇到特殊的字符，比如空格或者颜文字
                if (tempNode == root) {
                    //如果在判断第一个的时候就是特殊字符,begin指针直接后移
                    result.append(c);
                    ++begin;
                }
                //不管是与不是第一个都要讲position加1，联系指针后移的图解考虑。
                ++position;
                continue;
            }

            tempNode = tempNode.getSubNode(c);

            if (tempNode == null) {
//                如果没有匹配到这个字符，说明当前字符不是敏感字符，所以移动begin指针到后面
                result.append(text.charAt(begin));  //此处特别重要，追加的是begin,而不是目前position的位置
                position = begin + 1;
                begin = position;
                tempNode = root;
            } else if (tempNode.isKeywordEnd()) {
//                如果匹配成功，替换并移动begin指针
                result.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = root;
            } else {
//                如果不是另外两种情况。比如，这一个是敏感字符，但是没到KeywordEnd
                ++position;
            }
        }
//        将最后一次的加进去
        result.append(text.substring(begin));
        return result.toString();
    }

    private void addWord(String lineTxt) {
        TrieNode tempNode = root;
        for (int i = 0; i < lineTxt.length(); ++i) {
            Character c = lineTxt.charAt(i);
            if (isSymbol(c)) {
                continue;
            }
            TrieNode node = tempNode.getSubNode(c);
            if (node == null) {
                //初始化
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }
            tempNode = node;
            if (i == lineTxt.length() - 1) {
                tempNode.end = true;
            }
        }
    }

//    public static void main(String[] args) throws Exception {
//        SensitiveService s = new SensitiveService();
//        s.addWord("色情");
//        s.addWord("赌博");
//
//        System.out.println(s.filter("大家一起来赌 博测试， 这是小明提出的测试问题"));
//    }

}
