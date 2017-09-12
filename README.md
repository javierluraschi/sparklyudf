sparklyudf: Scala UDF from R - Example
================

sparklyudf demonstrates how to build a [sparklyr](http://github.com/rstudio/sparklyr) extension package that uses custom Scala code which is compiled, deployed to Apache Spark and registered as an UDF that can be used in SQL and [dplyr](https://github.com/tidyverse/dplyr).

Building
--------

First build this package, then build its Spark 2.0 jars by running:

``` r
spec <- sparklyr::spark_default_compilation_spec()
spec <- Filter(function(e) e$spark_version >= "2.0.0", spec)
sparklyr::compile_package_jars()
```

then build the R package as usual.

This package contains an Scala-based UDF defined as:

``` scala
object Main {
  def register_hello(spark: SparkSession) = {
    spark.udf.register("hello", (name: String) => {
      "Hello, " + name + "! - From Scala"
    })
  }
}
```

Getting Started
---------------

Connect and test this package as follows:

``` r
library(sparklyudf)
library(sparklyr)
library(dplyr)
```

    ## 
    ## Attaching package: 'dplyr'

    ## The following objects are masked from 'package:stats':
    ## 
    ##     filter, lag

    ## The following objects are masked from 'package:base':
    ## 
    ##     intersect, setdiff, setequal, union

``` r
sc <- spark_connect(master = "local")
```

    ## * Using Spark: 2.1.0

``` r
sparklyudf_register(sc)
```

    ## <jobj[13]>
    ##   class org.apache.spark.sql.expressions.UserDefinedFunction
    ##   UserDefinedFunction(<function1>,StringType,Some(List(StringType)))

Now the Scala UDF `hello()` is registered and can be used as follows:

``` r
data.frame(name = "Javier") %>%
  copy_to(sc, .) %>%
  mutate(hello = hello(name))
```

    ## # Source:   lazy query [?? x 2]
    ## # Database: spark_connection
    ##     name                       hello
    ##    <chr>                       <chr>
    ## 1 Javier Hello, Javier! - From Scala

``` r
spark_disconnect_all()
```

    ## [1] 1
