package app.pichiwa.extension.extension;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

@SuppressWarnings("unused")
public class PichiwaSettingsActivity extends Activity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("pichiwa_prefs", Context.MODE_PRIVATE);

        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(16);
        root.setPadding(pad, pad, pad, pad);

        addHeader(root, "Privacidad");
        addToggle(root, "Ocultar lectura", "No enviar ticks azules", "hide_read_receipts");
        addToggle(root, "Ocultar entregado", "No enviar doble check gris", "hide_delivered");
        addToggle(root, "Ocultar escritura", "No mostrar \"escribiendo...\"", "hide_typing");
        addToggle(root, "Anti-View Once", "Ver medios efímeros sin límite", "anti_view_once");
        addToggle(root, "Anti Revoke", "Evitar que borren mensajes", "anti_revoke");
        addToggle(root, "Anti Editar", "Evitar que editen mensajes", "anti_edit");
        addToggle(root, "Anti Desaparecer", "Mantener mensajes temporales", "anti_disappearing");
        addToggle(root, "Freeze Last Seen", "Congelar última conexión", "freeze_last_seen");
        addToggle(root, "DND Mode", "No marcar mensajes como leídos", "dnd_mode");

        addHeader(root, "Media");
        addToggle(root, "HD Media", "Enviar sin compresión", "hd_media");
        addToggle(root, "Descargar estados", "Guardar estados de otros", "download_status");
        addToggle(root, "Sin límite reenvío", "Reenviar a contactos ilimitados", "remove_forward_limit");

        addHeader(root, "UI");
        addToggle(root, "Ocultar reenviado", "Quitar etiqueta \"reenviado\"", "hide_forwarded_tag");
        addToggle(root, "Copiar estados", "Permitir copiar texto de estados", "enable_copy_status");

        scroll.addView(root);
        setContentView(scroll);
    }

    private void addHeader(LinearLayout parent, String title) {
        TextView tv = new TextView(this);
        tv.setText(title);
        tv.setTextSize(18);
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        tv.setPadding(0, dp(16), 0, dp(8));
        parent.addView(tv);
    }

    private void addToggle(LinearLayout parent, String title, String summary, String prefKey) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(0, dp(8), 0, dp(8));

        LinearLayout textCol = new LinearLayout(this);
        textCol.setOrientation(LinearLayout.VERTICAL);
        textCol.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        TextView titleTv = new TextView(this);
        titleTv.setText(title);
        titleTv.setTextSize(16);
        textCol.addView(titleTv);

        if (summary != null) {
            TextView sumTv = new TextView(this);
            sumTv.setText(summary);
            sumTv.setTextSize(12);
            sumTv.setAlpha(0.6f);
            textCol.addView(sumTv);
        }

        Switch sw = new Switch(this);
        boolean checked = prefs.getBoolean(prefKey, defaultEnabled(prefKey));
        sw.setChecked(checked);
        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(prefKey, isChecked).apply();
        });

        row.addView(textCol);
        row.addView(sw);
        parent.addView(row);
    }

    private boolean defaultEnabled(String key) {
        // ponytail: most privacy features default on
        return switch (key) {
            case "hide_read_receipts", "hide_typing", "anti_view_once",
                 "anti_revoke", "anti_edit", "remove_forward_limit",
                 "hd_media", "download_status" -> true;
            default -> false;
        };
    }

    private int dp(int px) {
        return (int) (px * getResources().getDisplayMetrics().density);
    }
}
