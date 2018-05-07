# Getting started

It's a simple application that returns the number of the unuque users which was request to REST API.

# For launch

## application of options
  - you can apply options by default:
  - you can custom options;

### Options by default
- if options by default when using SQLiteUserDao.
- host of rest api - localhost
- port of rest api - 9080

### Custom options
you should apply the options to argument of app in format
`<nameOption1>=<value> <nameOption2=<value>`

#### supported options
|   argument | definition        |       default           |
| --- | ---|---|
|`storage=<String>`| what storage will be used. Allow `MySql` `SqLite` `Memory`|  `SqLite`|
|`userTableName=<String>` | the name of a table | `userTable ` |
| `rest.host=<String>` | host of rest api.  | `localhost`|
| `rest.port=<Integer>` | port of rest api.  |`9080`|
| `rest.testPort=<Integer>` | port of rest api for integration tests.  |`9081`|
| `mysql.host=<String>` | host of mysql server.  | `localhost`|
| `mysql.port=<Integer>` | port of mysql server. | `3306`|
| `mysql.databaseName=<String>` | the name of database. You should use existing database.  | `userdb`|
| `mysql.autoReconnect=<Boolean>` | autoreconnect |   `true`|
| `mysql.useSSL=<Boolean>`  || `false`|
| `mysql.useJDBCCompliantTimezoneShift=<Boolean>`  || `true`|
| `mysql.useLegacyDatetimeCode=<Boolean>`  || `false`|
| `mysql.serverTimezone=<String>`  || `UTC`|
| `mysql.driver=<String>`  || `com.mysql.cj.jdbc.Driver`|
| `mysql.username=<String>` | username for connect to Mysql server.|  - `root`|
|`mysql.password=<String>`| password for connect to Mysql server. | `root`|
|`sqlite.workingConnection=<String>` | uses as argument for DriverManager. | `jdbc:sqlite:userdb.db` |
|`sqlite.testConnection=<String>` | users as argument for DriverManager for running integration tests | `jdbc:sqlite:testdb.db` |
| `sqlite.driver`| path to sqlite driver | `org.sqlite.JDBC` |

## Launch using sbt
### First step
install sbt
### Second step
clone this repository
### Third step
enter in a console command in source folder
`sbt run` or `sbt "run <nameOption1>=<value> <nameOption2=<value>"` for applying custom options

## Databases
- By default the application uses `SqLite`.
- You can also use `MySql` but you should run `MySql` server and create database. In running you should give custom option `mysql.databaseName=<String>` which equal the created database name.
- You can also use `Memory` storage but it not recomended because this storage doesn't store the state. After relaunch all data will be lost

## Using REST API
`REST API` supports three url
- `GET http://<host>:<port>/stat` - returns the number of the unique users

- `POST http://<host>:<port>/user` - request that increment number of the unique users if user_id is unique
    json example:

```json
{
    "user_id": "user1"
}
```
- `DELETE http://<host>:<port>/stat` - delets counter of the unique users
