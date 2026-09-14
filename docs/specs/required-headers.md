# Spec: Strict Header Schema Validation Mode — `requiredHeaders` 规则

> 状态：已评审 · 切片 v1 · 2026-09-14
> 落点：`org.apache.commons.csv.CSVFormat.Builder` + `CSVParser.createHeaders()`
> 基线 commit：`66a83820`（`mvn test` → 924 passed, 11 skipped）

---

## 1. 问题陈述

调用方明确知道上游 CSV 应当包含哪些列时，今天的 commons-csv 不提供任何声明方式。缺列的后果被推迟到业务代码里：`CSVRecord.get("currency")` 抛 `IllegalArgumentException`，此时已经解析了半个文件，错误信息也不说明"这份文件从一开始就不合格"。

本切片让调用方在 `CSVFormat` 上声明必需列，在**解析初始化阶段**一次性判定。

## 2. 用户故事

> 作为对账文件的解析方，我希望在构造 `CSVParser` 时就确认上游文件包含 `order_id`、`amount`、`currency` 三列，缺任何一列立刻失败并告诉我缺的是哪几列，这样我不必等到下游清算逻辑才发现问题。

## 3. 八点裁决

| # | 决策点 | 裁决 |
| --- | --- | --- |
| 1 | **API 形态** | `CSVFormat.Builder.setRequiredHeaders(String... requiredHeaders)`，配套 `CSVFormat.getRequiredHeaders()` 返回防御性副本 |
| 2 | **默认值** | 空数组。未声明 = 不校验，行为与 v1 之前逐字节相同 |
| 3 | **校验时机** | `CSVParser.createHeaders()` 建完 header map 之后、构造函数返回之前。抛错时**不产出任何 `CSVRecord`** |
| 4 | **异常类型** | 新增 `MissingRequiredHeadersException extends CSVException`（后者已 `extends IOException`，`CSVParser` 构造函数本就声明 `throws IOException`，调用方签名不变） |
| 5 | **错误信息** | `Missing required header(s): a, b` —— 缺失列按**声明顺序**列出，不按文件顺序 |
| 6 | **空白处理** | 比对前对文件表头两侧做 `trim()`；声明值同样 trim。`" order_id "` 视为 `order_id` |
| 7 | **大小写** | 区分大小写。`Currency` 不满足 `currency` 的要求，计为缺失 |
| 8 | **额外列** | 允许。文件多出未声明的列不报错（`allowUnknownHeaders` 属于后续切片） |

## 4. 明确不做（非目标）

- `allowUnknownHeaders`（是否允许额外列）
- `enforceHeaderOrder`（列顺序是否必须一致）
- 与 `setIgnoreHeaderCase(true)` 的联动语义 —— 本切片下两者互不感知，留待 v2 裁决
- 列类型校验、行级业务规则
- `CSVPrinter` 侧的 schema 输出
- 多套 schema profile
- 声明值为空串 / null 的处理：跳过，不计入缺失

## 5. 验收标准（六条业务事实，对应六个 approved scenario）

| # | 事实 | 夹具 |
| --- | --- | --- |
| 01 | 必需列全部存在 → 正常解析出记录 | `01-all-required-headers-present` |
| 02 | 缺一列 → 初始化即抛错，报出该列名 | `02-one-missing-header` |
| 03 | 缺多列 → 按**声明顺序**列出 | `03-multiple-missing-headers-declaration-order` |
| 04 | 文件有额外列 → 不报错 | `04-extra-headers-allowed` |
| 05 | 表头带空格且大小写不一致 → 空格被忽略，大小写不一致计为缺失 | `05-case-mismatch-counts-as-missing` |
| 06 | **未声明 requiredHeaders → 行为与改动前完全相同**（回归保险） | `06-flag-absent-behaves-as-before` |

## 6. 影响面

- `CSVFormat`：新增字段，需同步 `Builder` 拷贝构造、`equals`、`hashCode`、`toString`
- `CSVParser.createHeaders()`：末尾追加一次校验
- 既有 924 个测试：不得有任何一个变红
