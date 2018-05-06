package com.catran.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpEntity, HttpMethods, HttpRequest}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.util.ByteString

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

object Requester {
  private implicit val system = ActorSystem()
  private implicit val materializer = ActorMaterializer()
  private implicit val executionContext = system.dispatcher

  def sendGetRequest(host: String, port: Int, uri: String): String = {
    val url = s"http://${host}:${port}/${uri}"
    val timeout = 5000.millis


    val respEntity = for {
      response <- Http().singleRequest(HttpRequest(method = HttpMethods.GET, uri = url))
      entity <- Unmarshal(response.entity).to[ByteString]
    } yield entity

    val payload: Future[ByteString] = respEntity.andThen {
      case Success(entity) =>
        entity.utf8String
      case Failure(ex) =>
        s"""{"error": "${ex.getMessage}"}"""
    }
    val byteString = Await.result(payload, timeout)
    byteString.utf8String
  }

  def asynchPostRequest(host: String, port: Int, uri: String, json: String): Future[String] = {
    val url = s"http://${host}:${port}/${uri}"
    val timeout = 5000.millis
    val entity = HttpEntity(json)

    val respEntity = for {
      response <- Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = url, entity = entity))
      entity <- Unmarshal(response.entity).to[ByteString]
    } yield entity

    val payload: Future[ByteString] = respEntity.andThen {
      case Success(entity) =>
        entity.utf8String
      case Failure(ex) =>
        s"""{"error": "${ex.getMessage}"}"""
    }
    payload.map(f => f.utf8String)
  }

  def sendDeleteRequest(host: String, port: Int, uri: String): String = {
    val url = s"http://${host}:${port}/${uri}"
    val timeout = 5000.millis

    val respEntity = for {
      response <- Http().singleRequest(HttpRequest(method = HttpMethods.DELETE, uri = url))
      entity <- Unmarshal(response.entity).to[ByteString]
    } yield entity

    val payload: Future[ByteString] = respEntity.andThen {
      case Success(entity) =>
        entity.utf8String
      case Failure(ex) =>
        s"""{"error": "${ex.getMessage}"}"""
    }
    val byteString = Await.result(payload, timeout)
    byteString.utf8String
  }
}
