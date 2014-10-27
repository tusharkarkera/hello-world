package VO

import javax.validation.constraints.NotNull

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.validator.constraints.NotEmpty
import org.springframework.data.annotation.Id

import scala.beans.BeanProperty


/**
 * Created by tusharkarkera on 9/30/14.
 */
class BankAccount {

  @JsonIgnore
  @Id
  @BeanProperty
  var ba_id:String =_

  @BeanProperty
  var account_name:String =_

  @NotNull
  @NotEmpty
  @BeanProperty
  var routing_number:String =_

  @JsonIgnore
  @NotNull
  @NotEmpty
  @BeanProperty
  var account_number:String =_

  @JsonIgnore
  @BeanProperty
  var user_id:String=_

  @BeanProperty
  var message:String =_

  @BeanProperty
  var change_date:String =_

  @BeanProperty
  var office_code:String =_

  @BeanProperty
  var record_type_code:String =_

  @BeanProperty
  var new_routing_number:String =_

  @BeanProperty
  var rn:String =_

  @BeanProperty
  var state:String =_

  @BeanProperty
  var address:String =_

  @BeanProperty
  var telephone:String =_

  @BeanProperty
  var data_view_code:String =_

  @BeanProperty
  var code:String =_

  @BeanProperty
  var city:String =_

  @BeanProperty
  var institution_status_code:String =_

  @BeanProperty
  var zip:String =_

}
