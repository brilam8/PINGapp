package com.bigboiapps.brian.pingchecker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class isReachableCustom extends AsyncTask<Void, Integer, Wrapper> {

    private View view;
    int numPing;
    int waitPing;
    private String ipping;
    TextView pingResults;
    TextView pingStats;
    String serverName;
    Context context;

    OnDataSendToActivity dataSendToActivity;
    isReachableCustom(int numPing, int waitPing, String ipping, String serverName, Context context, Activity activity){
        this.numPing = numPing;
        this.waitPing = waitPing;
        this.ipping = ipping;
        this.pingResults = pingResults;
        this.pingStats = pingStats;
        this.serverName = serverName;
        this.context = context;
        dataSendToActivity = (OnDataSendToActivity)activity;
    }

    protected void onPreExecute() {
        //pingResults.setText("Please wait...");
    }


    protected Wrapper doInBackground(Void... options) {
        Wrapper w = new Wrapper();
        w.bugged = true;
        w.serverName = this.serverName;
        w.custom1 = true;
        try {

            //int numPing = Integer.parseInt(options[0]);
            //String waitPing = options[1];
            //String ipping = options[2];
            Runtime runtime = Runtime.getRuntime();
            Process process = null;
            process = runtime.exec("ping -c " + numPing +  " "+ ipping);
            //Scanner scanner = new Scanner(process.getInputStream());
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            process.waitFor();

            //pingResults.setText("Processing Pings...");
            ArrayList<String> strings = new ArrayList<>();
            String data = "";
            ArrayList<String> pingTimes = new ArrayList<String>();
            //
            String st;
            while ((st = stdInput.readLine()) != null) {

                System.out.println(st);
                data = data + st + "\n";
                strings.add(st);
                if (isCancelled()) break;
            }
            /*while (scanner.hasNextLine()) {
                String string = scanner.nextLine();
                data = data + string + "\n";
                strings.add(string);
            }*/

            if (data.contains("IP address must be specified.") || (data.contains("Ping request could not find host " + ipping + ".")) || (data.contains("Please check the name and try again."))) {
                //throw new Exception(data);
                w.bugged = true;
                return w;
            } else if (numPing > strings.size()) {
                System.out.println(strings.size());
                System.out.println(numPing);
                w.bugged = true;
                return w;
                //throw new Exception(data);
            }

            int index = 1;
            int requestsTransmitted = 0;
            int requestsReceived = 0;
            int avg = 0;
            int min = 0;
            int max = 0;
            String[] temp;
            for (int i = index; i < strings.size(); i++) {
                String string = strings.get(i);
                System.out.println(string);
                if (isCancelled()) break;
                if (string.contains("Destination host unreachable.")) {
                    pingTimes.add("Host unreachable");
                } else if (string.contains("Request timed out.")) {
                    pingTimes.add("Timed out");
                } else if (string.contains("bytes") && string.contains("time") && string.contains("ttl")) {
                    if (string.contains("time")) {
                        pingTimes.add(string.substring(string.indexOf("time")));
                    }

                } else if (string.contains("packets transmitted")){
                    pingTimes.add(string.substring(0, string.indexOf("time")-2));
                    requestsTransmitted = Integer.parseInt(string.substring(0,string.indexOf("packets transmitted")-1));
                    requestsReceived = Integer.parseInt(string.substring(string.indexOf("packets transmitted")+21,string.indexOf("received")-1));
                    System.out.println("requests received: "+requestsReceived+"/ requests transmitted : "+requestsTransmitted);
                }
                else if (string.contains("avg") && string.contains("max")){
                    pingTimes.add(string.substring(23, string.length()-3));
                }
                else {

                }
            }

            process.destroy();
            stdInput.close();

            String fullString = "";
            int indexInString = 0;

            StringBuilder sb2 = new StringBuilder();
            for (int i = 0; i < pingTimes.size(); i++){
                if (pingTimes.get(i) != "Timed out" && pingTimes.get(i) != "Host unreachable" && (!pingTimes.get(i).contains("packets transmitted")) && !pingTimes.get(i).contains("/")){
                    int pingMilli = (int)Double.parseDouble(pingTimes.get(i).substring(5,pingTimes.get(i).length()-3));
                    sb2.append(String.valueOf(pingMilli));
                    if (i != (pingTimes.size()-1)) {
                        sb2.append(" ");
                    }
                    //fullString += String.valueOf(pingMilli) + " ";
                }
                else if (pingTimes.get(i).contains("/")){
                    temp = pingTimes.get(i).split("/");
                    min = (int)Double.parseDouble(temp[0]);
                    avg = (int)Double.parseDouble(temp[1]);
                    max = (int)Double.parseDouble(temp[2]);
                    System.out.println("Minimum Ping : "+min+"/ Average ping : "+avg+"/ Maximum Ping : "+max);
                }
            }

            fullString = sb2.toString();

            String[] splitString = fullString.split(" ");
            ArrayList<Integer> arrayPings = new ArrayList<>();
            if (splitString.length < numPing){
                int neededTimedOuts = requestsTransmitted - requestsReceived;
                sb2.append(neededTimedOuts+" requests timed out ");
                fullString = sb2.toString();
                splitString = fullString.split(" ");
                //splitString += neededTimedOuts+" request timed outs";
            }
            Spannable spannable = new SpannableString(fullString);
            for (int i = 0; i < splitString.length; i++) {
                String testString = splitString[i].trim();
                if (!testString.contains("requests") && !testString.contains("timed") && !testString.contains("out") && testString != "") {

                    int pingMilli = Integer.parseInt(testString);
                    //arrayPings.add(pingMilli);
                    if (pingMilli < 150) {
                        spannable.setSpan(new ForegroundColorSpan(Color.GREEN), indexInString, indexInString + 1 + (String.valueOf(pingMilli).length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (pingMilli < 300) {
                        spannable.setSpan(new ForegroundColorSpan(Color.YELLOW), indexInString, indexInString + 1 + (String.valueOf(pingMilli).length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (pingMilli >= 300) {
                        spannable.setSpan(new ForegroundColorSpan(Color.RED), indexInString, indexInString + 1 + (String.valueOf(pingMilli).length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    indexInString += 1 + (String.valueOf(pingMilli).length());
                } else if (splitString[i].contains("requests")) {
                    indexInString = indexInString - (splitString[i-1].length()) - 1;
                    spannable.setSpan(new ForegroundColorSpan(Color.RED), indexInString, fullString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    //indexInString += 1 + (splitString[i].length());
                }
            }

            w.pingsReceived = requestsReceived;
            w.pingsTransmitted = requestsTransmitted;
            w.spannableText = spannable;

            w.pingList = fullString.trim();
            w.minPing = String.valueOf(min)+" ms";
            w.avgPing = String.valueOf(avg)+" ms";
            w.maxPing = String.valueOf(max)+" ms";
            w.bugged = false;
            if (min == 0 && max == 0 && avg == 0){
                w.minPing = "Not Available, no pings to process.";
                w.maxPing = "Not Available, no pings to process.";
                w.avgPing = "Not Available, no pings to process.";
            }

            return w;
        } catch (Exception e){
            e.printStackTrace();
        }
        return w;
    }

    /*protected void setProgressPercent(int progress){

    }
    protected void onProgressUpdate(Integer... progress) {
        setProgressPercent(progress[0]);
    }*/
    protected void onPostExecute(Wrapper w) {

        /*System.out.println(w.serverName);
        System.out.println(w.pingsTransmitted);
        System.out.println(w.pingsReceived);
        System.out.println((((w.pingsTransmitted - w.pingsReceived)/w.pingsTransmitted*100)));
        System.out.println(w.minPing);
        System.out.println(w.avgPing);
        System.out.println(w.maxPing);
        System.out.println(w.pingList);*/
        dataSendToActivity.sendData(w);
        //String pingStatsText = "Something went wrong. Sorry!";
        /*if (!w.bugged) {

        }
        else if (w.bugged){
            pingStatsText = context.getString(R.string.pingstatsBugged, w.serverName);

            pingResults.setText("ERROR PINGING");
            pingResults.setTextColor(Color.RED);
            pingStats.setText(pingStatsText);
        }*/

        //pingStats.setVisibility(View.VISIBLE);
    }
}
