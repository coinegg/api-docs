# REST行情与交易接口

## 基本信息

#### 1. API请求域名：https://api.coinegg.vip

#### 2. 请求过程说明

-   构造请求数据，用户数据按照COINEGG提供的接口规则，通过程序生成签名和要传输给CoinEgg的数据集合；
-   发送请求数据，把构造完成的数据集合通过POST/GET提交的方式传递给CoinEgg；
-   CoinEgg对请求数据进行处理，服务器在接收到请求后，会首先进行安全校验，验证通过后便会处理该次发送过来的请求；
-   返回响应结果数据，CoinEgg把响应结果以JSON的格式反馈给用户，具体的响应格式，错误代码参见接口部分；
-    对获取的返回结果数据进行处理；

#### 3. 安全认证

-   所有的private API都需要经过认证

-   Api的申请可以到财务中心 -> API，申请得到私钥和公钥，私钥CoinEgg将不做储存，一旦丢失将无法找回

    >   注意:请勿向任何人泄露这两个参数，这像您的密码一样重要

#### 4. 签名机制

​	每次请求private api 都需要验证签名，发送的参数示例如下

```php
$param = [
	'amount' => 1,
	'price' => 10000,
	'type' => 'buy',
	'nonce' => 141377098123,
	'key' => '5zi7w-4mnes-swmc4-egg9b-f2iqw-396z4-g541b',
	'signature' => '459c69d25c496765191582d9611028b9974830e9dfafd762854669809290ed82'
];
```

1.  nonce 可以理解为一个递增的整数：http://zh.wikipedia.org/wiki/Nonce
2.  key 是申请到的公钥
3.  signature是签名，是将amount price type nonce key等各个请求参数以key=value的形式使用'&'符将各个参数拼接起来(字符串结尾处不可有'&'),通过md5(私钥)为key,拼接字符串为值的形式进行sha256算法加密得到的值
4. （重要）签名的字段顺序要与提交的字段顺序务必保持一致，例如：生成签名的字段顺序为['a'=>'a','b'=>'b','c'=>'c']，在提交请求的时间也应该保证参数的顺序为['a'=>'a','b'=>'b','c'=>'c']


## 公开API接口



### 1. 聚合行情（Ticker）

**链接**

```
GET /api/v1/ticker/region/{region}?coin={coin}
```

**请求参数**

| 参数   | 示例 | 说明                                     |
| ------ | ---- | ---------------------------------------- |
| region | btc  | 交易区，可选参数：btc / usdt / eth / usc |
| coin   | xrp  | 交易币种                                 |

**返回参数**

| 参数 | 说明           |
| ---- | -------------- |
| high | 最高价         |
| low  | 最低价         |
| buy  | 买一价         |
| sell | 卖一价         |
| last | 最近一次成交价 |
| vol  | 成交量         |

**返回示例**

```json
{
    "high":22,
    "low":20,
    "buy":1.879,
    "sell":0,
    "last":38800,
    "vol":283.954
}
```



### 2. 市场深度（Depth）

**说明**

-   返回所有的市场深度，此回应的数据量会较大，所以请勿频繁调用。

**链接**

```
GET /api/v1/depth/region/{region}?coin={coin}
```

**请求参数**

| 参数   | 示例 | 说明                                     |
| ------ | ---- | ---------------------------------------- |
| region | btc  | 交易区，可选参数：btc / usdt / eth / usc |
| coin   | xrp  | 交易币种                                 |

**返回参数**

| 参数 | 说明                                   |
| ---- | -------------------------------------- |
| asks | 委买单[价格, 委单量]，价格从高到低排序 |
| bids | 委卖单[价格, 委单量]，价格从高到低排序 |

**返回示例**

```json
{
    "asks":[
        [
            70000,
            5
        ],
        [
            69000,
            0.48
        ]
    ],
    "bids":[
        [
            38300,
            1.879
        ],
        [
            38100,
            1.0078
        ]
    ]
}
```



### 3. 交易记录（Trade）

**说明**

-   返回100个最近的市场交易，按时间倒序排列，此回应的数据量会较大，所以请勿频繁调用。

**链接**

```
GET /api/v1/orders/region/{region}?coin={coin}
```

**请求参数**

| 参数   | 示例 | 说明                                     |
| ------ | ---- | ---------------------------------------- |
| region | btc  | 交易区，可选参数：btc / usdt / eth / usc |
| coin   | xrp  | 交易币种                                 |

**返回参数**

| 参数   | 说明     |
| ------ | -------- |
| date   | 时间戳   |
| price  | 交易价格 |
| amount | 交易数量 |
| tid    | 交易ID   |
| type   | 交易类型 |

**返回示例**

```json
[
    {
        "date":"0",
        "price":3,
        "amount":0.1,
        "tid":"1",
        "type":"buy"
    },
    {
        "date":"0",
        "price":32323,
        "amount":2,
        "tid":"2",
        "type":"sell"
    }
]
```



### 4. K线数据（Kline）

**说明**

-   返回规定时间段内的1分钟k线数据

**链接**

```
GET /api/v1/kline/region/{region}?coin={coin}
```

**请求参数**

| 参数   | 示例       | 说明                                            |
| ------ | ---------- | ----------------------------------------------- |
| region | btc        | 交易区，可选参数：btc / usdt / eth / usc        |
| coin   | xrp        | 交易币种                                        |
| since  | 1530374400 | 开始时间戳 返回的数据为since开始,一小时内的数据 |

**返回参数**

```json
[
    [
        "时间戳",
        "开",
        "高",
        "低",
        "收",
        "成交量"
    ]
]
```

**返回示例**

```json
[
    [
        "1530374400",
        "449.9361000000",
        "450.6376000000",
        "449.5974000000",
        "450.6376000000",
        "5.5848000000"
    ]
]
```

 

## 账户接口



### 1. 账户信息（Account Balance）

**链接**

```
POST /api/v1/balance/
```

**请求参数**

| 参数      | 说明    |
| --------- | ------- |
| key       | API key |
| signature | 签名    |
| nonce     | nonce   |

**返回参数**

| 参数        | 说明        |
| ----------- | ----------- |
| eth_balance | ETH总余额   |
| eth_lock    | ETH冻结余额 |

**返回示例**

```json
{
    "result":true,
    "data":{
        "uid":"1",
        "xas_balance":1,
        "xas_lock":0,
        "eth_balance":1,
        "eth_lock":0,
        "btc_balance":1,
        "btc_lock":0
    }
}
```

 

### 2. 查看账户当前挂单（Trade_list）

**说明**

-   您指定时间后的挂单，可以根据类型查询，比如查看正在挂单和全部挂单

**链接**

```
POST /api/v1/trade_list/region/{region}
```

**请求参数**

| 参数      | 示例       | 说明                                     |
| --------- | ---------- | ---------------------------------------- |
| key       |            | API key                                  |
| signature |            | 签名                                     |
| nonce     |            | nonce                                    |
| region    | btc        | 交易区，可选参数：btc / usdt / eth / usc |
| since     | 1530374400 | unix时间戳，如果为0返回所有的值          |
| coin      | xrp        | 交易币种                                 |
| type      | open       | 挂单类型（open:正在挂单 / all:所有挂单） |

**返回参数**

| 参数               | 说明         |
| ------------------ | ------------ |
| id                 | 挂单ID       |
| coin               | 交易币种     |
| datetime           | 挂单时间     |
| type               | 挂单类型     |
| price              | 价格         |
| amount_original    | 下单数量     |
| amount_outstanding | 当前剩余数量 |

**返回示例**

```json
{
    "result":true,
    "data":[
        {
            "id":"28",
            "datetime":"2016-10-26 14:47:54",
            "type":"sell",
            "price":0.000123,
            "amount_original":1213,
            "amount_outstanding":1213
        }
    ]
}
```



### 3. 查询订单详情

**链接**

```
POST /api/v1/trade_view/region/{region}
```

**请求参数**

| 参数      | 示例 | 说明                                     |
| --------- | ---- | ---------------------------------------- |
| key       |      | API key                                  |
| signature |      | 签名                                     |
| nonce     |      | nonce                                    |
| region    | btc  | 交易区，可选参数：btc / usdt / eth / usc |
| id        |      | 挂单ID                                   |
| coin      | xrp  | 交易币种                                 |

**返回参数**

| 参数               | 说明                                                         |
| ------------------ | ------------------------------------------------------------ |
| id                 | 挂单ID                                                       |
| datetime           | 挂单时间                                                     |
| type               | 挂单类型                                                     |
| price              | 价格                                                         |
| amount_original    | 下单数量                                                     |
| amount_outstanding | 当前剩余数量                                                 |
| status             | 状态：new(新挂单), open(开放交易), cancelled(撤消), closed(完全成交) |

**返回示例**

```json
{
    "result":true,
    "data":{
        "id":28,
        "datetime":"2016-10-26 14:47:54",
        "type":"sell",
        "price":0.000123,
        "amount_original":1213,
        "amount_outstanding":1213,
        "status":"open"
    }
}
```



### 4.撤销订单

**链接**

```
POST /api/v1/trade_cancel/region/{region}
```

**请求参数**

| 参数      | 示例 | 说明                                     |
| --------- | ---- | ---------------------------------------- |
| key       |      | API key                                  |
| signature |      | 签名                                     |
| nonce     |      | nonce                                    |
| region    | btc  | 交易区，可选参数：btc / usdt / eth / usc |
| id        |      | 挂单ID                                   |
| coin      | xrp  | 交易币种                                 |

**返回参数**

| 参数   | 说明                    |
| ------ | ----------------------- |
| id     | 挂单ID                  |
| result | true(成功), false(失败) |

**返回示例**

```json
{
    "result":true,
    "id":"11"
}
```



### 5.下单

**链接**

```
POST /api/v1/trade_add/region/{region}
```

**请求参数**

| 参数      | 示例 | 说明                                     |
| --------- | ---- | ---------------------------------------- |
| key       |      | API key                                  |
| signature |      | 签名                                     |
| nonce     |      | nonce                                    |
| region    | btc  | 交易区，可选参数：btc / usdt / eth / usc |
| amount    |      | 挂单ID                                   |
| coin      | xrp  | 交易币种                                 |
| price     |      | 购买价格                                 |
| type      |      | 挂单类型                                 |

**返回参数**

| 参数   | 说明                    |
| ------ | ----------------------- |
| id     | 挂单ID                  |
| result | true(成功), false(失败) |

**返回示例**

```json
{
    "result":true,
    "id":"11"
}
```



### 6.数据类型

| 字段      | 数据类型 |
| --------- | -------- |
| *_balance | float    |
| id        | int      |
| datetime  | datetime |
| since     | int      |
| type      | string   |
| price     | float    |
| amount    | float    |
| status    | string   |
| trade_id  | int      |
| fee       | float    |
| result    | bool     |
| message   | string   |
| address   | string   |

