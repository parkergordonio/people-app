import com.google.inject.AbstractModule
import java.time.Clock

import services.parser.{EmailParser, ParsingService}
import services.people.{PeopleService, SalesLoftPeopleService}


class Module extends AbstractModule {
  override def configure() = {

    bind(classOf[PeopleService]).to(classOf[SalesLoftPeopleService])

    bind(classOf[ParsingService]).to(classOf[EmailParser])
  }
}
