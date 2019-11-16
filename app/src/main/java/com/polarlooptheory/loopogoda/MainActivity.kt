package com.polarlooptheory.loopogoda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.*


class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private var json: JSONObject = JSONObject()
    private var name = String()
    private var ene = true
    private var obj = Forecast(.0,.0,.0,0,.0,0,.0,0,"","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            name = cityText.text.toString().trim().capitalize()
            if (name != "" && ene) {
                ene = false
                run(resources.getString(R.string.api_addres) + name)
                while(!ene)
                    tmpText.text = obj.read(Forecast.ITEM.TMP)
                    humText.text = obj.read(Forecast.ITEM.HUMIDITY)
                    hpaText.text = obj.read(Forecast.ITEM.PRESSURE)
                    windText.text = obj.read(Forecast.ITEM.WIND)
                    pogText.text = obj.read(Forecast.ITEM.DESC)
                    when (obj.read(Forecast.ITEM.ICON)){
                        "01d" -> imgText.setImageResource(R.drawable.i01d)
                        "01n" -> imgText.setImageResource(R.drawable.i01n)
                        "02d" -> imgText.setImageResource(R.drawable.i02d)
                        "02n" -> imgText.setImageResource(R.drawable.i02n)
                        "03d" -> imgText.setImageResource(R.drawable.i03d)
                        "03n" -> imgText.setImageResource(R.drawable.i03n)
                        "04d" -> imgText.setImageResource(R.drawable.i04d)
                        "04n" -> imgText.setImageResource(R.drawable.i04n)
                        "09d" -> imgText.setImageResource(R.drawable.i09d)
                        "09n" -> imgText.setImageResource(R.drawable.i09n)
                        "10d" -> imgText.setImageResource(R.drawable.i10d)
                        "10n" -> imgText.setImageResource(R.drawable.i10n)
                        "11d" -> imgText.setImageResource(R.drawable.i11d)
                        "11n" -> imgText.setImageResource(R.drawable.i11n)
                        "13d" -> imgText.setImageResource(R.drawable.i13d)
                        "13n" -> imgText.setImageResource(R.drawable.i13n)
                        "50d" -> imgText.setImageResource(R.drawable.i50d)
                        "50n" -> imgText.setImageResource(R.drawable.i50n)
                    }

            }
        }
    }

    private fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()
        runBlocking {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    ene = true
                }

                override fun onResponse(call: Call, response: Response) {
                    json = JSONObject(response.body()?.string())
                    if (json.getString("cod") == "200") {
                        val arr = json.getJSONArray("list")
                        val el = JSONObject(arr[0].toString())
                        val date = el.getLong("dt")
                        val main = el.getJSONObject("main")
                        val tmp = main.getDouble("temp")
                        val tmp_min = main.getDouble("temp_min")
                        val tmp_max = main.getDouble("temp_max")
                        val pressure = main.getDouble("pressure")
                        val humidity = main.getInt("humidity")
                        val clouds = el.getJSONObject("clouds").getInt("all")
                        val wind = el.getJSONObject("wind").getDouble("speed")
                        val weather = el.getJSONArray("weather").getJSONObject(0)
                        val icon = weather.getString("icon")
                        val desc = weather.getString("description")
                        obj = Forecast(tmp, tmp_min, tmp_max, humidity, pressure, clouds, wind, date, desc, icon)
                        // i wypisujesz textboxy w ten sposób: "textbox1.text = obj.read(Forecast.ITEM.'i co tu chcesz')", ew. dodajesz .toString()
                        /*val arg = Bundle()
                        arg.putParcelable("forecast", obj)
                        arg.putString("name", name)*/ //niepotrzebne, ale na zapas

                    } else {
                    }
                    ene = true
                }



            })}
    }
}
