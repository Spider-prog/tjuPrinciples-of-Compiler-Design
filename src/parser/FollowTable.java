package parser;

import java.util.ArrayList;
import java.util.HashMap;

public class FollowTable {
    static void setFollow(ArrayList<Formula> formulas, ArrayList<String> terminals,
                          ArrayList<String> nonTerminals, HashMap<String, ArrayList<String>> firsts,
                          HashMap<String, ArrayList<String>> follows) {
        ArrayList<String> follow;
        for (String nonTerminal : nonTerminals) {
            follow = new ArrayList<String>();
            follows.put(nonTerminal, follow);
        }
        // 将#加入到follow(S)中
        follows.get("program").add("#");

        boolean flag;
        boolean fab;
        while (true) {
            flag = true;
            for (Formula formula : formulas) {
                String left;
                String right;
                String[] rights;
                rights = formula.returnRights();
                for (int j = 0; j < rights.length; j++) {
                    right = rights[j];
                    if (nonTerminals.contains(right)) {
                        fab = true;
                        for (int k = j + 1; k < rights.length; k++) {

                            // 查找first集
                            for (int v = 0; v < firsts.get(rights[k]).size(); v++) {
                                if (follows.get(right).contains(firsts.get(rights[k]).get(v))) {
                                    continue;
                                } else {
                                    follows.get(right).add(firsts.get(rights[k]).get(v));
                                    flag = false;
                                }
                            }
                            if (isCanBeNull(formulas, rights[k])) {
                                continue;
                            } else {
                                fab = false;
                                break;
                            }
                        }
                        if (fab) {
                            left = formula.returnLeft();
                            for (int p = 0; p < follows.get(left).size(); p++) {
                                if (follows.get(right).contains(follows.get(left).get(p))) {
                                    continue;
                                } else {
                                    follows.get(right).add(follows.get(left).get(p));
                                    flag = false;
                                }
                            }
                        }
                    }

                }
            }
            if(flag){
                break;
            }
        }

        // 清除follow集中的#
        String left;
        for (String nonterminal : nonTerminals) {
            left = nonterminal;
            for (int v = 0; v < follows.get(left).size(); v++) {
                if (follows.get(left).get(v).equals("#"))
                    follows.get(left).remove(v);
            }
        }

        // -------------------
        // 为Follow加上#
        for (String notTerminal : nonTerminals) {
            follows.get(notTerminal).add("#");
        }
        // -------------------
    }

    // 判断是否产生$
    static boolean isCanBeNull(ArrayList<Formula> formulas, String symbol){
        String[] rights;
        for (Formula formula : formulas) {
            if (formula.returnLeft().equals(symbol)) {
                rights = formula.returnRights();
                if (rights[0].equals("$")) {
                    return true;
                }
            }
        }
        return false;
    }
}
