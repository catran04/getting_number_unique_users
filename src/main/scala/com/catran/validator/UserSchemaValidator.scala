package com.catran.validator

object UserSchemaValidator {
  private val SCHEMA_PATH = "/schema/user_schema.json"

  def apply(): JsonSchemaValidator = new JsonSchemaValidator(SCHEMA_PATH)
}
