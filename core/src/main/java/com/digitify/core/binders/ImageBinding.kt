package com.digitify.core.binders

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.io.File


object ImageBinding {
    @BindingAdapter("bitmapSrc")
    @JvmStatic
    fun setBitmapSrc(view: AppCompatImageView, bitmap: Bitmap?) {
        if (bitmap != null)
            view.setImageBitmap(bitmap)
    }

    @JvmStatic
    @BindingAdapter("app:srcDrawable")
    fun setImageViewResource(imageView: AppCompatImageView, resource: Int) {
        imageView.setImageResource(resource)
    }

    @JvmStatic
    @BindingAdapter("filePath")
    fun setImageFile(imageView: AppCompatImageView, path: String) {
        Glide.with(imageView.context)
            .load(Uri.fromFile(File(path)))
//            .apply(RequestOptions().override(100, 100))
            .into(imageView)
    }
}
