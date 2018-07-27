package fodel.com.fodelscanner.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by fula on 2018/6/12.
 */

public class ShareUtils {

    public static void whatsAppShare(Context context, String content) {
        if (AppUtils.isAvailable(context, "com.whatsapp")) {
            Intent vIt = new Intent("android.intent.action.SEND");
            vIt.setPackage("com.whatsapp");
            vIt.setType("text/plain");
            vIt.putExtra(Intent.EXTRA_TEXT, content);
            context.startActivity(vIt);
        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
    }

    public static void telegramShare(Context context, String content) {
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(Uri.parse("smsto:" + ""));
        sendIntent.putExtra("sms_body", content);
        context.startActivity(sendIntent);
    }
}
