package com.mobile.xplore_nu.ui.utils

object Validators {

    /**
     * Validates whether the given string is a valid email address
     *
     * @return `true` if the email is valid, `false` otherwise.
     */
    fun String.isValidEmail(): Boolean {
        val emailRegex = Regex("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}")
        return matches(emailRegex)
    }

    /**
     * Validates whether the given string meets strong password requirements.
     *
     * A valid password must:
     * - Be at least **8 characters long**
     * - Contain at least **one uppercase letter**
     * - Contain at least **one lowercase letter**
     * - Contain at least **one digit**
     * - Contain at least **one special character** (e.g., @, #, $, %, etc.)
     *
     * @return `true` if the password meets all criteria, `false` otherwise.
     */
    fun String.isValidPassword(): Boolean {
        val passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$".toRegex()
        return matches(passwordRegex)
    }

    /**
     * Validates whether the given string is a valid full name.
     *
     * A valid full name:
     * - Can be **one or more words**
     * - Each word contains **only letters** (A-Z, a-z)
     * - Allows spaces between words but no special characters or numbers
     *
     * @return `true` if the name contains only valid characters, `false` otherwise.
     */
    fun String.isValidFullName(): Boolean {
        val nameRegex = Regex("^[a-zA-Z]+(\\s[a-zA-Z]+)*$")
        return nameRegex.matches(this)
    }

}