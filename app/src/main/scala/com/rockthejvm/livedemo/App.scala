package com.rockthejvm.livedemo

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import cats.effect.*
import tyrian.*
import tyrian.Html.*
import tyrian.http.*
import io.circe.syntax.*
import io.circe.parser.*
import io.circe.generic.auto.*

import com.rockthejvm.livedemo.domain.job.Job

enum Msg {
  case NoMsg
  case LoadJobs(jobs: List[Job])
  case Error(e: String)
}

case class Model(jobs: List[Job] = List())

@JSExportTopLevel("RockTheJvmApp")
object App extends TyrianApp[Msg, Model] {

  def backendCall: Cmd[IO, Msg] =
    Http.send(
      Request.get("http://localhost:4041/jobs"),
      Decoder[Msg](
        resp =>
          parse(resp.body).flatMap(_.as[List[Job]]) match {
            case Left(e)     => Msg.Error(e.getMessage())
            case Right(list) => Msg.LoadJobs(list)
          },
        err => Msg.Error(err.toString)
      )
    )

  override def init(flags: Map[String, String]): (Model, Cmd[IO, Msg]) =
    (Model(), backendCall)

  override def view(model: Model): Html[Msg] =
    div(`class` := "row")(
      p("This is the first ScalaJS app by Rock the JVM"),
      div(`class` := "contents ")(
        model.jobs.map { job =>
          div(job.toString)
        }
      )
    )

  override def update(model: Model): Msg => (Model, Cmd[IO, Msg]) = msg =>
    msg match {
      case Msg.NoMsg          => (model, Cmd.None)
      case Msg.Error(e)       => (model, Cmd.None)
      case Msg.LoadJobs(list) => (model.copy(jobs = model.jobs ++ list), Cmd.None)
    }

  override def subscriptions(model: Model): Sub[IO, Msg] =
    Sub.None
}
