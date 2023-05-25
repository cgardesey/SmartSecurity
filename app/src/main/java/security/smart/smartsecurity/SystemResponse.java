package security.smart.smartsecurity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kobby on 10-Jun-16.
 */
public class SystemResponse implements Parcelable {

    private List<Boolean> alarmStatus = new ArrayList<Boolean>(4);
    private boolean power;
    private boolean intruder;
    private double balance;
    private String user;
    private int volume;

    public SystemResponse() {
    }

    protected SystemResponse(Parcel in) {
        if (in.readByte() == 0x01) {
            alarmStatus = new ArrayList<Boolean>();
            in.readList(alarmStatus, Boolean.class.getClassLoader());
        } else {
            alarmStatus = null;
        }
        power = in.readByte() != 0x00;
        intruder = in.readByte() != 0x00;
        balance = in.readDouble();
        user = in.readString();
        volume = in.readInt();
    }

    public List<Boolean> getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(List<Boolean> alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public boolean isPower() {
        return power;
    }

    public void setPower(boolean power) {
        this.power = power;
    }

    public boolean isIntruder() {
        return intruder;
    }

    public void setIntruder(boolean intruder) {
        this.intruder = intruder;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public static Creator<SystemResponse> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (alarmStatus == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(alarmStatus);
        }
        dest.writeByte((byte) (power ? 0x01 : 0x00));
        dest.writeByte((byte) (intruder ? 0x01 : 0x00));
        dest.writeDouble(balance);
        dest.writeString(user);
        dest.writeInt(volume);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SystemResponse> CREATOR = new Parcelable.Creator<SystemResponse>() {
        @Override
        public SystemResponse createFromParcel(Parcel in) {
            return new SystemResponse(in);
        }

        @Override
        public SystemResponse[] newArray(int size) {
            return new SystemResponse[size];
        }
    };
}
