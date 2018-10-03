import com.google.inject.AbstractModule
import java.time.Clock

import services.people.{PeopleService, SalesLoftPeopleService}
import utils.{CharCounter, CharUtil}


class Module extends AbstractModule {
  override def configure() = {

    bind(classOf[PeopleService]).to(classOf[SalesLoftPeopleService])

    bind(classOf[CharUtil]).to(classOf[CharCounter])
  }
}
