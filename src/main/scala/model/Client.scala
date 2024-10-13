package model

import zio.UIO
import zio.json.{DeriveJsonCodec, JsonCodec}

final case class Client(
    id: ClientId,
    firstName: String,
    lastName: String,
    address: String
)

object Client {
  def apply(firstName: String, lastName: String, address: String): UIO[Client] =
    ClientId.generate.map(Client(_, firstName, lastName, address))

  implicit val codec: JsonCodec[Client] = DeriveJsonCodec.gen[Client]
}

final case class ClientCreated(
    firstName: String,
    lastName: String,
    address: String
)

object ClientCreated {
  implicit val codec: JsonCodec[ClientCreated] =
    DeriveJsonCodec.gen[ClientCreated]
}

final case class ClientUpdated(
    firstName: Option[String],
    lastName: Option[String],
    address: Option[String]
)

object ClientUpdated {
  implicit val codec: JsonCodec[ClientUpdated] =
    DeriveJsonCodec.gen[ClientUpdated]
}
