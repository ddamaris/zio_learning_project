package model

import zio.json.JsonCodec
import zio.{Random, UIO, ZIO}

case class ClientId(id: String) extends AnyVal

object ClientId {
  def get(id: String): UIO[ClientId] = ZIO.succeed(ClientId(id))
  def generate: UIO[ClientId] = Random.nextUUID.map(u => ClientId("cln_" + u))
  implicit val codec: JsonCodec[ClientId] =
    JsonCodec[String].transform(ClientId(_), _.id)
}
