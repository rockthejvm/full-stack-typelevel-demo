package com.rockthejvm.livedemo.core

import cats.*
import cats.syntax.all.*
import cats.effect.kernel.Resource

trait Dummy[F[_]] {
  def action(arg: Int): F[String]
}

class DummyLive[F[_]: Applicative] private extends Dummy[F] {
  override def action(arg: Int): F[String] =
    s"Called an action with the arg $arg".pure[F]
}

object DummyLive {
  // "Smart constructor"
  def make[F[_]: Applicative]: F[Dummy[F]] =
    new DummyLive[F].pure[F]

  // resource constructor
  def resource[F[_]: Applicative]: Resource[F, Dummy[F]] =
    Resource.pure(new DummyLive[F])
}
