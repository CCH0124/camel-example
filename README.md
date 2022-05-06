## Routing messages
在 Camel 中做的最常見的事情之一是以不同的方式處理訊息，具體取決於訊息的內容。在 Camel 中可以稱為 "content-based routing"。

當需要根據訊息的某些屬性在 Camel 中對訊息執行某些操作時，會使用 `content-based routing`。它從`choice` 開始，相似 Java 中的 `if/else`。但在 Camel 中，其表示為 `when/otherwise`。

```java
from("anEndpoint")
  .choice()
  // do something....
  .when(someExpression).to("someEndpoint")
  .otherwise()
  // do something else...
  .to("anotherEndpoint")
  .end();
```

在 Camel 中，用於檢驗訊息的表達式稱為 `Predicate`。檢驗的內容可能包含 `body`、`header`或`message`。

表示該 `Predicate` 方式有很多種，最常見是以下
- Simple language
- XPath(適合 XML)

範例
```java
  @Override
  public void configure() throws Exception {
    // TODO Auto-generated method stub
    from("file:src/test/src-folder?fileName=simple.txt")
    .log(">>> ${body}")
      .choice()
        .when(simple("${body} == 'Hello, world!'")) // 檢查讀檔的內容是否是 Hello, world!
          .to("file:src/test/des-folder") // 如果是將其寫至 des-folder
        .otherwise()
          .to("file:src/test/trash"); // 如果是將其移寫至 trash
  }
```

Simple expression 

| Example expression | What it does|
|---|---|
|${body} contains 'eggs' |從 body 中檢查訊息是否含有 eggs|
|${body} starts with 'cheese' |從 body 中檢查訊息是否從 cheese 字串開始|
|${bodyAs('String')} contains 'eggs'|先將訊息轉換為 `String`，然後檢查字串是否含 eggs 字串|
|${header.EggType} =='scrambled'|檢查 Header 中 EggType 的值是否相等於 scrambled|
|${header.CupsOfCoffee} > 5|檢查 Header 中 CupsOfCoffee 的值是否大於 5|

不過 Simple 表達式，對於較複雜的操作，需要使用更進階的 Bean Language 來做到。

在上述範例中 `when` 裡面其實也是一個小 Route，因此條件符合時，會做 Route 的動作，範例中就是路由到 `des-folder` 目錄。當然這其中也可以堆疊多個 Camel DSL functions，下面是一個例子。

```java
  @Override
  public void configure() throws Exception {
    // TODO Auto-generated method stub
    from("file:src/test/src-folder?fileName=simple2.txt")
    .log(">>> ${body}")
      .choice()
        .when(simple("${body} == 'Hello, world!'"))
          .transform(simple("Hello, everyone!")) // 透過 transform 修改 body 訊息
          .log(">>> Transform: ${body}")
          .to("file:src/test/des-folder")
        .otherwise()
          .to("file:src/test/trash");
  }
```

如果要實現類似多個 `if` 可以如下

```java
from("file:src/data")
  .choice()
    .when(expression)
    .log(...)
    .to(endpoint)
  .when(expression)
    .log(...)
    .to(endpoint)
  .otherwise()
    .log(...)
    .to(endpoint);
```

## Joining routes together
在單個 Camel 應用程式或 `Camel Context` 中可以擁有多個 Camel Route。也就是說我們繼層 Camel 中 `RouteBuilder` 並重寫 `configure` 方法，當中定義多個 Route 時，我們運行應用程式時，它就會運行我們定義的多個 Route。定義多個的好處不外乎就是*維護性*和*可讀性*。

但透過 `Direct` 組件，可以將模組化的 Route 連接在一起，也就是說或許可以不用定義多個類似的 Route。

## Direct component

可以把 Direct 組件視為程式碼中的函數或方法。

當 `from` 使用 `Direct` 組件時，就像將 Route 內部的邏輯公開為方法，因此可從任何其他 Camel Route 調用，或者可從其它 Java 程式碼調用。如下，`your-name`(方法名稱) 內容可以是一些訊息處理邏輯。

```java
from("direct:your-name")
```

範例

```java
  @Override
  public void configure() throws Exception {
    // TODO Auto-generated method stub
    from("direct:process-file")
        .choice()
        .when(simple("${bodyAs(String)} contains 'Cilla Black'"))
        .log(">>> ${body}")
        .to("file:src/test/des-folder/cilla_black")
        .end();

    from("file:src/test/trash?fileName=direct.txt")
        .log(">>> trash: ${body}")
        .to("direct:process-file");
    from("file:src/test/src-folder?fileName=direct.txt")
        .log(">>> src-folder: ${body}")
        .to("direct:process-file");
  }
```
Direct 端點(endpoint)預設是同步(synchronous)的，如下
```java
from("file:incoming/files")
  .to("direct:sayHello") // 回傳 Hello! Body 訊息給下個端點
  .to("file:myfolder/greetings");

from("direct:sayHello")
  .setBody(simple("Hello!"));
```

## Modifying messages

Camel 中的訊息(message)由 Body 和 Header 組成。
- Body
  - 以 file 端點來說，就是檔案內容，也可以稱為 **Exchange Body**
- Header
  - 而外訊息，類似於 HTTP 的 Header

在 Camel 中，可透過幾種不同的方式修改 message 的內容。可以使用 transform EIP，該 transform 接受一個表達式，其表達式的結果會被定義到 Body。`constant` 表達式簡單回傳提供給它的字符串，如下。

constant 表達式範例
```java
  @Override
  public void configure() throws Exception {
    // TODO Auto-generated method stub
    from("file:src/test/src-folder?fileName=constant.txt") // read
        .log("received a file")
        .transform(constant("constant!")) // Modify Body
        .setBody(constant("setBody!")) // Modify Body；setBody 等價於 transform
        .transform(simple("Hello, ${body}!")) // Modify Body
        .to("file:src/test/des-folder/output"); // output 
  }
```

Simple language 也是一個表達式，是在 Camel 中編寫*動態表達式*的一種方式。即可使用當前訊息的 Body 或是 Header 產生字串或是表達式，如上述範例。

## Splitting messages
當一個檔案中，有多筆訊息，且該訊息都是符合結構規範時，想一筆一筆處理訊息時，可使用 `Splitter`。簡單來說，當一條訊息透過一個 Splitter 時，會根據一個表達式(Expression)被拆分成多條訊息。

Expression 可以用
- Simple
- XPath
- JSONPath

範例

```java
  @Override
  public void configure() throws Exception {
    // TODO Auto-generated method stub
    from("timer://generateEmployee?fixedRate=true&period=10000")
        .log("Generate Department...")
        .process(new Processor() {

          public void process(Exchange exchange) throws Exception {
            Department order = new Department();
            Employee e1 = new Employee("1", "Itachi", "N500", 20.0);
            Employee e2 = new Employee("2", "Naruto", "N501", 22.0);
            Employee e3 = new Employee("3", "Madara", "N501", 24.0);
            Employee e4 = new Employee("4", "Kevin", "N500", 15.0);
            List<Employee> list = new ArrayList<>();
            list.add(e1);
            list.add(e2);
            list.add(e3);
            list.add(e4);
            order.setEmployees(list);
            exchange.getIn().setBody(order.getEmployees());
          }
        })
        .to("direct:processDepartment");

    from("direct:processDepartment") // 宣告一個可被調用方法
        .log("Process Department ${body}")
        .split(simple("${body}")) 
        .to("direct:processEmployeeItem") //調薪動作
        .log("Processing done ${body}")
        .end()
        .log("Employee raise processed: ${body}");

    from("direct:processEmployeeItem") // 宣告一個可被調用方法
        .log("Processing item ${body.name}")
        .bean(EmployeeService.class, "salaryProcess(${body})");
  }
```

我們也可以透過某些字符進行切分，我們可以呼叫 `tokenize`，例如

```java
from("direct:tokenizeExample")
  .split(body().tokenize("\n")) // 透過 \n 切分
  .log("The body is now ${body}");
```

我們切分訊息後，如果要對原始訊息繼續處理，則要透過 `end` 來告訴 `split` 到哪個位置完成此動作。當使用了 `end` 後，*Exchange Body* 將恢復到拆分前的狀態。


## Scheduling routes
`Timer` 組件是一個添加到 Route 開頭（from）以按排程觸發定義的 Route。

假設要每 5 秒觸發一次可以如下設定

```java
from("timer:firstTimer?period=5000") // period 表示週期
  .log("Hello, Camel!"); 
```

但除了這個之外我們也可以控制該 Route 僅觸發一次。可能希望偶爾運行該 Route，或手動運行，並希望它只運行一次。可以透過 `Timer` 中 `repeatCount` 參數來完成需求，這指定了 `Timer` 應該觸發的次數，設置 `repeatCount=1` 表示執行一次，如下。

```java
  @Override
  public void configure() throws Exception {
    // TODO Auto-generated method stub
    from("timer:onceTimer?repeatCount=1&period=5000")
        .log("Executes just once!");
  }
```

如果想在之後終止應用程式可以設定以下參數
- camel.springboot.duration-max-messages
- camel.springboot.duration-max-seconds

這些屬性將在處理大量訊息或經過數秒後關閉應用程序。

## Transforming messages
Camel 在大多數場景還是會涉及到轉換部分。在 Camel 中，轉換(transformation)可用於將數據從一種格式更改為另一種格式，類似翻譯訊息。

### Using data formats
轉換通常是透過不同數據格式之間的 unmarshalling 和 marshalling 完成。這是分兩個階段轉換數據的行為

1. 將數據從文字或二進制形式 unmarshalling 為 Camel 可理解的通用格式（Java Object 或 Collection）
2. 然後，將物件 marshalling 為新的序列化格式，例如: CSV、JSON 等

Common data formats in Camel
| Format | Camel class name |
|---|---|
|JSON|JsonDataFormat|
|CSV|CsvDataFormat|
|Base64|Base64DataFormat|
|ZIP|ZipDataFormat|

Unmarshalling 是將基於文字檔案格式轉換為 Java Object 的行為。

```
File format → unmarshal (via data format) → Java object
```

