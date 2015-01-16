package service;

/**
 * Created by Fabian on 16.01.2015.
 */

/*
 * http://developer.android.com/guide/components/bound-services.html
 *
 * Binding to a Started Service

 As discussed in the Services document, you can create a service that is both started and bound.
 That is, the service can be started by calling startService(), which allows the service to run
 indefinitely, and also allow a client to bind to the service by calling bindService().

 If you do allow your service to be started and bound, then when the service has been started,
 the system does not destroy the service when all clients unbind. Instead, you must explicitly
 stop the service, by calling stopSelf() or stopService().
 *
 * */

        import java.io.BufferedReader;
        import java.io.DataOutputStream;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.net.Socket;
        import java.net.UnknownHostException;
        import java.util.ArrayList;

        import android.app.Notification;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.app.Service;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Color;
        import android.os.Binder;
        import android.os.Handler;
        import android.os.HandlerThread;
        import android.os.IBinder;
        import android.os.Looper;
        import android.os.Message;
        import android.support.v4.app.NotificationCompat;
        import android.util.Log;
        import android.widget.Toast;

        import garduino.moco.htwg.de.moco_ws14.InsideActivity;
        import garduino.moco.htwg.de.moco_ws14.R;
        import utils.ArduinoDataBean;
        import utils.GarduinoUtils;

public class GarduinoService extends Service {

    public final static int MESSAGE_CANCEL_HANDLER = 1;
    public final static int MESSAGE_CLEAR_DATABASE = 2;
    public final static int MESSAGE_NEW_BEAN = 3;

    private boolean notificationTemperatureIsShowing = false;
    private boolean notificationHumiAirIsShowing = false;
    // TODO Restliche noch hier hinzufügen und unten immer abfragen, bevor eine
    // neue gesendet wird

    InsideActivity myGuiActivity;

    GarduinoUtils garduinoUtils;

    String ip = "192.168.1.136";
    int port = 80;

    boolean connectedToArduino = false;

    private int serviceCheckIntervall = 5000;

    int mStartMode; // indicates how to behave if the service is killed
    IBinder mBinder; // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used

    int notificationCounter = 0;
    int timeToWaitForNextNotification;
    boolean allowedNextNotificationFromUser;

    private boolean serviceIsBoundedToActivity = false;

    private ArduinoDataBean aBean;

    private ArrayList<ArduinoDataBean> beans;

    NotificationManager mNM;

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private ResponseHandler responseHandler;

    Intent activityIntent;


    private String LOG_TAG = this.getClass().getSimpleName();

    public class LocalBinder extends Binder {
        public GarduinoService getService() {
            // Return this instance of LocalService so clients can call public
            // methods
            return GarduinoService.this;
        }
    }

    public GarduinoService() {
        garduinoUtils = new GarduinoUtils();
        // prepareDummyBean();
        aBean = new ArduinoDataBean();

        mBinder = new LocalBinder();
        beans = new ArrayList<ArduinoDataBean>();
        beans.add(aBean);
    }

    public void addGuiListener(InsideActivity myActivity) {
        this.myGuiActivity = myActivity;
    }

    private void prepareDummyBean() {
        aBean = new ArduinoDataBean();
        aBean.setAirOn(false);
        aBean.setAirPercent(20.22);
        aBean.setCurrentHumiAir(88.88);
        aBean.setCurrentHumiGround(50.00);
        aBean.setCurrentTemp_1(20.00);
        aBean.setCurrentTemp_2(22.00);
		/* TODO id automatisch vergeben */
        aBean.setId(8);
        aBean.setLightOn(true);
        aBean.setLightPercent(100.00);
        aBean.setMaxHumiAir(95);
        aBean.setMaxHumiGround(98);
        aBean.setMaxTemp(30);
        aBean.setMinHumiAir(20);
        aBean.setMinHumiGround(25);
        aBean.setMinTemp(10);
        // aBean.setPlantName("Petersilie");
        aBean.setTimeStamp(new java.sql.Timestamp(System.currentTimeMillis()));
        aBean.setWaterOn(true);
        aBean.setWaterTimeSeconds(10);
        String obToString = garduinoUtils.objectToJsonString(aBean);

    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {

        private String LOG_TAG = this.getClass().getSimpleName();
        ResponseHandler response;

        public ServiceHandler(Looper looper, ResponseHandler resp) {
            super(looper);
            this.response = resp;
        }

        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            Log.d(LOG_TAG + " handleMessage", "start Handler Thread loop");

            while (true) {

                synchronized (this) {

                    try {
                        ArduinoDataBean daBean = getDataFromArduinoByTcp();
                        Message responseMessage = new Message();
                        responseMessage.obj = daBean;
                        responseMessage.what = MESSAGE_NEW_BEAN;

                        response.sendMessage(responseMessage);

                        // TODO HIER MESSAGE ZURUECK SENDEN
                    } catch (UnknownHostException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    try {

                        // adde jede bean damit man später ein diagramm malen
                        // kann
                        beans.add(aBean);

                        if (this.hasMessages(MESSAGE_CANCEL_HANDLER)) {
                            this.removeMessages(MESSAGE_CANCEL_HANDLER);
                            // Stop the service using the startId, so that we
                            // don't stop
                            // the service in the middle of handling another job
                            Log.d(LOG_TAG + " handleMessage - while(true)",
                                    "STOP");
                            stopSelf(msg.arg1);
                            break;
                        }
                        Log.d(LOG_TAG + " handleMessage - while(true)",
                                "Warte auf daten..");

                        wait(getServiceCheckIntervall());
                        checkTemperatureAndHumidity();
                        // saveDataToProvider();
                    } // TODO: handle exception
                    catch (Exception e) {
                    }
                }
            }
            Log.d(LOG_TAG + " handleMessage", "left Loop");
        }

    }

    // This class handles the Service response
    class ResponseHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            int respCode = msg.what;

            switch (respCode) {
                case MESSAGE_NEW_BEAN: {
                    aBean = (ArduinoDataBean) msg.obj;
                    sendDataBeanToGui(aBean);
                }
            }
        }

    }

    @Override
    public void onCreate() {
        // The service is being created
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        responseHandler = new ResponseHandler();
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper, responseHandler);
        Log.i("onCreate", "CREATE");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the
        // job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);
        Log.i("onStartCommand", "START_COMMAND");

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        Log.i("onBind", "BIND");
        serviceIsBoundedToActivity = true;
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        serviceIsBoundedToActivity = false;
        Log.i("onUnbind", "UNBIND");
        return mAllowRebind;
    }

    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
        Log.i("onRebind", "REBIND");
    }

    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
        Log.i("onDestroy()", "DESTROY");
        Message m = new Message();
        m.what = MESSAGE_CANCEL_HANDLER;
        mServiceHandler.sendMessage(m);
    }

    public boolean isNotificationTemperatureIsShowing() {
        return notificationTemperatureIsShowing;
    }

    public void setNotificationTemperatureIsShowing(
            boolean notificationTemperatureIsShowing) {
        this.notificationTemperatureIsShowing = notificationTemperatureIsShowing;
    }

    public boolean isNotificationHumiAirIsShowing() {
        return notificationHumiAirIsShowing;
    }

    public void setNotificationHumiAirIsShowing(
            boolean notificationHumiAirIsShowing) {
        this.notificationHumiAirIsShowing = notificationHumiAirIsShowing;
    }

    private static NotificationCompat.Builder buildNotificationCommon(
            Context _context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                _context).setWhen(System.currentTimeMillis());
        // Vibration
        builder.setVibrate(new long[] { 500, 200, 700, 100, 900 });

        // LED
        builder.setLights(Color.RED, 3000, 3000);

        // Ton
        // builder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));

        return builder;
    }

    @SuppressWarnings("deprecation")
    private void showNotification(String _text, String label) {
        // In this sample, we'll use the same text for the ticker and the
        // expanded notification
        CharSequence text = _text;

        // Set the icon, scrolling text and timestamp
        // Notification notification = new Notification(R.drawable.stat_sample,
        // text,
        // System.currentTimeMillis());
        Notification noti = buildNotificationCommon(
                this.getApplicationContext())
                .setContentTitle("Garduino Service").setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher).build();

        // The PendingIntent to launch our activity if the user selects this
        // notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, InsideActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        noti.setLatestEventInfo(this, label, text, contentIntent);

        notificationCounter++;

        // Send the notification.
        mNM.notify(notificationCounter, noti);
    }

    private ArduinoDataBean getDataFromArduinoByTcp()
            throws UnknownHostException, IOException {

        String parameter;
        String serverAnswer = "";
        Socket clientSocket = new Socket(ip, port);

        DataOutputStream outToServer = new DataOutputStream(
                clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
                clientSocket.getInputStream()));

        parameter = "Hallo";
        outToServer.writeBytes(parameter + '\n');

        int i = 0;
        boolean firstTime = true;
        while (true) {
            String read = null;
            if (inFromServer.ready()) {
                read = inFromServer.readLine();
                if (read == null) {
                    break;
                }

                if (read.contains("waterOn") || read.contains("airOn")
                        || read.contains("lightOn")) {
                    read = read.replace("1", "true");
                    read = read.replace("0", "false");
                }

                if (i == 0) {
                    read = read.replace("}", "");
                } else if ((i > 0) && (i < 6)) {
                    if (firstTime) {
                        read = read.replace("{", ",");
                        read = read.replace("}", ",");
                        firstTime = false;
                    } else {
                        read = read.replace("{", "");
                        read = read.replace("}", ",");
                    }

                } else if (i == 6) {
                    read = read.replace("{", "");
                }

                i++;
            } else {
                continue;
            }

            serverAnswer += read;
        }

        Log.d("FROM SERVER: ", "ANTWORT = " + serverAnswer);
        clientSocket.close();

        return saveDataToBeanByJsonString(serverAnswer);
    }

    public ArduinoDataBean saveDataToBeanByJsonString(String json) {
        if (json != null && json != "") {
            // TODO GEHT NICHT bzw. auf this.bean kann nicht zugegriffen werden!
            // this.aBean = bean;

            ArduinoDataBean bean = garduinoUtils.StringToBean(json);
            return bean;
        } else
            return null;
    }

    public ArduinoDataBean getDataBean() {
        Log.i("GETBEAN", String.valueOf(aBean.getCurrentTemp_1()));

        return this.aBean;
    }

    public void sendDataBeanToGui(ArduinoDataBean bean) {
        Log.i("GETBEAN", String.valueOf(aBean.getCurrentTemp_1()));

        if(myGuiActivity != null) {
            myGuiActivity.addBeanListener(bean);
        }

    }

    public void checkTemperatureAndHumidity() {
        if (aBean.getCurrentTemp_1() >= aBean.getMaxTemp()
                && !notificationTemperatureIsShowing) {
            mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Display a notification about us starting. We put an icon in the
            // status bar.
            showNotification("Die Temperatur im Gewächshaus ist zu hoch!",
                    String.valueOf(aBean.getCurrentTemp_1() + "0° C"));
            notificationTemperatureIsShowing = true;
        } else if (aBean.getCurrentTemp_1() <= aBean.getMinTemp()) {
            mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            showNotification("Die Temperatur im Gewächshaus ist zu niedrig!",
                    String.valueOf(aBean.getCurrentTemp_1() + "0° C"));

        }
        if (aBean.getCurrentHumiAir() >= aBean.getMaxHumiAir()
                && !notificationHumiAirIsShowing) {
            mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            showNotification(
                    "Die Luftfeuchtigkeit im Gewächshaus ist zu hoch!",
                    String.valueOf(aBean.getCurrentHumiAir() + "0 %"));
            notificationHumiAirIsShowing = true;
        } else if (aBean.getCurrentHumiAir() <= aBean.getMinHumiAir()) {
            mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            showNotification(
                    "Die Luftfeuchtigkeit im Gewächshaus ist zu niedrig!",
                    String.valueOf(aBean.getCurrentHumiAir()));
        }
    }

    public int getServiceCheckIntervall() {
        return serviceCheckIntervall;
    }

    public void setServiceCheckIntervall(int serviceCheckIntervall) {
        this.serviceCheckIntervall = serviceCheckIntervall;
    }

}
