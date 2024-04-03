package com.example.gloomhavestoryline2


import com.example.gloomhavestoryline2.other.TextInputError
import com.example.gloomhavestoryline2.view_model.AuthViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class LoginUnitTest {

    private val emptyField = ""
    private val nickname = "mario"
    private val validEmail = "mario@rossi.it"
    private val invalidEmail = "rossi_mario.it"
    private val validPassword = "mario1234"
    private val invaliPassword = "marioo"


    @Test
    fun nickname_isCorrect() {
        assertEquals(TextInputError(emptyField).nicknameValidation(),R.string.empty_field)
        assertEquals(TextInputError(nickname).nicknameValidation(),null)
    }

    @Test
    fun email_isCorrect(){
        assertEquals(TextInputError(validEmail).emailValidation(),null)
        assertEquals(TextInputError(invalidEmail).emailValidation(),R.string.error_format_invalid)
        assertEquals(TextInputError(emptyField).emailValidation(),R.string.empty_field)
    }

    @Test
    fun password_isCorrect(){
        assertEquals(TextInputError(validPassword).passwordValidation(),null)
        assertEquals(TextInputError(invaliPassword).passwordValidation(),R.string.error_too_short)
        assertEquals(TextInputError(emptyField).passwordValidation(),R.string.empty_field)
    }
}