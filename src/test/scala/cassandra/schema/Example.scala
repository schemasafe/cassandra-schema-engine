package cassandra.schema

object Example extends App {

  val queries = Seq(
    "create keyspace if not exists test WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' :  1};",
    "create table if not exists test.posts(id int primary key, name text)"
  )

  val result = SchemaValidation.createSchema(queries)
  println(s"* create schema: $result")

  result.map { schema =>
    val select = SchemaValidation.checkSchema(schema, "select * from test.posts limit 1")
    val update = SchemaValidation.checkSchema(
      schema,
      "update test.posts set name = ? where id = 1")
    val delete =
      SchemaValidation.checkSchema(schema,
                                   "delete from test.posts where id = 1")
    val insert = SchemaValidation.checkSchema(
      schema,
      "INSERT INTO test.posts (id, name) VALUES (?, ?) IF NOT EXISTS;")

    println(s"* check select statement: $select")
    println(s"* check update statement: $update")
    println(s"* check delete statement: $delete")
    println(s"* check insert statement: $insert")
  }
}
