package VO

import javax.validation.constraints.NotNull

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.validator.constraints.NotEmpty

import scala.beans.BeanProperty


/**
 * Created by tusharkarkera on 9/30/14.
 */
class WebLogin {

  @BeanProperty
  var login_id:String =_

  @NotNull
  @NotEmpty
  @BeanProperty
  var url:String =_

  @NotNull
  @NotEmpty
  @BeanProperty
  var login:String =_

  @NotNull
  @NotEmpty
  @BeanProperty
  var password:String =_

  @JsonIgnore
  @BeanProperty
  var user_id:String=_

}
