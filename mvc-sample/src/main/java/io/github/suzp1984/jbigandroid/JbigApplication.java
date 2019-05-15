package io.github.suzp1984.jbigandroid;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import io.github.suzp1984.jbig.JniJbigCodec;
import io.github.suzp1984.jbigandroid.injector.component.ApplicationComponent;
import io.github.suzp1984.jbigandroid.injector.component.DaggerApplicationComponent;
import io.github.suzp1984.jbigandroid.injector.module.ApplicationModule;
import io.github.suzp1984.jbigandroid.injector.module.PersistenceModule;
import io.github.suzp1984.jbigandroid.injector.module.StateModule;
import io.github.suzp1984.jbigandroid.injector.module.UtilsModule;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by moses on 8/28/15.
 */
public class JbigApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    public static JbigApplication from(Context context) {
        return  (JbigApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        new JniJbigCodec();

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                        .detectAll()
                        .penaltyLog()
                        .penaltyDialog()
                        .build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                        .detectAll()
                        .penaltyLog()
                        .build());

        }

        super.onCreate();
    }

    public ApplicationComponent getApplicationComponent() {
        if (mApplicationComponent == null) {

            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .persistenceModule(new PersistenceModule())
                    .stateModule(new StateModule())
                    .utilsModule(new UtilsModule(this))
                    .build();
        }

        return mApplicationComponent;
    }

}
