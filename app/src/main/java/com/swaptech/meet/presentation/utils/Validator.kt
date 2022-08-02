package com.swaptech.meet.presentation.utils

import android.util.Patterns
import com.swaptech.meet.presentation.MAX_ABOUT_FIELD_LENGTH
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

    fun aboutFieldIsNotValid(aboutField: String) = aboutField.length > MAX_ABOUT_FIELD_LENGTH

    fun anyFieldIsEmpty(vararg field: String) = field.any { it.isEmpty() }

    //ddmmyyyy where d - day in month, m - month in year, y - year
    fun dateIsNotValid(date: String): Boolean {
        val (day, month, year) = date.split("-")
        val februaryDaysCount = if (year.toInt() % 4 == 0) {
            29
        } else {
            28
        }
        val monthDaysCount = when (month.toInt()) {
            2 -> {
                februaryDaysCount
            }
            4, 6, 9, 11 -> {
                30
            }
            else -> {
                31
            }
        }
        return day.toInt() > monthDaysCount || month.toInt() > 12
    }
}
