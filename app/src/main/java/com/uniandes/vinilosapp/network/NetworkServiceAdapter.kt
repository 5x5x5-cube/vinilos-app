package com.uniandes.vinilosapp.network

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.uniandes.vinilosapp.models.Album
import com.uniandes.vinilosapp.models.AlbumDetails
import com.uniandes.vinilosapp.models.Collector
import com.uniandes.vinilosapp.models.Comment
import com.uniandes.vinilosapp.models.Performer
import com.uniandes.vinilosapp.models.Track
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class NetworkServiceAdapter constructor(context: Context) {
    companion object{
        const val BASE_URL= "https://vynils-backend-2a8a921f0d28.herokuapp.com/"
        var instance: NetworkServiceAdapter? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: NetworkServiceAdapter(context).also {
                    instance = it
                }
            }
    }
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
    suspend fun getAlbums() = suspendCoroutine<List<Album>>{ cont->
        val list = mutableListOf<Album>()
        requestQueue.add(getRequest("albums",
            { response ->
                val resp = JSONArray(response)
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)
                    val album = Album(albumId = item.getInt("id"),name = item.getString("name"), cover = item.getString("cover"), recordLabel = item.getString("recordLabel"), releaseDate = item.getString("releaseDate"), genre = item.getString("genre"), description = item.getString("description"))
                    list.add(i, album)
                }
                cont.resume(list)
            },
            {
                throw it
            }))
    }

    suspend fun getAlbum(albumId:Int) = suspendCoroutine<AlbumDetails>{ cont->
        requestQueue.add(getRequest("albums/$albumId",
            { response ->
                val item = JSONObject(response)
                val arrayPerformer = JSONArray(item.getString("performers"))
                val arrayTracks = JSONArray(item.getString("tracks"))
                val listperformer = mutableListOf<Performer>()
                val listtracks = mutableListOf<Track>()
                for (i in 0 until arrayTracks.length())
                {
                    val item = arrayTracks.getJSONObject(i)
                    val track =  Track( trackId = item.getInt("id"), name = item.getString("name"), duration = item.getString("duration"))
                    listtracks.add(i, track)
                }
                for (i in 0 until arrayPerformer.length())
                {
                    val item = arrayPerformer.getJSONObject(i)
                    val perform =  Performer( performerID = item.getInt("id"), name = item.getString("name"), image = item.getString("image"), description = item.getString("description"))
                    listperformer.add(i, perform)
                }

                val album = AlbumDetails(albumId = item.getInt("id"),name = item.getString("name"), cover = item.getString("cover"), recordLabel = item.getString("recordLabel"), releaseDate = item.getString("releaseDate"), genre = item.getString("genre"), description = item.getString("description"), performers = listperformer, tracks = listtracks )
                cont.resume(album)
            },
            {
                throw it
            }))
    }

    private fun getRequest(path:String, responseListener: Response.Listener<String>, errorListener: Response.ErrorListener): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL+path, responseListener,errorListener)
    }
    private fun postRequest(path: String, body: JSONObject,  responseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener ):JsonObjectRequest{
        return  JsonObjectRequest(Request.Method.POST, BASE_URL+path, body, responseListener, errorListener)
    }
    private fun putRequest(path: String, body: JSONObject,  responseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener ):JsonObjectRequest{
        return  JsonObjectRequest(Request.Method.PUT, BASE_URL+path, body, responseListener, errorListener)
    }
}