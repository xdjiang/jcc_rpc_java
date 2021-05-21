package com.jccdex.rpc;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jccdex.core.client.Wallet;
import com.jccdex.rpc.config.Config;
import com.jccdex.rpc.config.RpcNode;
import com.jccdex.rpc.core.coretypes.AccountID;
import com.jccdex.rpc.core.coretypes.Amount;
import com.jccdex.rpc.core.coretypes.Currency;
import com.jccdex.rpc.core.coretypes.hash.Hash256;
import com.jccdex.rpc.core.coretypes.uint.UInt32;
import com.jccdex.rpc.core.types.known.tx.signed.SignedTransaction;
import com.jccdex.rpc.core.types.known.tx.txns.OfferCreate;
import com.jccdex.rpc.core.types.known.tx.txns.Payment;
import com.jccdex.rpc.http.OkhttpUtil;
import com.jccdex.rpc.utils.CheckUtils;
import com.jccdex.rpc.utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JccRpc {
    private RpcNode rpcNode;
    //请求次数
    private int tryTimes = 5;

    public JccRpc(ArrayList<String> _rpcNodes) {
        rpcNode = new RpcNode(_rpcNodes);
    }

    public JccRpc(String _alphabet, ArrayList<String> _rpcNodes) {
        Config.setAlphabet(_alphabet);
        rpcNode = new RpcNode(_rpcNodes);

    }

    public JccRpc(Integer _fee, String _baseToken, String _issuer, ArrayList<String> _rpcNodes) {
        Config.setFee(_fee);
        Config.setCurrency(_baseToken);
        Config.setIssuer(_issuer);
        rpcNode = new RpcNode(_rpcNodes);
    }


    public JccRpc(String _alphabet, Integer _fee, String _baseToken, String _issuer, ArrayList<String> _rpcNodes) {
        Config.setAlphabet(_alphabet);
        Config.setFee(_fee);
        Config.setCurrency(_baseToken);
        Config.setIssuer(_issuer);
        rpcNode = new RpcNode(_rpcNodes);
    }

    public void setTryTimes(int _tryTimes) {
        this.tryTimes = _tryTimes;
    }
    /**
     * 获取余额
     * @param _address 钱包地址
     * @return
     * @throws Exception
     */
    private String sequence(String _address) throws Exception {
        try {
            if(!Wallet.isValidAddress(_address)) {
                throw new Exception("钱包地址不合法");
            }
        } catch (Exception e) {
            throw e;
        }

        String sequence = "";
        int times = this.tryTimes;
        do {
            try {
                times--;
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode data = mapper.createObjectNode();
                ObjectNode object = mapper.createObjectNode();
                object.put("account", _address);
                ArrayList<ObjectNode> params = new ArrayList<>();
                params.add(object);
                ArrayNode array = mapper.valueToTree(params);
                data.put("method", "account_info");
                data.set("params", array);

                String url = rpcNode.getUrls();
                String res = OkhttpUtil.post(url, data.toString());
                String code = JSONObject.parseObject(res).getJSONObject("result").getString("status");
                if("success".equals(code)) {
                    sequence = JSONObject.parseObject(res).getJSONObject("result").getJSONObject("account_data").getString("Sequence");
                }

                if(!sequence.isEmpty()) {
                    break;
                }

                //延时500毫秒
                Thread.sleep(500);
                continue;
            } catch (Exception e) {
                continue;
            }
        } while(times > 0);

        if(sequence.isEmpty()) {
            throw new Exception("获取sequence失败");
        } else {
            return sequence;
        }
    }

    /**
     * 从指定的rpc节点服务器获取获取交易详情
     * @param hash 交易hash
     * @param rpcNode rpc节点服务器
     * @return
     */
    private String _requestTx(String hash, String rpcNode) throws Exception {
        String tx = "";

        int times = this.tryTimes;
        do{
            try {
                times--;
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode data = mapper.createObjectNode();
                ObjectNode object = mapper.createObjectNode();
                object.put("transaction", hash);
                object.put("binary", false);
                ArrayList<ObjectNode> params = new ArrayList();
                params.add(object);
                ArrayNode array = (ArrayNode) mapper.valueToTree(params);
                data.put("method", "tx");
                data.set("params", array);

                String res = OkhttpUtil.post(rpcNode, data.toString());
                String status = JSONObject.parseObject(res).getJSONObject("result").getString("status");
                Boolean validated = JSONObject.parseObject(res).getJSONObject("result").getBoolean("validated");
                if ("success".equals(status) && validated) {
                    tx = res;
                }

                if (!tx.isEmpty()) {
                    break;
                }

                //延时500毫秒
                Thread.sleep(1000);
                continue;
            }catch (Exception e) {
                continue;
            }

        } while(times > 0);

        if(tx.isEmpty()) {
            throw new Exception("获取交易信息失败");
        } else {
            return tx;
        }
    }

    /**
     * 根据hash获取交易详情
     * @param _hash 交易hash
     * @return
     */
    public String requestTx(String _hash) throws Exception {
        String tx = "";

        int times = this.tryTimes;
        do{
            try {
                times--;

                String url = rpcNode.getUrls();
                tx = this._requestTx(_hash,url);

                if (!tx.isEmpty()) {
                    break;
                }

                //延时500毫秒
                Thread.sleep(1000);
                continue;
            }catch (Exception e) {
                continue;
            }

        } while(times > 0);

        if(tx.isEmpty()) {
            throw new Exception("获取交易信息失败");
        } else {
            return tx;
        }
    }
    /**
     * 16进制备注内容直接转换成为字符串(无需Unicode解码)
     * @param _hexStrMemData
     * @return
     * @throws Exception
     */
    public String getMemoData(String _hexStrMemData) throws Exception{
        try {
            return Utils.hexStrToStr(_hexStrMemData);
        } catch (Exception e) {
            throw new Exception("获取备注内容失败");
        }
    }

    /**
     *  安全转账，每笔交易都会校验是否成功，适合普通转账，优点：每笔校交易都进行确认，缺点：转账效率底下
     * @param _secret 发送者钱包密钥
     * @param _receiver 接收者钱包地址
     * @param _token 转账Token
     * @param _amount 转账数量
     * @param _memos  转账辈子
     * @throws Exception
     */
    public String safePayment(String _secret, String _receiver, String _token, String _amount, String _memos) throws Exception {
        try {
            if(!Wallet.isValidSecret(_secret)) {
                throw new Exception("钱包密钥不合法");
            }
            if(!Wallet.isValidAddress(_receiver)) {
                throw new Exception("钱包地址不合法");
            }

            if(_token.isEmpty()) {
                throw new Exception("token名称不合法");
            }

            if(_amount.isEmpty()) {
                throw new Exception("token名称不合法");
            }

            Wallet wallet = Wallet.fromSecret(_secret);
            String sender = wallet.getAddress();
            String sequence = this.sequence(sender);

            ObjectMapper mapper = new ObjectMapper();
            String token = _token.toUpperCase();
            Amount amount;
            Payment payment = new Payment();
            payment.as(AccountID.Account, sender);
            payment.as(AccountID.Destination, _receiver);

            BigDecimal bigDecimal = new BigDecimal(_amount);
            if(bigDecimal.compareTo(new BigDecimal(0)) < 1){
                throw new Exception("token数量不能小于等于0");
            }

            if(Config.CURRENCY.equals(token)) {
                amount = new Amount(bigDecimal);
            } else {
                amount = new Amount(bigDecimal, Currency.fromString(token), AccountID.fromString(Config.ISSUER));
            }


            payment.as(Amount.Amount, amount);
            payment.as(Amount.Fee, String.valueOf(Config.FEE));
            payment.sequence(new UInt32(sequence));
            payment.flags(new UInt32(0));

            if (_memos.length() > 0) {
                ArrayList<String> _memoList = new ArrayList<>(1);
                _memoList.add(_memos);
                payment.addMemo(_memoList);
            }

            SignedTransaction _tx = payment.sign(_secret);
            String blob = _tx.tx_blob;
            String hash = _tx.hash.toHex();
            int times = this.tryTimes;
            String tx = "";

            ObjectNode data = mapper.createObjectNode();
            ObjectNode object = mapper.createObjectNode();
            object.put("tx_blob", blob);
            ArrayList<ObjectNode> params = new ArrayList<>();
            params.add(object);
            ArrayNode array = mapper.valueToTree(params);

            data.put("method", "submit");
            data.set("params", array);

            do{
                times--;
                String url = rpcNode.getUrls();
                String result = OkhttpUtil.post(url, data.toString());
                Thread.sleep(5000);
                try {
                    tx = this._requestTx(hash,url);
                    if(!tx.isEmpty()) {
                        break;
                    }
                }catch (Exception e) {
                    continue;
                }
                //延时500毫秒
                Thread.sleep(1000);
            }while(times > 0);

            if(tx.isEmpty()) {
                throw new Exception("转账失败");
            } else {
                return hash;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     *  快速转账，每笔交易不校验是否成功，适用于批量转账，优点：转账效率高，缺点：交易成功率无法保证，需要调用者自己进行校验
     * @param _secret 发送者钱包密钥
     * @param _receiver 接收者钱包地址
     * @param _token 转账Token
     * @param _amount 转账数量
     * @param _memos  转账辈子
     * @throws Exception
     */
    public String fastPayment(String _secret, String _receiver, String _token, String _amount, String _memos) throws Exception {
        try {
            if(!Wallet.isValidSecret(_secret)) {
                throw new Exception("钱包密钥不合法");
            }
            if(!Wallet.isValidAddress(_receiver)) {
                throw new Exception("钱包地址不合法");
            }
            if(_token.isEmpty()) {
                throw new Exception("token名称不合法");
            }

            if(_amount.isEmpty()) {
                throw new Exception("token名称不合法");
            }

            Wallet wallet = Wallet.fromSecret(_secret);
            String sender = wallet.getAddress();
            String sequence = this.sequence(sender);

            ObjectMapper mapper = new ObjectMapper();
            String token = _token.toUpperCase();
            Amount amount;
            Payment payment = new Payment();
            payment.as(AccountID.Account, sender);
            payment.as(AccountID.Destination, _receiver);

            BigDecimal bigDecimal = new BigDecimal(_amount);
            if(bigDecimal.compareTo(new BigDecimal(0)) < 1){
                throw new Exception("token数量不能小于等于0");
            }

            if(Config.CURRENCY.equals(token)) {
                amount = new Amount(bigDecimal);
            } else {
                amount = new Amount(bigDecimal, Currency.fromString(token), AccountID.fromString(Config.ISSUER));
            }


            payment.as(Amount.Amount, amount);
            payment.as(Amount.Fee, String.valueOf(Config.FEE));
            payment.sequence(new UInt32(sequence));
            payment.flags(new UInt32(0));

            if (_memos.length() > 0) {
                ArrayList<String> _memoList = new ArrayList<>(1);
                _memoList.add(_memos);
                payment.addMemo(_memoList);
            }

            SignedTransaction _tx = payment.sign(_secret);
            String blob = _tx.tx_blob;
            String hash = _tx.hash.toHex();

            ObjectNode data = mapper.createObjectNode();
            ObjectNode object = mapper.createObjectNode();
            object.put("tx_blob", blob);
            ArrayList<ObjectNode> params = new ArrayList<>();
            params.add(object);
            ArrayNode array = mapper.valueToTree(params);

            data.put("method", "submit");
            data.set("params", array);

                String url = rpcNode.getUrls();
                String result = OkhttpUtil.post(url, data.toString());
                String status = JSONObject.parseObject(result).getJSONObject("result").getString("engine_result");
                if("tesSUCCESS".equals(status)) {
                    return hash;
                } else {
                    throw new Exception("转账失败");
                }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 安全挂单，每笔挂单都会校验是否成功，适合普通调用，优点：每笔校交易都进行确认，缺点：效率底下
     * @param _secret 挂单方钱包密钥
     * @param _payToke  挂单方支付的Token名称
     * @param _payAmount 挂单方支付的Token数量
     * @param _getToken  挂单方期望得到的Token名称
     * @param _getAmount 挂单方期望得到的Token数量
     * @param _memos 备注
     * @return
     */
    public String safeCreateOrder(String _secret, String _payToke, String _payAmount, String _getToken, String _getAmount, String _memos) throws Exception{
        try {
            if(!Wallet.isValidSecret(_secret)) {
                throw new Exception("钱包密钥不合法");
            }

            if(_payToke.isEmpty() || _getToken.isEmpty()) {
                throw new Exception("token名称不合法");
            }

            if(_payAmount.isEmpty() || _getAmount.isEmpty()) {
                throw new Exception("token数量不合法");
            }

            Wallet wallet = Wallet.fromSecret(_secret);
            String address = wallet.getAddress();

            ObjectMapper mapper = new ObjectMapper();
            String payToken = _payToke.toUpperCase();
            String getToken = _getToken.toUpperCase();

            OfferCreate offerCreate = new OfferCreate();
            offerCreate.as(AccountID.Account, address);

            Amount payAmount;
            BigDecimal payBigDecimal = new BigDecimal(_payAmount);
            BigDecimal getBigDecimal = new BigDecimal(_getAmount);

            if(payBigDecimal.compareTo(new BigDecimal(0)) < 1){
                throw new Exception("token数量不能小于等于0");
            }

            if(getBigDecimal.compareTo(new BigDecimal(0)) < 1){
                throw new Exception("token数量不能小于等于0");
            }

            if(Config.CURRENCY.equals(payToken)) {
                payAmount = new Amount(payBigDecimal);
            } else {
                payAmount = new Amount(payBigDecimal, Currency.fromString(payToken), AccountID.fromString(Config.ISSUER));
            }

            Amount getAmount;

            if(Config.CURRENCY.equals(getToken)) {
                getAmount = new Amount(getBigDecimal);
            } else {
                getAmount = new Amount(getBigDecimal, Currency.fromString(getToken), AccountID.fromString(Config.ISSUER));
            }
            offerCreate.as(Amount.TakerPays, getAmount);
            offerCreate.as(Amount.TakerGets, payAmount);

            offerCreate.as(Amount.Fee, String.valueOf(Config.FEE));

            String sequence = this.sequence(address);
            offerCreate.sequence(new UInt32(sequence));

            if (_memos.length() > 0) {
                ArrayList<String> _memoList = new ArrayList<>(1);
                _memoList.add(_memos);
                offerCreate.addMemo(_memoList);
            }

            SignedTransaction _tx = offerCreate.sign(_secret);
            String blob = _tx.tx_blob;
            String hash = _tx.hash.toHex();
            int times = this.tryTimes;
            String tx = "";
            String result = "";

            ObjectNode data = mapper.createObjectNode();
            ObjectNode object = mapper.createObjectNode();
            object.put("tx_blob", blob);
            ArrayList<ObjectNode> params = new ArrayList<>();
            params.add(object);
            ArrayNode array = mapper.valueToTree(params);

            data.put("method", "submit");
            data.set("params", array);

            do{
                times--;
                String url = rpcNode.getUrls();
                result = OkhttpUtil.post(url, data.toString());

                Thread.sleep(5000);
                try {
                    tx = this._requestTx(hash,url);
                    if(!tx.isEmpty()) {
                        break;
                    }
                }catch (Exception e) {
                    continue;
                }
                //延时500毫秒
                Thread.sleep(1000);
            }while(times > 0);

            if(tx.isEmpty()) {
                throw new Exception("挂单失败");
            } else {
                return tx;
            }
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * 快速挂单，每笔交易不校验是否成功，适用于批量挂单，优点：挂单效率高，缺点：交易成功率无法保证，需要调用者自己进行校验
     * @param _secret 挂单方钱包密钥
     * @param _payToke  挂单方支付的Token名称
     * @param _payAmount 挂单方支付的Token数量
     * @param _getToken  挂单方期望得到的Token名称
     * @param _getAmount 挂单方期望得到的Token数量
     * @param _memos 备注
     * @return
     */
    public String fastCreateOrder(String _secret, String _payToke, String _payAmount, String _getToken, String _getAmount, String _memos) throws Exception{
        try {
            if(!Wallet.isValidSecret(_secret)) {
                throw new Exception("钱包密钥不合法");
            }

            if(_payToke.isEmpty() || _getToken.isEmpty()) {
                throw new Exception("token名称不合法");
            }

            if(_payAmount.isEmpty() || _getAmount.isEmpty()) {
                throw new Exception("token数量不合法");
            }

            Wallet wallet = Wallet.fromSecret(_secret);
            String address = wallet.getAddress();

            ObjectMapper mapper = new ObjectMapper();
            String payToken = _payToke.toUpperCase();
            String getToken = _getToken.toUpperCase();

            OfferCreate offerCreate = new OfferCreate();
            offerCreate.as(AccountID.Account, address);

            Amount payAmount;
            BigDecimal payBigDecimal = new BigDecimal(_payAmount);
            BigDecimal getBigDecimal = new BigDecimal(_getAmount);

            if(payBigDecimal.compareTo(new BigDecimal(0)) < 1){
                throw new Exception("token数量不能小于等于0");
            }

            if(getBigDecimal.compareTo(new BigDecimal(0)) < 1){
                throw new Exception("token数量不能小于等于0");
            }

            if(Config.CURRENCY.equals(payToken)) {
                payAmount = new Amount(payBigDecimal);
            } else {
                payAmount = new Amount(payBigDecimal, Currency.fromString(payToken), AccountID.fromString(Config.ISSUER));
            }

            Amount getAmount;

            if(Config.CURRENCY.equals(getToken)) {
                getAmount = new Amount(getBigDecimal);
            } else {
                getAmount = new Amount(getBigDecimal, Currency.fromString(getToken), AccountID.fromString(Config.ISSUER));
            }
            offerCreate.as(Amount.TakerPays, getAmount);
            offerCreate.as(Amount.TakerGets, payAmount);

            offerCreate.as(Amount.Fee, String.valueOf(Config.FEE));

            String sequence = this.sequence(address);
            offerCreate.sequence(new UInt32(sequence));

            if (_memos.length() > 0) {
                ArrayList<String> _memoList = new ArrayList<>(1);
                _memoList.add(_memos);
                offerCreate.addMemo(_memoList);
            }

            SignedTransaction _tx = offerCreate.sign(_secret);
            String blob = _tx.tx_blob;
            String hash = _tx.hash.toHex();
            int times = 1;
            String result = "";

            ObjectNode data = mapper.createObjectNode();
            ObjectNode object = mapper.createObjectNode();
            object.put("tx_blob", blob);
            ArrayList<ObjectNode> params = new ArrayList<>();
            params.add(object);
            ArrayNode array = mapper.valueToTree(params);

            data.put("method", "submit");
            data.set("params", array);

            do{
                times--;
                String url = rpcNode.getUrls();
                result = OkhttpUtil.post(url, data.toString());
                String status = JSONObject.parseObject(result).getJSONObject("result").getString("engine_result");
                if(!"tesSUCCESS".equals(status)) {
                    result = "";
                }
//                System.out.println(result);
                //延时500毫秒
                Thread.sleep(1000);
                continue;
            }while(times > 0);

            if(result.isEmpty()) {
                throw new Exception("挂单失败");
            } else {
                return result;
            }
        } catch (Exception e) {
            throw e;
        }

    }
}
