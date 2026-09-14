# 场景 01：必需列全部存在 → 正常解析

上游文件包含声明的全部三列。校验通过，解析行为与未开启校验时完全一致。

## 输入

```csv
order_id,amount,currency
A-1001,120.50,CNY
A-1002,88.00,USD
```

## 配置

```properties
format = DEFAULT
header = FROM_FIRST_RECORD
requiredHeaders = order_id, amount, currency
```

<!-- 以下由 runner 生成，请勿手工编辑 -->

## 表头

```text
[order_id] | [amount] | [currency]
```

## 记录

```text
1: [order_id]=A-1001 | [amount]=120.50 | [currency]=CNY
2: [order_id]=A-1002 | [amount]=88.00 | [currency]=USD
```
