package Exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Created by tusharkarkera on 10/26/14.
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="User incorrect")
class UserNotFoundException() extends RuntimeException{

}
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Incorrect card")
class CardNotFoundException() extends RuntimeException{

}

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Incorrect weblogin")
class WebLoginNotFoundException() extends RuntimeException{

}


@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Invalid bank user")
class NoBankAccFoundException() extends RuntimeException {

}

