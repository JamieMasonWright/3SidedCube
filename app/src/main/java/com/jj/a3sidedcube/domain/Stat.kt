package com.jj.a3sidedcube.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Stat(
    val name: String,
    val url: String
) : Parcelable