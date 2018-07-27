package fodel.com.fodelscanner.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import java.util.List;

import fodel.com.fodelscanner.Constants;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.utils.BitmapFile;
import fodel.com.fodelscanner.utils.SPUtils;
import fodel.com.fodelscanner.view.model.DrawerItem;
import fodel.com.fodelscanner.view.model.DrawerMenu;

public class DrawerItemAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<DrawerItem> drawerItems;
    private OnItemClickListener listener;
    private Context context;
    public static ImageView head_img;

    public DrawerItemAdapter(List<DrawerItem> drawerItems, Context context) {
        this.drawerItems = drawerItems;
        this.context = context;
    }

    private View mHeaderView;

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(1);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        switch (DrawerItem.Type.values()[viewType]) {
            case HEADER:
                View headerRootView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.drawer_header, parent, false);
                return new HeaderViewHolder(headerRootView);
            case DIVIDER:
                View dividerRootView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.drawer_divider, parent, false);
                return new DividerViewHolder(dividerRootView);
            case MENU:
                View menuRootView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.drawer_menu, parent, false);
                return new MenuViewHolder(menuRootView);
            case MENU_HEAD:
                return new MenuHeadViewHolder(mHeaderView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,
                                 final int position) {
        DrawerItem drawerItem = drawerItems.get(position);
        switch (drawerItem.getType()) {
            case MENU:
                MenuViewHolder menuViewHolder = (MenuViewHolder) holder;
                DrawerMenu drawerMenu = (DrawerMenu) drawerItem;
                menuViewHolder.itemTextView.setText(drawerMenu.getText());
                menuViewHolder.itemTextView
                        .setCompoundDrawablesWithIntrinsicBounds(
                                drawerMenu.getIconRes(), 0, 0, 0);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null)
                            listener.onClick(view, position);
                    }
                });
                break;
            default:
                break;

        }
    }


    @Override
    public int getItemViewType(int position) {
        return drawerItems.get(position).getType().ordinal();
    }

    @Override
    public int getItemCount() {
        return drawerItems.size();
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View rootView) {
            super(rootView);
            head_img = (ImageView) rootView.findViewById(R.id.profile_image);
            TextView tv_name = (TextView) rootView.findViewById(R.id.user_name);
            tv_name.setText(SPUtils.get(context, Constants.FIRST_NAME, "") + " " + SPUtils.get(context, Constants.LAST_NAME, ""));
            BitmapUtils bitmapUtils = new BitmapUtils(context);
            bitmapUtils.display(head_img, (String) SPUtils.get(context, Constants.PORTRAIT, ""), new BitmapLoadCallBack<ImageView>() {
                @Override
                public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                    if (bitmap != null) {
                        bitmap = BitmapFile.toRoundCorner(bitmap, 180);
                        imageView.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {

                }
            });
        }
    }


    class DividerViewHolder extends RecyclerView.ViewHolder {

        public DividerViewHolder(View rootView) {
            super(rootView);
        }
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {

        private TextView itemTextView;

        public MenuViewHolder(View rootView) {
            super(rootView);
            itemTextView = (TextView) rootView.findViewById(R.id.item);
        }
    }

    class MenuHeadViewHolder extends RecyclerView.ViewHolder {
        public MenuHeadViewHolder(View itemView) {
            super(itemView);
        }
    }


    public interface OnItemClickListener {
        void onClick(View view, int position);
    }
}