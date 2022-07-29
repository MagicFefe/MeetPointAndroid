package com.swaptech.meet.presentation.utils

import android.util.Patterns
import com.swaptech.meet.presentation.MAX_CITY_NAME_LENGTH
import com.swaptech.meet.presentation.MAX_EMAIL_LENGTH
import com.swaptech.meet.presentation.MAX_NAME_SURNAME_LENGTH
import com.swaptech.meet.presentation.MAX_PASSWORD_LENGTH
import com.swaptech.meet.presentation.MIN_PASSWORD_LENGTH

object Validator {

    fun emailIsNotValid(email: String): Boolean =
        !(Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                email.length <= MAX_EMAIL_LENGTH)

    fun passwordIsNotValid(password: String) =
        password.length !in MIN_PASSWORD_LENGTH..MAX_PASSWORD_LENGTH

    fun cityIsNotValid(city: String) =
        city.length > MAX_CITY_NAME_LENGTH

    fun nameSurnameIsNotValid(nameSurname: String) = nameSurname.length > MAX_NAME_SURNAME_LENGTH

    fun anyFieldIsEmpty(vararg field: String) = field.any { it.isEmpty() }
}
