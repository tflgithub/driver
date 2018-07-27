package fodel.com.fodelscanner.scanner.api.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.library.treerecyclerview.annotation.TreeItemClass;

import java.util.ArrayList;
import java.util.List;

import fodel.com.fodelscanner.activity.item.scan.ScanItem;
import fodel.com.fodelscanner.activity.item.scan.ScanItemParent;

/**
 * Created by Administrator on 2017/9/13.
 */

public class ResScanBean extends BaseEntity {

    public ArrayList<Bill> scanned = new ArrayList<>();

    public ArrayList<Bill> remaining = new ArrayList<>();

    public boolean need_print;

    @TreeItemClass(iClass = ScanItemParent.class)
    public static class Bill implements Parcelable {

        public Bill() {
        }

        public String site_id;

        public String collection_point_name;

        public ArrayList<Awb> awbs = new ArrayList<>();

        protected Bill(Parcel in) {
            site_id = in.readString();
            collection_point_name = in.readString();
            awbs = in.createTypedArrayList(Awb.CREATOR);
        }

        public static final Creator<Bill> CREATOR = new Creator<Bill>() {
            @Override
            public Bill createFromParcel(Parcel in) {
                return new Bill(in);
            }

            @Override
            public Bill[] newArray(int size) {
                return new Bill[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(site_id);
            dest.writeString(collection_point_name);
            dest.writeTypedList(awbs);
        }

        @TreeItemClass(iClass = ScanItem.class)
        public static class Awb implements Parcelable {

            public Awb() {
            }

            public String awb;

            public boolean verified;

            public int pieces;

            public int scanNumber;

            protected Awb(Parcel in) {
                awb = in.readString();
                verified = in.readByte() != 0;
                pieces = in.readInt();
                scanNumber = in.readInt();
            }

            public static final Creator<Awb> CREATOR = new Creator<Awb>() {
                @Override
                public Awb createFromParcel(Parcel in) {
                    return new Awb(in);
                }

                @Override
                public Awb[] newArray(int size) {
                    return new Awb[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(awb);
                dest.writeByte((byte) (verified ? 1 : 0));
                dest.writeInt(pieces);
                dest.writeInt(scanNumber);
            }
        }
    }
}
