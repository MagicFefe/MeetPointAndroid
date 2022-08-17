package com.swaptech.meet.presentation.utils

import android.util.Patterns
import com.swaptech.meet.presentation.MAX_ABOUT_FIELD_LENGTH
import com.swaptech.meet.presentation.MAX_CITY_NAME_LENGTH
import com.swaptech.meet.presentation.MAX_EMAIL_LENGTH
import com.swaptech.meet.presentation.MAX_NAME_SURNAME_LENGTH
import com.swaptech.meet.presentation.MAX_PASSWORD_LENGTH
import com.swaptech.meet.presentation.MAX_YEARS_COUNT
import com.swaptech.meet.presentation.MIN_PASSWORD_LENGTH
import com.swaptech.meet.presentation.MIN_YEARS_COUNT
import java.util.*

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
        val (day, month, year) = date.split("-").map { item -> item.toInt() }
        val februaryDaysCount = if (year % 4 == 0) {
            29
        } else {
            28
        }
        val monthDaysCount = when (month) {
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
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return day > monthDaysCount || month > 12 || year > currentYear
    }

    fun ageIsNotValid(
        dateStr: String,
        onAgeIsTooSmall: () -> Unit,
        onAgeIsTooBig: () -> Unit
    ): Boolean {
        val (day, month, year) = dateStr.split("-").map { item -> item.toInt() }
        val currYear = Calendar.getInstance().get(Calendar.YEAR)
        val currMonth = Calendar.getInstance().get(Calendar.MONTH)
        val currDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        return when(currYear - year) {
            MIN_YEARS_COUNT -> {
                val birthdayHasNotYetOccurred = currDay < day || currMonth < month
                if(birthdayHasNotYetOccurred) {
                    onAgeIsTooSmall()
                }
                birthdayHasNotYetOccurred
            }
            in (MIN_YEARS_COUNT+1..MAX_YEARS_COUNT) -> {
                false
            }
            else -> {
                onAgeIsTooBig()
                true
            }
        }
    }
}
