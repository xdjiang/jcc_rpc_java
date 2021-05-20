//package com.jccdex.rpc.explorer;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import com.jccdex.rpc.data.BalanceData;
//
//import com.jccdex.rpc.token.TokenItem;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.Map;
//
///**
// * 浏览器实现类
// * @author xdjiang
// */
//public class JccExplorer implements IJccExplorer{
//    private ExplorerNode expNode;
//    private BlockConfig config;
//    private ObjectMapper mapper = new ObjectMapper();
//
//    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//    private SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    public JccExplorer(ArrayList<String> expNodes) {
//
//        expNode = new ExplorerNode(expNodes);
//        config = BlockConfig.getInstance();
//    }
//
//    /**
//     * 获取余额
//     * @param address 钱包地址
//     * @return  BalanceResponse
//     * @throws Exception
//     */
//    @Override
//    public BalanceResponse requestBalance(String address) throws Exception {
//        String node = expNode.getUrls();
//        String uuid = address;
//        String url = node + "/wallet/balance/" + uuid + "?w=" + address;
//        String res = OkhttpUtil.get(url);
//        JsonNode jsonNode = mapper.readTree(res);
//        String successCode = "0";
//        BalanceResponse response = new BalanceResponse();
//        String code = jsonNode.get("code").asText();
//        String msg = jsonNode.get("msg").asText();
//        response.setCode(code);
//        response.setMsg(msg);
//
//        if (successCode.equals(jsonNode.get("code").asText())) {
//            JsonNode data = jsonNode.get("data");
//            if (data.isObject()) {
//                Iterator<Map.Entry<String, JsonNode>> it = data.fields();
//
//                BalanceData balanceData = new BalanceData();
//                ArrayList<TokenItem> balance = new ArrayList<TokenItem>();
//                while (it.hasNext()) {
//                    Map.Entry<String, JsonNode> entry = it.next();
//                    String key = entry.getKey();
//
//                    if ("_id".equals(key)) {
//                        balanceData.setAddress(entry.getValue().asText());
//                    } else if ("feeflag".equals(key)) {
//                        balanceData.setFeeflag(entry.getValue().asInt());
//                    } else {
//                        String[] keys = key.split("_");
//                        if(keys.length == 1) {
//                            TokenItem item = new TokenItem(keys[0], "", entry.getValue().get("value").asText(),
//                                    entry.getValue().get("frozen").asText());
//                            balance.add(item);
//                        } else if(keys.length == 2) {
//                            TokenItem item = new TokenItem(keys[0], keys[1], entry.getValue().get("value").asText(),
//                                    entry.getValue().get("frozen").asText());
//                            balance.add(item);
//                        }
//
//                    }
//                }
//                balanceData.setList(balance);
//                response.setData(balanceData);
//            }
//        } else {
//            response.setData(null);
//        }
//
//        return response;
//    }
//}
