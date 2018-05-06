package com.catran.validator

import com.catran.exception.IllegalRequestException
import org.everit.json.schema.loader.SchemaLoader
import org.everit.json.schema.{Schema, ValidationException}
import org.json.{JSONException, JSONObject, JSONTokener}

class JsonSchemaValidator(val schemaResourceFile: String) {

  var schema: Schema = initSchema()

  /**
    * @return schema for validation json
    */
  protected def initSchema(): Schema = {
    val source = scala.io.Source.fromURL(getClass.getResource(schemaResourceFile))
    try {
      val rawSchema = new JSONObject(new JSONTokener(source.mkString))
      SchemaLoader.load(rawSchema)
    } finally source.close()
  }

  /**
    * validates json by json-schema
    */
  def validate(json: String): Unit = {
    try {
      val jsonObject = new JSONObject(json)
      schema.validate(jsonObject)
    } catch {
      case e: ValidationException => {
        throw new IllegalRequestException(e.getMessage, e)
      }
      case e: JSONException => {
        throw new IllegalRequestException("json does not correspond RFC", e)
      }
    }
  }
}

object JsonSchemaValidator {
  def apply(schemaResourceFile: String): JsonSchemaValidator = new JsonSchemaValidator(schemaResourceFile)
}