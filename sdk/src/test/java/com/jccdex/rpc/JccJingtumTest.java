package com.jccdex.rpc;

import com.jccdex.rpc.data.BalanceData;
import com.jccdex.rpc.token.TokenItem;
import com.jccdex.rpc.JccRpc;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;

public class JccRpcTest extends TestCase {

    JccRpc jccRpc;
    public void setUp() throws Exception {
        super.setUp();
        ArrayList<String> rpcNodes = new ArrayList<String>();
        rpcNodes.add("http://47.119.132.78:5050");
        rpcNodes.add("http://47.119.132.78:5050");

        ArrayList<String> expNodes = new ArrayList<String>();

        expNodes.add("https://expji39bdbdba1e1.swtc.top");
        expNodes.add("https://expjma3a3da190b6.swtc.top");

        jccRpc = new JccRpc(100,"SWT","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or", rpcNodes);
    }

//    @Test
//    public void testSequence() {
//
//        System.out.println("in testSequence");
//        try {
//            String sequence = jccRpc.sequence("jwmrVAAzT8cks3VjUJoWvKci5AUH7kEGYY");
//            System.out.println(sequence);
//
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//
//    }

//    @Test
//    public void testPayment() {
//
//        System.out.println("in testPayment");
//        try {
//            String ret = jccRpc.fastPayment("snEETifWAcQcKANPJ1fzoTbqWznpV","jUkqDXufgPLF2nxQKdu7gGnvXvoKHLNu6x","SWT","1","test");
//            System.out.println(ret);
//
//
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//
//    }

//    @Test
//    public void testSafeCreateTx() {
//
//        System.out.println("in testCreateTx");
//        try {
//            String ret = jccRpc.safeCreateOrder("snEETifWAcQcKANPJ1fzoTbqWznpV","SWT","1","CNY","1","sss");
//            System.out.println(ret);
//
//
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//
//    }

//    @Test
//    public void testFastCreateTx() {
//
//        System.out.println("in testCreateTx");
//        try {
//            String ret = jccRpc.fastCreateOrder("snEETifWAcQcKANPJ1fzoTbqWznpV","SWT","1","CNY","1","sss");
//            System.out.println(ret);
//
//
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//
//    }

    @Test
    public void testCancleOrder() {

        System.out.println("in testCancleOrder");
        try {
            String ret = jccRpc.cancleOrder("spowZSeGVf1tgrde7YVLxAMrky1cw","2345");
//            System.out.println(ret);


        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

//    @Test
//    public void testRequestTX() {
//
//        System.out.println("in testRequestTX");
//        try {
//            String res = jccRpc.requestTx("D01366D59E9678BBFCEFBF4BC9F7778ED3DE219BF0E342D045B6CC35395FB613");
//            System.out.println(res);
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//
//    }

//    @Test
//    public void testGetMemoData() {
//
//        System.out.println("in testGetMemoData");
//        try {
//            String memo = jccRpc.getMemoData("7B22656E645F74696D65223A312E36323134313138394531322C22706C616E223A2241222C22746F74616C5F616D6F756E74223A2230222C2273746172745F74696D65223A313632313431313032373435352C22757365725F77616C6C65745F61646472657373223A226A4D674B34427A36774E6E626638537775687A52326451454C324A6A7A38574D6233227D");
//            System.out.println(memo);
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//
//    }

//    @Test
//    public void testRequestBalance() {
//        System.out.println("in testRequestBalance");
//        String[] keys = "JPG_jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or".split("_");
//        System.out.println(keys.length);
//        try {
//            BalanceData bd = jt.requestBalance("jwmrVAAzT8cks3VjUJoWvKci5AUH7kEGYY");
//            ArrayList<TokenItem> list = bd.getList();
//            Iterator it = list.iterator();
//             while(it.hasNext()) {
//                 TokenItem _item =  (TokenItem) it.next();
//                 System.out.println(_item.getName());
//                 System.out.println(_item.getValue());
//                 System.out.println(_item.getFrozen());
//                 System.out.println("----------------------");
//             }
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//
//    }

    public void tearDown() throws Exception {
    }
}