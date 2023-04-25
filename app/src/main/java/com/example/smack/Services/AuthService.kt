package com.example.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.smack.Utilities.URL_REGISTER
import org.json.JSONObject

object AuthService {

    // replicate the POST register web request of postman in android studio
    fun registerService(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        // equivalent to postman 'body' data
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        // creating the web request and passing in the json string data
        val registerRequest = object: StringRequest(Method.POST, URL_REGISTER, Response.Listener { response ->
            println(response)
            complete(true)
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not register user: $error")
            complete(false)
        }) {

            // equivalent to postman 'headers' data, 'content-type' key value pair
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            // pass in the jsonBody, convert it to string since which is then converted to byteArray, since it needs a bytearray
            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(registerRequest)
    }
}