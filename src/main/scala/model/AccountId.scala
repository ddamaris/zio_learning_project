package model

import zio.json.JsonCodec
import zio.{Random, UIO, ZIO}

case class AccountId(id: String) extends AnyVal

object AccountId {
  def get(id: String): UIO[AccountId] = ZIO.succeed(AccountId(id))
  def generate: UIO[AccountId] = Random.nextUUID.map(u => AccountId("acc_" + u))
  implicit val codec: JsonCodec[AccountId] =
    JsonCodec[String].transform(AccountId(_), _.id)
}
