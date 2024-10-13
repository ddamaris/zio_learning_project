package model

import zio.UIO
import zio.json.{DeriveJsonCodec, JsonCodec}

final case class Transaction(
    id: TransactionId,
    acc_from: AccountId,
    acc_to: AccountId,
    amount: Long
)

object Transaction {
  def apply(
      acc_from: AccountId,
      acc_to: AccountId,
      amount: Long
  ): UIO[Transaction] =
    TransactionId.generate.map(Transaction(_, acc_from, acc_to, amount))

  implicit val codec: JsonCodec[Transaction] = DeriveJsonCodec.gen[Transaction]
}

final case class TransactionCreated(
    from: AccountId,
    to: AccountId,
    amount: Long
)

object TransactionCreated {
  implicit val codec: JsonCodec[TransactionCreated] =
    DeriveJsonCodec.gen[TransactionCreated]
}
