# 场景 05：表头两侧空白被忽略，大小写不一致算缺失

上游导出工具在分隔符两侧留了空格，并把 `currency` 写成了 `Currency`。

裁决两条：**空白不算差异**（trim 后比对，`order_id` 与 `amount` 判定为存在）；**大小写算差异**（`Currency` 不满足 `currency`，计为缺失）。

## 输入

```csv
 order_id , amount , Currency
A-1001,120.50,CNY
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
