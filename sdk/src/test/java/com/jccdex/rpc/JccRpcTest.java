package com.jccdex.rpc;

import com.jccdex.rpc.data.BalanceData;
import com.jccdex.rpc.token.TokenItem;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;

public class JingTumTest extends TestCase {

    JingTum jt;
    public void setUp() throws Exception {
        super.setUp();
        ArrayList<String> rpcNodes = new ArrayList<String>();
        rpcNodes.add("https://srje115qd43qw2.swtc.top");
        rpcNodes.add("https://srje071qdew231.swtc.top");

        ArrayList<String> expNodes = new ArrayList<String>();

        expNodes.add("https://expji39bdbdba1e1.swtc.top");
        expNodes.add("https://expjma3a3da190b6.swtc.top");

        jt = new JingTum(100,"SWTC","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or", rpcNodes, expNodes);
    }

    @Test
    public void testRequestBalance() {
        System.out.println("in testRequestBalance");
        String[] keys = "JPG_jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or".split("_");
        System.out.println(keys.length);
        try {
            BalanceData bd = jt.requestBalance("jwmrVAAzT8cks3VjUJoWvKci5AUH7kEGYY");
            ArrayList<TokenItem> list = bd.getList();
            Iterator it = list.iterator();
             while(it.hasNext()) {
                 TokenItem _item =  (TokenItem) it.next();
                 System.out.println(_item.getName());
                 System.out.println(_item.getValue());
                 System.out.println(_item.getFrozen());
                 System.out.println("----------------------");
             }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    public void tearDown() throws Exception {
    }
}