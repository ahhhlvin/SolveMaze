import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by alvin2 on 4/9/16.
 * Alvin Kuang
 * C4Q Access Code 2.1
 */
public class Maze
{

    public static void main(String[] args)
    {
        String moves = new String();
        ArrayList<String> visited = new ArrayList<>();
        System.out.println(solveMaze("https://challenge.flipboard.com/start", moves, visited));
    }

    public static String solveMaze(String url, String movesTracker, ArrayList<String> visited)
    {
        String identifier = "";
        String link = "";
        String nextLink = "";
        Map<String, String> paramsMap = new HashMap<>();
        URLConnection con = null;
        int counter = 0;

        try
        {
            con = new URL(url).openConnection();
            InputStream is = con.getInputStream();
            System.out.println("Step URL: " + con.getURL());
            String redirectURL = con.getURL().toString();
            link = redirectURL;
            is.close();

            String[] params = redirectURL.split("\\?");

            int equalCount = 0;
            for(int j = 0; j < params[1].length(); j++)
            {
                if(params[1].charAt(j) == '=')
                {
                    equalCount++;
                }
            }

            String[] firstSplit = params[1].split("&");

            for(int i = 0; i < equalCount; i++)
            {
                String[] split = firstSplit[i].split("=");
                paramsMap.put(split[0], split[1]);
            }


            for(String str : paramsMap.keySet())
            {
                if(str.equals("s"))
                {
                    identifier = paramsMap.get(str);
                }
            }


            JSONObject jsonObj = new JSONObject(run(url));
            Move currMove = new Move();
            JSONArray adjacentArr = jsonObj.getJSONArray("adjacent");
            for(int i = 0; i < adjacentArr.length(); i++)
            {
                currMove.adjacentSteps.add(new Step(adjacentArr.getJSONObject(i).getInt("x"),
                                                    adjacentArr.getJSONObject(i).getInt("y")));
            }

            currMove.end = jsonObj.getBoolean("end");
            currMove.letterStep = jsonObj.getString("letter");
            counter = currMove.adjacentSteps.size();

            // if current step is the end, return true and print out success path


            if(currMove.end)
            {

                                                String checkLink = "https://challenge.flipboard.com/check?s=" + identifier + "&guess=" + movesTracker;

                                                JSONObject checkObj = new JSONObject(run(checkLink));
                                                boolean checkResult = checkObj.getBoolean("success");

                                                if(checkResult)
                                                {
                                                    System.out.println("SUCCESS! " + movesTracker);
                                                }
                                                else
                                                {
                                                    System.out
                                                            .println("FAILED :(");
                                                }

                return currMove.letterStep;
            }
            else
            {
                visited.add(link);
                while(currMove.adjacentSteps.size() > 0)
                {
                    for(int i = 0; i < counter; i++)
                    {
                        nextLink = "https://challenge.flipboard.com/step?s=" + identifier + "&x=" + currMove.adjacentSteps
                                .get(i).getX() + "&y=" + currMove.adjacentSteps.get(i).getY();
                    }

//                    if (!visited.contains(nextLink))
//                    {
                        String successPath = solveMaze(nextLink, movesTracker, visited);
                        if (!successPath.equals(""))
                        {
                            return (currMove.letterStep += successPath);
                        }
//                    }

                    counter--;
                }


                return "";

            }


        }
        catch(IOException e1)
        {
            e1.printStackTrace();
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        return movesTracker;
    }


    public static String run(String url) throws IOException
    {

        String result = null;
        InputStream is = null;
        HttpURLConnection conn = null;


        try
        {

            URL endpointUrl = new URL(url);
            conn = (HttpURLConnection) endpointUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            is = conn.getInputStream();
            result = InputStreamToString(is);
            is.close();

        }
        finally
        {
            if(conn != null)
            {
                conn.disconnect();
            }

            if(is != null)
            {
                is.close();
            }
        }

        return result;
    }

    public static String InputStreamToString(InputStream is) throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = br.readLine()) != null)
        {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}
