package rpt.tool.mementobibere.basic.appbasiclibs.utils

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.module.GlideModule

class GlideConfiguration : GlideModule {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    //    @Override
    fun registerComponents(context: Context?, glide: Glide?) {
        // register ModelLoaders here.
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
    }
}