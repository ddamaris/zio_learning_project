package model

import zio.UIO
import zio.json.{DeriveJsonCodec, JsonCodec}

final case class Account(
    id: AccountId,
    kind: String,
    balance: Long,
    clientId: ClientId
)

object Account {
  def apply(kind: String, clientId: ClientId): UIO[Account] =
    AccountId.generate.map(Account(_, kind, 0, clientId))

  /** Derives a JSON codec for the User type allowing it to be (de)serialized.
    */
  implicit val codec: JsonCodec[Account] = DeriveJsonCodec.gen[Account]

}

final case class AccountCreated(kind: String, balance: Long, clientId: ClientId)

object AccountCreated {
  implicit val codec: JsonCodec[AccountCreated] =
    DeriveJsonCodec.gen[AccountCreated]
}

final case class AccountUpdated(
    kind: Option[String],
    balance: Option[Long],
    clientId: Option[ClientId]
)

object AccountUpdated {
  implicit val codec: JsonCodec[AccountUpdated] =
    DeriveJsonCodec.gen[AccountUpdated]
}
