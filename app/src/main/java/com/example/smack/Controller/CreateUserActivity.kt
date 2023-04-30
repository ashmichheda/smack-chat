package com.example.smack.Controller

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.widget.AutoScrollHelper
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.smack.R
import com.example.smack.Services.AuthService
import com.example.smack.Utilities.BROADCAST_USER_DATA_CHANGE
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"
    private lateinit var createSpinner: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        createSpinner = findViewById<ProgressBar>(R.id.createSpinner)
        createSpinner.visibility = View.INVISIBLE

    }

    fun generateUserAvatar(view: View) {
        val random = Random()
        val color = random.nextInt(2) // Randomly generates number between 0 and 1, excluding 2 (upperbound)
        val avatar = random.nextInt(28)

        if (color == 0) { // light color
            userAvatar = "light$avatar"
        } else { // dark color
            userAvatar = "dark$avatar"
        }
        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
        val createAvatarImgView: ImageView = findViewById(R.id.createAvatarImgView)
        createAvatarImgView.setImageResource(resourceId)
    }

    fun generateColorClicked(view: View) {
        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)
        val createAvatarImgView: ImageView = findViewById(R.id.createAvatarImgView)
        createAvatarImgView.setBackgroundColor(Color.rgb(r, g, b))

        // convert these r, g, b values on a scale between 0 - 1 that is saved in api and avatarColor to sync with ioS
        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255

        avatarColor = "[$savedR, $savedG, $savedB, 1]"
    }

    fun createUserClicked(view: View) {
        enableSpinner(true)
        val userName = findViewById<TextView>(R.id.createUserNameTxt).text.toString()
        val email: String = findViewById<TextView?>(R.id.createEmailTxt).text.toString()
        val password = findViewById<TextView>(R.id.createPasswordTxt).text.toString()

        // input checks to make sure is not empty
        if (userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.registerService(this, email, password) {registerSuccess ->
                if (registerSuccess) {
                    println("User successfully registered")
                    AuthService.loginUser(this, email, password) {loginSuccess ->
                        if (loginSuccess) {
                            AuthService.createUser(this, userName, email, userAvatar, avatarColor) {createSuccess ->
                                if (createSuccess) {
                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)
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
                    errorToast()
                }
            }
        } else {
            Toast.makeText(this, "Make sure username, email & password are filled in.", Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }
    }

    fun errorToast() {
        Toast.makeText(this, "Something went wrong, Please try again.", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean) {
        val createUserBtn = findViewById<Button>(R.id.createUserBtn)
        val createAvatarImageView = findViewById<ImageView>(R.id.createAvatarImgView)
        val backgroundColorBtn = findViewById<Button>(R.id.backgroundColorBtn)

        if (enable) {
            createSpinner.visibility = View.VISIBLE
        } else {
            createSpinner.visibility = View.INVISIBLE
        }
        createUserBtn.isEnabled = !enable
        createAvatarImageView.isEnabled = !enable
        backgroundColorBtn.isEnabled = !enable
    }
}