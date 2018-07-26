package com.kikopalomares.googlesheetsexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var LOG_TAG = "kikopalomares"

    var SPREAD_SHEET_ID = "1jBtXZdoxIYJlEAnJ8YbQ3NbUmPrBFqgtSbmMHMIQMck"
    var TABLE_USERS = "users"
    var TABLE_PETS = "pets"

    //IMPORTANTE: estas URL tienes que cambiarlas por las de tus propios scripts
    var sheetInJsonURL = "https://script.google.com/macros/s/AKfycbzzUzP9c9uSwEPT22BA-Klan9uKniUPR60IvojXohC6wjmY1no/exec?spreadsheetId=$SPREAD_SHEET_ID&sheet="
    val addRowURL = "https://script.google.com/macros/s/AKfycby45N3EBA7Zn2DxLyOgCL67GY4lxJXyKiGIC03c-z-SKZm99Kz0/exec"

    private lateinit var recyclerUserAdapter: RecyclerUserAdapter
    private lateinit var recyclerPetAdapter: RecyclerPetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Podemos cambiar entre una u otra funcion para ver unos datos u otros en la lista
        getUsers()
        //getPets()

        //Descomenta esta linea para añadir usuarios a la hoja de calculo
        addUsers()
    }

    fun initList(){
        recyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL

        llm.reverseLayout = true
        llm.stackFromEnd = true

        recyclerView.layoutManager = llm

        progressBar.visibility = View.GONE
    }

    /**
     * Obtenemos los datos de la hoja de users
     */
    fun getUsers(){
        val queue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, sheetInJsonURL + TABLE_USERS, null,
                Response.Listener { response ->
                    Log.i(LOG_TAG, "Response is: $response")

                    val gson = Gson()
                    val jsonArray: JsonArray = gson.fromJson<JsonArray>(response.toString(), JsonArray::class.java)

                    for (element: JsonElement in jsonArray){
                        Log.i(LOG_TAG, element.asJsonObject.toString())
                    }

                    recyclerUserAdapter = RecyclerUserAdapter(jsonArray)
                    initList()
                    recyclerView.adapter = recyclerUserAdapter

                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                    Log.e(LOG_TAG, "That didn't work!")
                }
        )
        queue.add(jsonArrayRequest)
    }

    /**
     * Obtenemos los datos de la hoja de pets
     */
    fun getPets(){
        val queue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest( Request.Method.GET,sheetInJsonURL + TABLE_PETS, null,
                Response.Listener { response ->
                    Log.i(LOG_TAG, "Response is: $response")

                    val gson = Gson()
                    val jsonArray: JsonArray = gson.fromJson<JsonArray>(response.toString(), JsonArray::class.java)

                    for (element: JsonElement in jsonArray){
                        Log.i(LOG_TAG, element.asJsonObject.toString())
                    }

                    recyclerPetAdapter = RecyclerPetAdapter(jsonArray)
                    initList()
                    recyclerView.adapter = recyclerPetAdapter

                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                    Log.e(LOG_TAG, "That didn't work!")
                }
        )
        queue.add(jsonArrayRequest)
    }


    /**
     * Añadimos nuevos usuarios a la hoja de calculo
     */
    fun addUsers(){

        /* Montamos un JSON como este:
         * {"spreadsheet_id":"1jBtXZdoxIYJlEAnJ8YbQ3NbUmPrBFqgtSbmMHMIQMck", "sheet": "users", "rows":[["4", "Juan", "juan@gmail.com"], ["5", "Maria", "maria@gmail.com"]]}
         */

        val jsonObject = JSONObject()
        jsonObject.put("spreadsheet_id", SPREAD_SHEET_ID)
        jsonObject.put("sheet", TABLE_USERS)

        val rowsArray = JSONArray()

        val row1 = JSONArray()
        row1.put("4")
        row1.put("Juan")
        row1.put("juan@gmail.com")

        val row2 = JSONArray()
        row2.put("5")
        row2.put("Maria")
        row2.put("maria@gmail.com")

        rowsArray.put(row1)
        rowsArray.put(row2)

        jsonObject.put("rows", rowsArray)

        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest( addRowURL, jsonObject,
                Response.Listener { response ->
                    Log.i(LOG_TAG, "Response is: $response")
                    Toast.makeText(this, "Users added", Toast.LENGTH_LONG).show()

                    //Refrescamos la lista de usuarios
                    getUsers()
                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                    Log.e(LOG_TAG, "That didn't work!")
                }
        )
        queue.add(jsonObjectRequest)


    }
}
