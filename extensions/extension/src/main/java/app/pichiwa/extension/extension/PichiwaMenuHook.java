package app.pichiwa.extension.extension;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

@SuppressWarnings("unused")
public final class PichiwaMenuHook {

    private PichiwaMenuHook() {}

    public static void injectMenuItems(Activity activity, Menu menu) {
        MenuItem item = menu.add(0, 0, 9999, "PichiWA");
        item.setOnMenuItemClickListener(i -> {
            Intent intent = new Intent(activity, PichiwaSettingsActivity.class);
            activity.startActivity(intent);
            return true;
        });
    }
}
