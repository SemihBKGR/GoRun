package com.semihbkgr.gorun;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.semihbkgr.gorun.util.DatabaseHelper;
import com.semihbkgr.gorun.snippet.SnippetClient;
import com.semihbkgr.gorun.snippet.SnippetClientImpl;
import com.semihbkgr.gorun.snippet.SnippetService;
import com.semihbkgr.gorun.snippet.SnippetServiceImpl;
import com.semihbkgr.gorun.util.ResourceHelper;
import com.semihbkgr.gorun.util.ListenedThreadPoolWrapper;
import okhttp3.OkHttpClient;

import java.io.File;

import static com.semihbkgr.gorun.AppConstant.File.EXTERNAL_DIR_NAME;

public class AppContext {

    private static final String TAG = AppContext.class.getName();

    private static AppContext instance;

    public final OkHttpClient httpClient;
    public final Gson gson;
    public final File rootDir;
    public final SnippetClient snippetClient;
    public final SnippetService snippetService;
    public final DatabaseHelper databaseHelper;
    public final ResourceHelper resourceHelper;

    private AppContext(Context context) {
        this.httpClient = new OkHttpClient();
        this.gson = new GsonBuilder().create();
        this.rootDir = createAndGetExternalDir(context);
        this.snippetClient = new SnippetClientImpl(httpClient, gson);
        this.snippetService = new SnippetServiceImpl(snippetClient);
        this.databaseHelper = new DatabaseHelper(context);
        this.resourceHelper = new ResourceHelper(context, gson);
    }

    public static void initialize(@NonNull Context context) {
        if (instance != null)
            throw new IllegalStateException("AppContext instance has already been initialized before");
        Log.i(TAG, "initialize: AppContext initialization is being started");
        instance = new AppContext(context);
        Log.i(TAG, "initialize: AppContext has been initialized");
    }

    public static AppContext instance() {
        if (instance == null)
            throw new IllegalStateException("AppContext instance has not been initialized yet");
        return instance;
    }

    public static boolean initialized() {
        return instance != null;
    }

    @Nullable
    private File createAndGetExternalDir(@NonNull Context context) {
        File dir = context.getExternalFilesDir(EXTERNAL_DIR_NAME);
        if (dir == null) return null;
        if (!dir.exists()) {
            Log.i(TAG, "createAndGetExternalDir: Root dir is not exist");
            boolean isCreated = dir.mkdirs();
            if (!isCreated) throw new IllegalStateException("Root dir cannot be created");
            Log.i(TAG, "createAndGetExternalDir: Root dir has been created");
        } else Log.i(TAG, "createAndGetExternalDir: Root dir has been already created");
        return dir;
    }

}
