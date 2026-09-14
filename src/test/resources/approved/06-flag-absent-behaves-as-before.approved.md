# 场景 06：未声明 requiredHeaders → 行为与改动前逐字节相同（回归保险）

这条是**回归保险夹具**：输入与场景 05 完全一样，唯独不声明 `requiredHeaders`。任何让未开启该特性的调用方付出代价的实现——比如无条件 trim 表头、无条件改写 header map——都会在这里显形。

注意表头名两侧的空格被原样保留：commons-csv 默认不 trim 表头，本切片也不改这一点。trim 只发生在**校验比对时**，不落到 header map 上。

## 输入

```csv
 order_id , amount , Currency
A-1001,120.50,CNY
```

## 配置

```properties
format = DEFAULT
header = FROM_FIRST_RECORD
```

<!-- 以下由 runner 生成，请勿手工编辑 -->

## 表头

```text
[ order_id ] | [ amount ] | [ Currency]
```

## 记录

```text
1: [ order_id ]=A-1001 | [ amount ]=120.50 | [ Currency]=CNY
```
