package com.example.googlemapusingmanual;

import org.json.JSONException;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WeatherAPI extends Thread{
    public int func(double lat, double lng, mapTab tab) throws IOException, JSONException {

        try {
            Thread.sleep(1000); // 날씨 변경 딜레이 변수로 바꿀것
        } catch (InterruptedException e) {
            e.printStackTrace();
        }




        String endPoint =  "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst";
        //String serviceKey = "bRsNyPW+NewTx2aUvwqDdABLPU5afYJQI5PoS/SLSbiJ3llFANs1eBUdVnq5zaSNfvQ2AqLWJRrHvNOPJm3k8Q==";
//        String serviceKey = "bRsNyPW%2BNewTx2aUvwqDdABLPU5afYJQI5PoS%2FSLSbiJ3llFANs1eBUdVnq5zaSNfvQ2AqLWJRrHvNOPJm3k8Q%3D%3D";
//        String serviceKey = "FJU2wtWWUakhuFu7GuMmDXwJjfPd8zf7Ee/JvfvozHQRI/E5zdAhxjSm4NwgTKoNL/sD+0BQu1Em2DC6Tsfc5A==";
        String serviceKey = "FJU2wtWWUakhuFu7GuMmDXwJjfPd8zf7Ee%2FJvfvozHQRI%2FE5zdAhxjSm4NwgTKoNL%2FsD%2B0BQu1Em2DC6Tsfc5A%3D%3D";

        String pageNo = "1";
        String numOfRows = "50";
        String dataType = "XML";
        String baseDate = ""; //원하는 날짜
        String baseTime = ""; //원하는 시간
        String nx = "89"; //위경도 아님 좌표임
        String ny = "90"; //좌표 정보는 api문서 볼 것


        LocalDate currentDay = LocalDate.now();
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("uuuuMMdd");
        String formattedDate = currentDay.format(formatDate);
        baseDate = formattedDate;

        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatHour = DateTimeFormatter.ofPattern("hh");
        DateTimeFormatter formatMinute = DateTimeFormatter.ofPattern("mm");
        String formattedHour = currentTime.toString().substring(0,2);
        String formattedMinute = currentTime.format(formatMinute);
//        baseTime = formattedTime;
        int baseTimeInt;
        if(Integer.parseInt(formattedMinute) > 30)
            formattedMinute = "00";
        else
            formattedMinute = "00";

        baseTime =  (Integer.parseInt(formattedHour) + 8) + formattedMinute;

        String s = endPoint+"?serviceKey="+serviceKey
                +"&pageNo=" + pageNo
                +"&numOfRows=" + numOfRows
                +"&dataType=" + dataType
                +"&base_date=" + baseDate
                +"&base_time="+baseTime
                +"&nx="+nx
                +"&ny="+ny;

//        s = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst?serviceKey=FJU2wtWWUakhuFu7GuMmDXwJjfPd8zf7Ee%2FJvfvozHQRI%2FE5zdAhxjSm4NwgTKoNL%2FsD%2B0BQu1Em2DC6Tsfc5A%3D%3D&pageNo=1&numOfRows=50&dataType=XML&base_date=20221126&base_time=2000&nx=55&ny=127";
//        s = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst?serviceKey=FJU2wtWWUakhuFu7GuMmDXwJjfPd8zf7Ee%2FJvfvozHQRI%2FE5zdAhxjSm4NwgTKoNL%2FsD%2B0BQu1Em2DC6Tsfc5A%3D%3D&pageNo=1&numOfRows=50&dataType=XML&base_date=20221126&base_time=2330&nx=55&ny=127";
        URL url = new URL(s);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader bufferedReader = null;
        if(conn.getResponseCode() == 200) {
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }else{
            //connection error :(
            System.out.println("error");
        }
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        String result= stringBuilder.toString();
        conn.disconnect();

        String resultLines[] = result.split("<item>");

        ArrayList<String> resultList = new ArrayList<String>(resultLines.length);
//
//        for(int i=0; i<resultLines.length; i++){
//            resultList.add(resultLines[i]);
//        }
//
        resultList.addAll(Arrays.asList(resultLines));

        Map<String, String> skyMap = new HashMap<>();
        Map<String, String> ptyMap = new HashMap<>();
//        ArrayList<String> skyList = new ArrayList<>(10);
//        ArrayList<String> ptyList = new ArrayList<>(10);
        for(int i=0; i<resultList.size(); i++){
            //sky와 pty만 고려할것
            if(resultList.get(i).indexOf("SKY") == -1 && resultList.get(i).indexOf("PTY") == -1){
                resultList.remove(i);
                i--;
                continue;
            }
        }
        String temp = "";
        for(int i=0; i<resultList.size(); i++){
            temp = resultList.get(i).split("</fcstValue>")[0];
            temp = temp.substring(temp.indexOf("<fcstValue>") + 11);
            if(resultList.get(i).indexOf("SKY") != -1){
                skyMap.put(
                        resultList.get(i).substring(
                        resultList.get(i).indexOf("<fcstTime>") + 10,
                        resultList.get(i).indexOf("<fcstTime>") + 14)
                , temp);
            }
            else if (resultList.get(i).indexOf("PTY") != -1){
                ptyMap.put(
                        resultList.get(i).substring(
                                resultList.get(i).indexOf("<fcstTime>") + 10,
                                resultList.get(i).indexOf("<fcstTime>") + 14)
                , temp);
            }
        }
        String tempInt =
                Integer.toString((Integer.parseInt(baseTime) / 100 + 1) * 100);
        if(tempInt.compareTo("2400") == 0)
            tempInt = "0000";
        int rainState = Integer.parseInt(ptyMap.get(tempInt));
//        int rainState = 3;

        return rainState;

    }
}
