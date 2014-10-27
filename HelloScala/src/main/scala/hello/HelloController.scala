package hello

import java.text.SimpleDateFormat
import java.util
import java.util.Calendar
import javax.validation.Valid
import Exception.InvalidRequestException
import VO.{BankAccount, WebLogin, IDCard, User}
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.MongoClient
import org.springframework.data.authentication.UserCredentials
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.core.query.{Update, Query, Criteria}
import org.springframework.data.mongodb.core.{MongoTemplate, SimpleMongoDbFactory}
import org.springframework.http.{ResponseEntity, HttpStatus}
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation._
import org.springframework.web.client.RestTemplate



@RestController
@RequestMapping(Array("/api/v1"))
class HelloController {

  def mongoDbFactory(): MongoDbFactory = {
    new SimpleMongoDbFactory(new MongoClient("ds049170.mongolab.com:49170"), "cmpe273",new UserCredentials("tushar","tushar"))
  }

  println("connected to db")
  val document = new MongoTemplate(mongoDbFactory())

  var users: Map[String, User] = Map()


  @RequestMapping(value = Array("/users"), headers = Array({"content-type=application/json"}), method = Array(RequestMethod.POST))
  @ResponseBody
  @ResponseStatus(HttpStatus.CREATED)
  def createUser(@Valid @RequestBody user: User, bindingResult: BindingResult): User = {
    if (bindingResult.hasErrors())
      throw new InvalidRequestException("Invalid User",bindingResult)
    val today = Calendar.getInstance().getTime()
    var dateFormat = new SimpleDateFormat("YYYY-MM-DD'T'24HH:MM:SS.SSS'Z'")
    var createdAt = dateFormat.format(today)
    user.setCreated_at(createdAt)
    var user_id: String = "u-" + System.currentTimeMillis
    user.setId(user_id)
    println("User created")
    document.save(user)
    user
  }

  @RequestMapping(value = Array("/users/{user_id}"), method = Array(RequestMethod.GET))
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  def getUser(@PathVariable("user_id") user_id: String): User = {
    var user= document.findOne(new Query(Criteria.where("id").is(user_id)),classOf[User])
    if(user==null)
      null
    else
      user
  }

  @RequestMapping(value = Array("/users/{user_id}"), headers = Array({"content-type=application/json"}), method = Array(RequestMethod.PUT))
  @ResponseBody
  @ResponseStatus(HttpStatus.CREATED)
  def createUser(@PathVariable("user_id") user_id: String, @RequestBody newUser: User): User = {

    val update = new Update()
    update.set("email", newUser.getEmail)
    update.set("password", newUser.getPassword)
    document.updateFirst(new Query(Criteria.where("id").is(user_id)),update,classOf[User])
    var user= document.findOne(new Query(Criteria.where("id").is(user_id)),classOf[User])
    if(user==null)
      null
    else
      user
  }


  @RequestMapping(value = Array("/users/{user_id}/idcards"), headers = Array({"content-type=application/json"}), method = Array(RequestMethod.POST))
  @ResponseBody
  @ResponseStatus(HttpStatus.CREATED)
  def createUserId(@Valid @RequestBody idcards: IDCard, bindingResult: BindingResult, @PathVariable user_id: String): IDCard = {
    if(bindingResult.hasErrors()) {
      throw new InvalidRequestException("Invalid card login input",bindingResult)
    }
    var user = document.findOne(new Query(Criteria.where("id").is(user_id)),classOf[User])
    if (user != null) {
      var idCards: Array[IDCard] = user.getIdCards
      if (idCards == null) {
        val rand_id = System.currentTimeMillis
        idcards.setCard_id("c-" + rand_id.toString())
        idCards = Array[IDCard](idcards)
      }
      else {
        val rand_id = System.currentTimeMillis
        idcards.setCard_id("c-" + rand_id.toString())
        idCards = idCards :+ idcards
      }
      document.save(idCards)
      idcards
    }else
      null
   }


  @RequestMapping(value = Array("/users/{user_id}/idcards"), method = Array(RequestMethod.GET))
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  def getUserID(@PathVariable("user_id") user_id: String): util.List[IDCard] = {
    var user= document.findOne(new Query(Criteria.where("id").is(user_id)),classOf[User])

    if (user != null)
      (document.find(new Query(Criteria.where("user_id").is(user_id)), classOf[IDCard]))
    else
      null
  }

  @RequestMapping(value = Array("/users/{user_id}/idcards/{id_card}"), method = Array(RequestMethod.DELETE))
  @ResponseBody
  @ResponseStatus(HttpStatus.NO_CONTENT)
  def deleteUserID(@PathVariable("id_card") id_card: String, @PathVariable("user_id") user_id: String) {
    var user= document.findOne(new Query(Criteria.where("id").is(user_id)),classOf[User])
    if (user != null) {
      var card = document.findOne(new Query(Criteria.where("id").is(id_card)), classOf[User])
      if (card != null) {
        (document.remove(new Query(Criteria.where("_id").is(id_card)), classOf[IDCard]))
      }else
        print("No card")
    }
  }


  @RequestMapping(value = Array("/users/{user_id}/weblogins"), headers = Array({"content-type=application/json"}), method = Array(RequestMethod.POST))
  @ResponseBody
  @ResponseStatus(HttpStatus.CREATED)
  def createUserWebLogin(  @Valid @RequestBody weblogins: WebLogin, @PathVariable user_id: String): WebLogin = {
    var user= document.findOne(new Query(Criteria.where("id").is(user_id)),classOf[User])
    if (user != null) {
      weblogins.setLogin_id("l-" + System.currentTimeMillis.toString())
      weblogins.setUser_id(user_id)
      document.save(weblogins)
      weblogins
    }
    else
      null
  }

  @RequestMapping(value = Array("/users/{user_id}/weblogins"), method = Array(RequestMethod.GET))
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  def getUserWebLogin(@PathVariable("user_id") user_id: String): util.List[WebLogin] = {
    var user = document.findOne(new Query(Criteria.where("id").is(user_id)),classOf[User])
    if (user != null){
      (document.find(new Query(Criteria.where("user_id").is(user_id)), classOf[WebLogin]))
  }else
    null
  }

  @RequestMapping(value = Array("/users/{user_id}/weblogins/{login_id}"), method = Array(RequestMethod.DELETE))
  @ResponseBody
  @ResponseStatus(HttpStatus.NO_CONTENT)
  def deleteUserWebLogin(@PathVariable("login_id") login_id: String, @PathVariable("user_id") user_id: String) {
    var user= document.findOne(new Query(Criteria.where("id").is(user_id)),classOf[User])
    if (user != null) {
      var bankAcc= document.findOne(new Query(Criteria.where("_id").is(login_id)),classOf[WebLogin])
      if (bankAcc != null){
        (document.remove(new Query(Criteria.where("_id").is(login_id)), classOf[WebLogin]))
      }else
        println("Not found")
    }
  }

  @Valid
  @RequestMapping(value = Array("/users/{user_id}/bankaccounts"), headers = Array({"content-type=application/json"}), method = Array(RequestMethod.POST))
  @ResponseBody
  @ResponseStatus(HttpStatus.CREATED)
  def createUserBankAccount(@RequestBody bankaccounts: BankAccount, @PathVariable user_id: String, bindingResult: BindingResult) = {
    if(bindingResult.hasErrors()) {
      throw new InvalidRequestException("Invalid Account",bindingResult)
    }
    val URI="http://www.routingnumbers.info/api/data.json?rn="
    val restTemplate = new RestTemplate()
    val response=restTemplate.getForObject(URI+bankaccounts.getRouting_number,classOf[String])
    var map = new util.HashMap[String, String]()
    val mapper = new ObjectMapper()
    map = mapper.readValue(response, new TypeReference[util.HashMap[String, String]]() {
    })
    val code = map.get("code")
    if(code.equalsIgnoreCase("200")) {
      bankaccounts.setAccount_name(map.get("customer_name"))
      bankaccounts.setBa_id("b-" + System.currentTimeMillis)
      bankaccounts.setUser_id(user_id)
      bankaccounts.setMessage(map.get("message"))
      bankaccounts.setOffice_code(map.get("office_code"))
      bankaccounts.setRecord_type_code(map.get("record_type_code"))
      bankaccounts.setNew_routing_number(map.get("new_routing_number"))
      bankaccounts.setChange_date(map.get("change_date"))
      bankaccounts.setState(map.get("state"))
      bankaccounts.setAddress(map.get("address"))
      bankaccounts.setTelephone(map.get("telephone"))
      bankaccounts.setData_view_code(map.get("data_view_code"))
      bankaccounts.setCode(map.get("code"))
      bankaccounts.setCity(map.get("city"))
      bankaccounts.setRn(map.get("rn"))
      bankaccounts.setInstitution_status_code(map.get("institution_status_code"))
      bankaccounts.setZip(map.get("zip"))
      println("Bank account created")
      document.save(bankaccounts)
      bankaccounts
    }else
      new ResponseEntity[String]("Invalid Routing Number",HttpStatus.NOT_FOUND)
  }

  @RequestMapping(value = Array("/users/{user_id}/bankaccounts"), method = Array(RequestMethod.GET))
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  def getUserBankAccounts(@PathVariable("user_id") user_id: String): util.List[BankAccount] = {
    var user= document.findOne(new Query(Criteria.where("id").is(user_id)),classOf[User])
    if (user != null)
      (document.find(new Query(Criteria.where("user_id").is(user_id)), classOf[BankAccount]))
    else
      null
  }

  @RequestMapping(value = Array("/users/{user_id}/bankaccounts/{ba_id}"), method = Array(RequestMethod.DELETE))
  @ResponseBody
  @ResponseStatus(HttpStatus.NO_CONTENT)
  def deleteUserBankAccount(@PathVariable("ba_id") ba_id: String, @PathVariable("user_id") user_id: String)  {
    var user= document.findOne(new Query(Criteria.where("id").is(user_id)),classOf[User])
    if (user != null) {
      var ba= document.findOne(new Query(Criteria.where("_id").is(ba_id)),classOf[BankAccount])
      if (ba != null){
          (document.remove(new Query(Criteria.where("_id").is(ba_id)), classOf[BankAccount]))
      }else
          println("Account not found")
    }
  }
}
