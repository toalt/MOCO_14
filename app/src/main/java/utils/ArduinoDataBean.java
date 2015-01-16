package utils;

/**
 * Created by Fabian on 16.01.2015.
 */

        import java.sql.Timestamp;

        import android.os.Parcel;
        import android.os.Parcelable;

        import com.fasterxml.jackson.annotation.JsonPropertyOrder;



/* Spaeter mit Jackson vielleicht? Dann kann man den Json direkt in das Arduino Bean Objekt umwandeln
 *
 * https://github.com/bblanchon/ArduinoJson
 * Sensordaten->Arduino->ArduinoJson->perPOSTversenden->Android->Jackson->ArduinoDataBean->MainActivity und umgekehrt!
 *
 *
 * */
@JsonPropertyOrder({ "id", "timeStamp", "currentTemp_1", "currentTemp_2", "currentHumiAir", "currentHumiGround", "lightPercent", "airPercent", "lightOn", "airOn", "waterOn", "maxTemp", "minTemp", "maxHumiAir", "minHumiAir", "maxHumiGround", "minHumiGround", "waterTimeSeconds" })
public class ArduinoDataBean implements Parcelable {

    public ArduinoDataBean() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    /*
        public String getPlantName() {
            return plantName;
        }

        public void setPlantName(String plantName) {
            this.plantName = plantName;
        }
    */
    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getCurrentTemp_1() {
        return currentTemp_1;
    }

    public void setCurrentTemp_1(double currentTemp_1) {
        this.currentTemp_1 = currentTemp_1;
    }

    public double getCurrentTemp_2() {
        return currentTemp_2;
    }

    public void setCurrentTemp_2(double currentTemp_2) {
        this.currentTemp_2 = currentTemp_2;
    }

    public double getCurrentHumiAir() {
        return currentHumiAir;
    }

    public void setCurrentHumiAir(double currentHumiAir) {
        this.currentHumiAir = currentHumiAir;
    }

    public double getCurrentHumiGround() {
        return currentHumiGround;
    }

    public void setCurrentHumiGround(double currentHumiGround) {
        this.currentHumiGround = currentHumiGround;
    }

    public double getLightPercent() {
        return lightPercent;
    }

    public void setLightPercent(double lightPercent) {
        this.lightPercent = lightPercent;
    }

    public double getAirPercent() {
        return airPercent;
    }

    public void setAirPercent(double airPercent) {
        this.airPercent = airPercent;
    }

    public boolean isLightOn() {
        return lightOn;
    }

    public void setLightOn(boolean lightOn) {
        this.lightOn = lightOn;
    }

    public boolean isAirOn() {
        return airOn;
    }

    public void setAirOn(boolean airOn) {
        this.airOn = airOn;
    }

    public boolean isWaterOn() {
        return waterOn;
    }

    public void setWaterOn(boolean waterOn) {
        this.waterOn = waterOn;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxHumiAir() {
        return maxHumiAir;
    }

    public void setMaxHumiAir(int maxHumiAir) {
        this.maxHumiAir = maxHumiAir;
    }

    public double getMinHumiAir() {
        return minHumiAir;
    }

    public void setMinHumiAir(int minHumiAir) {
        this.minHumiAir = minHumiAir;
    }

    public double getMaxHumiGround() {
        return maxHumiGround;
    }

    public void setMaxHumiGround(int maxHumiGround) {
        this.maxHumiGround = maxHumiGround;
    }

    public double getMinHumiGround() {
        return minHumiGround;
    }

    public void setMinHumiGround(int minHumiGround) {
        this.minHumiGround = minHumiGround;
    }

    public int getWaterTimeSeconds() {
        return waterTimeSeconds;
    }

    public void setWaterTimeSeconds(int waterTimeSeconds) {
        this.waterTimeSeconds = waterTimeSeconds;
    }

    private int id;

    //private String plantName;

    private Timestamp timeStamp;

    private double currentTemp_1;
    private double currentTemp_2;
    private double currentHumiAir;
    private double currentHumiGround;


    private double lightPercent;
    private double airPercent;

    private boolean lightOn;
    private boolean airOn;
    private boolean waterOn;

    private int maxTemp;
    private int minTemp;
    private int maxHumiAir;
    private int minHumiAir;
    private int maxHumiGround;
    private int minHumiGround;

    private int waterTimeSeconds;


    protected ArduinoDataBean(Parcel in) {
        id = in.readInt();
        timeStamp = (Timestamp) in.readValue(Timestamp.class.getClassLoader());
        currentTemp_1 = in.readDouble();
        currentTemp_2 = in.readDouble();
        currentHumiAir = in.readDouble();
        currentHumiGround = in.readDouble();
        lightPercent = in.readDouble();
        airPercent = in.readDouble();
        lightOn = in.readByte() != 0x00;
        airOn = in.readByte() != 0x00;
        waterOn = in.readByte() != 0x00;
        maxTemp = in.readInt();
        minTemp = in.readInt();
        maxHumiAir = in.readInt();
        minHumiAir = in.readInt();
        maxHumiGround = in.readInt();
        minHumiGround = in.readInt();
        waterTimeSeconds = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeValue(timeStamp);
        dest.writeDouble(currentTemp_1);
        dest.writeDouble(currentTemp_2);
        dest.writeDouble(currentHumiAir);
        dest.writeDouble(currentHumiGround);
        dest.writeDouble(lightPercent);
        dest.writeDouble(airPercent);
        dest.writeByte((byte) (lightOn ? 0x01 : 0x00));
        dest.writeByte((byte) (airOn ? 0x01 : 0x00));
        dest.writeByte((byte) (waterOn ? 0x01 : 0x00));
        dest.writeInt(maxTemp);
        dest.writeInt(minTemp);
        dest.writeInt(maxHumiAir);
        dest.writeInt(minHumiAir);
        dest.writeInt(maxHumiGround);
        dest.writeInt(minHumiGround);
        dest.writeInt(waterTimeSeconds);
    }

    public static final Parcelable.Creator<ArduinoDataBean> CREATOR = new Parcelable.Creator<ArduinoDataBean>() {
        @Override
        public ArduinoDataBean createFromParcel(Parcel in) {
            return new ArduinoDataBean(in);
        }

        @Override
        public ArduinoDataBean[] newArray(int size) {
            return new ArduinoDataBean[size];
        }
    };
}

