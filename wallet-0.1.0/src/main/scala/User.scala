import java.util.Date
import java.util.Calendar
import org.springframework.web.bind.annotation.{RequestMapping, PathVariable, RequestBody}
;
/**
 * Created by tusharkarkera on 9/21/14.
 */
class User(uid: Int, mail: String, pwd: String, nme: String) {
  var userId: Int = uid
  var email: String = mail
  var password: String = pwd
  var name: String = nme

  /*def getUser(userId: Int, email: String, password: String): String = {
    println("user_id: " + userId)
    println("email: " + email)
    println("password: " + password)
  }*/

  @RequestMapping(Array("/users"))
  def createUser(@PathVariable email: String, @PathVariable password: String) = {

    val today = Calendar.getInstance().getTime()
    println("userid: " )
    println("email: " + email)
    println("password: " + password)
    println("created_at: " + today)
  }

}