package fodel.com.fodelscanner.scanner.api.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by fula on 2017/12/7.
 */

public class ResInvoice extends BaseEntity implements Parcelable {
    public ResInvoice(){}

    public Money debt_money;

    public Money pending_money;

    public ArrayList<Bill> with_driver;

    public ArrayList<Bill> pending;

    protected ResInvoice(Parcel in) {
        with_driver = in.createTypedArrayList(Bill.CREATOR);
        pending = in.createTypedArrayList(Bill.CREATOR);
    }

    public static final Creator<ResInvoice> CREATOR = new Creator<ResInvoice>() {
        @Override
        public ResInvoice createFromParcel(Parcel in) {
            return new ResInvoice(in);
        }

        @Override
        public ResInvoice[] newArray(int size) {
            return new ResInvoice[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(with_driver);
        dest.writeTypedList(pending);
    }


    public static class Money implements  Parcelable{
        public Money(){}
        public int amount;
        public String currency;

        protected Money(Parcel in) {
            amount = in.readInt();
            currency = in.readString();
        }

        public static final Creator<Money> CREATOR = new Creator<Money>() {
            @Override
            public Money createFromParcel(Parcel in) {
                return new Money(in);
            }

            @Override
            public Money[] newArray(int size) {
                return new Money[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(amount);
            dest.writeString(currency);
        }
    }

    public static class Bill implements Parcelable {
        public Bill() {
        }

        public String station_name;
        public String area;
        public String bill_no;
        public Money money;
        public ArrayList<Awb> awbs;

        protected Bill(Parcel in) {
            station_name = in.readString();
            area = in.readString();
            bill_no = in.readString();
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
            dest.writeString(station_name);
            dest.writeString(area);
            dest.writeString(bill_no);
            dest.writeTypedList(awbs);
        }
    }

    public static class Awb implements Parcelable {
        public Awb() {
        }

        public String awb;
        public Money money;


        protected Awb(Parcel in) {
            awb = in.readString();
            money = in.readParcelable(Money.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(awb);
            dest.writeParcelable(money, flags);
        }

        @Override
        public int describeContents() {
            return 0;
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
    }
}
