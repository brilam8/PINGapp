package com.bigboiapps.brian.pingchecker;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bigboiapps.brian.pingchecker.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;



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
    @BindView(R.id.customButton)
    ImageButton customButton;
    @BindView(R.id.navigationLayout)
    LinearLayout navigationLayout;
    @BindView(R.id.serverList)
    RecyclerView recyclerView;
    ArrayList<String> mServerList = new ArrayList<>();
    JSONObject leagueServers = new JSONObject();
    JSONObject pubgServers = new JSONObject();
    JSONObject fortniteServers = new JSONObject();
    JSONObject currentJSON = new JSONObject();
    isReachable status = null;
    isReachableCustom customStatus = null;
    boolean readyForPing = true;
    @BindView(R.id.pingStats)
    TextView pingStats;

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
    EditText editText;
    @BindView(R.id.full_results)
    TextView fullResults;
    @BindView(R.id.custom_ping)
    ConstraintLayout customPing;
    @BindView(R.id.parent)
    ConstraintLayout parent;

    MyGifView lucianGIF;
    MyGifView dariusGIF;
    MyGifView udyrBGIF;
    MyGifView udyrPGIF;
    MyGifView udyrTiGIF;
    MyGifView udyrTuGIF;
    ArrayList<MyGifView> leagueGifs = new ArrayList<>();
    Bundle bund1 = new Bundle(1);

    MyGifView pubg1;
    ArrayList<MyGifView> pubgGifs = new ArrayList<>();

    MyGifView fortnite1;
    ArrayList<MyGifView> fortniteGifs = new ArrayList<>();

    ArrayList<Drawable> leagueBackgrounds = new ArrayList<>();
    ArrayList<Drawable> pubgBackgrounds = new ArrayList<>();
    ArrayList<Drawable> fortniteBackgrounds = new ArrayList<>();

    @BindView(R.id.gifView)
    FrameLayout frameLayout;
    @BindView(R.id.settingsButton)
    ImageButton settingsButton;
    @BindView(R.id.guideline4)
    Guideline navigationLine;
    @BindView(R.id.navigationScroll)
    HorizontalScrollView navigationScroll;

    public int getGuidelineDistance() {
        navigationLine.measure(0, 0);
        int desiredHeight = navigationLine.getMeasuredHeight();
        return desiredHeight;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void setupUI(View view) {

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping_check);
        //setupUI(findViewById(R.id.parent));
        ButterKnife.bind(this);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //leagueGifs.add(dravenGIF = new MyGifView(this, "draven"));
        leagueGifs.add(lucianGIF = new MyGifView(this, "lucian"));
        leagueGifs.add(dariusGIF = new MyGifView(this, "darius"));
        leagueGifs.add(udyrBGIF = new MyGifView(this, "udyr_bear"));
        leagueGifs.add(udyrPGIF = new MyGifView(this, "udyr_tiger"));
        /*leagueGifs.add(udyrTiGIF = new MyGifView(this, "udyr_turtle"));
        leagueGifs.add(udyrTuGIF = new MyGifView(this, "udyr_phoenix"));
        leagueGifs.add(dariusGIF = new MyGifView(this, "darius"));*/


        pubgGifs.add(pubg1 = new MyGifView(this, "pubg1"));

        fortniteGifs.add(fortnite1 = new MyGifView(this, "fortnite1"));

        leagueBackgrounds.add(getResources().getDrawable(R.drawable.league_static1));
        leagueBackgrounds.add(getResources().getDrawable(R.drawable.league_static2));
        leagueBackgrounds.add(getResources().getDrawable(R.drawable.league_static3));
        leagueBackgrounds.add(getResources().getDrawable(R.drawable.league_static4));

        pubgBackgrounds.add(getResources().getDrawable(R.drawable.pubg_static1));
        pubgBackgrounds.add(getResources().getDrawable(R.drawable.pubg_static2));
        pubgBackgrounds.add(getResources().getDrawable(R.drawable.pubg_static3));
        pubgBackgrounds.add(getResources().getDrawable(R.drawable.pubg_static4));

        fortniteBackgrounds.add(getResources().getDrawable(R.drawable.fortnite_static1));
        fortniteBackgrounds.add(getResources().getDrawable(R.drawable.fortnite_static2));
        fortniteBackgrounds.add(getResources().getDrawable(R.drawable.fortnite_static3));
        fortniteBackgrounds.add(getResources().getDrawable(R.drawable.fortnite_static4));

        /*leagueGifs.add(dravenGIF);
        leagueGifs.add(lucianGIF);
        leagueGifs.add(dariusGIF);*/
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        // My AdMob app ID: ca-app-pub-9081923015947764~9422599654

        try {
            leagueServers.put("North America", "104.160.131.3");
            leagueServers.put("Europe West", "104.160.141.3");
            leagueServers.put("Europe Nordic & East", "104.160.142.3");
            leagueServers.put("Oceania", "104.160.156.1");
            leagueServers.put("Latin America North", "104.160.136.3");
            leagueServers.put("Brazil", "104.160.152.3");
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
        }

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        ConstraintSet set = new ConstraintSet();
        set.clone(welcomeLayout);
        welcome.measure(0, 0);
        set.connect(Introduction.getId(), ConstraintSet.TOP, welcome.getId(), ConstraintSet.TOP, (welcome.getMeasuredHeight() + 2));
        set.applyTo(welcomeLayout);

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
        //recyclerView.setAdapter(adapter);
    }
    /** Called when leaving the activity */

    @Override
    public void onItemClick(View v, int position) {
        String serverName = adapter.getItem(position);
        TextView pingResults = v.findViewById(R.id.pingResults);

        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String numPingPref = prefs.getString(SettingsActivity.KEY_PREF_NUMPINGS, "5");
        boolean animatedBG = prefs.getBoolean(SettingsActivity.KEY_PREF_ANIMATEDBG, false);

        try {
            String host = currentJSON.getString(serverName);

            System.out.println(host);
            //System.out.println(executeCommand());
            if (status == null || status.getStatus() == AsyncTask.Status.FINISHED) {
                pingResults.setText("Pinging..." + adapter.getItem(position));
                status = new isReachable(Integer.parseInt(numPingPref), 2000, host, pingResults, pingStats, serverName, this);
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
        //customAdView.setVisibility(View.GONE);

        pingStats.setVisibility(View.GONE);
        frameLayout.removeAllViews();
        if (getFragmentManager().findFragmentByTag("settings") != null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.remove(getFragmentManager().findFragmentByTag("settings"));
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
        }
    }

    Iterator<String> keysIterator;

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
                pingsTransmit.setText(this.getString(R.string.customPingTransmit, w.pingsTransmitted));
                pingReceive.setText(this.getString(R.string.customPingReceived, w.pingsReceived));
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
                    packetLoss.setTextColor(getResources().getColor(R.color.red_900));
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
                        min.setTextColor(getResources().getColor(R.color.red_900));
                    }
                    if (actualMax < 100) {
                        max.setTextColor(getResources().getColor(R.color.green_400));
                    } else if (actualMax < 300) {
                        max.setTextColor(getResources().getColor(R.color.yellow_400));
                    } else if (actualMax >= 300) {
                        max.setTextColor(getResources().getColor(R.color.red_900));
                    }
                    if (actualAvg < 100) {
                        avg.setTextColor(getResources().getColor(R.color.green_400));
                    } else if (actualAvg < 300) {
                        avg.setTextColor(getResources().getColor(R.color.yellow_400));
                    } else if (actualAvg >= 300) {
                        avg.setTextColor(getResources().getColor(R.color.red_900));
                    }
                } else if (w.minPing.equals("Not Available, no pings to process.")) {
                    avg.setTextColor(getResources().getColor(R.color.red_900));
                    min.setTextColor(getResources().getColor(R.color.red_900));
                    max.setTextColor(getResources().getColor(R.color.red_900));
                }
                String[] split = w.pingList.split(", ");
                String bigText = this.getString(R.string.customPingFullPingResults, w.pingList);
                Spannable tempSpan = new SpannableString(bigText);
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
                            tempSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_900)), indexInString, indexInString + String.valueOf(tempTest).length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
                    tempSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_900)), bigText.indexOf("requests timed out") - 1 - (String.valueOf(timeOuts).length()), bigText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                fullResults.setText(tempSpan);
            } else if (w.bugged) {
                pingStatus.setText("ERROR WITH HOST (unreachable?)");
                pingStatus.setTextColor(getResources().getColor(R.color.red_900));
            }
        }


    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @OnClick({R.id.leagueButton, R.id.pubgButton, R.id.fortniteButton, R.id.customButton, R.id.pingStats, R.id.ping_button, R.id.parent, R.id.settingsButton})
    public void onViewClicked(View view) {
        navigationScroll.measure(0, 0);
        int desiredHeight = navigationScroll.getHeight();
        bund1.putInt("height", desiredHeight);

        int random = 0;
        int randomBG = 0;
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String numPingPref = prefs.getString(SettingsActivity.KEY_PREF_NUMPINGS, "5");//"No name defined" is the default value.
        boolean animatedBG = prefs.getBoolean(SettingsActivity.KEY_PREF_ANIMATEDBG, false); //0 is the default value.
        System.out.println(SettingsActivity.KEY_PREF_ANIMATEDBG);
        System.out.println(SettingsActivity.KEY_PREF_NUMPINGS);
        System.out.println(numPingPref);
        System.out.println(animatedBG);
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
                    leagueButton.setBackgroundColor((R.attr.actionModeBackground));

                    recyclerView.setVisibility(View.VISIBLE);

                    if (status != null && status.getStatus() != AsyncTask.Status.FINISHED) {
                        status.cancel(true);

                        Toast.makeText(this, "Ping cancelled because you moved to a different page!", Toast.LENGTH_SHORT).show();
                    }
                    keysIterator = currentJSON.keys();
                    random = (int) (Math.random() * leagueGifs.size());
                    randomBG = (int) (Math.random() * leagueBackgrounds.size());
                    if (animatedBG) {
                        frameLayout.addView(leagueGifs.get(random));
                    } else {
                        frameLayout.setBackground(leagueBackgrounds.get(randomBG));
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
                }
                else{
                    break;
                }
                break;
            case R.id.pubgButton:
                if (currentJSON != pubgServers) {
                    changeVisibilitiesOnClick();

                    currentJSON = pubgServers;
                    recyclerView.setVisibility(View.VISIBLE);
                    pubgButton.setBackgroundColor((R.attr.actionModeBackground));
                    if (status != null && status.getStatus() != AsyncTask.Status.FINISHED) {
                        status.cancel(true);
                        Toast.makeText(this, "Ping cancelled because you moved to a different page!", Toast.LENGTH_SHORT).show();
                    }
                    random = (int) (Math.random() * pubgGifs.size());
                    randomBG = (int) (Math.random() * pubgBackgrounds.size());
                    if (animatedBG) {
                        frameLayout.addView(pubgGifs.get(random));
                    } else {
                        frameLayout.setBackground(pubgBackgrounds.get(randomBG));
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
                }
                else {
                    break;
                }
                break;
            case R.id.fortniteButton:
                if (currentJSON != fortniteServers) {
                    changeVisibilitiesOnClick();

                    currentJSON = fortniteServers;

                    fortniteButton.setBackgroundColor((R.attr.actionModeBackground));
                    recyclerView.setVisibility(View.VISIBLE);
                    if (status != null && status.getStatus() != AsyncTask.Status.FINISHED) {
                        status.cancel(true);
                        Toast.makeText(this, "Ping cancelled because you moved to a different page!", Toast.LENGTH_SHORT).show();
                    }
                    keysIterator = currentJSON.keys();
                    random = (int) (Math.random() * fortniteGifs.size());
                    randomBG = (int) (Math.random() * fortniteBackgrounds.size());
                    if (animatedBG) {
                        frameLayout.addView(fortniteGifs.get(random));
                    } else {
                        frameLayout.setBackground(fortniteBackgrounds.get(randomBG));
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
                }
                else {
                    break;
                }
                break;
            case R.id.customButton:
                changeVisibilitiesOnClick();
                currentJSON = null;
                if (status != null && status.getStatus() != AsyncTask.Status.FINISHED) {
                    status.cancel(true);
                    Toast.makeText(this, "Ping cancelled because you moved to a different page!", Toast.LENGTH_SHORT).show();
                }
                customButton.setBackgroundColor((R.attr.actionModeBackground));
                customPing.setVisibility(View.VISIBLE);
                //customAdView.setVisibility(View.VISIBLE);
                break;
            case R.id.pingStats:
                pingStats.setVisibility(View.GONE);
                break;
            case R.id.ping_button:
                if (customStatus == null || customStatus.getStatus() == AsyncTask.Status.FINISHED) {
                    String customHost = editText.getText().toString();
                    if (!customHost.trim().equals("")) {
                        try {
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(customPing.getWindowToken(), 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        pingStatus.setText("Pinging " + editText.getText() + "...");
                        pingStatus.setTextColor(getResources().getColor(R.color.yellow_400));
                        customStatus = new isReachableCustom(Integer.parseInt(numPingPref), 2000, customHost, customHost, this, this);
                        customStatus.execute();
                    } else {
                        Toast.makeText(this, "Please input a valid host before pinging.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    System.out.println("Waiting for previous Async to finish...");
                    Toast.makeText(this, "Please wait for previous ping to finish.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.settingsButton:
                changeVisibilitiesOnClick();
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