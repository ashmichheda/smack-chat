package com.example.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.smack.Utilities.URL_LOGIN
import com.example.smack.Utilities.URL_REGISTER
import org.json.JSONException
import org.json.JSONObject

object AuthService {

    var isLoggedIn = false
    var userEmail = ""
    var authToken = ""


    // replicate the POST /account/register web request of postman in android studio
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

    // replicate the POST /account/login web request of postman in android studio (returns json object)
    fun loginUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {

        // equivalent to postman 'body' data
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val loginRequest = object: JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener {response ->

            // this is where we parse the json object
            println(response)
            // getString() throws JSONException incases when parsing json with a key that may not exist. Handling it using try/catch.
            try {
                authToken = response.getString("token")
                userEmail = response.getString("user")
                isLoggedIn = true
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC:" + e.localizedMessage)
                complete(false)
            }

        }, Response.ErrorListener {error ->
            // this is where we deal with the error
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
        Volley.newRequestQueue(context).add(loginRequest)

    }
}