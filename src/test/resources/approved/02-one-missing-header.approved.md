# 场景 02：缺失一列 → 解析初始化即抛错，报出缺失列名

上游漏发了 `currency` 列。校验在解析初始化阶段就报错，一条记录都不产出——不产出半截结果。

## 输入

```csv
order_id,amount
A-1001,120.50
```

## 配置

```properties
format = DEFAULT
header = FROM_FIRST_RECORD
requiredHeaders = order_id, amount, currency
```

<!-- 以下由 runner 生成，请勿手工编辑 -->

## 异常

```text
MissingRequiredHeadersException: Missing required header(s): currency
```
