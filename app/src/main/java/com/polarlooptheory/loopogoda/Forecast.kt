package com.polarlooptheory.loopogoda

import android.icu.util.Calendar
import android.os.Parcel
import android.os.Parcelable

class Forecast(private val tmp:Double,private val tmp_min:Double,
                private val tmp_max:Double,private val humidity:Int,private val pressure:Double,private val clouds:Int,
                private val wind:Double,private val data:Long,private val desc:String,private val icon:String) : Parcelable {
    private val date : Calendar

    enum class ITEM{
        TMP,TMP_MAX,TMP_MIN,HUMIDITY,PRESSURE,CLOUDS,WIND,DESC,ICON,DAY,HOUR,DATE
    }
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    init {
        val data = Calendar.getInstance()
        data.timeInMillis = this.data*1000
        date = data
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(tmp)
        parcel.writeDouble(tmp_min)
        parcel.writeDouble(tmp_max)
        parcel.writeInt(humidity)
        parcel.writeDouble(pressure)
        parcel.writeInt(clouds)
        parcel.writeDouble(wind)
        parcel.writeLong(data)
        parcel.writeString(desc)
        parcel.writeString(icon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Forecast> {
        override fun createFromParcel(parcel: Parcel): Forecast {
            return Forecast(parcel)
        }

        override fun newArray(size: Int): Array<Forecast?> {
            return arrayOfNulls(size)
        }
    }
    fun read(item: ITEM) =
        when(item) {
            ITEM.TMP -> Math.round(tmp).toString() + "°C"
            ITEM.TMP_MAX -> Math.round(tmp_max).toString() + "°C"
            ITEM.TMP_MIN -> Math.round(tmp_min).toString() + "°C"
            ITEM.HUMIDITY -> "$humidity%"
            ITEM.PRESSURE -> "$pressure hPa"
            ITEM.CLOUDS -> "$clouds%"
            ITEM.WIND -> "$wind m/s"
            ITEM.DESC -> desc
            ITEM.ICON -> icon
            ITEM.DAY -> getDayName(date.get(Calendar.DAY_OF_WEEK))
            ITEM.HOUR -> "${date.get(Calendar.HOUR_OF_DAY)}:00"
            ITEM.DATE -> "${date.get(Calendar.DAY_OF_MONTH)}.${date.get(Calendar.MONTH)}"
        }

    private fun getDayName(i: Int) = when(i){
        1->"Niedziela"
        2->"Poniedziałek"
        3->"Wtorek"
        4->"Środa"
        5->"Czwartek"
        6->"Piątek"
        7->"Sobota"
        else->""
    }
}