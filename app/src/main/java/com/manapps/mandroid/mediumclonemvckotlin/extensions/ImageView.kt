package com.manapps.mandroid.mediumclonemvckotlin.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.manapps.mandroid.mediumclonemvckotlin.R

fun ImageView.loadImage(uri: String, circleCrop: Boolean = false) {
    if (!uri.isNullOrEmpty()) {
        if (circleCrop) {
            Glide.with(this).load(uri).circleCrop().into(this)
        } else {
            Glide.with(this).load(uri).into(this)
        }
    } else {
        this.setBackgroundResource(R.drawable.ic_launcher_background)
    }
}