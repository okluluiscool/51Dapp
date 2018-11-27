package store.dapp.bean;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: liulu3
 * @Date: 2018/11/9 14:47
 * @Description: 暂时用于存Dapp对应的合约，后续改为从ES中查询
 */
public class AccountBean {
    private static Map<String, Set<String>> accountMap = new HashMap<>();
    private static Set<String> betDiceSet = new HashSet<>();
    private static Set<String> eosFunSet = new HashSet<>();
    private static Set<String> endlessSet = new HashSet<>();
    private static Set<String> _1DiceSet = new HashSet<>();
    private static Set<String> eos_knights = new HashSet<>();

    static {
//        betDiceSet.add("betdicegiver");
//        betDiceSet.add("betdicelucky");
//        betDiceSet.add("betdicesales");
//        betDiceSet.add("betdiceadmin");
//        betDiceSet.add("betdicetoken");
//        accountMap.put("BetDice", betDiceSet);
//
//        eosFunSet.add("eosfuneos111");
//        accountMap.put("EOSFUN.WIN", eosFunSet);

        endlessSet.add("endlessdivdn");
        endlessSet.add("lottologger1");
        endlessSet.add("endlesslotto");
        endlessSet.add("endlessbank1");
        endlessSet.add("endlessdicex");
        endlessSet.add("endlesstoken");
        endlessSet.add("endlesslogs1");
        endlessSet.add("endlesshouse");
        accountMap.put("Endless Dice", endlessSet);

//        _1DiceSet.add("onedicewarm1");
//        accountMap.put("1DICE", _1DiceSet);

        eos_knights.add("eosknightsio");
        accountMap.put("EosKnights", eos_knights);

    }

    public static Map<String, Set<String>> getAccountMap(){
        return accountMap;
    }

    public static void main(String[] args) {
        System.out.println(getAccountMap());
    }
}
