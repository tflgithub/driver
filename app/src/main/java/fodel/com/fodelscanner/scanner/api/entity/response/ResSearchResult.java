package fodel.com.fodelscanner.scanner.api.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fula on 2017/7/24.
 */

public class ResSearchResult extends BaseEntity implements Parcelable {

    public String awb;

    public String orderNo;

    public String recipientPhoneNumber;

    public String recipientName;

    public String address;

    public String weight;

    public String isCashDelivery;

    public Money price;

    public String status;

    public CollectionPoint collectionPointEntity;

    public Ecommerce ecommerceEntity;

    public Driver assignedDriverEntity;

    public WareHouse warehouseDetailDto;

    public ResSearchResult() {
    }

    protected ResSearchResult(Parcel in) {
        awb = in.readString();
        orderNo = in.readString();
        recipientPhoneNumber = in.readString();
        recipientName = in.readString();
        address = in.readString();
        weight = in.readString();
        isCashDelivery = in.readString();
        price = in.readParcelable(Money.class.getClassLoader());
        status = in.readString();
        collectionPointEntity = in.readParcelable(CollectionPoint.class.getClassLoader());
        ecommerceEntity = in.readParcelable(Ecommerce.class.getClassLoader());
        assignedDriverEntity = in.readParcelable(Driver.class.getClassLoader());
    }

    public static final Creator<ResSearchResult> CREATOR = new Creator<ResSearchResult>() {
        @Override
        public ResSearchResult createFromParcel(Parcel in) {
            return new ResSearchResult(in);
        }

        @Override
        public ResSearchResult[] newArray(int size) {
            return new ResSearchResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(awb);
        dest.writeString(orderNo);
        dest.writeString(recipientPhoneNumber);
        dest.writeString(recipientName);
        dest.writeString(address);
        dest.writeString(weight);
        dest.writeString(isCashDelivery);
        dest.writeParcelable(price, flags);
        dest.writeString(status);
        dest.writeParcelable(collectionPointEntity, flags);
        dest.writeParcelable(ecommerceEntity, flags);
        dest.writeParcelable(assignedDriverEntity, flags);
    }

    public static class Money implements Parcelable {
        public Money() {
        }

        public double amount;
        public String currency;

        protected Money(Parcel in) {
            amount = in.readDouble();
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
            dest.writeDouble(amount);
            dest.writeString(currency);
        }
    }

    public static class CollectionPoint implements Parcelable {
        public CollectionPoint() {
        }

        public String siteId;

        public String name;

        public String address;

        public Area areaEntity;

        public ManagerUser managerUserEntity;


        protected CollectionPoint(Parcel in) {
            siteId = in.readString();
            name = in.readString();
            address = in.readString();
            areaEntity = in.readParcelable(Area.class.getClassLoader());
            managerUserEntity = in.readParcelable(ManagerUser.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(siteId);
            dest.writeString(name);
            dest.writeString(address);
            dest.writeParcelable(areaEntity, flags);
            dest.writeParcelable(managerUserEntity, flags);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<CollectionPoint> CREATOR = new Creator<CollectionPoint>() {
            @Override
            public CollectionPoint createFromParcel(Parcel in) {
                return new CollectionPoint(in);
            }

            @Override
            public CollectionPoint[] newArray(int size) {
                return new CollectionPoint[size];
            }
        };
    }

    public static class Area implements Parcelable {
        public Area() {
        }

        public String name;

        protected Area(Parcel in) {
            name = in.readString();
        }

        public static final Creator<Area> CREATOR = new Creator<Area>() {
            @Override
            public Area createFromParcel(Parcel in) {
                return new Area(in);
            }

            @Override
            public Area[] newArray(int size) {
                return new Area[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
        }
    }

    public static class ManagerUser implements Parcelable {
        public ManagerUser() {
        }

        public String companyName;

        public String firstName;

        public String email;

        public String address;

        public String phone;

        protected ManagerUser(Parcel in) {
            companyName = in.readString();
            firstName = in.readString();
            email = in.readString();
            address = in.readString();
            phone = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(companyName);
            dest.writeString(firstName);
            dest.writeString(email);
            dest.writeString(address);
            dest.writeString(phone);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ManagerUser> CREATOR = new Creator<ManagerUser>() {
            @Override
            public ManagerUser createFromParcel(Parcel in) {
                return new ManagerUser(in);
            }

            @Override
            public ManagerUser[] newArray(int size) {
                return new ManagerUser[size];
            }
        };
    }


    public static class Ecommerce implements Parcelable {
        public Ecommerce() {
        }

        public int id;
        public String name;

        public String phone;

        public String address;

        public String email;

        public String returnDays;

        protected Ecommerce(Parcel in) {
            id = in.readInt();
            name = in.readString();
            phone = in.readString();
            address = in.readString();
            email = in.readString();
            returnDays = in.readString();
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
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeString(phone);
            dest.writeString(address);
            dest.writeString(email);
            dest.writeString(returnDays);
        }
    }

    public static class Driver implements Parcelable {

        public Driver() {
        }

        public String firstName;

        public String lastName;

        protected Driver(Parcel in) {
            firstName = in.readString();
            lastName = in.readString();
        }

        public static final Creator<Driver> CREATOR = new Creator<Driver>() {
            @Override
            public Driver createFromParcel(Parcel in) {
                return new Driver(in);
            }

            @Override
            public Driver[] newArray(int size) {
                return new Driver[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(firstName);
            dest.writeString(lastName);
        }
    }


    public static class WareHouse implements Parcelable {
        public WareHouse() {
        }

        public int id;

        protected WareHouse(Parcel in) {
            id = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<WareHouse> CREATOR = new Creator<WareHouse>() {
            @Override
            public WareHouse createFromParcel(Parcel in) {
                return new WareHouse(in);
            }

            @Override
            public WareHouse[] newArray(int size) {
                return new WareHouse[size];
            }
        };
    }
}
