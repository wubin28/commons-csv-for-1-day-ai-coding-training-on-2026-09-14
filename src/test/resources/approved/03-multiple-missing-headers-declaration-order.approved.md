# 场景 03：缺失多列 → 按声明顺序列出，而非文件顺序

声明顺序是 `currency, amount, order_id`，文件里只有 `order_id`。错误信息按**声明顺序**报 `currency, amount`，这样调用方对照自己的 schema 定义逐条排查即可。

## 输入

```csv
order_id
A-1001
```

## 配置

```properties
format = DEFAULT
header = FROM_FIRST_RECORD
requiredHeaders = currency, amount, order_id
```

<!-- 以下由 runner 生成，请勿手工编辑 -->

## 异常

```text
MissingRequiredHeadersException: Missing required header(s): currency, amount
```
