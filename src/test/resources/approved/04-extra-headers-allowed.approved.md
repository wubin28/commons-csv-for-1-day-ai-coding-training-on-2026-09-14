# 场景 04：文件含额外列 → 不报错

本切片只管"必需的在不在"，不管"多出来的该不该在"。文件多出 `memo` 列，校验放行，记录照常产出。（`allowUnknownHeaders` 属于后续切片，见 spec 非目标。）

## 输入

```csv
order_id,amount,currency,memo
A-1001,120.50,CNY,手工补录
```

## 配置

```properties
format = DEFAULT
header = FROM_FIRST_RECORD
requiredHeaders = order_id, currency
```

<!-- 以下由 runner 生成，请勿手工编辑 -->

## 表头

```text
[order_id] | [amount] | [currency] | [memo]
```

## 记录

```text
1: [order_id]=A-1001 | [amount]=120.50 | [currency]=CNY | [memo]=手工补录
```
