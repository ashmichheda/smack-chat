package com.example.smack.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.smack.R
import com.example.smack.Services.AuthService

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginLoginBtnClicked(view: View) {
        val loginEmailTxt = findViewById<TextView>(R.id.loginEmailTxt)
        val loginPasswordTxt = findViewById<TextView>(R.id.loginPasswordTxt)

        val email = loginEmailTxt.text.toString()
        val password = loginPasswordTxt.text.toString()

        AuthService.loginUser(this, email, password) { loginSuccess ->
            if (loginSuccess) {
                AuthService.findUserByEmail(this) {findSuccess ->
                    if (findSuccess) {
                        finish()
                    } else {

                    }
                }
            } else {

            }
        }
    }

    fun loginCreateUserBtnClicked(view: View) {
        // When this button is clicked, go to CreateUserActivity page
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }
}