package com.example.smack.Controller

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.smack.R
import com.example.smack.Services.AuthService

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<ProgressBar>(R.id.loginSpinner).visibility = View.INVISIBLE
    }

    fun loginLoginBtnClicked(view: View) {
        enableSpinner(true)
        val loginEmailTxt = findViewById<TextView>(R.id.loginEmailTxt)
        val loginPasswordTxt = findViewById<TextView>(R.id.loginPasswordTxt)
        hideKeyboard()

        val email = loginEmailTxt.text.toString()
        val password = loginPasswordTxt.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.loginUser(this, email, password) { loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserByEmail(this) {findSuccess ->
                        if (findSuccess) {
                            enableSpinner(false)
                            finish()
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
                }
            }
        } else {
            Toast.makeText(this, "Please fill in both email and password.", Toast.LENGTH_LONG).show()
        }

    }

    fun loginCreateUserBtnClicked(view: View) {
        // When this button is clicked, go to CreateUserActivity page
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }

    fun errorToast() {
        Toast.makeText(this, "Something went wrong, Please try again.", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean) {
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val loginCreateUserBtn = findViewById<Button>(R.id.loginCreateUserBtn)

        val loginSpinner = findViewById<ProgressBar>(R.id.loginSpinner)
        if (enable) {
            loginSpinner.visibility = View.VISIBLE
        } else {
            loginSpinner.visibility = View.INVISIBLE
        }
        loginBtn.isEnabled = !enable
        loginCreateUserBtn.isEnabled = !enable

    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }
}