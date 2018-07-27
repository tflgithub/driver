package fodel.com.fodelscanner.scanner.api.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.library.treerecyclerview.annotation.TreeItemClass;

import java.util.ArrayList;

import fodel.com.fodelscanner.activity.item.CodDateItemPrent;
import fodel.com.fodelscanner.activity.item.CodMerchantItemPrent;
import fodel.com.fodelscanner.activity.item.DeliveryDateItemPrent;
import fodel.com.fodelscanner.activity.item.DeliveryParcelItem;
import fodel.com.fodelscanner.activity.item.InvoiceItem;

/**
 * Created by fula on 2017/7/24.
 */

public class ResHistory extends BaseEntity implements Parcelable{

    public ResHistory() {
    }

    public Delivery pickup, delivery;

    public Cod cod;

    protected ResHistory(Parcel in) {
        pickup = in.readParcelable(Delivery.class.getClassLoader());
        delivery = in.readParcelable(Delivery.class.getClassLoader());
        cod = in.readParcelable(Cod.class.getClassLoader());
    }

    public static final Creator<ResHistory> CREATOR = new Creator<ResHistory>() {
        @Override
        public ResHistory createFromParcel(Parcel in) {
            return new ResHistory(in);
        }

        @Override
        public ResHistory[] newArray(int size) {
            return new ResHistory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(pickup, flags);
        dest.writeParcelable(delivery, flags);
        dest.writeParcelable(cod, flags);
    }


    public static class Cod implements Parcelable {
        public Cod() {
        }

        public String name, amount;

        public ArrayList<CodDateItem> codDateItem;

        @TreeItemClass(iClass = CodDateItemPrent.class)
        public static class CodDateItem implements Parcelable {

            public CodDateItem() {
            }

            public String date, amount;

            public ArrayList<CodMerchantItems> codMerchantItems;

            protected CodDateItem(Parcel in) {
                date = in.readString();
                amount = in.readString();
                codMerchantItems = in.createTypedArrayList(CodMerchantItems.CREATOR);
            }

            public static final Creator<CodDateItem> CREATOR = new Creator<CodDateItem>() {
                @Override
                public CodDateItem createFromParcel(Parcel in) {
                    return new CodDateItem(in);
                }

                @Override
                public CodDateItem[] newArray(int size) {
                    return new CodDateItem[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(date);
                dest.writeString(amount);
                dest.writeTypedList(codMerchantItems);
            }

            @TreeItemClass(iClass = CodMerchantItemPrent.class)
            public static class CodMerchantItems implements Parcelable {

                public CodMerchantItems() {
                }

                public String name, amount;

                public ArrayList<InvoiceItems> invoiceItems;

                protected CodMerchantItems(Parcel in) {
                    name = in.readString();
                    amount = in.readString();
                    invoiceItems = in.createTypedArrayList(InvoiceItems.CREATOR);
                }

                public static final Creator<CodMerchantItems> CREATOR = new Creator<CodMerchantItems>() {
                    @Override
                    public CodMerchantItems createFromParcel(Parcel in) {
                        return new CodMerchantItems(in);
                    }

                    @Override
                    public CodMerchantItems[] newArray(int size) {
                        return new CodMerchantItems[size];
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
                    dest.writeTypedList(invoiceItems);
                }

                @TreeItemClass(iClass = InvoiceItem.class)
                public static class InvoiceItems implements Parcelable {

                    public InvoiceItems() {
                    }

                    public String time, billNo, amount, currency;
                    public int intAmount;

                    protected InvoiceItems(Parcel in) {
                        time = in.readString();
                        billNo = in.readString();
                        amount = in.readString();
                        intAmount = in.readInt();
                        currency = in.readString();
                    }

                    public static final Creator<InvoiceItems> CREATOR = new Creator<InvoiceItems>() {
                        @Override
                        public InvoiceItems createFromParcel(Parcel in) {
                            return new InvoiceItems(in);
                        }

                        @Override
                        public InvoiceItems[] newArray(int size) {
                            return new InvoiceItems[size];
                        }
                    };

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeString(time);
                        dest.writeString(billNo);
                        dest.writeString(amount);
                        dest.writeInt(intAmount);
                        dest.writeString(currency);
                    }
                }
            }
        }

        protected Cod(Parcel in) {
            name = in.readString();
            amount = in.readString();
            codDateItem = in.createTypedArrayList(CodDateItem.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(amount);
            dest.writeTypedList(codDateItem);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Cod> CREATOR = new Creator<Cod>() {
            @Override
            public Cod createFromParcel(Parcel in) {
                return new Cod(in);
            }

            @Override
            public Cod[] newArray(int size) {
                return new Cod[size];
            }
        };
    }


    public static class Delivery implements Parcelable {
        public Delivery() {
        }

        public String name;

        public String amount;

        public ArrayList<SubItem> subItem = new ArrayList<>();

        protected Delivery(Parcel in) {
            name = in.readString();
            amount = in.readString();
            subItem = in.createTypedArrayList(SubItem.CREATOR);
        }

        public static final Creator<Delivery> CREATOR = new Creator<Delivery>() {
            @Override
            public Delivery createFromParcel(Parcel in) {
                return new Delivery(in);
            }

            @Override
            public Delivery[] newArray(int size) {
                return new Delivery[size];
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
            dest.writeTypedList(subItem);
        }

        public static class SubItem implements Parcelable {

            public SubItem() {
            }

            public String name;

            public String amount;

            public ArrayList<DateItem> dateItems = new ArrayList<>();

            protected SubItem(Parcel in) {
                name = in.readString();
                amount = in.readString();
                dateItems = in.createTypedArrayList(DateItem.CREATOR);
            }

            public static final Creator<SubItem> CREATOR = new Creator<SubItem>() {
                @Override
                public SubItem createFromParcel(Parcel in) {
                    return new SubItem(in);
                }

                @Override
                public SubItem[] newArray(int size) {
                    return new SubItem[size];
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
                dest.writeTypedList(dateItems);
            }

            @TreeItemClass(iClass = DeliveryDateItemPrent.class)
            public static class DateItem implements Parcelable {

                public DateItem() {
                }

                public String date;

                public String amount;

                public ArrayList<ParcelItem> parcelItems = new ArrayList<>();

                protected DateItem(Parcel in) {
                    date = in.readString();
                    amount = in.readString();
                    parcelItems = in.createTypedArrayList(ParcelItem.CREATOR);
                }

                public static final Creator<DateItem> CREATOR = new Creator<DateItem>() {
                    @Override
                    public DateItem createFromParcel(Parcel in) {
                        return new DateItem(in);
                    }

                    @Override
                    public DateItem[] newArray(int size) {
                        return new DateItem[size];
                    }
                };

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(date);
                    dest.writeString(amount);
                    dest.writeTypedList(parcelItems);
                }

                @TreeItemClass(iClass = DeliveryParcelItem.class)
                public static class ParcelItem implements Parcelable {

                    public ParcelItem() {
                    }

                    public String name;

                    public String awb;

                    public String time;

                    protected ParcelItem(Parcel in) {
                        name = in.readString();
                        awb = in.readString();
                        time = in.readString();
                    }

                    public static final Creator<ParcelItem> CREATOR = new Creator<ParcelItem>() {
                        @Override
                        public ParcelItem createFromParcel(Parcel in) {
                            return new ParcelItem(in);
                        }

                        @Override
                        public ParcelItem[] newArray(int size) {
                            return new ParcelItem[size];
                        }
                    };

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeString(name);
                        dest.writeString(awb);
                        dest.writeString(time);
                    }
                }
            }
        }
    }
}
