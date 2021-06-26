<!-- markdownlint-disable MD024 -->

# jcc_rpc_java(v3.1.0)

jcc rpc java version

[![Build Status](https://travis-ci.com/JCCDex/jcc_rpc_java.svg?branch=master)](https://travis-ci.com/JCCDex/jcc_rpc_java)
[![Coverage Status](https://coveralls.io/repos/github/JCCDex/jcc_rpc_java/badge.svg?branch=master)](https://coveralls.io/github/JCCDex/jcc_rpc_java?branch=master)
[![JitPack](https://jitpack.io/v/JCCDex/jcc_rpc_java.svg)](https://jitpack.io/#JCCDex/jcc_rpc_java)

## 引用依赖包
```maven
<dependency>
    <groupId>io.github.jccdex</groupId>
    <artifactId>JccWallet</artifactId>
    <version>1.0.0</version>
</dependency>
<dependency>
    <groupId>io.github.jccdex</groupId>
    <artifactId>JccRPC</artifactId>
    <version>3.1.0</version>
</dependency>
```

## 接口描述
[接口文档](https://github.com/xdjiang/jcc_rpc_java/blob/3.1.0/doc/index.html)
### 构造函数
```java
/**
* @param rpcNodes rpc节点服务器地址列表
*/
public JccJingtum(ArrayList<String> rpcNodes)

/**
* 井通公链、联盟链RPC服务构造函数
* @param fee 每笔交易燃料费(燃料费计算公式=fee/1000000)
* @param baseToken 交易燃料手续费通证,也是公链的本币
* @param issuer 银关地址
* @param rpcNodes rpc节点服务器地址列表
*/
public JccJingtum(Integer fee, String baseToken, String issuer, ArrayList<String> rpcNodes)

/**
 * 井通公链、联盟链RPC服务构造函数
 * @param alphabet 字母表，每一条联盟链都可以用不同的或者相同alphabet
 * @param fee 每笔交易燃料费(燃料费计算公式=fee/1000000)
 * @param baseToken 交易燃料手续费通证,也是公链的本币
 * @param issuer 银关地址
 * @param rpcNodes rpc节点服务器地址列表
 */
public JccJingtum(String alphabet, Integer fee, String baseToken, String issuer, ArrayList<String> rpcNodes)

/**
* 井通公链、联盟链RPC服务构造函数
* @param alphabet 字母表，每一条联盟链都可以用不同的或者相同alphabet
* @param fee 交易手续费(燃料费计算公式=fee/1000000)
* @param baseToken 交易燃料手续费通证,也是公链的本币
* @param issuer 银关地址
* @param platform 交易平台账号
* @param rpcNodes rpc节点服务器地址列表
*/
public JccJingtum(String alphabet, Integer fee, String baseToken, String issuer, String platform, ArrayList<String> rpcNodes)
```
### 设置每笔交易燃料费

```java
/**
* 设置每笔交易燃料费
* @param fee (燃料费计算公式=fee/1000000)
*/
public void setFee(Integer fee) throws  Exception
```

### 获取每笔交易燃料费
```java
/**
* 获取每笔交易燃料费
* @return 每笔交易燃料费
*/
public Integer getFee()
```

### 设置交易平台账号
```java
/**
* 设置交易平台账号
* @param platform 交易平台账号
*/
public void setPlatform(String platform) throws  Exception
```

### 获取交易平台账号
```java
/**
* 获取交易平台账号
* @return 交易平台账号
*/
public String getPlatform()
```



### 创建钱包(账号)

```java
/**
* 创建钱包(账号)
* @return 钱包字符串,json格式 ({"secret":****,"address":****})
* @throws Exception 抛出异常
*/
public String createWallet()  throws Exception
```

### 通过钱包密钥获取钱包地址
```java
/**
* 通过钱包密钥获取钱包地址
* @param secret 钱包密钥
* @return 钱包地址
* @throws Exception 抛出异常
*/
public String getWalletAddress(String secret) throws  Exception
```

### 设置出错尝试次数
```java
/**
* 设置出错尝试次数
* @param tryTimes 次数
*/
public void setTryTimes(int tryTimes)
```
### 根据hash获取交易详情
```java
/**
* 根据hash获取交易详情
* @param hash 交易hash
* @return 交易详情 json格式
* @throws Exception 抛出异常
*/
public String requestTx(String hash) throws Exception
```

### 16进制备注内容直接转换成为字符串(无需Unicode解码)
```java
/**
* 16进制备注内容直接转换成为字符串(无需Unicode解码)
* @param hexStrMemData 16进制备注内容
* @return 备注内容
* @throws Exception 抛出异常
*/
public String getMemoData(String hexStrMemData) throws Exception
```

### 转账并校验

```java
/**
*  转账并校验，每笔交易都会校验是否成功，适合普通转账，优点：每笔交易都进行确认，缺点：转账效率低下
* @param secret 发送者钱包密钥
* @param receiver 接收者钱包地址
* @param pToken 转账Token
* @param pAmount 转账数量
* @param memos  转账备注
* @return 交易详情 json格式
* @throws Exception 抛出异常
*/
public String paymentWithCheck(String secret, String receiver, String pToken, String pAmount, String memos) throws Exception
```

### 快速转账(转账不校验)
```java
/**
*  快速转账，每笔交易不校验是否成功，适用于批量转账，优点：转账效率高，缺点：交易成功率无法保证，需要调用者自己进行校验
* @param secret 发送者钱包密钥
* @param receiver 接收者钱包地址
* @param pToken 转账Token
* @param pAmount 转账数量
* @param memos  转账备注
* @return 交易详情 json格式
* @throws Exception 抛出异常
*/
public String paymentNoCheck(String secret, String receiver, String pToken, String pAmount, String memos) throws Exception
```

### 挂单并校验
```java
/**
* 挂单并校验，每笔挂单都会校验是否成功，适合普通调用，优点：每笔交易都进行确认，缺点：效率低下
* @param secret 挂单方钱包密钥
* @param pPayToke  挂单方支付的Token名称
* @param pPayAmount 挂单方支付的Token数量
* @param pGetToken  挂单方期望得到的Token名称
* @param pGetAmount 挂单方期望得到的Token数量
* @param memos 备注
* @return 交易详情 json格式
*/
public String createOrderWithCheck(String secret, String pPayToke, String pPayAmount, String pGetToken, String pGetAmount, String memos) throws Exception
```

### 快速挂单(挂单不校验)
```java
/**
* 快速挂单，每笔交易不校验是否成功，适用于批量挂单，优点：挂单效率高，缺点：交易成功率无法保证，需要调用者自己进行校验
* @param secret 挂单方钱包密钥
* @param pPayToke  挂单方支付的Token名称
* @param pPayAmount 挂单方支付的Token数量
* @param pGetToken  挂单方期望得到的Token名称
* @param pGetAmount 挂单方期望得到的Token数量
* @param memos 备注
* @return 交易详情 json格式
*/
public String createOrderNoCheck(String secret, String pPayToke, String pPayAmount, String pGetToken, String pGetAmount, String memos) throws Exception
```

### 取消挂单
```java
/**
* 取消挂单
* @param secret 钱包密钥
* @param pSequence 挂单序列号
* @return 交易详情 json格式
*/
public String cancleOrder(String secret, String pSequence) throws Exception
```

### 向节点发送交易请求并确认
```java
/**
* 向节点发送交易请求，并且根据签名得到的hash进行交易确认
* @param txBlob 交易信息
* @param hash hash
* @return 交易信息
* @throws Exception 抛出异常
*/
public String submit(String txBlob, String hash) throws Exception
```

### 向节点发送交易不确认
```java
/**
* 向节点发送交易请求
* @param txBlob 交易信息
* @return 交易信息
* @throws Exception 抛出异常
*/
public String submit(String txBlob) throws Exception
```

## 调用示例
```java
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jccdex.rpc.JccJingtum;
import com.jccdex.rpc.http.OkhttpUtil;
import java.util.ArrayList;

JccJingtum jccJingtum = null;
try {
    String serverConfigHost = "https://gateway.swtc.top/rpcservice";
    String res = OkhttpUtil.get(serverConfigHost);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode obj = mapper.readTree(res);
    String list = obj.get("rpcpeers").toString();
    ArrayList<String> rpcNodes = mapper.readValue(list, new TypeReference<ArrayList<String>>() {
    });
    jccJingtum = new JccJingtum(100,"SWT","jGa9J9TkqtBcUoHe2zqhVFFbgUVED6o9or", rpcNodes);
    //创建钱包
    String ret = jccJingtum.createWallet();
    System.out.println(ret);
    
    //转账并校验
    ret = jccJingtum.paymentWithCheck("****","****","SWT","1","test");
    System.out.println(ret);
    
    //转账不校验
    ret = jccJingtum.paymentNoCheck("****","****","SWT","1","test");
    System.out.println(ret);
    
    //挂单并校验
    ret = jccJingtum.createOrderWithCheck("****","SWT","1","CNY","1","test");
    System.out.println(ret);
    
     //挂单不校验
    ret = jccJingtum.createOrderNoCheck("****","SWT","1","CNY","1","test");
    System.out.println(ret);
    
    //撤销挂单
    ret = jccJingtum.cancleOrder("****","****");
    System.out.println(ret);

    //获取交易详情
    ret = jccJingtum.requestTx("****");
    System.out.println(ret);
    
} catch (Exception e) {
    e.printStackTrace();
}
```

