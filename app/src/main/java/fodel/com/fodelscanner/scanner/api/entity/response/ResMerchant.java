package fodel.com.fodelscanner.scanner.api.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by fula on 2017/7/21.
 */

public class ResMerchant extends BaseEntity implements Parcelable {

    public ResMerchant() {
    }

    public List<TypeMark> type_mark;

    public String distance_mark;

    public String area;

    public String address;

    public String phone_number;

    public List<String> work_status_mark;

    public double lg;

    public double lt;

    public String station_id;

    public String site_id;

    public String station_name;

    public String sms_text;

    protected ResMerchant(Parcel in) {
        type_mark = in.readArrayList(TypeMark.class.getClassLoader());
        distance_mark = in.readString();
        area = in.readString();
        address = in.readString();
        phone_number=in.readString();
        work_status_mark = in.createStringArrayList();
        lg = in.readDouble();
        lt = in.readDouble();
        station_id = in.readString();
        site_id = in.readString();
        station_name = in.readString();
    }

    public static final Creator<ResMerchant> CREATOR = new Creator<ResMerchant>() {
        @Override
        public ResMerchant createFromParcel(Parcel in) {
            return new ResMerchant(in);
        }

        @Override
        public ResMerchant[] newArray(int size) {
            return new ResMerchant[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(type_mark);
        dest.writeString(distance_mark);
        dest.writeString(area);
        dest.writeString(phone_number);
        dest.writeString(address);
        dest.writeStringList(work_status_mark);
        dest.writeDouble(lg);
        dest.writeDouble(lt);
        dest.writeString(station_id);
        dest.writeString(site_id);
        dest.writeString(station_name);
    }

    public static class TypeMark implements Parcelable {

        public TypeMark() {
        }

        public String label_name;

        @Deprecated
        public String button_name;

        public String type;

        protected TypeMark(Parcel in) {
            label_name = in.readString();
            button_name = in.readString();
            type = in.readString();
        }

        public static final Creator<TypeMark> CREATOR = new Creator<TypeMark>() {
            @Override
            public TypeMark createFromParcel(Parcel in) {
                return new TypeMark(in);
            }

            @Override
            public TypeMark[] newArray(int size) {
                return new TypeMark[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(label_name);
            dest.writeString(button_name);
            dest.writeString(type);
        }
    }
}
