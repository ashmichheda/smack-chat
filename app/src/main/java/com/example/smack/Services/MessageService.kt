package com.example.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.smack.Controller.App
import com.example.smack.Model.Channel
import com.example.smack.Model.Message
import com.example.smack.Utilities.URL_GET_CHANNELS
import com.example.smack.Utilities.URL_GET_MESSAGES
import org.json.JSONException

object MessageService {

    val channels = ArrayList<Channel>()
    val messages = ArrayList<Message>()

    fun getChannels(complete: (Boolean) -> Unit) {
        val channelsRequest = object : JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null, Response.Listener { response ->
            try {
                for (x in 0 until response.length()) {
                    val channel = response.getJSONObject(x)
                    val name = channel.getString("name")
                    val channelDesc = channel.getString("description")
                    val channelId = channel.getString("_id")

                    val newChannel = Channel(name, channelDesc, channelId)
                    this.channels.add(newChannel)
                }
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC:" + e.localizedMessage)
                complete(false)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not retrieve channels.")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers;
            }
        }
        App.prefs.requestQueue.add(channelsRequest)
    }

    fun getMessages(channelId: String, complete: (Boolean) -> Unit) {
        val url = "$URL_GET_MESSAGES$channelId"
        clearMessages()
        val messageRequest = object: JsonArrayRequest(Method.GET, url, null, Response.Listener { response ->
            try {
                for (x in 0 until response.length()) {
                    val channel = response.getJSONObject(x)
                    val messageBody = channel.getString("messageBody")
                    val userId = channel.getString("userId")
                    val channelId = channel.getString("channelId")
                    val id = channel.getString("_id")
                    val userName = channel.getString("userName")
                    val userAvatar = channel.getString("userAvatar")
                    val userAvatarColor = channel.getString("userAvatarColor")
                    val timeStamp = channel.getString("timeStamp")

                    val newMessage = Message(messageBody, userName, channelId, userAvatar, userAvatarColor, id, timeStamp)
                    this.messages.add(newMessage)
                }
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC:" + e.localizedMessage)
                complete(false)
            }


        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not retrieve channels.")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers;
            }
        }
        App.prefs.requestQueue.add(messageRequest)
    }

    fun clearMessages() {
        messages.clear()
    }

    fun clearChannels() {
        channels.clear()
    }
}