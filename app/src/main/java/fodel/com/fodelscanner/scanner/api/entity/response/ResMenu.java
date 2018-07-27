package fodel.com.fodelscanner.scanner.api.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by fula on 2017/7/21.
 */

public class ResMenu extends BaseEntity implements Parcelable {

    public String name;

    public String amount;

    public String draw_id;

    public String class_name;

    public String type;

    public List<SubMenu> list;

    protected ResMenu(Parcel in) {
        name = in.readString();
        amount = in.readString();
        draw_id = in.readString();
        class_name = in.readString();
        type = in.readString();
        list = in.createTypedArrayList(SubMenu.CREATOR);
    }

    public static final Creator<ResMenu> CREATOR = new Creator<ResMenu>() {
        @Override
        public ResMenu createFromParcel(Parcel in) {
            return new ResMenu(in);
        }

        @Override
        public ResMenu[] newArray(int size) {
            return new ResMenu[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(amount);
        dest.writeString(draw_id);
        dest.writeString(class_name);
        dest.writeString(type);
        dest.writeTypedList(list);
    }

    public static class SubMenu  implements Parcelable{

        public SubMenu(){

        }

        public String name;

        public String amount;

        public String class_name;

        public String type;

        public List<Ecommerce> list;

        protected SubMenu(Parcel in) {
            name = in.readString();
            amount = in.readString();
            class_name = in.readString();
            type = in.readString();
            list = in.createTypedArrayList(Ecommerce.CREATOR);
        }

        public static final Creator<SubMenu> CREATOR = new Creator<SubMenu>() {
            @Override
            public SubMenu createFromParcel(Parcel in) {
                return new SubMenu(in);
            }

            @Override
            public SubMenu[] newArray(int size) {
                return new SubMenu[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(amount);
            dest.writeString(class_name);
            dest.writeString(type);
            dest.writeTypedList(list);
        }
    }

    public static class Ecommerce  implements Parcelable{

        public Ecommerce(){}

        public String id;

        public String name;

        public String amount;

        protected Ecommerce(Parcel in) {
            id = in.readString();
            name = in.readString();
            amount = in.readString();
        }

        public static final Creator<Ecommerce> CREATOR = new Creator<Ecommerce>() {
            @Override
            public Ecommerce createFromParcel(Parcel in) {
                return new Ecommerce(in);
            }

            @Override
            public Ecommerce[] newArray(int size) {
                return new Ecommerce[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(name);
            dest.writeString(amount);
        }
    }

    public ResMenu() {
    }

}
