package com.unicorn.vamped;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicassoClient {
    public static void downloadImage(Context context,String url,ImageView img)
    {
        if(url != null && url.length()>0)
        {
            Picasso.get()
                    .load(url)
                    .resize(200, 200)
                    .centerCrop()
                    .into(img);
        }else
        {
            Picasso.get().load(R.drawable.ic_flower)  .into(img);
        }
    }
}
