package model

import zio.json.JsonCodec
import zio.{Random, UIO, ZIO}

case class TransactionId(id: String) extends AnyVal

object TransactionId {
  def get(id: String): UIO[TransactionId] = ZIO.succeed(TransactionId(id))
  def generate: UIO[TransactionId] =
    Random.nextUUID.map(u => TransactionId("trn_" + u))
  implicit val codec: JsonCodec[TransactionId] =
    JsonCodec[String].transform(TransactionId(_), _.id)
}
