package sparklyudf

import org.apache.spark.sql.SparkSession;

object Main {
  def register_hello(spark: SparkSession) = {
    spark.udf.register("hello", (name: String) => {
      "Hello, " + name + "! - From Scala"
    })
  }
}
