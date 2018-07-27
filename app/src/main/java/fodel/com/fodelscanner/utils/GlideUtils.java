package fodel.com.fodelscanner.utils;
import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import fodel.com.fodelscanner.R;

/**
 * Created by fula on 2017/7/25.
 */

public class GlideUtils {

    /**
     * Circular images
     *
     * @param mContext
     * @param path
     * @param imageview
     * @param border
     * @param  resourceId
     */
    public static void loadCircleImage(Context mContext, String path,
                                       ImageView imageview, int border, int resourceId) {
        if (border == 0) {
            Glide.with(mContext).load(path).centerCrop().placeholder(R.drawable.touxiang)
                    .transform(new GlideRoundTransform(mContext, border, resourceId))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageview);
        } else {
            Glide.with(mContext).load(path).centerCrop().placeholder(R.drawable.touxiang)
                    .transform(new GlideRoundTransform(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageview);
        }
    }
}
