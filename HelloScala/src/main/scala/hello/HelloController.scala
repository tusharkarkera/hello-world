package hello

import java.util
import java.util.Calendar
import javax.validation.Valid
import VO.{BankAccount, WebLogin, IDCard, User}
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation._

import scala.collection.JavaConverters._
import scala.util.control.Breaks

/**
 * This config class will trigger Spring @annotation scanning and auto configure Spring context.
 *
 * @author saung
 * @since 1.0
 */

@RestController
@RequestMapping(Array("/api/v1"))
class HelloController {

  var users: Map[String, User] = Map()


  @RequestMapping(value = Array("/users"), headers = Array({"content-type=application/json"}), method = Array(RequestMethod.POST))
  @ResponseBody
  @ResponseStatus(HttpStatus.CREATED)
  def createUser(@Valid @RequestBody user: User): User = {
    val today = Calendar.getInstance().getTime()
    user.setCreated_at(today.toString)
    var int_user_id: Integer = users.size + 1
    var user_id: String = "u-" + int_user_id
    user.setId(user_id)
    users += (user_id -> user);
    user
  }

  @RequestMapping(value = Array("/users/{user_id}"), method = Array(RequestMethod.GET))
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  def getUser(@PathVariable("user_id") user_id: String): User = {
    users(user_id)
  }

  @RequestMapping(value = Array("/users/{user_id}"), headers = Array({"content-type=application/json"}), method = Array(RequestMethod.PUT))
  @ResponseBody
  @ResponseStatus(HttpStatus.CREATED)
  def createUser(@PathVariable("user_id") user_id: String, @RequestBody newUser: User): User = {

    var oldUser: User = users(user_id)
    oldUser.setEmail(newUser.getEmail)
    oldUser.setPassword(newUser.getPassword)
    users += (user_id -> oldUser);
    users(user_id)
  }


  @RequestMapping(value = Array("/users/{user_id}/idcards"), headers = Array({"content-type=application/json"}), method = Array(RequestMethod.POST))
  @ResponseBody
  @ResponseStatus(HttpStatus.CREATED)
  def createUserId(@Valid @RequestBody idcards: IDCard, @PathVariable user_id: String): IDCard = {
    var user: User = users(user_id)
    if (user != null) {
      var idCards: Array[IDCard] = user.getIdCards
      if (idCards == null) {
        idcards.setCard_id("c-" + 1)
        idCards = Array[IDCard](idcards)
      }
      else {
        idcards.setCard_id("c-" + idCards.length + 1)
        idCards = idCards :+ idcards
      }
      user.setIdCards(idCards)
    }
    idcards
  }


  @RequestMapping(value = Array("/users/{user_id}/idcards"), method = Array(RequestMethod.GET))
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  def getUserID(@PathVariable("user_id") user_id: String): Array[IDCard] = {
    var user: User = users(user_id)
    var idCards: Array[IDCard] = null
    if (user != null)
      idCards = user.getIdCards
    idCards
  }

  @RequestMapping(value = Array("/users/{user_id}/idcards/{id_card}"), method = Array(RequestMethod.DELETE))
  @ResponseBody
  @ResponseStatus(HttpStatus.NO_CONTENT)
  def deleteUserID(@PathVariable("id_card") id_card: String, @PathVariable("user_id") user_id: String) {
    var user: User = users(user_id)
    var idCards: Array[IDCard] = null
    if (user != null) {
      idCards = user.getIdCards
      var breaks = new Breaks;
      breaks.breakable {
        for (i <- 1 to idCards.length - 1) {
          if (idCards(i).getCard_id.equals(id_card)) {
            val buff = idCards.toBuffer
            buff.remove(i)
            idCards = buff.toArray
            breaks.break()
          }

        }
      }
      user.setIdCards(idCards)
    }
  }


  @RequestMapping(value = Array("/users/{user_id}/weblogins"), headers = Array({"content-type=application/json"}), method = Array(RequestMethod.POST))
  @ResponseBody
  @ResponseStatus(HttpStatus.CREATED)
  def createUserWebLogin(  @Valid @RequestBody weblogins: WebLogin, @PathVariable user_id: String): WebLogin = {
    var user: User = users(user_id)
    if (user != null) {
      var webLogins: Array[WebLogin] = user.getWebLogins

      if (webLogins == null) {
        weblogins.setLogin_id("l-" + 123)
        webLogins = Array[WebLogin](weblogins)
      }
      else {
        weblogins.setLogin_id("l-" + webLogins.length + 1)
        webLogins = webLogins :+ weblogins
      }
      user.setWebLogins(webLogins)
    }
    weblogins
  }

  @RequestMapping(value = Array("/users/{user_id}/weblogins"), method = Array(RequestMethod.GET))
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  def getUserWebLogin(@PathVariable("user_id") user_id: String): Array[WebLogin] = {
    var user: User = users(user_id)
    var webLogins: Array[WebLogin] = null
    if (user != null)
      webLogins = user.getWebLogins
    webLogins
  }

  @RequestMapping(value = Array("/users/{user_id}/weblogins/{login_id}"), method = Array(RequestMethod.DELETE))
  @ResponseBody
  @ResponseStatus(HttpStatus.NO_CONTENT)
  def deleteUserWebLogin(@PathVariable("login_id") login_id: String, @PathVariable("user_id") user_id: String) {
    var user: User = users(user_id)
    var webLogins: Array[WebLogin] = null
    if (user != null) {
      webLogins = user.getWebLogins
      var breaks = new Breaks;
      breaks.breakable {
        for (i <- 1 to webLogins.length - 1) {
          if (webLogins(i).getLogin_id.equals(login_id)) {
            val buff = webLogins.toBuffer
            buff.remove(i)
            webLogins = buff.toArray
            breaks.break()
            //var list: List[IDCard] =idCards.toList
            //list.drop(i)
            // idCards = list.toArray
          }

        }
      }
      user.setWebLogins(webLogins)
    }
  }

  @Valid
  @RequestMapping(value = Array("/users/{user_id}/bankaccounts"), headers = Array({"content-type=application/json"}), method = Array(RequestMethod.POST))
  @ResponseBody
  @ResponseStatus(HttpStatus.CREATED)
  def createUserBankAccount(@RequestBody bankaccounts: BankAccount, @PathVariable user_id: String): BankAccount = {
    var user: User = users(user_id)
    if (user != null) {
      var bankAccounts: Array[BankAccount] = user.getBankAccounts

      if (bankAccounts == null) {
        bankaccounts.setBa_id("b-" + 12345)
        bankAccounts = Array[BankAccount](bankaccounts)
      }
      else {
        bankaccounts.setBa_id("b-" + bankAccounts.length + 1)
        bankAccounts = bankAccounts :+ bankaccounts
      }

      user.setBankAccounts(bankAccounts)
    }
    bankaccounts
  }

  @RequestMapping(value = Array("/users/{user_id}/bankaccounts"), method = Array(RequestMethod.GET))
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  def getUserBankAccounts(@PathVariable("user_id") user_id: String): Array[BankAccount] = {
    var user: User = users(user_id)
    var bankaccounts: Array[BankAccount] = null
    if (user != null)
      bankaccounts = user.getBankAccounts
    bankaccounts
  }

  @RequestMapping(value = Array("/users/{user_id}/bankaccounts/{ba_id}"), method = Array(RequestMethod.DELETE))
  @ResponseBody
  @ResponseStatus(HttpStatus.NO_CONTENT)
  def deleteUserBankAccount(@PathVariable("ba_id") ba_id: String, @PathVariable("user_id") user_id: String)  {
    var user: User = users(user_id)
    var ba_ids: Array[BankAccount] = null
    if (user != null) {
      ba_ids = user.getBankAccounts
      var breaks = new Breaks;
      breaks.breakable {
        for (i <- 1 to ba_ids.length - 1) {
          if (ba_ids(i).getBa_id.equals(ba_id)) {
            val buff = ba_ids.toBuffer
            buff.remove(i)
            ba_ids = buff.toArray
            breaks.break()
            //var list: List[IDCard] =idCards.toList
            //list.drop(i)
            // idCards = list.toArray
          }

        }
      }
      user.setBankAccounts(ba_ids)

    }
  }

}