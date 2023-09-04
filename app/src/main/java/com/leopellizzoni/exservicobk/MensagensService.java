package com.leopellizzoni.exservicobk;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class MensagensService extends IntentService {
    private static final String CHANNEL_ID = "21020";
    public static final int NOTIFICACAO_CHAVE = 5453;
    static final String TAG_LOGS = "Msg Service";
    public static final String EXTRA_MSG = "msg";

    public MensagensService() {
        super("MensagensService");
    }

    private Handler handler;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        criarCanalDeNotificacoes();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        logar("Está executando o onHandleIntent. Você deveria usar o WorkManager");

        synchronized (this){

            try {
                wait(10000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        String texto = intent.getStringExtra(EXTRA_MSG);

        mostrarToast(texto);
        logar(texto);
        notificar(texto);
    }

    private void mostrarToast(final String texto) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void logar(String texto){
        Log.d(TAG_LOGS, texto);
    }


    //https://developer.android.com/develop/ui/views/notifications/channels
    private void criarCanalDeNotificacoes() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription(getString(R.string.channel_description));
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void notificar(String texto){
        Notification notificacao = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificacao = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(getString(R.string.assuntoNotificacao))
                    .setContentText(texto)
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID)
                    .build();
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICACAO_CHAVE, notificacao);
    }
}