package com.example.smack

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
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

    }
}