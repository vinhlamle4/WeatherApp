/* 
Copyright (c) 2020 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */
package com.example.weatherapp.model.location

import com.google.gson.annotations.SerializedName

data class Location (

	@SerializedName("Version") val version : Int,
	@SerializedName("Key") val Key : String,
	@SerializedName("Type") val type : String,
	@SerializedName("Rank") val rank : Int,
	@SerializedName("LocalizedName") val localizedName : String,
	@SerializedName("EnglishName") val englishName : String,
	@SerializedName("PrimaryPostalCode") val primaryPostalCode : String,
	@SerializedName("Region") val region : Region,
	@SerializedName("Country") val country : Country,
	@SerializedName("AdministrativeArea") val administrativeArea : AdministrativeArea,
	@SerializedName("TimeZone") val timeZone : TimeZone,
	@SerializedName("GeoPosition") val geoPosition : GeoPosition,
	@SerializedName("IsAlias") val isAlias : Boolean,
	@SerializedName("ParentCity") val parentCity : ParentCity,
	@SerializedName("SupplementalAdminAreas") val supplementalAdminAreas : List<String>,
	@SerializedName("DataSets") val dataSets : List<String>
)