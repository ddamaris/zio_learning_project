import errors.AppError
import zio.IO
import zhttp.http.Request
import zio.json._
import zio._

package object utils {

  def parseBody[A: JsonDecoder](request: Request): IO[AppError, A] =
    for {
      body   <- request.bodyAsString.orElseFail(AppError.MissingBodyError)
      parsed <- ZIO.from(body.fromJson[A]).mapError(AppError.JsonDecodingError)
    } yield parsed
}
