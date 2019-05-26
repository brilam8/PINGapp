package com.bigboiapps.brian.pingchecker;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.Keep;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.kobakei.ratethisapp.RateThisApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

//import com.google.gson.Gson;
//import java.sql.Array;

public class PingCheck extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener, OnDataSendToActivity {

    MyRecyclerViewAdapter adapter;
    @BindView(R.id.welcome)
    TextView welcome;
    @BindView(R.id.Introduction)
    TextView Introduction;
    @BindView(R.id.get_started)
    TextView get_started;
    //@BindView(R.id.started3)
    //TextView started3;
    @BindView(R.id.started2)
    TextView started2;
    @BindView(R.id.started1)
    TextView started1;
    @BindView(R.id.welcomeLayout)
    ConstraintLayout welcomeLayout;
    @BindView(R.id.leagueButton)
    ImageButton leagueButton;
    @BindView(R.id.pubgButton)
    ImageButton pubgButton;
    @BindView(R.id.fortniteButton)
    ImageButton fortniteButton;
    @BindView(R.id.csgoButton)
    ImageButton csgoButton;
    @BindView(R.id.dotaButton)
    ImageButton dotaButton;
    @BindView(R.id.overwatchButton)
    ImageButton overwatchButton;
    @BindView(R.id.customButton)
    ImageButton customButton;
    @BindView(R.id.navigationLayout)
    LinearLayout navigationLayout;
    @BindView(R.id.serverList)
    RecyclerView recyclerView;
    ArrayList<String> mServerList = new ArrayList<>();

    String currentJSONData = "";
    JSONObject currentJSON = new JSONObject();
    isReachable status = null;
    isReachableCustom customStatus = null;
    /*@BindView(R.id.pingStats)
    TextView pingStats;*/
    int adPlayed = 0;
    @BindView(R.id.ping_button)
    Button pingButton;
    @BindView(R.id.host_name)
    TextView hostName;
    @BindView(R.id.pings_transmit)
    TextView pingsTransmit;
    @BindView(R.id.ping_receive)
    TextView pingReceive;
    @BindView(R.id.packet_loss)
    TextView packetLoss;
    @BindView(R.id.min)
    TextView min;
    @BindView(R.id.avg)
    TextView avg;
    @BindView(R.id.max)
    TextView max;
    @BindView(R.id.ping_status)
    TextView pingStatus;
    @BindView(R.id.editText)
    AutoCompleteTextView editText;
    @BindView(R.id.full_results)
    TextView fullResults;
    @BindView(R.id.custom_ping)
    ConstraintLayout customPing;
    @BindView(R.id.parent)
    ConstraintLayout parent;

    @BindView(R.id.gifView)
    ImageView frameLayout;

    @BindView(R.id.settingsButton)
    ImageButton settingsButton;
    //@Nullable @BindView(R.id.navigationScroll) HorizontalScrollView navigationScroll;

    @BindView(R.id.adRecyclerView)
    AdView adRecyclerView;
    @BindView(R.id.customAdView)
    AdView customAdView;
    @BindView(R.id.adView)
    AdView adViewWelcome;


    JSONObject leagueServers;
    int[] leagueGifs = {R.drawable.lucian, R.drawable.darius, R.drawable.udyr_bear, R.drawable.udyr_tiger};
    //ArrayList<MyGifView> leagueGifs = new ArrayList<>();
    //ArrayList<Drawable> leagueBackgrounds = new ArrayList<>();
    int[] leagueBackgrounds = {R.drawable.league_static1, R.drawable.league_static2, R.drawable.league_static3, R.drawable.league_static4};
    //Drawable [] leagueBackgrounds = new Drawable[4];

    Bundle bund1 = new Bundle(2);

    JSONObject pubgServers;
    int[] pubgGifs = {R.drawable.pubg1};
    //ArrayList<MyGifView> pubgGifs = new ArrayList<>();
    //ArrayList<Drawable> pubgBackgrounds = new ArrayList<>();
    int[] pubgBackgrounds = {R.drawable.pubg_static1, R.drawable.pubg_static2, R.drawable.pubg_static3, R.drawable.pubg_static4};

    JSONObject fortniteServers;
    int[] fortniteGifs = {R.drawable.fortnite1};
    //ArrayList<MyGifView> fortniteGifs = new ArrayList<>();
    //ArrayList<Drawable> fortniteBackgrounds = new ArrayList<>();
    int[] fortniteBackgrounds = {R.drawable.fortnite_static1, R.drawable.fortnite_static2, R.drawable.fortnite_static3, R.drawable.fortnite_static4};

    JSONObject dotaServers;
    int[] dotaGifs = {R.drawable.dota1};
    //ArrayList<MyGifView> dotaGifs = new ArrayList<>();
    //ArrayList<Drawable> dotaBackgrounds = new ArrayList<>();
    int[] dotaBackgrounds = {R.drawable.dota_static1, R.drawable.dota_static2, R.drawable.dota_static3, R.drawable.dota_static4};

    JSONObject overwatchServers;
    int[] overwatchGifs = {R.drawable.overwatch1};
    //ArrayList<MyGifView> overwatchGifs = new ArrayList<>();
    //ArrayList<Drawable> overwatchBackgrounds = new ArrayList<>();
    int[] overwatchBackgrounds = {R.drawable.overwatch_static1, R.drawable.overwatch_static2, R.drawable.overwatch_static3, R.drawable.overwatch_static4};

    JSONObject csgoServers;
    int[] csgoGifs = {R.drawable.csgo_gif1};
    //ArrayList<MyGifView> overwatchGifs = new ArrayList<>();
    //ArrayList<Drawable> overwatchBackgrounds = new ArrayList<>();
    int[] csgoBackgrounds = {R.drawable.csgo_static1, R.drawable.csgo_static2, R.drawable.csgo_static3, R.drawable.csgo_static4};

    ArrayList<String> suggestionsDropDown;
    ArrayAdapter<String> suggestionAdapter;
    private static final String DEBUG_TAG = "NetworkStatusExample";

    private static final String leagueServersJSON = "{" +
            "\"North America (NA)\":\"104.160.131.3\"," +
            "\"Europe West (EUW)\":\"104.160.141.3\"," +
            "\"Europe Nordic & East (EUNE)\":\"104.160.142.3\"," +
            "\"Russia (RUS)\":\"162.249.72.103\"," +
            "\"Oceania (OCE)\":\"104.160.156.1\"," +
            "\"Latin America North (LAN)\":\"104.160.136.3\"," +
            "\"Latin America South (LAS)\":\"159.63.22.66\"," +
            "\"Brazil (BR)\":\"104.160.152.3\"," +
            "\"Turkey (TR)\":\"104.160.157.4\"," +
            "\"Garena\":\"122.11.128.127\"," +
            "\"Japan (JP)\":\"104.160.154.1\"" +
            "}";

    private static final String dotaServersJSON = "{\"US East (Sterling, VA)\":\"iad.valve.net\"," +
            "\"US West (Seattle, WA)\":\"eat.valve.net\"," +
            "\"Europe East 1 (Vienna, Austria)\":\"vie.valve.net\"," +
            "\"Europe East 2 (Vienna, Austria)\":\"185.25.182.1\"," +
            "\"Europe West 1 (Luxembourg)\":\"lux.valve.net\"," +
            "\"Europe West 2 (Luxembourg)\":\"146.66.158.1\"," +
            "\"SE Asia 1 (Singapore)\":\"sgp-1.valve.net\"," +
            "\"SE Asia 2 (Singapore)\":\"sgp-2.valve.net\"," +
            "\"Japan\":\"45.121.186.1\"," +
            "\"South Africa 1 (Cape Town)\":\"197.80.200.1\"," +
            "\"South Africa 2 (Cape Town)\":\"197.84.209.1\"," +
            "\"South Africa 3 (Johannesburg)\":\"196.38.180.1\"," +
            "\"South America 1 (Brazil)\":\"gru.valve.net\"," +
            "\"South America 2 (Brazil)\":\"209.197.25.1\"," +
            "\"Russia 1 (Stockholm, Sweden)\":\"sto.valve.net\"," +
            "\"Russia 2 (Stockholm, Sweden)\":\"185.25.180.1\"," +
            "\"Dubai (UAE)\":\"dxb.valve.net\"," +
            "\"Australia\":\"103.10.125.146\"," +
            "\"Chile\":\"155.133.249.1\"," +
            "\"India\":\"155.133.233.12\"," +
            "\"Peru (Lima)\":\"191.98.144.1\"}";

    private static final String csgoServersJSON = "{\"EU North (Russia/Stockholm)\":\"146.66.156.254\"," +
            "\"EU East (Vienna)\":\"146.66.155.254\"," +
            "\"EU West (Luxembourg)\":\"146.66.152.254\"," +
            "\"US East (Sterling/Virginia)\":\"208.78.164.25\"," +
            "\"US West (Seattle/Washington)\":\"192.69.96.254\"," +
            "\"US South West (California)\":\"162.254.194.24\"," +
            "\"US South East (Atlanta)\":\"162.254.199.254\"," +
            "\"SE Asia (Singapore)\":\"45.121.184.254\"," +
            "\"SE Asia 2 (Singapore)\":\"103.28.54.254\"," +
            "\"South Africa (Cape town)\":\"197.80.4.37\"," +
            "\"South Africa (Johannesburg)\":\"197.80.4.37\"," +
            "\"Australia (Sydney)\":\"103.10.125.254\"," +
            "\"Dubai/Middle East\":\"185.25.183.254\"," +
            "\"South America (Brazil/Sao Paulo)\":\"205.185.194.254\"," +
            "\"Spain\":\"155.133.247.24\"," +
            "\"Chile\":\"155.133.249.24\"," +
            "\"Poland\":\"155.133.243.24\"," +
            "\"India (New Delhi)\":\"155.133.233.24\"," +
            "\"India (Chennai)\":\"155.133.232.24\"," +
            "\"Japan\":\"45.121.186.24\"," +
            "\"Hong Kong\":\"155.133.244.24\"," +
            "\"Peru\":\"190.216.121.24\"}";

    private static final String overwatchServersJSON = "{\"US West\":\"24.105.30.129\"," +
            "\"US Central\":\"24.105.62.129\"," +
            "\"Brazil\":\"54.233.152.145\"," +
            "\"South America (Chile)\":\"200.29.32.109\"," +
            "\"Europe 1\":\"185.60.112.157\"," +
            "\"Australia (Sydney)\":\"13.55.197.247\"," +
            "\"SE Asia (Singapore)\":\"54.254.187.187\"," +
            "\"Korea\":\"121.254.206.1\"," +
            "\"Taiwan\":\"210.242.32.201\"}";
    private static final String pubgServersJSON = "{\"US East (Virginia)\":\"dynamodb.us-east-1.amazonaws.com\"," +
            "\"US East (Ohio)\":\"dynamodb.us-east-2.amazonaws.com\"," +
            "\"US West (California)\":\"dynamodb.us-west-1.amazonaws.com\"," +
            "\"US West (Oregon)\":\"dynamodb.us-west-2.amazonaws.com\"," +
            "\"Canada (Central)\":\"dynamodb.ca-central-1.amazonaws.com\"," +
            "\"Europe (Frankfurt)\":\"dynamodb.eu-central-1.amazonaws.com\"," +
            "\"Europe (Ireland)\":\"dynamodb.eu-west-1.amazonaws.com\"," +
            "\"Europe (London)\":\"dynamodb.eu-west-2.amazonaws.com\"," +
            "\"Europe (Paris)\":\"dynamodb.eu-west-3.amazonaws.com\"," +
            "\"Asia Pacific (Mumbai)\":\"dynamodb.ap-south-1.amazonaws.com\"," +
            "\"Asia Pacific (Seoul)\":\"dynamodb.ap-northeast-2.amazonaws.com\"," +
            "\"Asia Pacific (Singapore)\":\"dynamodb.ap-southeast-1.amazonaws.com\"," +
            "\"Asia Pacific (Sydney)\":\"dynamodb.ap-southeast-2.amazonaws.com\"," +
            "\"Asia Pacific (Tokyo)\":\"dynamodb.ap-northeast-1.amazonaws.com\"," +
            "\"South America (Sao Paulo)\":\"dynamodb.sa-east-1.amazonaws.com\"," +
            "\"China (Beijing)\":\"dynamodb.cn-north-1.amazonaws.com.cn\"," +
            "\"China (Ningxia)\":\"dynamodb.cn-northwest-1.amazonaws.com.cn\"}";
    private static final String fortniteServersJSON = "{\"US East (Virginia)\":\"dynamodb.us-east-1.amazonaws.com\"," +
            "\"US East (Ohio)\":\"dynamodb.us-east-2.amazonaws.com\"," +
            "\"US West (California)\":\"dynamodb.us-west-1.amazonaws.com\"," +
            "\"US West (Oregon)\":\"dynamodb.us-west-2.amazonaws.com\"," +
            "\"Canada (Central)\":\"dynamodb.ca-central-1.amazonaws.com\"," +
            "\"Europe (Frankfurt)\":\"dynamodb.eu-central-1.amazonaws.com\"," +
            "\"Europe (Ireland)\":\"dynamodb.eu-west-1.amazonaws.com\"," +
            "\"Europe (London)\":\"dynamodb.eu-west-2.amazonaws.com\"," +
            "\"Europe (Paris)\":\"dynamodb.eu-west-3.amazonaws.com\"," +
            "\"Asia Pacific (Mumbai)\":\"dynamodb.ap-south-1.amazonaws.com\"," +
            "\"Asia Pacific (Seoul)\":\"dynamodb.ap-northeast-2.amazonaws.com\"," +
            "\"Asia Pacific (Singapore)\":\"dynamodb.ap-southeast-1.amazonaws.com\"," +
            "\"Asia Pacific (Sydney)\":\"dynamodb.ap-southeast-2.amazonaws.com\"," +
            "\"Asia Pacific (Tokyo)\":\"dynamodb.ap-northeast-1.amazonaws.com\"," +
            "\"South America (Sao Paulo)\":\"dynamodb.sa-east-1.amazonaws.com\"," +
            "\"China (Beijing)\":\"dynamodb.cn-north-1.amazonaws.com.cn\"," +
            "\"China (Ningxia)\":\"dynamodb.cn-northwest-1.amazonaws.com.cn\"}";
    @BindView(R.id.stats_hostname)
    TextView statsHostname;
    @BindView(R.id.stats_fullresults)
    TextView statsFullresults;
    @BindView(R.id.stats_ping_transmit)
    TextView statsPingTransmit;
    @BindView(R.id.stats_ping_receive)
    TextView statsPingReceive;
    @BindView(R.id.stats_packet_loss)
    TextView statsPacketLoss;
    @BindView(R.id.stats_min)
    TextView statsMin;
    @BindView(R.id.stats_max)
    TextView statsMax;
    @BindView(R.id.stats_avg)
    TextView statsAvg;
    @BindView(R.id.pingStatsHolder)
    ConstraintLayout pingStatsHolder;


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /*public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(PingCheck.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }*/


    private InterstitialAd mInterstitialAd;

    @Override
    @Keep
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_ping_check);
        //setupUI(findViewById(R.id.parent));
        ButterKnife.bind(this);

        /*RateThisApp.Config config = new RateThisApp.Config(5, 6);
        config.setTitle(R.string.rateButtonTitle);
        RateThisApp.init(config);

        RateThisApp.onCreate(this);
        RateThisApp.showRateDialogIfNeeded(this);*/

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }


        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    String customHost = editText.getText().toString().trim();
                    performCustomPing(customHost);
                    editText.clearFocus();

                    return true;
                }
                return false;
            }
        });
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        /*leagueGifs.add(udyrTiGIF = new MyGifView(this, "udyr_turtle"));
        leagueGifs.add(udyrTuGIF = new MyGifView(this, "udyr_phoenix"));
        leagueGifs.add(dariusGIF = new MyGifView(this, "darius"));*/
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //leagueGifs.add(dravenGIF = new MyGifView(this, "draven"));


        if (leagueServers == null) {
            try {
                leagueServers = new JSONObject(leagueServersJSON);
                dotaServers = new JSONObject(dotaServersJSON);
                pubgServers = new JSONObject(pubgServersJSON);
                overwatchServers = new JSONObject(overwatchServersJSON);
                fortniteServers = new JSONObject(fortniteServersJSON);
                csgoServers = new JSONObject(csgoServersJSON);
            } catch (Throwable t) {
                //Log.e("My App", "Could not parse malformed JSON: \"" + leagueServers + "\"");
            }
        }


        /*leagueGifs[0] = (new MyGifView(this, "lucian"));
        leagueGifs[1] = (new MyGifView(this, "darius"));
        leagueGifs[2] = (new MyGifView(this, "udyr_bear"));
        leagueGifs[3] = (new MyGifView(this, "udyr_tiger"));



        pubgGifs[0] = (new MyGifView(this, "pubg1"));

        fortniteGifs[0] = (new MyGifView(this, "fortnite1"));

        dotaGifs[0] = (new MyGifView(this, "dota1"));

        overwatchGifs[0] = (new MyGifView(this, "overwatch1"));*/

        /*leagueBackgrounds[0] = (getResources().getDrawable(R.drawable.league_static1));
        leagueBackgrounds[1] = (getResources().getDrawable(R.drawable.league_static2));
        leagueBackgrounds[2] = (getResources().getDrawable(R.drawable.league_static3));
        leagueBackgrounds[3] = (getResources().getDrawable(R.drawable.league_static4));

        pubgBackgrounds[0] = (getResources().getDrawable(R.drawable.pubg_static1));
        pubgBackgrounds[1] = (getResources().getDrawable(R.drawable.pubg_static2));
        pubgBackgrounds[2] = (getResources().getDrawable(R.drawable.pubg_static3));
        pubgBackgrounds[3] = (getResources().getDrawable(R.drawable.pubg_static4));

        fortniteBackgrounds[0] = (getResources().getDrawable(R.drawable.fortnite_static1));
        fortniteBackgrounds[1] = (getResources().getDrawable(R.drawable.fortnite_static2));
        fortniteBackgrounds[2] = (getResources().getDrawable(R.drawable.fortnite_static3));
        fortniteBackgrounds[3] = (getResources().getDrawable(R.drawable.fortnite_static4));

        dotaBackgrounds[0] = (getResources().getDrawable(R.drawable.dota_static1));
        dotaBackgrounds[1] = (getResources().getDrawable(R.drawable.dota_static2));
        dotaBackgrounds[2] = (getResources().getDrawable(R.drawable.dota_static3));
        dotaBackgrounds[3] = (getResources().getDrawable(R.drawable.dota_static4));

        overwatchBackgrounds[0] = (getResources().getDrawable(R.drawable.overwatch_static1));
        overwatchBackgrounds[1] = (getResources().getDrawable(R.drawable.overwatch_static2));
        overwatchBackgrounds[2] = (getResources().getDrawable(R.drawable.overwatch_static3));
        overwatchBackgrounds[3] = (getResources().getDrawable(R.drawable.overwatch_static4));*/
        /*leagueGifs.add(dravenGIF);
        leagueGifs.add(lucianGIF);
        leagueGifs.add(dariusGIF);*/
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        // My AdMob app ID: ca-app-pub-9081923015947764~9422599654
        Bundle extras = new Bundle();
        extras.putString("max_ad_content_rating", "T");
        MobileAds.initialize(this, "ca-app-pub-9081923015947764~9422599654");
        final AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();
        //adRequestBuilder.addTestDevice("D497A6B6D42766D2FC73F69FD3F8E6E3");
        //adRequestBuilder.addTestDevice("6115A4D1F9246F173D31E84BA91EE287");
        mInterstitialAd = new InterstitialAd(this);



        mInterstitialAd.setAdUnitId("ca-app-pub-9081923015947764/8494349148");

        //ca-app-pub-9081923015947764/8494349148 real man
        //ca-app-pub-3940256099942544/1033173712 fake man
        //mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                //mInterstitialAd.loadAd(adRequest);
            }

        });
        //adViewWelcome.loadAd(adRequest);
        //customAdView.loadAd(adRequest);
        /*
        adRecyclerView.loadAd(adRequest);
        */







        /*try {
            leagueServers.put("North America", "104.160.131.3");
            leagueServers.put("Europe West", "104.160.141.3");
            leagueServers.put("Europe Nordic & East", "104.160.142.3");
            leagueServers.put("Oceania", "104.160.156.1");
            leagueServers.put("Latin America North", "104.160.136.3");
            //leagueServers.put("Brazil", "104.160.152.3");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            dotaServers.put("US East (Sterling, VA)", "iad.valve.net");
            dotaServers.put("US West (Seattle, WA)", "eat.valve.net");
            dotaServers.put("Europe East 1 (Vienna, Austria)", "vie.valve.net");
            dotaServers.put("Europe East 2 (Vienna, Austria)", "185.25.182.1");
            dotaServers.put("Europe West 1 (Luxembourg)", "lux.valve.net");
            dotaServers.put("Europe West 2 (Luxembourg)", "146.66.158.1");
            dotaServers.put("SE Asia 1 (Singapore)", "sgp-1.valve.net");
            dotaServers.put("SE Asia 2 (Singapore)", "sgp-2.valve.net");
            dotaServers.put("South Africa 1 (Cape Town)", "197.80.200.1");
            dotaServers.put("South Africa 2 (Cape Town)", "197.84.209.1");
            dotaServers.put("South Africa 3 (Cape Town)", "196.38.180.1");
            dotaServers.put("South Africa 4 (Johannesburg)", "196.38.180.1");
            dotaServers.put("South America 1 (Brazil)", "gru.valve.net");
            dotaServers.put("South America 2 (Brazil)", "209.197.25.1");
            dotaServers.put("Russia 1 (Stockholm, Sweden)", "sto.valve.net");
            dotaServers.put("Russia 2 (Stockholm, Sweden)", "185.25.180.1");
            //dotaServers.put("Australia (Sydney)", "103.10.125.1");
            //dotaServers.put("Chile (Santiago)", "200.73.67.1");
            dotaServers.put("Dubai (UAE)", "dxb.valve.net");
            dotaServers.put("Peru (Lima)", "191.98.144.1");



        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            overwatchServers.put("US West", "24.105.30.129");
            overwatchServers.put("US Central", "24.105.62.129");
            overwatchServers.put("Brazil", "54.233.152.145");
            overwatchServers.put("South America (Chile)", "200.29.32.109");
            overwatchServers.put("Europe 1", "185.60.112.157");
            overwatchServers.put("Australia (Sydney)", "13.55.197.247");
            overwatchServers.put("SE Asia (Singapore)", "54.254.187.187");
            overwatchServers.put("Korea", "121.254.206.1");
            overwatchServers.put("Taiwan", "210.242.32.201");



        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            pubgServers.put("US East (Virginia)", "dynamodb.us-east-1.amazonaws.com");
            pubgServers.put("US East (Ohio)", "dynamodb.us-east-2.amazonaws.com");
            pubgServers.put("US West (California)", "dynamodb.us-west-1.amazonaws.com");
            pubgServers.put("US West (Oregon)", "dynamodb.us-west-2.amazonaws.com");
            pubgServers.put("Canada (Central)", "dynamodb.ca-central-1.amazonaws.com");
            pubgServers.put("Europe (Frankfurt)", "dynamodb.eu-central-1.amazonaws.com");
            pubgServers.put("Europe (Ireland)", "dynamodb.eu-west-1.amazonaws.com");
            pubgServers.put("Europe (London)", "dynamodb.eu-west-2.amazonaws.com");
            pubgServers.put("Europe (Paris)", "dynamodb.eu-west-3.amazonaws.com");
            pubgServers.put("Asia Pacific (Mumbai)", "dynamodb.ap-south-1.amazonaws.com");
            pubgServers.put("Asia Pacific (Seoul)", "dynamodb.ap-northeast-2.amazonaws.com");
            pubgServers.put("Asia Pacific (Singapore)", "dynamodb.ap-southeast-1.amazonaws.com");
            pubgServers.put("Asia Pacific (Sydney)", "dynamodb.ap-southeast-2.amazonaws.com");
            pubgServers.put("Asia Pacific (Tokyo)", "dynamodb.ap-northeast-1.amazonaws.com");
            pubgServers.put("South America (Sao Paulo)", "dynamodb.sa-east-1.amazonaws.com");
            pubgServers.put("China (Beijing)", "dynamodb.cn-north-1.amazonaws.com.cn");
            pubgServers.put("China (Ningxia)", "dynamodb.cn-northwest-1.amazonaws.com.cn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            fortniteServers.put("US East (Ohio)", "dynamodb.us-east-2.amazonaws.com");
            fortniteServers.put("US West (Oregon)", "dynamodb.us-west-2.amazonaws.com");
            fortniteServers.put("US East (Virginia)", "dynamodb.us-east-1.amazonaws.com");
            fortniteServers.put("US West (California)", "dynamodb.us-west-1.amazonaws.com");
            fortniteServers.put("Canada (Central)", "dynamodb.ca-central-1.amazonaws.com");
            fortniteServers.put("Europe (London)", "dynamodb.eu-west-2.amazonaws.com");
            fortniteServers.put("Europe (Frankfurt)", "dynamodb.eu-central-1.amazonaws.com");
            fortniteServers.put("Europe (Ireland)", "dynamodb.eu-west-1.amazonaws.com");
            fortniteServers.put("Europe (Paris)", "dynamodb.eu-west-3.amazonaws.com");
            fortniteServers.put("Asia Pacific (Sydney)", "dynamodb.ap-southeast-2.amazonaws.com");
            fortniteServers.put("Asia Pacific (Singapore)", "dynamodb.ap-southeast-1.amazonaws.com");
            fortniteServers.put("Asia Pacific (Mumbai)", "dynamodb.ap-south-1.amazonaws.com");
            fortniteServers.put("Asia Pacific (Seoul)", "dynamodb.ap-northeast-2.amazonaws.com");
            fortniteServers.put("Asia Pacific (Tokyo)", "dynamodb.ap-northeast-1.amazonaws.com");
            fortniteServers.put("South America (Sao Paulo)", "dynamodb.sa-east-1.amazonaws.com");
            fortniteServers.put("China (Ningxia)", "dynamodb.cn-northwest-1.amazonaws.com.cn");
            fortniteServers.put("China (Beijing)", "dynamodb.cn-north-1.amazonaws.com.cn");
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        /*suggestionsDropDown = new ArrayList<String>();
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        suggestionsDropDown.clear();
        try {
            suggestionsDropDown = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString("SUGGESTIONS_LIST", ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        suggestionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, suggestionsDropDown);
        editText.setAdapter(suggestionAdapter);*/
        editText.setThreshold(1);

        //suggestionAdapter.notifyDataSetChanged();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyRecyclerViewAdapter(this, mServerList);
        recyclerView.setItemAnimator(new SlideInRightAnimator());
        recyclerView.setHasFixedSize(true);
        //recyclerView.getItemAnimator().setAddDuration(1000);
        //recyclerView.getItemAnimator().setRemoveDuration(500);
        //recyclerView.getItemAnimator().setMoveDuration(1000);
        //recyclerView.getItemAnimator().setChangeDuration(500);

        /*SlideInRightAnimationAdapter rightAdapter = new SlideInRightAnimationAdapter(adapter);
        rightAdapter.setDuration(1000);
        rightAdapter.setInterpolator(new OvershootInterpolator());
        recyclerView.setAdapter(rightAdapter);
        rightAdapter.setFirstOnly(false);*/
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
        recyclerView.setItemViewCacheSize(mServerList.size());
        //recyclerView.setAdapter(adapter);
    }

    /**
     * Called when leaving the activity
     */


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        savedInstanceState.putInt("welcomeVisibility", welcomeLayout.getVisibility());
        savedInstanceState.putInt("customVisibility", customPing.getVisibility());
        savedInstanceState.putString("currentJSON", currentJSONData);
        //savedInstanceState.putString("frameBackground", frameLayout.getBackground().toString());

        // etc.
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        boolean animatedBG = prefs.getBoolean(SettingsActivity.KEY_PREF_ANIMATEDBG, false); //0 is the default value.
        int random = 0;
        int randomBG = 0;
        if (savedInstanceState.getInt("welcomeVisibility") == 0 || savedInstanceState.getString("currentJSON") == "") {
            welcomeLayout.setVisibility(View.VISIBLE);
            if (getResources().getConfiguration().orientation == 1) {
                adViewWelcome.setVisibility(View.VISIBLE);
            } else if (getResources().getConfiguration().orientation == 2) {
                adViewWelcome.setVisibility(View.GONE);
            }
            if (getResources().getConfiguration().orientation == 2) {
                findViewById(R.id.background).setVisibility(View.GONE);
            }
        } else if (savedInstanceState.getInt("customVisibility") == 0 && savedInstanceState.getString("currentJSON") == "customServers") {
            welcomeLayout.setVisibility(View.GONE);
            customPing.setVisibility(View.VISIBLE);
            if (getResources().getConfiguration().orientation == 1) {
                customAdView.setVisibility(View.VISIBLE);
            } else if (getResources().getConfiguration().orientation == 2) {
                customAdView.setVisibility(View.GONE);
            }
            currentJSONData = "customServers";
        } else if (savedInstanceState.getInt("welcomeVisibility") != 0 && savedInstanceState.getString("currentJSON") != "") {
            welcomeLayout.setVisibility(View.GONE);
            changeVisibilitiesOnClick();
            recyclerView.setVisibility(View.VISIBLE);
            if (getResources().getConfiguration().orientation == 1) {
                adRecyclerView.setVisibility(View.VISIBLE);
            } else if (getResources().getConfiguration().orientation == 2) {
                adRecyclerView.setVisibility(View.GONE);
            }
            if (savedInstanceState.getString("currentJSON") == "leagueServers") {
                currentJSON = leagueServers;
                currentJSONData = "leagueServers";
                leagueButton.setBackgroundColor((R.attr.actionModeBackground));
                if (getResources().getConfiguration().orientation == 2) {
                    findViewById(R.id.background).setVisibility(View.VISIBLE);
                }
                keysIterator = currentJSON.keys();
                random = (int) (Math.random() * leagueGifs.length);
                randomBG = (int) (Math.random() * leagueBackgrounds.length);
                if (animatedBG) {
                    Glide.with(this).load((leagueGifs[random])).into(frameLayout);
                    //frameLayout.setBackground(getResources().getDrawable(leagueGifs[random]));
                    //frameLayout.setImageResource(leagueGifs[random]);
                    //frameLayout.addView(leagueGifs[(random)]);
                } else {
                    Glide.with(this).load(leagueBackgrounds[randomBG]).into(frameLayout);
                    //frameLayout.setBackgroundResource(leagueBackgrounds[(randomBG)]);
                }
                while (keysIterator.hasNext()) {
                    String keyStr = (String) keysIterator.next();
                    //try {
                    //String valueStr = currentJSON.getString(keyStr);
                    //} catch (JSONException e) {
                    //    Toast.makeText(this, "ERROR PROCESSING JSON", Toast.LENGTH_SHORT).show();
                    //}
                    mServerList.add(keyStr);
                    //adapter.notifyItemChanged(mServerList.size()-1);
                }
                adapter.notifyItemRangeInserted(0, mServerList.size());
                recyclerView.setItemViewCacheSize(mServerList.size());
            } else if (savedInstanceState.getString("currentJSON") == "pubgServers") {
                currentJSON = pubgServers;
                currentJSONData = "pubgServers";
                pubgButton.setBackgroundColor((R.attr.actionModeBackground));
                if (getResources().getConfiguration().orientation == 2) {
                    findViewById(R.id.background).setVisibility(View.VISIBLE);
                }
                keysIterator = currentJSON.keys();
                random = (int) (Math.random() * pubgGifs.length);
                randomBG = (int) (Math.random() * pubgBackgrounds.length);
                if (animatedBG) {
                    Glide.with(this).load((pubgGifs[random])).into(frameLayout);
                    //frameLayout.setBackground(getResources().getDrawable(leagueGifs[random]));
                    //frameLayout.setImageResource(leagueGifs[random]);
                    //frameLayout.addView(leagueGifs[(random)]);
                } else {
                    Glide.with(this).load(pubgBackgrounds[randomBG]).into(frameLayout);
                    //frameLayout.setBackgroundResource(leagueBackgrounds[(randomBG)]);
                }
                while (keysIterator.hasNext()) {
                    String keyStr = (String) keysIterator.next();
                    //try {
                    //String valueStr = currentJSON.getString(keyStr);
                    //} catch (JSONException e) {
                    //    Toast.makeText(this, "ERROR PROCESSING JSON", Toast.LENGTH_SHORT).show();
                    //}
                    mServerList.add(keyStr);
                    //adapter.notifyItemChanged(mServerList.size()-1);
                }
                adapter.notifyItemRangeInserted(0, mServerList.size());
                recyclerView.setItemViewCacheSize(mServerList.size());
            } else if (savedInstanceState.getString("currentJSON") == "fortniteServers") {
                currentJSON = fortniteServers;
                currentJSONData = "fortniteServers";
                fortniteButton.setBackgroundColor((R.attr.actionModeBackground));
                if (getResources().getConfiguration().orientation == 2) {
                    findViewById(R.id.background).setVisibility(View.VISIBLE);
                }
                keysIterator = currentJSON.keys();
                random = (int) (Math.random() * fortniteGifs.length);
                randomBG = (int) (Math.random() * fortniteBackgrounds.length);
                if (animatedBG) {
                    Glide.with(this).load((fortniteGifs[random])).into(frameLayout);
                    //frameLayout.setBackground(getResources().getDrawable(leagueGifs[random]));
                    //frameLayout.setImageResource(leagueGifs[random]);
                    //frameLayout.addView(leagueGifs[(random)]);
                } else {
                    Glide.with(this).load(fortniteBackgrounds[randomBG]).into(frameLayout);
                    //frameLayout.setBackgroundResource(leagueBackgrounds[(randomBG)]);
                }
                while (keysIterator.hasNext()) {
                    String keyStr = (String) keysIterator.next();
                    //try {
                    //String valueStr = currentJSON.getString(keyStr);
                    //} catch (JSONException e) {
                    //    Toast.makeText(this, "ERROR PROCESSING JSON", Toast.LENGTH_SHORT).show();
                    //}
                    mServerList.add(keyStr);
                    //adapter.notifyItemChanged(mServerList.size()-1);
                }
                adapter.notifyItemRangeInserted(0, mServerList.size());
                recyclerView.setItemViewCacheSize(mServerList.size());
            } else if (savedInstanceState.getString("currentJSON") == "dotaServers") {
                currentJSON = dotaServers;
                currentJSONData = "dotaServers";
                dotaButton.setBackgroundColor((R.attr.actionModeBackground));
                if (getResources().getConfiguration().orientation == 2) {
                    findViewById(R.id.background).setVisibility(View.VISIBLE);
                }
                keysIterator = currentJSON.keys();
                random = (int) (Math.random() * dotaGifs.length);
                randomBG = (int) (Math.random() * dotaBackgrounds.length);
                if (animatedBG) {
                    Glide.with(this).load((dotaGifs[random])).into(frameLayout);
                    //frameLayout.setBackground(getResources().getDrawable(leagueGifs[random]));
                    //frameLayout.setImageResource(leagueGifs[random]);
                    //frameLayout.addView(leagueGifs[(random)]);
                } else {
                    Glide.with(this).load(dotaBackgrounds[randomBG]).into(frameLayout);
                    //frameLayout.setBackgroundResource(leagueBackgrounds[(randomBG)]);
                }
                while (keysIterator.hasNext()) {
                    String keyStr = (String) keysIterator.next();
                    //try {
                    //String valueStr = currentJSON.getString(keyStr);
                    //} catch (JSONException e) {
                    //    Toast.makeText(this, "ERROR PROCESSING JSON", Toast.LENGTH_SHORT).show();
                    //}
                    mServerList.add(keyStr);
                    //adapter.notifyItemChanged(mServerList.size()-1);
                }
                adapter.notifyItemRangeInserted(0, mServerList.size());
                recyclerView.setItemViewCacheSize(mServerList.size());
            } else if (savedInstanceState.getString("currentJSON") == "overwatchServers") {
                currentJSON = overwatchServers;
                currentJSONData = "overwatchServers";
                overwatchButton.setBackgroundColor((R.attr.actionModeBackground));
                if (getResources().getConfiguration().orientation == 2) {
                    findViewById(R.id.background).setVisibility(View.VISIBLE);
                }
                keysIterator = currentJSON.keys();
                random = (int) (Math.random() * overwatchGifs.length);
                randomBG = (int) (Math.random() * overwatchBackgrounds.length);
                if (animatedBG) {
                    Glide.with(this).load((overwatchGifs[random])).into(frameLayout);
                    //frameLayout.setBackground(getResources().getDrawable(leagueGifs[random]));
                    //frameLayout.setImageResource(leagueGifs[random]);
                    //frameLayout.addView(leagueGifs[(random)]);
                } else {
                    Glide.with(this).load(overwatchBackgrounds[randomBG]).into(frameLayout);
                    //frameLayout.setBackgroundResource(leagueBackgrounds[(randomBG)]);
                }
                while (keysIterator.hasNext()) {
                    String keyStr = (String) keysIterator.next();
                    //try {
                    //String valueStr = currentJSON.getString(keyStr);
                    //} catch (JSONException e) {
                    //    Toast.makeText(this, "ERROR PROCESSING JSON", Toast.LENGTH_SHORT).show();
                    //}
                    mServerList.add(keyStr);
                    //adapter.notifyItemChanged(mServerList.size()-1);
                }
                adapter.notifyItemRangeInserted(0, mServerList.size());
                recyclerView.setItemViewCacheSize(mServerList.size());
            }
            else if (savedInstanceState.getString("currentJSON") == "csgoServers") {
                currentJSON = csgoServers;
                currentJSONData = "csgoServers";
                csgoButton.setBackgroundColor((R.attr.actionModeBackground));
                if (getResources().getConfiguration().orientation == 2) {
                    findViewById(R.id.background).setVisibility(View.VISIBLE);
                }
                keysIterator = currentJSON.keys();
                random = (int) (Math.random() * csgoGifs.length);
                randomBG = (int) (Math.random() * csgoBackgrounds.length);
                if (animatedBG) {
                    Glide.with(this).load((csgoGifs[random])).into(frameLayout);
                    //frameLayout.setBackground(getResources().getDrawable(leagueGifs[random]));
                    //frameLayout.setImageResource(leagueGifs[random]);
                    //frameLayout.addView(leagueGifs[(random)]);
                } else {
                    Glide.with(this).load(csgoBackgrounds[randomBG]).into(frameLayout);
                    //frameLayout.setBackgroundResource(leagueBackgrounds[(randomBG)]);
                }
                while (keysIterator.hasNext()) {
                    String keyStr = (String) keysIterator.next();
                    //try {
                    //String valueStr = currentJSON.getString(keyStr);
                    //} catch (JSONException e) {
                    //    Toast.makeText(this, "ERROR PROCESSING JSON", Toast.LENGTH_SHORT).show();
                    //}
                    mServerList.add(keyStr);
                    //adapter.notifyItemChanged(mServerList.size()-1);
                }
                adapter.notifyItemRangeInserted(0, mServerList.size());
                recyclerView.setItemViewCacheSize(mServerList.size());
            }
        }
        if (getFragmentManager().findFragmentByTag("settings") != null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.remove(getFragmentManager().findFragmentByTag("settings"));
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            System.out.println("LUL NO FRAGMENT YET");
        }
    }

    @Override
    public void onPause() {
        if (adRecyclerView != null) {
            adRecyclerView.pause();
        }
        if (adViewWelcome != null) {
            adViewWelcome.pause();
        }
        if (customAdView != null) {
            customAdView.pause();
        }
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
        ConstraintSet set = new ConstraintSet();
        set.clone(welcomeLayout);
        welcome.measure(0, 0);
        //int desiredHeight = findViewById(R.id.welcome).getHeight();
        set.connect(Introduction.getId(), ConstraintSet.TOP, welcome.getId(), ConstraintSet.TOP, (welcome.getMeasuredHeight() + 2));
        set.applyTo(welcomeLayout);


        if (adRecyclerView != null) {
            adRecyclerView.resume();
        }
        if (adViewWelcome != null) {
            adViewWelcome.resume();
        }
        if (customAdView != null) {
            customAdView.resume();
        }
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        if (adRecyclerView != null) {
            adRecyclerView.destroy();
        }
        if (adViewWelcome != null) {
            adViewWelcome.destroy();
        }
        if (customAdView != null) {
            customAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onItemClick(View v, int position) {
        String serverName = adapter.getItem(position);
        TextView pingResults = v.findViewById(R.id.pingResults);

        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String numPingPref = prefs.getString(SettingsActivity.KEY_PREF_NUMPINGS, "5");
        boolean animatedBG = prefs.getBoolean(SettingsActivity.KEY_PREF_ANIMATEDBG, false);
        boolean dispWarnings = prefs.getBoolean(SettingsActivity.KEY_PREF_WARNINGS, true);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo networkInfo2 = connMgr.getActiveNetworkInfo();
        boolean isWifiConn = networkInfo.isConnected();
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo.isConnected();
        try {
            String host = currentJSON.getString(serverName);
            if (dispWarnings) {
                if (!isWifiConn && !isMobileConn) {
                    Toast.makeText(this, R.string.notConnectedToANY, Toast.LENGTH_LONG).show();
                } else if (!isWifiConn && isMobileConn) {
                    Toast.makeText(this, R.string.connectedToMobile, Toast.LENGTH_LONG).show();
                }
            }
            System.out.println(host);
            //System.out.println(executeCommand());
            if (status == null || status.getStatus() == AsyncTask.Status.FINISHED) {
                pingResults.setText("Pinging..." + adapter.getItem(position));
                status = new isReachable(Integer.parseInt(numPingPref), 2000, host, pingResults, serverName, this, this);
                status.execute();
            } else {
                System.out.println("Waiting for previous Async to finish...");
                Toast.makeText(this, "Please wait for previous ping to finish.", Toast.LENGTH_SHORT).show();
            }

            //ArrayList<String> pingTimes = isReachable(5,2000, host);

            /*String fullString;
            int indexInString = 0;
            StringBuilder sb = new StringBuilder();
            for (String s : pingTimes)
            {
                sb.append(s);
                sb.append(" ");
            }

            fullString = sb.toString();
            Spannable spannable = new SpannableString(fullString);
            for (int i = 0; i < pingTimes.size(); i++){
                if (pingTimes.get(i) != "Timed out" && pingTimes.get(i) != "Host unreachable"){
                    int pingMilli = Integer.parseInt(pingTimes.get(i).substring(0,pingTimes.get(i).length()-2));
                    if (pingMilli < 100){
                        spannable.setSpan(new ForegroundColorSpan(Color.GREEN), indexInString, indexInString+1+(pingTimes.get(i).length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    else if (pingMilli < 300){
                        spannable.setSpan(new ForegroundColorSpan(Color.YELLOW), indexInString, indexInString+1+(pingTimes.get(i).length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    else if (pingMilli >= 300){
                        spannable.setSpan(new ForegroundColorSpan(Color.RED), indexInString, indexInString+1+(pingTimes.get(i).length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    indexInString += 1 + (pingTimes.get(i).length());
                }
                else if (pingTimes.get(i) != "Timed out" && pingTimes.get(i) != "Host unreachable") {
                    spannable.setSpan(new ForegroundColorSpan(Color.RED), indexInString, indexInString+1+(pingTimes.get(i).length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    indexInString += 1 + (pingTimes.get(i).length());
                }

                pingResults.setText(spannable, TextView.BufferType.SPANNABLE);
            }
        */
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    public boolean iterateChildrenTransparent(LinearLayout layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if (v instanceof ImageButton) {
                v.setBackgroundColor(Color.TRANSPARENT);
                //validate your EditText here
            } else if (v instanceof RadioButton) {
                //validate RadioButton
            } //etc. If it fails anywhere, just return false.
        }
        return true;
    }

    private void changeVisibilitiesOnClick() {
        welcomeLayout.setVisibility(View.GONE);
        customPing.setVisibility(View.GONE);

        int amount = mServerList.size();
        mServerList.clear();
        iterateChildrenTransparent(navigationLayout);
        //adapter.notifyDataSetChanged();
        //adapter.notifyItemRangeChanged(0, amount);
        adapter.notifyItemRangeRemoved(0, amount);

        recyclerView.setVisibility(View.GONE);
        adRecyclerView.setVisibility(View.GONE);
        //customAdView.setVisibility(View.GONE);
        if (getResources().getConfiguration().orientation == 2) {
            findViewById(R.id.background).setVisibility(View.GONE);
        }
        pingStatsHolder.setVisibility(View.GONE);
        //pingStats.setVisibility(View.GONE);
        //frameLayout.removeAllViews();
        /*if (frameLayout != null && frameLayout.getDrawable() != null && frameLayout.getDrawable() instanceof GifDrawable) {
            ((GifDrawable)frameLayout.getDrawable()).recycle();
        }
        else if (frameLayout != null && frameLayout.getDrawable() != null && frameLayout.getDrawable() instanceof BitmapDrawable){
            ((BitmapDrawable)frameLayout.getDrawable()).getBitmap().recycle();
        }*/

        if (getFragmentManager().findFragmentByTag("settings") != null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.remove(getFragmentManager().findFragmentByTag("settings"));
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            System.out.println("LUL NO FRAGMENT YET");
        }
    }

    Iterator<String> keysIterator;

    @Override
    public void sendData(Wrapper w) {
        if (w.custom1) {
            if (!w.bugged) {
                pingStatus.setText("Successful pinging!");
                pingStatus.setTextColor(getResources().getColor(R.color.green_400));
                min.setText(this.getString(R.string.customPingMin, w.minPing));
                max.setText(this.getString(R.string.customPingMax, w.maxPing));
                avg.setText(this.getString(R.string.customPingAvg, w.avgPing));
                int timeOuts = w.pingsTransmitted - w.pingsReceived;
                boolean special = false;
                int packetLossValue = (int) (((double) timeOuts / w.pingsTransmitted) * 100);
                String pingTransmitBefore = (this.getString(R.string.customPingTransmit, w.pingsTransmitted));
                String pingReceiveBefore = (this.getString(R.string.customPingReceived, w.pingsReceived));
                //pingsTransmit.setText(this.getString(R.string.customPingTransmit, w.pingsTransmitted));
                Spannable tempSpan = new SpannableString(pingTransmitBefore);
                tempSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green_400)), 0, pingTransmitBefore.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                pingsTransmit.setText(tempSpan);
                tempSpan = new SpannableString(pingReceiveBefore);
                if (w.pingsTransmitted - w.pingsReceived <= 0) {
                    tempSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green_400)), 0, pingReceiveBefore.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (w.pingsTransmitted - w.pingsReceived >= w.pingsTransmitted) {
                    tempSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_700)), 0, pingReceiveBefore.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (w.pingsTransmitted - w.pingsReceived > 0) {
                    tempSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_400)), 0, pingReceiveBefore.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                pingReceive.setText(tempSpan);

                packetLoss.setText(this.getString(R.string.customPingPacketLoss, packetLossValue));
                hostName.setText(this.getString(R.string.customPingHost, w.serverName));
                String newString = w.pingList.trim();
                if (w.pingList.contains("requests timed out")) {
                    newString = w.pingList.substring(0, w.pingList.indexOf("requests timed out") - 1 - (String.valueOf(timeOuts).length()));
                    newString = newString.replaceAll(" ", ", ");
                    w.pingList = newString + " " + w.pingList.substring(w.pingList.indexOf("requests timed out") - 1 - (String.valueOf(timeOuts).length()));
                    special = true;
                } else {
                    w.pingList = newString.replaceAll(" ", ", ");
                }

                if (packetLossValue > 10) {
                    packetLoss.setTextColor(getResources().getColor(R.color.red_700));
                } else if (packetLossValue >= 4) {
                    packetLoss.setTextColor(getResources().getColor(R.color.yellow_400));
                } else if (packetLossValue < 4) {
                    packetLoss.setTextColor(getResources().getColor(R.color.green_400));
                }
                if (!w.minPing.equals("Not Available, no pings to process.")) {
                    int actualMin = Integer.parseInt(w.minPing.substring(0, w.minPing.length() - 3));
                    int actualMax = Integer.parseInt(w.maxPing.substring(0, w.maxPing.length() - 3));
                    int actualAvg = Integer.parseInt(w.avgPing.substring(0, w.avgPing.length() - 3));
                    if (actualMin < 100) {
                        min.setTextColor(getResources().getColor(R.color.green_400));
                    } else if (actualMin < 300) {
                        min.setTextColor(getResources().getColor(R.color.yellow_400));
                    } else if (actualMin >= 300) {
                        min.setTextColor(getResources().getColor(R.color.red_700));
                    }
                    if (actualMax < 100) {
                        max.setTextColor(getResources().getColor(R.color.green_400));
                    } else if (actualMax < 300) {
                        max.setTextColor(getResources().getColor(R.color.yellow_400));
                    } else if (actualMax >= 300) {
                        max.setTextColor(getResources().getColor(R.color.red_700));
                    }
                    if (actualAvg < 100) {
                        avg.setTextColor(getResources().getColor(R.color.green_400));
                    } else if (actualAvg < 300) {
                        avg.setTextColor(getResources().getColor(R.color.yellow_400));
                    } else if (actualAvg >= 300) {
                        avg.setTextColor(getResources().getColor(R.color.red_700));
                    }
                } else if (w.minPing.equals("Not Available, no pings to process.")) {
                    avg.setTextColor(getResources().getColor(R.color.red_700));
                    min.setTextColor(getResources().getColor(R.color.red_700));
                    max.setTextColor(getResources().getColor(R.color.red_700));
                }
                String[] split = w.pingList.split(", ");
                String bigText = this.getString(R.string.customPingFullPingResults, w.pingList);
                tempSpan = new SpannableString(bigText);
                int indexInString = bigText.indexOf("Full ping results") + ("Full ping results:".length());
                for (int i = 0; i < split.length; i++) {
                    System.out.println(split[i]);
                    if (!split[i].contains("requests") && !split[i].contains("timed") && !split[i].contains("out") && !split[i].equals("")) {
                        int tempTest = Integer.parseInt(split[i]);
                        if (i != split.length - 1) {
                            indexInString += 1;
                        }
                        if (tempTest < 100) {
                            tempSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green_400)), indexInString, indexInString + String.valueOf(tempTest).length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else if (tempTest < 300) {
                            tempSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_400)), indexInString, indexInString + String.valueOf(tempTest).length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else if (tempTest >= 300) {
                            tempSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_700)), indexInString, indexInString + String.valueOf(tempTest).length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }

                        indexInString += String.valueOf(tempTest).length() + 1;
                    }
                    /*else if (split[i].contains("requests")){
                        if (i != split.length - 1) {
                            indexInString += 1;
                        }
                        spannable.setSpan(new ForegroundColorSpan(Color.RED), indexInString-(split[i-1].length())-1, pingStatsText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        indexInString += split[i].length() + 1;
                    }*/
                }
                if (special == true) {
                    tempSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_700)), bigText.indexOf("requests timed out") - 1 - (String.valueOf(timeOuts).length()), bigText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                fullResults.setText(tempSpan);
            } else if (w.bugged) {
                pingStatus.setText("ERROR WITH HOST (unreachable?)");
                pingStatus.setTextColor(getResources().getColor(R.color.red_700));
            }
        } else if (!w.custom1) {
            if (!w.bugged) {
                //pingStatus.setText("Successful pinging!");
                //pingStatus.setTextColor(getResources().getColor(R.color.green_400));
                statsMin.setText(this.getString(R.string.customPingMin, w.minPing));
                statsMax.setText(this.getString(R.string.customPingMax, w.maxPing));
                statsAvg.setText(this.getString(R.string.customPingAvg, w.avgPing));
                int timeOuts = w.pingsTransmitted - w.pingsReceived;
                boolean special = false;
                int packetLossValue = (int) (((double) timeOuts / w.pingsTransmitted) * 100);
                String pingTransmitBefore = (this.getString(R.string.customPingTransmit, w.pingsTransmitted));
                String pingReceiveBefore = (this.getString(R.string.customPingReceived, w.pingsReceived));
                //pingsTransmit.setText(this.getString(R.string.customPingTransmit, w.pingsTransmitted));
                Spannable tempSpan = new SpannableString(pingTransmitBefore);
                tempSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green_400)), 0, pingTransmitBefore.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                statsPingTransmit.setText(tempSpan);
                tempSpan = new SpannableString(pingReceiveBefore);
                if (w.pingsTransmitted - w.pingsReceived <= 0) {
                    tempSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green_400)), 0, pingReceiveBefore.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (w.pingsTransmitted - w.pingsReceived >= w.pingsTransmitted) {
                    tempSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_700)), 0, pingReceiveBefore.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (w.pingsTransmitted - w.pingsReceived > 0) {
                    tempSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_400)), 0, pingReceiveBefore.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                statsPingReceive.setText(tempSpan);

                statsPacketLoss.setText(this.getString(R.string.customPingPacketLoss, packetLossValue));
                statsHostname.setText(this.getString(R.string.customPingHost, w.serverName));
                String newString = w.pingList.trim();
                if (w.pingList.contains("requests timed out")) {
                    newString = w.pingList.substring(0, w.pingList.indexOf("requests timed out") - 1 - (String.valueOf(timeOuts).length()));
                    newString = newString.replaceAll(" ", ", ");
                    w.pingList = newString + " " + w.pingList.substring(w.pingList.indexOf("requests timed out") - 1 - (String.valueOf(timeOuts).length()));
                    special = true;
                } else {
                    w.pingList = newString.replaceAll(" ", ", ");
                }

                if (packetLossValue > 10) {
                    statsPacketLoss.setTextColor(getResources().getColor(R.color.red_700));
                } else if (packetLossValue >= 4) {
                    statsPacketLoss.setTextColor(getResources().getColor(R.color.yellow_400));
                } else if (packetLossValue < 4) {
                    statsPacketLoss.setTextColor(getResources().getColor(R.color.green_400));
                }
                if (!w.minPing.equals("Not Available, no pings to process.")) {
                    int actualMin = Integer.parseInt(w.minPing.substring(0, w.minPing.length() - 3));
                    int actualMax = Integer.parseInt(w.maxPing.substring(0, w.maxPing.length() - 3));
                    int actualAvg = Integer.parseInt(w.avgPing.substring(0, w.avgPing.length() - 3));
                    if (actualMin < 100) {
                        statsMin.setTextColor(getResources().getColor(R.color.green_400));
                    } else if (actualMin < 300) {
                        statsMin.setTextColor(getResources().getColor(R.color.yellow_400));
                    } else if (actualMin >= 300) {
                        statsMin.setTextColor(getResources().getColor(R.color.red_700));
                    }
                    if (actualMax < 100) {
                        statsMax.setTextColor(getResources().getColor(R.color.green_400));
                    } else if (actualMax < 300) {
                        statsMax.setTextColor(getResources().getColor(R.color.yellow_400));
                    } else if (actualMax >= 300) {
                        statsMax.setTextColor(getResources().getColor(R.color.red_700));
                    }
                    if (actualAvg < 100) {
                        statsAvg.setTextColor(getResources().getColor(R.color.green_400));
                    } else if (actualAvg < 300) {
                        statsAvg.setTextColor(getResources().getColor(R.color.yellow_400));
                    } else if (actualAvg >= 300) {
                        statsAvg.setTextColor(getResources().getColor(R.color.red_700));
                    }
                } else if (w.minPing.equals("Not Available, no pings to process.")) {
                    statsAvg.setTextColor(getResources().getColor(R.color.red_700));
                    statsMin.setTextColor(getResources().getColor(R.color.red_700));
                    statsMax.setTextColor(getResources().getColor(R.color.red_700));
                }
                String[] split = w.pingList.split(", ");
                String bigText = this.getString(R.string.customPingFullPingResults, w.pingList);
                tempSpan = new SpannableString(bigText);
                int indexInString = bigText.indexOf("Full ping results") + ("Full ping results:".length());
                for (int i = 0; i < split.length; i++) {
                    System.out.println(split[i]);
                    if (!split[i].contains("requests") && !split[i].contains("timed") && !split[i].contains("out") && !split[i].equals("")) {
                        int tempTest = Integer.parseInt(split[i]);
                        if (i != split.length - 1) {
                            indexInString += 1;
                        }
                        if (tempTest < 100) {
                            tempSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green_400)), indexInString, indexInString + String.valueOf(tempTest).length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else if (tempTest < 300) {
                            tempSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_400)), indexInString, indexInString + String.valueOf(tempTest).length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else if (tempTest >= 300) {
                            tempSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_700)), indexInString, indexInString + String.valueOf(tempTest).length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }

                        indexInString += String.valueOf(tempTest).length() + 1;
                    }
                    /*else if (split[i].contains("requests")){
                        if (i != split.length - 1) {
                            indexInString += 1;
                        }
                        spannable.setSpan(new ForegroundColorSpan(Color.RED), indexInString-(split[i-1].length())-1, pingStatsText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        indexInString += split[i].length() + 1;
                    }*/
                }
                if (special == true) {
                    tempSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_700)), bigText.indexOf("requests timed out") - 1 - (String.valueOf(timeOuts).length()), bigText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                statsFullresults.setText(tempSpan);
                pingStatsHolder.setVisibility(View.VISIBLE);
                Toast.makeText(this, R.string.tipToClose , Toast.LENGTH_SHORT).show();
            } else if (w.bugged) {
                statsHostname.setText("ERROR WITH HOST. The host is most likely unreachable. Please check your internet connection and try again later.");
                statsHostname.setTextColor(getResources().getColor(R.color.red_700));
                pingStatsHolder.setVisibility(View.VISIBLE);
                Toast.makeText(this, R.string.tipToClose , Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void performCustomPing(String hostname) {
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String numPingPref = prefs.getString(SettingsActivity.KEY_PREF_NUMPINGS, "5");//"No name defined" is the default value.
        if (customStatus == null || customStatus.getStatus() == AsyncTask.Status.FINISHED) {
            if (!hostname.trim().equals("")) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(customPing.getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SharedPreferences.Editor editor = prefs.edit();
                ArrayList<String> strings = new ArrayList<String>();
                try {
                    strings = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString("SUGGESTIONS_LIST", ObjectSerializer.serialize(new ArrayList<String>())));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                pingStatus.setText("Pinging " + hostname + "...");
                if (!strings.contains(hostname.toLowerCase()) && strings != null) {
                    if (strings.size() > 10) {
                        strings.remove(0);
                    }

                    strings.add(hostname.toLowerCase());

                    suggestionsDropDown = strings;
                    try {
                        System.out.println("ADDED " + hostname.toLowerCase() + " to suggestions!");
                        System.out.println("These are the strings: " + strings);
                        editor.putString("SUGGESTIONS_LIST", ObjectSerializer.serialize(strings));
                        editor.apply();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                suggestionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, suggestionsDropDown);
                editText.setAdapter(suggestionAdapter);
                //suggestionAdapter.notifyDataSetChanged();

                pingStatus.setTextColor(getResources().getColor(R.color.yellow_400));
                Random rand = new Random();
                int n = rand.nextInt(2);
                if (getResources().getConfiguration().orientation == 2) {
                    if ((adPlayed % 4 == 0) && (n == 0)) {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }
                        adPlayed++;
                    }
                    else if (adPlayed % 4 != 0 ){
                        adPlayed++;
                    }
                }
                customStatus = new isReachableCustom(Integer.parseInt(numPingPref), 2000, hostname, hostname, this, this);
                customStatus.execute();
            } else {
                Toast.makeText(this, "Please input a valid host before pinging.", Toast.LENGTH_SHORT).show();
            }
        } else {
            System.out.println("Waiting for previous Async to finish...");
            Toast.makeText(this, "Please wait for previous ping to finish.", Toast.LENGTH_SHORT).show();
        }
    }

    public int getImage(String imageName) {

        int drawableResourceId = this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());

        return drawableResourceId;
    }

    @OnClick({R.id.leagueButton, R.id.pubgButton, R.id.fortniteButton, R.id.customButton, R.id.ping_button, R.id.parent, R.id.settingsButton, R.id.dotaButton, R.id.overwatchButton, R.id.pingStatsHolder, R.id.csgoButton})
    public void onViewClicked(View view) {
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String numPingPref = prefs.getString(SettingsActivity.KEY_PREF_NUMPINGS, "5");//"No name defined" is the default value.
        boolean animatedBG = prefs.getBoolean(SettingsActivity.KEY_PREF_ANIMATEDBG, false);
        boolean isWifiConn = true;
        boolean isMobileConn = true;
        boolean dispWarnings = prefs.getBoolean(SettingsActivity.KEY_PREF_WARNINGS, true);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        //NetworkInfo networkInfo2 = connMgr.getActiveNetworkInfo();
        if (networkInfo != null) {
            isWifiConn = networkInfo.isConnected();
        }
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo != null) {
            isMobileConn = networkInfo.isConnected();
        }
        /*Log.d(DEBUG_TAG, "Wifi connected: " + isWifiConn);
        Log.d(DEBUG_TAG, "Mobile connected: " + isMobileConn);*/

        if (getResources().getConfiguration().orientation == 2) {
            int desiredHeight = findViewById(R.id.navigationScroll2).getWidth();
            bund1.putInt("height", desiredHeight);
            bund1.putInt("type", 2);
        } else if (getResources().getConfiguration().orientation == 1) {
            int desiredHeight = findViewById(R.id.navigationScroll).getHeight();
            bund1.putInt("height", desiredHeight);
            bund1.putInt("type", 1);
        }

        int random = 0;
        int randomBG = 0;

        /*System.out.println(SettingsActivity.KEY_PREF_ANIMATEDBG);
        System.out.println(SettingsActivity.KEY_PREF_NUMPINGS);
        System.out.println(numPingPref);
        System.out.println(animatedBG);*/
        /*String restoredText = prefs.getString("text", null);
        if (restoredText != null) {
            String numPingPref = prefs.getString(SettingsActivity.KEY_PREF_NUMPINGS, "5");//"No name defined" is the default value.
            boolean animatedBG = prefs.getBoolean(SettingsActivity.KEY_PREF_ANIMATEDBG, true); //0 is the default value.
        }*/

        switch (view.getId()) {
            case R.id.leagueButton:
                if (currentJSON != leagueServers) {
                    changeVisibilitiesOnClick();
                    currentJSON = leagueServers;
                    currentJSONData = "leagueServers";
                    leagueButton.setBackgroundColor((R.attr.actionModeBackground));
                    if (getResources().getConfiguration().orientation == 2) {
                        findViewById(R.id.background).setVisibility(View.VISIBLE);
                    }
                    recyclerView.setVisibility(View.VISIBLE);
                    if (getResources().getConfiguration().orientation == 1) {
                        adRecyclerView.setVisibility(View.VISIBLE);
                    } else if (getResources().getConfiguration().orientation == 2) {
                        adRecyclerView.setVisibility(View.GONE);
                    }

                    if (status != null && status.getStatus() != AsyncTask.Status.FINISHED) {
                        status.cancel(true);

                        Toast.makeText(this, "Ping cancelled because you moved to a different page!", Toast.LENGTH_SHORT).show();
                    }
                    if (dispWarnings) {
                        if (!isWifiConn && !isMobileConn) {
                            Toast.makeText(this, R.string.notConnectedToANY, Toast.LENGTH_LONG).show();
                            Toast.makeText(this, R.string.notConnectedToANY, Toast.LENGTH_LONG).show();
                        } else if (!isWifiConn && isMobileConn) {
                            Toast.makeText(this, R.string.connectedToMobile, Toast.LENGTH_LONG).show();
                            Toast.makeText(this, R.string.connectedToMobile, Toast.LENGTH_LONG).show();
                        }
                    }


                    keysIterator = currentJSON.keys();
                    random = (int) (Math.random() * leagueGifs.length);
                    randomBG = (int) (Math.random() * leagueBackgrounds.length);
                    if (animatedBG) {
                        Glide.with(this).load((leagueGifs[random])).into(frameLayout);
                        //frameLayout.setBackground(getResources().getDrawable(leagueGifs[random]));
                        //frameLayout.setImageResource(leagueGifs[random]);
                        //frameLayout.addView(leagueGifs[(random)]);
                    } else {
                        Glide.with(this).load(leagueBackgrounds[randomBG]).into(frameLayout);
                        //frameLayout.setBackgroundResource(leagueBackgrounds[(randomBG)]);
                    }
                    while (keysIterator.hasNext()) {
                        String keyStr = (String) keysIterator.next();
                        //try {
                        //String valueStr = currentJSON.getString(keyStr);
                        //} catch (JSONException e) {
                        //    Toast.makeText(this, "ERROR PROCESSING JSON", Toast.LENGTH_SHORT).show();
                        //}
                        mServerList.add(keyStr);
                        //adapter.notifyItemChanged(mServerList.size()-1);
                    }
                    adapter.notifyItemRangeInserted(0, mServerList.size());
                    recyclerView.setItemViewCacheSize(mServerList.size());
                } else {
                    break;
                }
                break;
            case R.id.pubgButton:
                if (currentJSON != pubgServers) {
                    changeVisibilitiesOnClick();
                    if (getResources().getConfiguration().orientation == 2) {
                        findViewById(R.id.background).setVisibility(View.VISIBLE);
                    }
                    currentJSON = pubgServers;
                    currentJSONData = "pubgServers";
                    recyclerView.setVisibility(View.VISIBLE);
                    if (getResources().getConfiguration().orientation == 1) {
                        adRecyclerView.setVisibility(View.VISIBLE);
                    } else if (getResources().getConfiguration().orientation == 2) {
                        adRecyclerView.setVisibility(View.GONE);
                    }
                    pubgButton.setBackgroundColor((R.attr.actionModeBackground));
                    if (status != null && status.getStatus() != AsyncTask.Status.FINISHED) {
                        status.cancel(true);
                        Toast.makeText(this, "Ping cancelled because you moved to a different page!", Toast.LENGTH_SHORT).show();
                    }
                    if (dispWarnings) {
                        if (!isWifiConn && !isMobileConn) {
                            Toast.makeText(this, R.string.notConnectedToANY, Toast.LENGTH_LONG).show();
                            Toast.makeText(this, R.string.notConnectedToANY, Toast.LENGTH_LONG).show();
                        } else if (!isWifiConn && isMobileConn) {
                            Toast.makeText(this, R.string.connectedToMobile, Toast.LENGTH_LONG).show();
                            Toast.makeText(this, R.string.connectedToMobile, Toast.LENGTH_LONG).show();
                        }
                    }


                    random = (int) (Math.random() * pubgGifs.length);
                    randomBG = (int) (Math.random() * pubgBackgrounds.length);
                    if (animatedBG) {
                        //frameLayout.setImageResource(pubgGifs[random]);
                        //frameLayout.setBackground(getResources().getDrawable(pubgGifs[random]));
                        Glide.with(this).load((pubgGifs[random])).into(frameLayout);
                        //frameLayout.addView(pubgGifs[(random)]);
                    } else {
                        Glide.with(this).load(pubgBackgrounds[randomBG]).into(frameLayout);
                        //frameLayout.setBackgroundResource(pubgBackgrounds[randomBG]);
                    }
                    keysIterator = currentJSON.keys();
                    while (keysIterator.hasNext()) {
                        String keyStr = (String) keysIterator.next();
                        //try {
                        //String valueStr = currentJSON.getString(keyStr);
                        //} catch (JSONException e) {
                        //    Toast.makeText(this, "ERROR PROCESSING JSON", Toast.LENGTH_SHORT).show();
                        //}
                        mServerList.add(keyStr);
                        //adapter.notifyItemChanged(mServerList.size()-1);
                    }
                    adapter.notifyItemRangeInserted(0, mServerList.size());
                    recyclerView.setItemViewCacheSize(mServerList.size());
                } else {
                    break;
                }
                break;
            case R.id.fortniteButton:
                if (currentJSON != fortniteServers) {
                    changeVisibilitiesOnClick();
                    if (getResources().getConfiguration().orientation == 2) {
                        findViewById(R.id.background).setVisibility(View.VISIBLE);
                    }
                    currentJSON = fortniteServers;
                    currentJSONData = "fortniteServers";
                    if (getResources().getConfiguration().orientation == 1) {
                        adRecyclerView.setVisibility(View.VISIBLE);
                    } else if (getResources().getConfiguration().orientation == 2) {
                        adRecyclerView.setVisibility(View.GONE);
                    }

                    fortniteButton.setBackgroundColor((R.attr.actionModeBackground));
                    recyclerView.setVisibility(View.VISIBLE);
                    if (status != null && status.getStatus() != AsyncTask.Status.FINISHED) {
                        status.cancel(true);
                        Toast.makeText(this, "Ping cancelled because you moved to a different page!", Toast.LENGTH_SHORT).show();
                    }
                    if (dispWarnings) {
                        if (!isWifiConn && !isMobileConn) {
                            Toast.makeText(this, R.string.notConnectedToANY, Toast.LENGTH_LONG).show();
                            Toast.makeText(this, R.string.notConnectedToANY, Toast.LENGTH_LONG).show();
                        } else if (!isWifiConn && isMobileConn) {
                            Toast.makeText(this, R.string.connectedToMobile, Toast.LENGTH_LONG).show();
                            Toast.makeText(this, R.string.connectedToMobile, Toast.LENGTH_LONG).show();
                        }
                    }


                    keysIterator = currentJSON.keys();
                    random = (int) (Math.random() * fortniteGifs.length);
                    randomBG = (int) (Math.random() * fortniteBackgrounds.length);
                    if (animatedBG) {
                        Glide.with(this).load((fortniteGifs[random])).into(frameLayout);
                        //frameLayout.setImageResource(fortniteGifs[random]);
                        //frameLayout.setBackground(getResources().getDrawable(fortniteGifs[random]));
                        //frameLayout.addView(fortniteGifs[(random)]);
                    } else {
                        Glide.with(this).load(fortniteBackgrounds[randomBG]).into(frameLayout);
                        //frameLayout.setBackgroundResource(fortniteBackgrounds[(randomBG)]);
                    }
                    while (keysIterator.hasNext()) {
                        String keyStr = (String) keysIterator.next();
                        //try {
                        //String valueStr = currentJSON.getString(keyStr);
                        //} catch (JSONException e) {
                        //    Toast.makeText(this, "ERROR PROCESSING JSON", Toast.LENGTH_SHORT).show();
                        //}
                        mServerList.add(keyStr);
                    }
                    adapter.notifyItemRangeInserted(0, mServerList.size());
                    recyclerView.setItemViewCacheSize(mServerList.size());
                } else {
                    break;
                }
                break;
            case R.id.dotaButton:
                if (currentJSON != dotaServers) {
                    changeVisibilitiesOnClick();
                    if (getResources().getConfiguration().orientation == 2) {
                        findViewById(R.id.background).setVisibility(View.VISIBLE);
                    }
                    currentJSON = dotaServers;
                    currentJSONData = "dotaServers";
                    if (getResources().getConfiguration().orientation == 1) {
                        adRecyclerView.setVisibility(View.VISIBLE);
                    } else if (getResources().getConfiguration().orientation == 2) {
                        adRecyclerView.setVisibility(View.GONE);
                    }

                    dotaButton.setBackgroundColor((R.attr.actionModeBackground));
                    recyclerView.setVisibility(View.VISIBLE);
                    if (status != null && status.getStatus() != AsyncTask.Status.FINISHED) {
                        status.cancel(true);
                        Toast.makeText(this, "Ping cancelled because you moved to a different page!", Toast.LENGTH_SHORT).show();
                    }
                    if (dispWarnings) {
                        if (!isWifiConn && !isMobileConn) {
                            Toast.makeText(this, R.string.notConnectedToANY, Toast.LENGTH_LONG).show();
                            Toast.makeText(this, R.string.notConnectedToANY, Toast.LENGTH_LONG).show();
                        } else if (!isWifiConn && isMobileConn) {
                            Toast.makeText(this, R.string.connectedToMobile, Toast.LENGTH_LONG).show();
                            Toast.makeText(this, R.string.connectedToMobile, Toast.LENGTH_LONG).show();
                        }
                    }


                    keysIterator = currentJSON.keys();
                    random = (int) (Math.random() * dotaGifs.length);
                    randomBG = (int) (Math.random() * dotaBackgrounds.length);
                    if (animatedBG) {
                        //frameLayout.setBackground(getResources().getDrawable(dotaGifs[random]));
                        Glide.with(this).load(dotaGifs[random]).into(frameLayout);

                        //frameLayout.setImageResource(dotaGifs[random]);
                        //frameLayout.addView(dotaGifs[(random)]);
                    } else {
                        Glide.with(this).load(dotaBackgrounds[randomBG]).into(frameLayout);
                        //frameLayout.setBackgroundResource(dotaBackgrounds[(randomBG)]);
                    }
                    while (keysIterator.hasNext()) {
                        String keyStr = (String) keysIterator.next();
                        //try {
                        //String valueStr = currentJSON.getString(keyStr);
                        //} catch (JSONException e) {
                        //    Toast.makeText(this, "ERROR PROCESSING JSON", Toast.LENGTH_SHORT).show();
                        //}
                        mServerList.add(keyStr);
                    }
                    adapter.notifyItemRangeInserted(0, mServerList.size());
                    recyclerView.setItemViewCacheSize(mServerList.size());
                } else {
                    break;
                }
                break;
            case R.id.overwatchButton:
                if (currentJSON != overwatchServers) {
                    changeVisibilitiesOnClick();
                    if (getResources().getConfiguration().orientation == 2) {
                        findViewById(R.id.background).setVisibility(View.VISIBLE);
                    }
                    currentJSON = overwatchServers;
                    currentJSONData = "overwatchServers";
                    if (getResources().getConfiguration().orientation == 1) {
                        adRecyclerView.setVisibility(View.VISIBLE);
                    } else if (getResources().getConfiguration().orientation == 2) {
                        adRecyclerView.setVisibility(View.GONE);
                    }

                    overwatchButton.setBackgroundColor((R.attr.actionModeBackground));
                    recyclerView.setVisibility(View.VISIBLE);
                    if (status != null && status.getStatus() != AsyncTask.Status.FINISHED) {
                        status.cancel(true);
                        Toast.makeText(this, "Ping cancelled because you moved to a different page!", Toast.LENGTH_SHORT).show();
                    }
                    if (dispWarnings) {
                        if (!isWifiConn && !isMobileConn) {
                            Toast.makeText(this, R.string.notConnectedToANY, Toast.LENGTH_LONG).show();
                            Toast.makeText(this, R.string.notConnectedToANY, Toast.LENGTH_LONG).show();
                        } else if (!isWifiConn && isMobileConn) {
                            Toast.makeText(this, R.string.connectedToMobile, Toast.LENGTH_LONG).show();
                            Toast.makeText(this, R.string.connectedToMobile, Toast.LENGTH_LONG).show();
                        }
                    }


                    keysIterator = currentJSON.keys();
                    random = (int) (Math.random() * overwatchGifs.length);
                    randomBG = (int) (Math.random() * overwatchBackgrounds.length);
                    if (animatedBG) {
                        //frameLayout.setBackground(getResources().getDrawable(overwatchGifs[random]));
                        Glide.with(this).load((overwatchGifs[random])).into(frameLayout);
                        //frameLayout.setImageResource(overwatchGifs[random]);
                        //frameLayout.addView(overwatchGifs[(random)]);
                    } else {
                        Glide.with(this).load(overwatchBackgrounds[randomBG]).into(frameLayout);
                        //frameLayout.setBackgroundResource(overwatchBackgrounds[(randomBG)]);
                    }
                    while (keysIterator.hasNext()) {
                        String keyStr = (String) keysIterator.next();
                        //try {
                        //String valueStr = currentJSON.getString(keyStr);
                        //} catch (JSONException e) {
                        //    Toast.makeText(this, "ERROR PROCESSING JSON", Toast.LENGTH_SHORT).show();
                        //}
                        mServerList.add(keyStr);
                    }
                    adapter.notifyItemRangeInserted(0, mServerList.size());
                    recyclerView.setItemViewCacheSize(mServerList.size());
                } else {
                    break;
                }
                break;
            case R.id.csgoButton:
                if (currentJSON != csgoServers) {
                    changeVisibilitiesOnClick();
                    if (getResources().getConfiguration().orientation == 2) {
                        findViewById(R.id.background).setVisibility(View.VISIBLE);
                    }
                    currentJSON = csgoServers;
                    currentJSONData = "csgoServers";
                    if (getResources().getConfiguration().orientation == 1) {
                        adRecyclerView.setVisibility(View.VISIBLE);
                    } else if (getResources().getConfiguration().orientation == 2) {
                        adRecyclerView.setVisibility(View.GONE);
                    }

                    csgoButton.setBackgroundColor((R.attr.actionModeBackground));
                    recyclerView.setVisibility(View.VISIBLE);
                    if (status != null && status.getStatus() != AsyncTask.Status.FINISHED) {
                        status.cancel(true);
                        Toast.makeText(this, "Ping cancelled because you moved to a different page!", Toast.LENGTH_SHORT).show();
                    }
                    if (dispWarnings) {
                        if (!isWifiConn && !isMobileConn) {
                            Toast.makeText(this, R.string.notConnectedToANY, Toast.LENGTH_LONG).show();
                            Toast.makeText(this, R.string.notConnectedToANY, Toast.LENGTH_LONG).show();
                        } else if (!isWifiConn && isMobileConn) {
                            Toast.makeText(this, R.string.connectedToMobile, Toast.LENGTH_LONG).show();
                            Toast.makeText(this, R.string.connectedToMobile, Toast.LENGTH_LONG).show();
                        }
                    }


                    keysIterator = currentJSON.keys();
                    random = (int) (Math.random() * csgoGifs.length);
                    randomBG = (int) (Math.random() * csgoBackgrounds.length);
                    if (animatedBG) {
                        //frameLayout.setBackground(getResources().getDrawable(overwatchGifs[random]));
                        Glide.with(this).load((csgoGifs[random])).into(frameLayout);
                        //frameLayout.setImageResource(overwatchGifs[random]);
                        //frameLayout.addView(overwatchGifs[(random)]);
                    } else {
                        Glide.with(this).load(csgoBackgrounds[randomBG]).into(frameLayout);
                        //frameLayout.setBackgroundResource(overwatchBackgrounds[(randomBG)]);
                    }
                    while (keysIterator.hasNext()) {
                        String keyStr = (String) keysIterator.next();
                        //try {
                        //String valueStr = currentJSON.getString(keyStr);
                        //} catch (JSONException e) {
                        //    Toast.makeText(this, "ERROR PROCESSING JSON", Toast.LENGTH_SHORT).show();
                        //}
                        mServerList.add(keyStr);
                    }
                    adapter.notifyItemRangeInserted(0, mServerList.size());
                    recyclerView.setItemViewCacheSize(mServerList.size());
                } else {
                    break;
                }
                break;
            case R.id.customButton:
                changeVisibilitiesOnClick();
                frameLayout.setImageResource(0);
                frameLayout.setBackgroundResource(0);
                suggestionsDropDown = new ArrayList<String>();
                try {
                    suggestionsDropDown = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString("SUGGESTIONS_LIST", ObjectSerializer.serialize(new ArrayList<String>())));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                suggestionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, suggestionsDropDown);
                editText.setAdapter(suggestionAdapter);

                currentJSON = null;
                currentJSONData = "customServers";
                if (status != null && status.getStatus() != AsyncTask.Status.FINISHED) {
                    status.cancel(true);
                    Toast.makeText(this, "Ping cancelled because you moved to a different page!", Toast.LENGTH_SHORT).show();
                }
                customButton.setBackgroundColor((R.attr.actionModeBackground));
                customPing.setVisibility(View.VISIBLE);
                //customAdView.setVisibility(View.VISIBLE);
                break;
            /*case R.id.pingStats:
                pingStats.setVisibility(View.GONE);
                if (getResources().getConfiguration().orientation == 2) {
                    if (((int) Math.random() * 2) + 1 == 1) {
                        if (adPlayed % 4 == 0) {
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            adPlayed++;
                        }
                    }

                }
                break;*/
            case R.id.pingStatsHolder:
                pingStatsHolder.setVisibility(View.GONE);
                Random rand = new Random();
                int  n = rand.nextInt(2);
                if (getResources().getConfiguration().orientation == 2) {
                    System.out.println(adPlayed);
                        if ((adPlayed % 4) == 0 && n == 0) {
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            }
                            adPlayed++;
                        }
                        else if ((adPlayed % 4) != 0 ){
                            adPlayed++;
                        }
                }
                break;
            case R.id.ping_button:
                String customHost = editText.getText().toString().trim();
                if (dispWarnings) {
                    if (!isWifiConn && !isMobileConn) {
                        Toast.makeText(this, R.string.notConnectedToANY, Toast.LENGTH_LONG).show();
                        Toast.makeText(this, R.string.notConnectedToANY, Toast.LENGTH_LONG).show();
                    } else if (!isWifiConn && isMobileConn) {
                        Toast.makeText(this, R.string.connectedToMobile, Toast.LENGTH_LONG).show();
                        Toast.makeText(this, R.string.connectedToMobile, Toast.LENGTH_LONG).show();
                    }
                }
                performCustomPing(customHost);

                /*if (customStatus == null || customStatus.getStatus() == AsyncTask.Status.FINISHED) {
                    String customHost = editText.getText().toString().trim();
                    if (!customHost.trim().equals("")) {
                        try {
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(customPing.getWindowToken(), 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        SharedPreferences.Editor editor = prefs.edit();
                        ArrayList<String> strings = new ArrayList<String>();
                        try {
                            strings = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString("SUGGESTIONS_LIST", ObjectSerializer.serialize(new ArrayList<String>())));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        pingStatus.setText("Pinging " + customHost + "...");
                        if (!strings.contains(customHost.toLowerCase()) && strings != null){
                            if (strings.size() > 10){
                                strings.remove(0);
                            }

                            strings.add(customHost.toLowerCase());

                            suggestionsDropDown = strings;
                            try {
                                System.out.println("ADDED "+customHost.toLowerCase()+" to suggestions!");
                                System.out.println("These are the strings: "+strings);
                                editor.putString("SUGGESTIONS_LIST", ObjectSerializer.serialize(strings));
                                editor.apply();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        suggestionAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, suggestionsDropDown);
                        editText.setAdapter(suggestionAdapter);
                        //suggestionAdapter.notifyDataSetChanged();

                        pingStatus.setTextColor(getResources().getColor(R.color.yellow_400));
                        customStatus = new isReachableCustom(Integer.parseInt(numPingPref), 2000, customHost, customHost, this, this);
                        customStatus.execute();
                    } else {
                        Toast.makeText(this, "Please input a valid host before pinging.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    System.out.println("Waiting for previous Async to finish...");
                    Toast.makeText(this, "Please wait for previous ping to finish.", Toast.LENGTH_SHORT).show();
                }*/
                break;
            case R.id.settingsButton:
                changeVisibilitiesOnClick();
                frameLayout.setImageResource(0);
                frameLayout.setBackgroundResource(0);
                currentJSON = null;
                if (status != null && status.getStatus() != AsyncTask.Status.FINISHED) {
                    status.cancel(true);
                    Toast.makeText(this, "Ping cancelled because you moved to a different page!", Toast.LENGTH_SHORT).show();
                }

                settingsButton.setBackgroundColor((R.attr.actionModeBackground));
                SettingsFragment options = new SettingsFragment();
                options.setArguments(bund1);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(android.R.id.content, options, "settings");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                break;
        }
    }

}

